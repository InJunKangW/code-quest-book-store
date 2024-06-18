package com.nhnacademy.bookstoreinjun.repository;


import com.nhnacademy.bookstoreinjun.entity.ProductTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductTagRepository extends JpaRepository<ProductTag, Long> {
}
