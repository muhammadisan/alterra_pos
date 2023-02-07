package com.alterra.pos.repository;

import com.alterra.pos.entity.PaymentMethod;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PaymentMethodRepository extends JpaRepository<PaymentMethod, Integer> {
    @Query
    List<PaymentMethod> findAllByIsValidTrue();

    @Override
    Optional<PaymentMethod> findById(Integer id);
}
