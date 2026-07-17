package com.ecommerce.storeServices.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Entity
@Table(name = "StoreProduct")
public class StoreProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name",nullable = false)
    @Size(min = 3,max =20)
    private String name;

    @Column(name = "description")
    @Size(min = 3,max = 1000)
    private String description;

    @ManyToOne
    @JoinColumn(name = "store_id",nullable = false)
    private Store store;

    @Column(name = "price",nullable = false)
    private BigDecimal price;

    @Column(name = "createdAt")
    private String CreatedAt;

    @Column(name = "updatedAt")
    private String UpdatedAt;


}
