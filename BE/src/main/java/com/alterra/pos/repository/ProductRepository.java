package com.alterra.pos.repository;

import com.alterra.pos.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
    @Query
    List<Product> findAllByIsValidTrue();

    @Query("SELECT p FROM Product p WHERE p.category.id = ?1")
    List<Product> findAllByCategoryId(Integer categoryId);

    @Override
    Optional<Product> findById(Integer id);
}
