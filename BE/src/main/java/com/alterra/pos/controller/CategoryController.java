package com.alterra.pos.controller;

import com.alterra.pos.entity.Category;
import com.alterra.pos.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("categories")
public class CategoryController {
    @Autowired
    private CategoryRepository categoryRepository;

    @GetMapping
    public List<Category> getCategories() {
        return categoryRepository.findAllByIsValidTrue();
    }

    @GetMapping("/{id}")
    public Optional<Category> getCategoryById(@PathVariable Integer id) { return categoryRepository.findById(id); }

    @PostMapping
    public Category addCategory(@Validated @RequestBody Category category) {
        return categoryRepository.save(category);
    }
}
