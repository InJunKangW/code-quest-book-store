package com.nhnacademy.bookstoreinjun.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class CartRemoveType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cartRemoveTypeId;

    @Column(nullable = false)
    private String cartRemoveTypeName;
}
