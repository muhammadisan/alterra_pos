package com.alterra.pos.service;

import com.alterra.pos.entity.Receipt;
import com.alterra.pos.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReceiptService {
    @Autowired
    private OrdersRepository ordersRepository;
    @Autowired
    private ReceiptRepository receiptRepository;

    public ReceiptService(ReceiptRepository receiptRepository) {
        this.receiptRepository = receiptRepository;
    }

    public List<Receipt> getReceipts() {
        return receiptRepository.findAll();
    }

    public List<Receipt> getReceiptsByOrderNo(String orderNo) { return receiptRepository.findAllByOrderNo(orderNo); }

    public List<Receipt> getReceiptsByReceiptNo(String receiptNo) { return receiptRepository.findAllByReceiptNo(receiptNo); }
}
