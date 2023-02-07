package com.alterra.pos.repository;

import com.alterra.pos.entity.Orders;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrdersRepository extends JpaRepository<Orders, Integer> {
    List<Orders> findAllByOrderNo(String OrderNo);
}
