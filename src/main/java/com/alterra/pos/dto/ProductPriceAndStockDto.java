package com.alterra.pos.dto;

import com.alterra.pos.entity.PriceAndStock;
import com.alterra.pos.entity.Product;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductPriceAndStockDto {
    private Product product;
    private PriceAndStock priceAndStock;
}