package com.nhnacademy.bookstoreinjun.repository;

import com.nhnacademy.bookstoreinjun.entity.Product;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Product findByProductId(Long productId);

    Page<Product> findByProductNameContaining(Pageable pageable, String productName);


    @Modifying
    @Query("update Product p SET p.productViewCount = p.productViewCount + 1 where p.productId = :productId")
    void addProductViewCount(@Param("productId") Long productId);
}
