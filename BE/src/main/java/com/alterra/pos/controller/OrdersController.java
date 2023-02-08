package com.alterra.pos.controller;

import com.alterra.pos.dto.OrdersDto;
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
@RequestMapping("orders")
public class OrdersController {
    @Autowired
    private OrdersRepository ordersRepository;
    @Autowired
    private PaymentMethodRepository paymentMethodRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private PriceAndStockRepository priceAndStockRepository;

    @GetMapping
    public List<Orders> getOrders() {
        return ordersRepository.findAll();
    }

    @GetMapping("/orderNo/{orderNo}")
    public List<Orders> getOrdersByOrderNo(@PathVariable String orderNo) { return ordersRepository.findAllByOrderNo(orderNo); }

    @PostMapping
    @Transactional(rollbackFor = Exception.class)
    public List<Orders> addOrders(@Validated @RequestBody OrdersDto ordersDto) throws Exception {
        // validasi
        int paymentMethodId = ordersDto.getPaymentMethodId();
        PaymentMethod paymentMethod = paymentMethodRepository.findById(paymentMethodId).orElse(null);
        if (paymentMethod == null) throw new Exception("Payment method not found with id " + paymentMethodId);
        if (!paymentMethod.getIsValid()) throw new Exception("Payment method is not valid with id " + paymentMethodId);

        // generate order number
        Date date = new Date();
        String orderNo = date.getTime() + "";

        List<Orders> res = new ArrayList<Orders>();
        List<OrdersDto.Products> products = ordersDto.getProducts();
        for (OrdersDto.Products product : products) {
            int productId = product.getProductId();
            int amount = product.getAmount();

            Product product1 = productRepository.findById(productId).orElse(null);
            if (product1 == null) throw new Exception("Product not found with id " + productId);
            if (!product1.getIsValid()) throw new Exception("Product is not valid with id " + productId);

            PriceAndStock priceAndStock = priceAndStockRepository.findById(product1.getPriceAndStock().getId()).orElse(null);
            if (priceAndStock == null) throw new Exception("Product price not found with id " + productId);
            if (priceAndStock.getStock() < amount) throw new Exception("Insufficient stocks with id " + productId);

            // save
            Orders orders = new Orders();
            orders.setOrderNo(orderNo);
            orders.setProduct(product1);
            orders.setAmount(product.getAmount());
            orders.setPrice(product1.getPriceAndStock().getPrice());
            orders.setPaymentMethod(paymentMethod);
            orders.setCreatedAt(new Date());
            Orders orders1 = ordersRepository.save(orders);
            res.add(orders1);
        }

        return res;
    }
}
