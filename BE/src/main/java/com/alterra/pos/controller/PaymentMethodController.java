package com.alterra.pos.controller;

import com.alterra.pos.entity.PaymentMethod;
import com.alterra.pos.repository.PaymentMethodRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("paymentMethod")
public class PaymentMethodController {
    @Autowired
    private PaymentMethodRepository paymentMethodRepository;

    @GetMapping
    public List<PaymentMethod> getPaymentMethods() {
        return paymentMethodRepository.findAllByIsValidTrue();
    }

    @GetMapping("/{id}")
    public Optional<PaymentMethod> getPaymentMethodById(@PathVariable Integer id) { return paymentMethodRepository.findById(id); }

    @PostMapping
    public PaymentMethod addPaymentMethod(@Validated @RequestBody PaymentMethod paymentMethod) {
        return paymentMethodRepository.save(paymentMethod);
    }
}
