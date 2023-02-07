package com.alterra.pos.repository;

import com.alterra.pos.entity.PriceAndStock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface PriceAndStockRepository extends JpaRepository<PriceAndStock, Integer> {
    @Query
    Optional<PriceAndStock> findByProductId(Integer id);
}
