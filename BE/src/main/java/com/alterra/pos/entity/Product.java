package com.alterra.pos.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.util.Date;

@Entity
@Table(name = "product")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Product {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "category_id")
//    @JsonIgnore
    private Category category;

    @Column(nullable = false)
    private String name;

    private String description;

    @Column(name = "is_valid", columnDefinition = "boolean default true")
    private Boolean isValid = true;

    @Column(name = "created_by",  nullable = false, updatable = false)
    private String createdBy = "system";

    @Column(name = "created_at", nullable = false, updatable = false)
    @CreationTimestamp
    private Date createdAt;

    @Column(name = "modified_by")
    private String modifiedBy;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "modified_at")
    @LastModifiedDate
    private Date modifiedAt;
}
