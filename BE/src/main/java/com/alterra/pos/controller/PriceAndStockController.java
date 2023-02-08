package com.alterra.pos.controller;

import com.alterra.pos.entity.PriceAndStock;
import com.alterra.pos.repository.PriceAndStockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("priceAndStocks")
public class PriceAndStockController {
    @Autowired
    PriceAndStockRepository priceAndStockRepository;

    @GetMapping
    List<PriceAndStock> getPriceAndStocks() { return priceAndStockRepository.findAll(); };
}
