package com.alterra.pos.repository;

import com.alterra.pos.entity.Receipt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReceiptRepository extends JpaRepository<Receipt, Integer> {
    @Query("SELECT r FROM Receipt r WHERE r.orderNo = ?1")
    List<Receipt> findAllByOrderNo(String OrderNo);

    @Query("SELECT r FROM Receipt r WHERE r.receiptNo = ?1")
    List<Receipt> findAllByReceiptNo(String receiptNo);
}
