package com.alterra.pos.service;

import com.alterra.pos.dto.OrdersDto;
import com.alterra.pos.entity.Orders;
import com.alterra.pos.entity.PaymentMethod;
import com.alterra.pos.entity.PriceAndStock;
import com.alterra.pos.entity.Product;
import com.alterra.pos.repository.OrdersRepository;
import com.alterra.pos.repository.PaymentMethodRepository;
import com.alterra.pos.repository.PriceAndStockRepository;
import com.alterra.pos.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

    public List<Orders> getOrders() {
        return ordersRepository.findAll();
    }

    public List<Orders> getOrdersByOrderNo(String orderNo) {
        return ordersRepository.findAllByOrderNo(orderNo);
    }

    @Transactional(rollbackFor = Exception.class)
    public List<Orders> addOrders(OrdersDto ordersDto) throws Exception {
        // validasi
        int paymentMethodId = ordersDto.getPaymentMethodId();
        PaymentMethod paymentMethod = paymentMethodRepository.findById(paymentMethodId).orElse(null);
        if (paymentMethod == null) throw new Exception("Payment method not found with id " + paymentMethodId);
        if (!paymentMethod.getIsValid()) throw new Exception("Payment method is not valid with id " + paymentMethodId);

        String orderNo = ordersDto.getOrderNo();
        List<Orders> orders2 = ordersRepository.findAllByOrderNo(orderNo);
        if (orders2.size() != 0) throw new Exception("Orders already printed with Order No " + orderNo);

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
