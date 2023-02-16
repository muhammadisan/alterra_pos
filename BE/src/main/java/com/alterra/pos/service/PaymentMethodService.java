package com.alterra.pos.service;

import com.alterra.pos.entity.PaymentMethod;
import com.alterra.pos.repository.PaymentMethodRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PaymentMethodService {
    @Autowired
    private PaymentMethodRepository paymentMethodRepository;

    public List<PaymentMethod> getPaymentMethods() {
        return paymentMethodRepository.findAllByIsValidTrue();
    }

    public Optional<PaymentMethod> getPaymentMethodById(Integer id) {
        return paymentMethodRepository.findById(id);
    }

    public PaymentMethod addPaymentMethod(PaymentMethod paymentMethod) {
        return paymentMethodRepository.save(paymentMethod);
    }
}
