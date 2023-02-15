package com.alterra.pos.service;

import com.alterra.pos.entity.Category;
import com.alterra.pos.entity.PriceAndStock;
import com.alterra.pos.entity.Product;
import com.alterra.pos.repository.CategoryRepository;
import com.alterra.pos.repository.PriceAndStockRepository;
import com.alterra.pos.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private PriceAndStockRepository priceAndStockRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> getProducts() {
        return productRepository.findAllByIsValidTrue();
    }

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
}
