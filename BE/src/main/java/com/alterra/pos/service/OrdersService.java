package com.alterra.pos.service;

import com.alterra.pos.dto.OrdersDto;
import com.alterra.pos.dto.ResponseDto;
import com.alterra.pos.entity.*;
import com.alterra.pos.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring4.SpringTemplateEngine;
import org.w3c.tidy.Tidy;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.*;
import java.nio.file.FileSystems;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.*;

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
    @Autowired
    SpringTemplateEngine templateEngine;

    public List<Map> getOrders() {
        List<List<Object>> orders = ordersRepository.findAllGroupByOrderNo();
        List<Map> response = new ArrayList<Map>();
        for (List<Object> order : orders) {
            Map map = new HashMap();
            map.put("id", order.get(0));
            map.put("orderNo", order.get(1));
            map.put("receiptNo", "");
            map.put("paymentMethodId", order.get(2));
            map.put("paymentMethod", order.get(3));
            map.put("userId", order.get(4));
            map.put("username", order.get(5));
            map.put("createdAt", order.get(6));

            response.add(map);
        }
        return response;
        // return ordersRepository.findAll();
    }

    public List<Orders> getOrdersByOrderNo(String orderNo) {
        return ordersRepository.findAllByOrderNo(orderNo);
    }

    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity<?> addOrders(OrdersDto ordersDto) {
        try {
            // validasi
            int paymentMethodId = ordersDto.getPaymentMethodId();
            PaymentMethod paymentMethod = paymentMethodRepository.findById(paymentMethodId).orElse(null);
            if (paymentMethod == null)
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(ResponseDto.builder().success(false)
                                .message("Payment method not found with id " + paymentMethodId).build());
            if (!paymentMethod.getIsValid())
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(ResponseDto.builder().success(false)
                                .message("Payment method is not valid with id " + paymentMethodId).build());

            int userId = ordersDto.getUserId();
            User user = userRepository.findById(userId).orElse(null);

            if (user == null)
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(ResponseDto.builder().success(false).message("User not found with id " + userId).build());
            if (!user.getIsValid())
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(ResponseDto.builder().success(false).message("User is not valid with id " + userId)
                                .build());
            if (user.getRole().name() != "ROLE_MEMBERSHIP")
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(ResponseDto.builder().success(false).message("Unauthorized").build());

            double subtotal = 0;
            double discount = 0;
            double total = 0;

            long date = new Date().getTime();
            String orderNo = date + "";

            Format formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String invoiceDate = formatter.format(date);

            List<Orders> data = new ArrayList<Orders>();
            List<OrdersDto.Products> products = ordersDto.getProducts();
            for (OrdersDto.Products product : products) {
                int productId = product.getProductId();
                int amount = product.getAmount();

                Product product1 = productRepository.findById(productId).orElse(null);
                if (product1 == null)
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                            .body(ResponseDto.builder().success(false).message("Product not found with id " + productId)
                                    .build());
                if (!product1.getIsValid())
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                            .body(ResponseDto.builder().success(false)
                                    .message("Product is not valid with id " + productId).build());

                PriceAndStock priceAndStock = priceAndStockRepository.findById(product1.getPriceAndStock().getId())
                        .orElse(null);
                if (priceAndStock == null)
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                            .body(ResponseDto.builder().success(false)
                                    .message("Product price not found with id " + productId).build());
                if (priceAndStock.getStock() < amount)
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                            .body(ResponseDto.builder().success(false)
                                    .message("Insufficient stocks with id " + productId).build());

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
                subtotal += product.getAmount() * product1.getPriceAndStock().getPrice();
            }
            total = subtotal - discount;

            Context context = new Context();
            context.setVariable("data", data);
            context.setVariable("invoiceDate", invoiceDate);
            context.setVariable("subtotal", subtotal);
            context.setVariable("discount", discount);
            context.setVariable("total", total);

            String htmlContentToRender = templateEngine.process("order-template", context);
            String xHtml = xhtmlConvert(htmlContentToRender);

            ITextRenderer renderer = new ITextRenderer();

            String baseUrl = FileSystems
                    .getDefault()
                    .getPath("src", "main", "resources", "templates")
                    .toUri()
                    .toURL()
                    .toString();
            renderer.setDocumentFromString(xHtml, baseUrl);
            renderer.layout();

            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            OutputStream outputStream = new FileOutputStream("src//invoice.pdf");
            renderer.createPDF(outputStream);
            renderer.createPDF(baos);
            outputStream.close();
            baos.close();

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(baos.toByteArray());
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(ResponseDto.builder().success(false).message(e.getMessage()).build());
        }
    }

    private String xhtmlConvert(String html) throws UnsupportedEncodingException {
        Tidy tidy = new Tidy();
        tidy.setInputEncoding("UTF-8");
        tidy.setOutputEncoding("UTF-8");
        tidy.setXHTML(true);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(html.getBytes("UTF-8"));
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        tidy.parseDOM(inputStream, outputStream);
        return outputStream.toString("UTF-8");
    }
}
