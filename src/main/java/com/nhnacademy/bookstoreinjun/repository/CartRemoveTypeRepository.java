package com.nhnacademy.bookstoreinjun.repository;

import com.nhnacademy.bookstoreinjun.entity.CartRemoveType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRemoveTypeRepository extends JpaRepository<CartRemoveType, Long> {
    CartRemoveType findByCartRemoveTypeName(String cartRemoveTypeName);
}
