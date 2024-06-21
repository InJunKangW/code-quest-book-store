package com.nhnacademy.bookstoreinjun.entity;

import jakarta.persistence.*;

@Entity
public class Package {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long packageId;
    private String packageName;
    @ManyToOne
    @JoinColumn(name = "productId")
    private Product product;
}
