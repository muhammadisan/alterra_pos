package com.alterra.pos.service;

import com.alterra.pos.dto.ReceiptDto;
import com.alterra.pos.dto.ResponseDto;
import com.alterra.pos.entity.*;
import com.alterra.pos.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
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
    private UserRepository userRepository;

    public List<Receipt> getReceipts() {
        return receiptRepository.findAll();
    }

    public List<Receipt> getReceiptsByOrderNo(String orderNo) {
        return receiptRepository.findAllByOrderNo(orderNo);
    }

    public List<Receipt> getReceiptsByReceiptNo(String receiptNo) {
        return receiptRepository.findAllByReceiptNo(receiptNo);
    }

    @Transactional(rollbackFor = Exception.class)
    public ResponseDto addReceipt(ReceiptDto receiptDto) {
        try {
            // validasi
            int adminId = receiptDto.getUserId();
            User admin = userRepository.findById(adminId).get();
            if (admin == null || !admin.getIsValid() || admin.getRole().name() != "ROLE_ADMIN")
                return ResponseDto.builder().success(false).message("Unauthorized").build();

            String orderNo = receiptDto.getOrderNo();
            List<Orders> orders = ordersRepository.findAllByOrderNo(orderNo);
            if (orders.size() == 0)
                return ResponseDto.builder().success(false).message("Order No not found with no " + orderNo).build();
            List<Receipt> receipts = receiptRepository.findAllByOrderNo(orderNo);
            if (receipts.size() > 0)
                return ResponseDto.builder().success(false).message("Order No have been completed with no " + orderNo).build();

            int paymentMethodId = receiptDto.getPaymentMethodId();
            PaymentMethod paymentMethod = paymentMethodRepository.findById(paymentMethodId).orElse(null);
            if (paymentMethod == null)
                return ResponseDto.builder().success(false).message("Payment method not found with id " + paymentMethodId).build();
            if (!paymentMethod.getIsValid())
                return ResponseDto.builder().success(false).message("Payment method is not valid with id " + paymentMethodId).build();

            List<Receipt> data = new ArrayList<Receipt>();
            List<ReceiptDto.Products> products = receiptDto.getProducts();
            for (ReceiptDto.Products product : products) {
                int productId = product.getProductId();
                int amount = product.getAmount();

                Product product1 = productRepository.findById(productId).orElse(null);
                if (product1 == null)
                    return ResponseDto.builder().success(false).message("Product not found with id " + productId).build();
                if (!product1.getIsValid())
                    return ResponseDto.builder().success(false).message("Product is not valid with id " + productId).build();

                PriceAndStock priceAndStock = priceAndStockRepository.findById(product1.getPriceAndStock().getId()).orElse(null);
                if (priceAndStock == null)
                    return ResponseDto.builder().success(false).message("Product price not found with id " + productId).build();
                if (priceAndStock.getStock() < amount)
                    return ResponseDto.builder().success(false).message("Insufficient stocks with id " + productId).build();

                // update stocks
                priceAndStock.setStock(priceAndStock.getStock() - amount);
                priceAndStock.setModifiedBy(admin.getUsername());
                priceAndStock.setModifiedAt(new Date());
                priceAndStockRepository.save(priceAndStock);

                // save receipt
                Receipt receipt = new Receipt();
                receipt.setOrderNo(orderNo);
                receipt.setReceiptNo(receiptDto.getReceiptNo());
                receipt.setProduct(product1);
                receipt.setAmount(product.getAmount());
                receipt.setPrice(product1.getPriceAndStock().getPrice());
                receipt.setPaymentMethod(paymentMethod);
                receipt.setUser(admin);
                receipt.setCreatedAt(new Date());
                Receipt receipt1 = receiptRepository.save(receipt);
                data.add(receipt1);
            }

            return ResponseDto.builder().success(true).message("Success create receipt").data(data).build();
        } catch (Exception e) {
            return ResponseDto.builder().success(false).message(e.getMessage()).build();
        }
    }
}
