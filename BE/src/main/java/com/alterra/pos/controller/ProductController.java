package com.alterra.pos.controller;

import com.alterra.pos.entity.Product;
import com.alterra.pos.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("products")
public class ProductController {
    @Autowired
    ProductService productService;

    @GetMapping
    public List<Product> getProducts() { return productService.getProducts(); }

    @GetMapping("/{id}")
    public Optional<Product> getProductById(@PathVariable Integer id) { return productService.getProductById(id); }

    @GetMapping("/category/{categoryId}")
    public List<Product> getProductsByCategoryId(@PathVariable Integer categoryId) {
        return productService.getProductsByCategoryId(categoryId);
    }

    @PostMapping("/{categoryId}")
    public Product addProduct(@PathVariable Integer categoryId, @Validated @RequestBody Product product) throws Exception {
        return productService.addProduct(categoryId, product);
    }

    @PutMapping
    public Product editProduct(@Validated @RequestBody Product product) throws Exception {
        return productService.editProduct(product);
    }

    @DeleteMapping("/{productId}")
    public String deleteProduct(@PathVariable Integer productId) throws Exception {
        return productService.deleteProduct(productId);
    }
}
