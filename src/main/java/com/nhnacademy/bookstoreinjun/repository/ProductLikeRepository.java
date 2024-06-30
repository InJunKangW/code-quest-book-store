package com.nhnacademy.bookstoreinjun.repository;

import com.nhnacademy.bookstoreinjun.entity.Product;
import com.nhnacademy.bookstoreinjun.entity.ProductLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface ProductLikeRepository extends JpaRepository<ProductLike, Long> {
    /**
     * 특정 유저가 특정 상품에 대한 좋아요를 이미 남겼는지 여부를 확인합니다.
     * @param userId 좋아요 요청을 보낸 유저의 아이디
     * @param product 유저가 좋아요를 누른 상품의 아이디
     * @return 이미 좋아요를 눌렀는 지의 여부
     */
    boolean existsByClientIdAndProduct(Long userId, Product product);
}
