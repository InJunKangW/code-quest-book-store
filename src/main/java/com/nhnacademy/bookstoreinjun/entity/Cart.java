package com.nhnacademy.bookstoreinjun.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cartId;

    @NotNull
    @Min(1)
    private Long clientId;

    @ManyToOne
    @JoinColumn(name = "productId")
    private Product product;

    @NotNull
    @Min(1)
    private Long quantity;

    public void addProduct() {
        quantity++;
    }

    public void setProductQuantity(Long quantity) {
        this.quantity = quantity;
    }
}
