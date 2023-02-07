package com.alterra.pos.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDate;
import java.util.Date;

@Entity
@Table(name = "discount")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Discount {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private Product product;

    private Double percent;

    private Double nominal;

    private Date discount_start;

    private Date discount_end;

    @Column(name = "is_valid", columnDefinition = "boolean default true")
    private Boolean isValid = true;

    @Column(name = "created_by", nullable = false, updatable = false)
    private String createdBy = "system";

    @Column(name = "created_at", nullable = false, updatable = false)
    @CreationTimestamp
    private Date createdAt;
}
