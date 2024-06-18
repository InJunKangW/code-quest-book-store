package com.nhnacademy.bookstoreinjun.repository;

import com.nhnacademy.bookstoreinjun.entity.ProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductCategoryRepository extends JpaRepository<ProductCategory, Long> {
}
