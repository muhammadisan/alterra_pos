package com.alterra.pos.controller;

import com.alterra.pos.dto.ReceiptDto;
import com.alterra.pos.entity.*;
import com.alterra.pos.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("receipts")
public class ReceiptController {
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

    @GetMapping
    public List<Receipt> getReceipts() {
        return receiptRepository.findAll();
    }

    @GetMapping("/orderNo/{orderNo}")
    public List<Receipt> getReceiptsByOrderNo(@PathVariable String orderNo) { return receiptRepository.findAllByOrderNo(orderNo); }

    @GetMapping("/receiptNo/{receiptNo}")
    public List<Receipt> getReceiptsByReceiptNo(@PathVariable String receiptNo) { return receiptRepository.findAllByReceiptNo(receiptNo); }

    @PostMapping
    @Transactional(rollbackFor = Exception.class)
    public List<Receipt> addReceipt(@Validated @RequestBody ReceiptDto receiptDto) throws Exception {
        // validasi
        int adminId = receiptDto.getAdminId();
        Admin admin = adminRepository.findById(adminId).get();
        if (admin == null || !admin.getIsValid()) throw new Exception("Unauthorized");

        String orderNo = receiptDto.getOrderNo();
        List<Orders> orders = ordersRepository.findAllByOrderNo(orderNo);
        if (orders.size() == 0) throw new Exception("Order No not found with no " + orderNo);
        List<Receipt> receipts = receiptRepository.findAllByOrderNo(orderNo);
        if (receipts.size() > 0) throw new Exception("Order No have been completed with no " + orderNo);

        int paymentMethodId = receiptDto.getPaymentMethodId();
        PaymentMethod paymentMethod = paymentMethodRepository.findById(paymentMethodId).orElse(null);
        if (paymentMethod == null) throw new Exception("Payment method not found with id " + paymentMethodId);
        if (!paymentMethod.getIsValid()) throw new Exception("Payment method is not valid with id " + paymentMethodId);

        // generate receipt number
        Date date = new Date();
        String receiptNo = date.getTime() + "";

        List<Receipt> res = new ArrayList<Receipt>();
        List<ReceiptDto.Products> products = receiptDto.getProducts();
        for (ReceiptDto.Products product : products) {
            int productId = product.getProductId();
            int amount = product.getAmount();

            Product product1 = productRepository.findById(productId).orElse(null);
            if (product1 == null) throw new Exception("Product not found with id " + productId);
            if (!product1.getIsValid()) throw new Exception("Product is not valid with id " + productId);

            PriceAndStock priceAndStock = priceAndStockRepository.findById(product1.getPriceAndStock().getId()).orElse(null);
            if (priceAndStock == null) throw new Exception("Product price not found with id " + productId);
            if (priceAndStock.getStock() < amount) throw new Exception("Insufficient stocks with id " + productId);

            // update stocks
            priceAndStock.setStock(priceAndStock.getStock() - amount);
            priceAndStock.setModifiedBy(admin.getUsername());
            priceAndStock.setModifiedAt(new Date());
            priceAndStockRepository.save(priceAndStock);

            // save receipt
            Receipt receipt = new Receipt();
            receipt.setOrderNo(orderNo);
            receipt.setReceiptNo(receiptNo);
            receipt.setProduct(product1);
            receipt.setAmount(product.getAmount());
            receipt.setPrice(product1.getPriceAndStock().getPrice());
            receipt.setPaymentMethod(paymentMethod);
            receipt.setAdmin(admin);
            receipt.setCreatedAt(new Date());
            Receipt receipt1 = receiptRepository.save(receipt);
            res.add(receipt1);
        }

        return res;
    }
}
