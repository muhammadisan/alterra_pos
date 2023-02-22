package com.alterra.pos.controller;

import com.alterra.pos.dto.OrdersDto;
import com.alterra.pos.entity.*;
import com.alterra.pos.service.OrdersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("orders")
public class OrdersController {
    @Autowired
    private OrdersService ordersService;

    @GetMapping
    public List<Map> getOrders() {
        return ordersService.getOrders();
    }

    @GetMapping("/orderNo/{orderNo}")
    public List<Orders> getOrdersByOrderNo(@PathVariable String orderNo) {
        return ordersService.getOrdersByOrderNo(orderNo);
    }

    @PostMapping
    public ResponseEntity<?> addOrders(@Validated @RequestBody OrdersDto ordersDto) {
        return ordersService.addOrders(ordersDto);
    }
}
