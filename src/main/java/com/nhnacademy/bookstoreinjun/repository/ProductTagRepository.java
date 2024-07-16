package com.nhnacademy.bookstoreinjun.repository;


import com.nhnacademy.bookstoreinjun.entity.Product;
import com.nhnacademy.bookstoreinjun.entity.ProductTag;
import com.nhnacademy.bookstoreinjun.entity.Tag;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductTagRepository extends JpaRepository<ProductTag, Long> {
    /**
     * 특정 product 에 달린 모든 태그를 찾아내기 위해 사용합니다.
     * @param product 찾아낼 기준이 되는 product
     * @return 해당 product 에 달린 모든 태그 관계
     */
    List<ProductTag> findByProduct(Product product);

    boolean existsByTag(Tag tag);

    /**
     * product 와 관련된 모든 태그 관계를 삭제합니다. (태그 자체 삭제가 아닙니다.) 상품 업데이트 시 태그를 초기화 후 새로운 태그 관계를 맺기 위해 사용합니다.
     * @param product 찾아낼 기준이 되는 product
     */
    void deleteByProduct(Product product);

    long countByTag(Tag tag);
}
