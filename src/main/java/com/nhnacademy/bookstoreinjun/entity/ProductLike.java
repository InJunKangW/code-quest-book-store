package com.nhnacademy.bookstoreinjun.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "product_like", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"clientId", "productId"})
})
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ProductLike {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productLikeId;

    private Long clientId;

    @ManyToOne
    @JoinColumn(name = "productId")
    private Product product;
}
