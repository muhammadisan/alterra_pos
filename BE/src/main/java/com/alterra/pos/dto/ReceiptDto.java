package com.alterra.pos.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReceiptDto {
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Products {
        private Integer productId;
        private Integer amount;
    }
    private List<ReceiptDto.Products> products;
    private String orderNo;
    private String receiptNo;
    private Integer paymentMethodId;
    private Integer userId;
}
