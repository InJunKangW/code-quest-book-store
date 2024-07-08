package com.nhnacademy.bookstoreinjun.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductCategoryRelation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productCategoryRelationId;

    @JoinColumn(name = "productId", nullable = false)
    @ManyToOne
    private Product product;

    @JoinColumn(name = "productCategoryId", nullable = false)
    @ManyToOne
    private ProductCategory productCategory;
}
