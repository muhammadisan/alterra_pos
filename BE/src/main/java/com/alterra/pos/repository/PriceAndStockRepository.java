package com.alterra.pos.repository;

import com.alterra.pos.entity.PriceAndStock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PriceAndStockRepository extends JpaRepository<PriceAndStock, Integer> {
    @Override
    Optional<PriceAndStock> findById(Integer id);
}
