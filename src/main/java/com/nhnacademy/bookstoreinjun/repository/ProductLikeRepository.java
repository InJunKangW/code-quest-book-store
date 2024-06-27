package com.nhnacademy.bookstoreinjun.repository;

import com.nhnacademy.bookstoreinjun.entity.Product;
import com.nhnacademy.bookstoreinjun.entity.ProductLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface ProductLikeRepository extends JpaRepository<ProductLike, Long> {
    boolean existsByClientIdAndProduct(Long userId, Product product);
}
