package com.alterra.pos.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrdersDto {
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Products {
        private Integer productId;
        private Integer amount;
    }

    private String orderNo;
    private List<OrdersDto.Products> products;
    private Integer paymentMethodId;
}
