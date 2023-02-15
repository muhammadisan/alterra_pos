package com.alterra.pos.service;

import com.alterra.pos.entity.PriceAndStock;
import com.alterra.pos.repository.PriceAndStockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class PriceAndStockService {
    @Autowired
    PriceAndStockRepository priceAndStockRepository;
    public List<PriceAndStock> getPriceAndStocks() { return priceAndStockRepository.findAll(); }
}
