package com.alterra.pos.service;

import com.alterra.pos.entity.Receipt;
import com.alterra.pos.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Service
public class ReceiptService {
    @Autowired
    private OrdersRepository ordersRepository;
    @Autowired
    private ReceiptRepository receiptRepository;
    @Autowired
    private PaymentMethodRepository paymentMethodRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private PriceAndStockRepository priceAndStockRepository;
    @Autowired
    private AdminRepository adminRepository;

    public List<Receipt> getReceipts() {
        return receiptRepository.findAll();
    }

    public List<Receipt> getReceiptsByOrderNo(@PathVariable String orderNo) { return receiptRepository.findAllByOrderNo(orderNo); }

    public List<Receipt> getReceiptsByReceiptNo(@PathVariable String receiptNo) { return receiptRepository.findAllByReceiptNo(receiptNo); }
}
