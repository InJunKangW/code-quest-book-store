package com.nhnacademy.bookstoreinjun.repository;


import com.nhnacademy.bookstoreinjun.entity.Product;
import com.nhnacademy.bookstoreinjun.entity.ProductTag;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductTagRepository extends JpaRepository<ProductTag, Long> {
    List<ProductTag> findByProduct(Product product);
}
