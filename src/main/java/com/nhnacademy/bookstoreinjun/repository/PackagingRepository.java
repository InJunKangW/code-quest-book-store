package com.nhnacademy.bookstoreinjun.repository;

import com.nhnacademy.bookstoreinjun.entity.Packaging;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PackagingRepository extends JpaRepository<Packaging, Long> {
    Optional<Packaging> findByProduct_ProductId(Long productId);
    List<Packaging> findByProduct_ProductState(Integer productState);
    Page<Packaging> findByProduct_ProductState(Integer productState, Pageable pageable);
}
