package com.alterra.pos.service;

import com.alterra.pos.dto.OrdersDto;
import com.alterra.pos.dto.ResponseDto;
import com.alterra.pos.dto.Status;
import com.alterra.pos.entity.*;
import com.alterra.pos.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class OrdersService {
    @Autowired
    private OrdersRepository ordersRepository;
    @Autowired
    private PaymentMethodRepository paymentMethodRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private PriceAndStockRepository priceAndStockRepository;
    @Autowired
    private UserRepository userRepository;

    public List<Orders> getOrders() {
        return ordersRepository.findAll();
    }

    public List<Orders> getOrdersByOrderNo(String orderNo) {
        return ordersRepository.findAllByOrderNo(orderNo);
    }

    @Transactional(rollbackFor = Exception.class)
    public ResponseDto addOrders(OrdersDto ordersDto) {
        try {
            // validasi
            int paymentMethodId = ordersDto.getPaymentMethodId();
            PaymentMethod paymentMethod = paymentMethodRepository.findById(paymentMethodId).orElse(null);
            if (paymentMethod == null)
                return ResponseDto.builder().success(false).message("Payment method not found with id " + paymentMethodId).build();
            if (!paymentMethod.getIsValid())
                return ResponseDto.builder().success(false).message("Payment method is not valid with id " + paymentMethodId).build();

            String orderNo = ordersDto.getOrderNo();
            List<Orders> orders2 = ordersRepository.findAllByOrderNo(orderNo);
            if (orders2.size() != 0)
                return ResponseDto.builder().success(false).message("Orders already printed with Order No " + orderNo).build();

            int userId = ordersDto.getUserId();
            User user = userRepository.findById(userId).orElse(null);
            ;
            if (user == null)
                return ResponseDto.builder().success(false).message("User not found with id " + userId).build();
            if (!user.getIsValid())
                return ResponseDto.builder().success(false).message("User is not valid with id " + userId).build();
            if (user.getRole().name() != "ROLE_MEMBERSHIP")
                return ResponseDto.builder().success(false).message("Unauthorized").build();

            List<Orders> data = new ArrayList<Orders>();
            List<OrdersDto.Products> products = ordersDto.getProducts();
            for (OrdersDto.Products product : products) {
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

                // save
                Orders orders = new Orders();
                orders.setOrderNo(orderNo);
                orders.setProduct(product1);
                orders.setAmount(product.getAmount());
                orders.setPrice(product1.getPriceAndStock().getPrice());
                orders.setPaymentMethod(paymentMethod);
                orders.setUser(user);
                orders.setCreatedAt(new Date());
                Orders orders1 = ordersRepository.save(orders);
                data.add(orders1);
            }

            return ResponseDto.builder().success(true).message("Success create order").data(data).build();
        } catch (Exception e) {
            return ResponseDto.builder().success(false).message(e.getMessage()).build();
        }
    }
}
