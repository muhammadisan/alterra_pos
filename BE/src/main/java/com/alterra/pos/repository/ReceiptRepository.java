package com.alterra.pos.repository;

import com.alterra.pos.entity.Receipt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReceiptRepository extends JpaRepository<Receipt, Integer> {
    @Query("SELECT MIN(r.id), r.orderNo, r.receiptNo, r.paymentMethod.id, r.paymentMethod.payment, r.user.id, r.user.name, MAX(r.createdAt)" +
            " FROM Receipt AS r" +
            " GROUP BY r.orderNo, r.receiptNo, r.paymentMethod.id, r.paymentMethod.payment, r.user.id, r.user.name")
    List<List<Object>> findAllGroupByReceiptNo();

    @Query("SELECT r FROM Receipt r WHERE r.orderNo = ?1")
    List<Receipt> findAllByOrderNo(String OrderNo);

    @Query("SELECT r FROM Receipt r WHERE r.receiptNo = ?1")
    List<Receipt> findAllByReceiptNo(String receiptNo);
}
