package com.alterra.pos.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrdersDto {
    public class Products {
        private Integer productId;
        private Integer amount;
        private Double price;
    }

    private List<Products> products;
    private Integer paymentMethodId;
}
