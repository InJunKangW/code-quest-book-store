package com.nhnacademy.bookstoreinjun.repository;

import com.nhnacademy.bookstoreinjun.entity.Cart;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Long> {

    List<Cart> findByClientIdAndProduct_ProductIdAndCartRemoveTypeIsNull(Long clientId, Long productId);

    List<Cart> findAllByClientIdAndCartRemoveTypeIsNull(Long clientId);
}
