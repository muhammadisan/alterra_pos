package com.alterra.pos.controller;

import com.alterra.pos.dto.ReceiptDto;
import com.alterra.pos.dto.ResponseDto;
import com.alterra.pos.entity.*;
import com.alterra.pos.service.ReceiptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("receipts")
public class ReceiptController {
    @Autowired
    ReceiptService receiptService;

    @GetMapping
    public List<Receipt> getReceipts() {
        return receiptService.getReceipts();
    }

    @GetMapping("/orderNo/{orderNo}")
    public List<Receipt> getReceiptsByOrderNo(@PathVariable String orderNo) {
        return receiptService.getReceiptsByOrderNo(orderNo);
    }

    @GetMapping("/receiptNo/{receiptNo}")
    public List<Receipt> getReceiptsByReceiptNo(@PathVariable String receiptNo) {
        return receiptService.getReceiptsByReceiptNo(receiptNo);
    }

    @PostMapping
    public ResponseDto addReceipt(@Validated @RequestBody ReceiptDto receiptDto) {
        return receiptService.addReceipt(receiptDto);
    }
}
