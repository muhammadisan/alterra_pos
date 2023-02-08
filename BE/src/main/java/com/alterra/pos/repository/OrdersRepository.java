package com.alterra.pos.repository;

import com.alterra.pos.entity.Orders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrdersRepository extends JpaRepository<Orders, Integer> {
    @Query("SELECT o from Orders o WHERE o.orderNo = ?1")
    List<Orders> findAllByOrderNo(String OrderNo);
}
