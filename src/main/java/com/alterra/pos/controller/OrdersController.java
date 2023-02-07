package com.alterra.pos.controller;

import com.alterra.pos.dto.OrdersDto;
import com.alterra.pos.entity.Admin;
import com.alterra.pos.entity.Orders;
import com.alterra.pos.repository.AdminRepository;
import com.alterra.pos.repository.OrdersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("orders")
public class OrdersController {
    @Autowired
    private OrdersRepository ordersRepository;

    @GetMapping
    public List<Orders> getOrders() {
        return ordersRepository.findAll();
    }

    @GetMapping("/{id}")
    public Optional<Orders> getOrdersById(@PathVariable Integer id) { return ordersRepository.findById(id); }

    @GetMapping("/{orderNo}")
    public List<Orders> getOrdersByOrderNo(@PathVariable String orderNo) { return ordersRepository.findAllByOrderNo(orderNo); }

//    @PostMapping
//    public OrdersDto addOrders(@Validated @RequestBody OrdersDto ordersDto) {
//        List<OrdersDto.Products> products = ordersDto.getProducts();
//        int paymentMethodId = ordersDto.getPaymentMethodId();
//
//
//        return ordersRepository.save(admin);
//    }
}
