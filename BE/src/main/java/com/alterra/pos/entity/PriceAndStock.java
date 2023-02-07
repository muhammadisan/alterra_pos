package com.alterra.pos.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.data.annotation.LastModifiedDate;

import java.util.Date;

@Entity
@Table(name = "price_and_stock")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PriceAndStock {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(nullable = false)
    private Double price;

    @Column(nullable = false)
    private Integer stock;

    @Column(name = "modified_by")
    private String modifiedBy = "system";

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "modified_at")
    @LastModifiedDate
    private Date modifiedAt;
}
