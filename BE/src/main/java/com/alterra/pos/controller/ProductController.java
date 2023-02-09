package com.alterra.pos.controller;

import com.alterra.pos.entity.Category;
import com.alterra.pos.entity.PriceAndStock;
import com.alterra.pos.entity.Product;
import com.alterra.pos.repository.CategoryRepository;
import com.alterra.pos.repository.PriceAndStockRepository;
import com.alterra.pos.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("products")
public class ProductController {
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private PriceAndStockRepository priceAndStockRepository;

    @GetMapping
    public List<Product> getProducts() { return productRepository.findAllByIsValidTrue(); }

    @GetMapping("/{id}")
    public Optional<Product> getProductById(@PathVariable Integer id) { return productRepository.findById(id); }

    @GetMapping("/category/{categoryId}")
    public List<Product> getProductsByCategoryId(@PathVariable Integer categoryId) {
        return productRepository.findAllByCategoryId(categoryId);
    }

    @PostMapping("/{categoryId}")
    @Transactional(rollbackFor = Exception.class)
    public Product addProduct(@PathVariable Integer categoryId, @Validated @RequestBody Product product) throws Exception {
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

    @PutMapping
    @Transactional(rollbackFor = Exception.class)
    public Product editProduct(@Validated @RequestBody Product product) throws Exception {
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

    @DeleteMapping("/{productId}")
    public String deleteProduct(@PathVariable Integer productId) throws Exception {
        Product product = productRepository.findById(productId).orElse(null);
        if (product == null) throw new Exception("Product not found with id " + productId);

        product.setIsValid(false);
        product.setModifiedAt(new Date());

        productRepository.save(product);

        return "Product deleted with id " + productId;
    }
}
