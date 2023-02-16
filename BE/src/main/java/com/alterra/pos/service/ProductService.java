package com.alterra.pos.service;

import com.alterra.pos.entity.Category;
import com.alterra.pos.entity.PriceAndStock;
import com.alterra.pos.entity.Product;
import com.alterra.pos.repository.CategoryRepository;
import com.alterra.pos.repository.PriceAndStockRepository;
import com.alterra.pos.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private PriceAndStockRepository priceAndStockRepository;

    public List<Product> getProducts() {
        return productRepository.findAllByIsValidTrue();
    }

    public Optional<Product> getProductById(Integer id) {
        return productRepository.findById(id);
    }

    public List<Product> getProductsByCategoryId(Integer categoryId) {
        return productRepository.findAllByCategoryId(categoryId);
    }

    @Transactional(rollbackFor = Exception.class)
    public Product addProduct(Integer categoryId, Product product) throws Exception {
        // validate
        Category category = categoryRepository.findById(categoryId).orElse(null);
        if (category == null) throw new Exception("Category not found with id " + categoryId);
        if (!category.getIsValid()) throw new Exception("Category is no longer valid with id " + categoryId);

        // save
        PriceAndStock priceAndStock = priceAndStockRepository.saveAndFlush(product.getPriceAndStock());
        product.setCategory(category);
        product.setPriceAndStock(priceAndStock);

        return productRepository.save(product);
    }
    
    @Transactional(rollbackFor = Exception.class)
    public Product editProduct(Product product) throws Exception {
        // validate
        int productId = product.getId();
        Product product1 = productRepository.findById(productId).orElse(null);
        if (product1 == null) throw new Exception("Product not found with id " + productId);
        if (!product1.getIsValid()) throw new Exception("Product is no longer valid with id " + productId);
        if (product.getPriceAndStock().getId() != product1.getPriceAndStock().getId()) throw new Exception("Product id doesn't match with Price id with product id " + productId);

        int categoryId = product.getCategory().getId();
        Category category = categoryRepository.findById(categoryId).orElse(null);
        if (category == null) throw new Exception("Category not found with id " + categoryId);
        if (!category.getIsValid()) throw new Exception("Category is no longer valid with id " + categoryId);

        product.setModifiedAt(new Date());
        product.getPriceAndStock().setModifiedAt(new Date());
        product.setCategory(category);
        priceAndStockRepository.save(product.getPriceAndStock());

        return productRepository.save(product);
    }

    @Transactional(rollbackFor = Exception.class)
    public String deleteProduct(Integer productId) throws Exception {
        Product product = productRepository.findById(productId).orElse(null);
        if (product == null) throw new Exception("Product not found with id " + productId);

        product.setIsValid(false);
        product.setModifiedAt(new Date());

        productRepository.save(product);

        return "Product deleted with id " + productId;
    }
}
