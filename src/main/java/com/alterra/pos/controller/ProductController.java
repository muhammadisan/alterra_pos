package com.alterra.pos.controller;

import com.alterra.pos.dto.ProductPriceAndStockDto;
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

import java.util.Date;
import java.util.List;
import java.util.Optional;

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
    public List<Product> getProducts() {
        return productRepository.findAllByIsValidTrue();
    }

    @GetMapping("/{id}")
    public Optional<Product> getProductById(@PathVariable Integer id) {
        return productRepository.findById(id);
    }

    @GetMapping("/category/{categoryId}")
    public List<Product> getProductsByCategoryId(@PathVariable Integer categoryId) {
        return productRepository.findAllByCategoryId(categoryId);
    }

    @PostMapping("/{categoryId}")
    @Transactional(rollbackFor = Exception.class)
    public ProductPriceAndStockDto addProduct(@PathVariable Integer categoryId,
                                              @Validated @RequestBody ProductPriceAndStockDto productPriceAndStockDto)
            throws Exception
    {
        // validate
        Category category = categoryRepository.findById(categoryId).orElse(null);
        if (category == null) throw new Exception("Category not found with id " + categoryId);
        if (!category.getIsValid()) throw new Exception("Category is no longer valid with id " + categoryId);

        // save
        Product product = productPriceAndStockDto.getProduct();
        product.setCategory(category);
        Product product1 = productRepository.saveAndFlush(product);

        PriceAndStock priceAndStock = productPriceAndStockDto.getPriceAndStock();
        priceAndStock.setProduct(product1);
        priceAndStock.setModifiedAt(new Date());
        PriceAndStock priceAndStock1 = priceAndStockRepository.save(priceAndStock);

        // response
        ProductPriceAndStockDto res = new ProductPriceAndStockDto();
        res.setProduct(product1);
        res.setPriceAndStock(priceAndStock1);

        return res;
    }

    @PutMapping("/{productId}")
    public ProductPriceAndStockDto editProduct(@PathVariable Integer productId,
                                               @Validated @RequestBody ProductPriceAndStockDto productPriceAndStockDto)
            throws Exception
    {
        // validate
        Product product = productRepository.findById(productId).orElse(null);
        if (product == null) throw new Exception("Product not found with id " + productId);
        if (!product.getIsValid()) throw new Exception("Product is no longer valid with id " + productId);

        int categoryId = product.getCategory().getId();
        Category category = categoryRepository.findById(categoryId).orElse(null);
        if (category == null) throw new Exception("Category not found with id " + categoryId);
        if (!category.getIsValid()) throw new Exception("Category is no longer valid with id " + categoryId);

        int priceAndStockId = -1;
        PriceAndStock priceAndStock = priceAndStockRepository.findByProductId(productId).orElse(null);
        if (priceAndStock != null) priceAndStockId = priceAndStock.getId(); // use available id

        // save
        Product product1 = productPriceAndStockDto.getProduct();
        product1.setId(productId);
        product1.setCategory(category);
        Product product2 = productRepository.saveAndFlush(product1);

        PriceAndStock priceAndStock1 = productPriceAndStockDto.getPriceAndStock();
        if (priceAndStockId != -1) priceAndStock1.setId(priceAndStockId);
        priceAndStock1.setProduct(product2);
        priceAndStock1.setModifiedAt(new Date());
        PriceAndStock priceAndStock2 = priceAndStockRepository.save(priceAndStock1);

        // response
        ProductPriceAndStockDto res = new ProductPriceAndStockDto();
        res.setProduct(product2);
        res.setPriceAndStock(priceAndStock2);

        return res;
    }
}
