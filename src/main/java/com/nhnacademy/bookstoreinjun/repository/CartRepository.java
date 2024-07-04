package com.nhnacademy.bookstoreinjun.repository;

import com.nhnacademy.bookstoreinjun.entity.Cart;
import com.nhnacademy.bookstoreinjun.entity.Product;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Long> {
    List<Cart> findAllByClientId(Long clientId);

    void deleteByClientId(Long clientId);

    void deleteByClientIdAndProduct(Long clientId, Product product);

    Cart findByClientIdAndProduct(Long clientId, Product product);

    Cart findByClientIdAndProduct_ProductId(Long clientId, Long productId);
}
