package com.nhnacademy.bookstoreinjun.repository;

import com.nhnacademy.bookstoreinjun.entity.Product;
import com.nhnacademy.bookstoreinjun.entity.ProductCategory;
import com.nhnacademy.bookstoreinjun.entity.ProductCategoryRelation;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductCategoryRelationRepository extends JpaRepository<ProductCategoryRelation, Long> {

    boolean existsByProductCategoryIn(Set<ProductCategory> productCategorySet);
    /**
     * product 와 관련된 모든 카테고리 관계를 삭제합니다. (카테고리 자체 삭제가 아닙니다.)
     * 상품 업데이트 시 카테고리를 초기화 후 새로운 카테고리 관계를 맺기 위해 사용합니다.
     * @param product 찾아낼 기준이 되는 product
     */
    void deleteByProduct(Product product);
}
