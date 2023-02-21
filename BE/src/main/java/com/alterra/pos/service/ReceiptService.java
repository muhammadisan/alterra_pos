package com.alterra.pos.service;

import com.alterra.pos.dto.ReceiptDto;
import com.alterra.pos.dto.ResponseDto;
import com.alterra.pos.entity.*;
import com.alterra.pos.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
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
    @Autowired
    SpringTemplateEngine templateEngine;

//    public List<Receipt> getReceipts() {
//        return receiptRepository.findAll();
//    }

    public List<Map> getReceipts() {
        List<List<Object>> receipts = receiptRepository.findAllGroupByReceiptNo();
        List<Map> response = new ArrayList<Map>();
        for (List<Object> receipt: receipts) {
            Map map = new HashMap();
            map.put("id", receipt.get(0));
            map.put("orderNo", receipt.get(1));
            map.put("receiptNo", receipt.get(2));
            map.put("paymentMethodId", receipt.get(3));
            map.put("paymentMethod", receipt.get(4));
            map.put("userId", receipt.get(5));
            map.put("username", receipt.get(6));
            map.put("createdAt", receipt.get(7));

            response.add(map);
        }
        return response;
    }

    public List<Receipt> getReceiptsByOrderNo(String orderNo) {
        return receiptRepository.findAllByOrderNo(orderNo);
    }

    public List<Receipt> getReceiptsByReceiptNo(String receiptNo) {
        return receiptRepository.findAllByReceiptNo(receiptNo);
    }

    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity<?> addReceipt(ReceiptDto receiptDto) {
        try {
            // validasi
            int adminId = receiptDto.getUserId();
            User admin = userRepository.findById(adminId).get();
            if (admin == null || !admin.getIsValid() || admin.getRole().name() != "ROLE_ADMIN")
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(ResponseDto.builder().success(false).message("Unauthorized").build());

            String orderNo = receiptDto.getOrderNo();
            if (orderNo == null)
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(ResponseDto.builder().success(false).message("Invalid Order No").build());

            List<Orders> orders = ordersRepository.findAllByOrderNo(orderNo);
            if (orders.size() == 0)
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(ResponseDto.builder().success(false).message("Order No "+orderNo+" not found").build());
            List<Receipt> receipts = receiptRepository.findAllByOrderNo(orderNo);
            if (receipts.size() > 0)
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(ResponseDto.builder().success(false).message("Order "+orderNo+" have been completed").build());

            int paymentMethodId = receiptDto.getPaymentMethodId();
            PaymentMethod paymentMethod = paymentMethodRepository.findById(paymentMethodId).orElse(null);
            if (paymentMethod == null)
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(ResponseDto.builder().success(false).message("Payment method not found with id "+paymentMethodId).build());
            if (!paymentMethod.getIsValid())
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(ResponseDto.builder().success(false).message("Payment method is not valid with id "+paymentMethodId).build());

            double subtotal = 0;
            double discount = 0;
            double total = 0;

            long date = new Date().getTime();
            String receiptNo = date + "";

            Format formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String invoiceDate = formatter.format(date);

            List<Receipt> data = new ArrayList<Receipt>();
            List<ReceiptDto.Products> products = receiptDto.getProducts();
            for (ReceiptDto.Products product : products) {
                int productId = product.getProductId();
                int amount = product.getAmount();

                Product product1 = productRepository.findById(productId).orElse(null);
                if (product1 == null)
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                            .body(ResponseDto.builder().success(false).message("Product not found with id "+productId).build());
                if (!product1.getIsValid())
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                            .body(ResponseDto.builder().success(false).message("Product is not valid with id "+productId).build());

                PriceAndStock priceAndStock = priceAndStockRepository.findById(product1.getPriceAndStock().getId()).orElse(null);
                if (priceAndStock == null)
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                            .body(ResponseDto.builder().success(false).message("Product price not found with id "+productId).build());
                if (priceAndStock.getStock() < amount)
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                            .body(ResponseDto.builder().success(false).message("Insufficient stocks with id "+productId).build());

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
                receipt.setUser(admin);
                receipt.setCreatedAt(new Date());
                Receipt receipt1 = receiptRepository.save(receipt);
                data.add(receipt1);
                subtotal += product.getAmount() * product1.getPriceAndStock().getPrice();
            }
            total = subtotal - discount;

            Context context = new Context();
            context.setVariable("data", data);
            context.setVariable("invoiceDate", invoiceDate);
            context.setVariable("subtotal", subtotal);
            context.setVariable("discount", discount);
            context.setVariable("total", total);

            String htmlContentToRender = templateEngine.process("receipt-template", context);
            String xHtml = xhtmlConvert(htmlContentToRender);

            ITextRenderer renderer = new ITextRenderer();

            String baseUrl = FileSystems
                    .getDefault()
                    .getPath("src", "main", "resources","templates")
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
