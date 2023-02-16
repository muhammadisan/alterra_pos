package com.alterra.pos.service;

import com.alterra.pos.entity.Category;
import com.alterra.pos.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    public List<Category> getCategories() {
        return categoryRepository.findAllByIsValidTrue();
    }

    public Optional<Category> getCategoryById(Integer id) {
        return categoryRepository.findById(id);
    }

    @Transactional(rollbackFor = Exception.class)
    public Category addCategory(Category category) {
        return categoryRepository.save(category);
    }
}
