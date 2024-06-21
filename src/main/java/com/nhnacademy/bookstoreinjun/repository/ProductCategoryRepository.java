package com.nhnacademy.bookstoreinjun.repository;

import com.nhnacademy.bookstoreinjun.entity.Product;
import com.nhnacademy.bookstoreinjun.entity.ProductCategoryRelation;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductCategoryRepository extends JpaRepository<ProductCategoryRelation, Long> {
    List<ProductCategoryRelation> findByProduct(Product product);
}
