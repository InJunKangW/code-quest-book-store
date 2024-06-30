package com.nhnacademy.bookstoreinjun.repository;

import com.nhnacademy.bookstoreinjun.entity.ProductCategory;
import java.util.List;
import java.util.Set;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

public interface ProductCategoryRepository extends JpaRepository<ProductCategory, Long> {

    /**
     * 카테고리명에 해당하는 데이터가 존재하는지 확인합니다. 카테고리명을 Unique 로 지정했기 때문에 중복 여부를 확인하기 위해 사용합니다.
     * @param categoryName 중복을 확인할 카테고리명
     * @return 해당 카테고리명이 이미 데이터베이스에 존재하는 지 여부
     */
    boolean existsByCategoryName(String categoryName);

    /**
     * 카테고리명에 해당하는 카테고리를 찾아냅니다. 카테고리명을 Unique 로 지정했기 때문에 단일 데이터가 반환됩니다.
     * @param categoryName 찾아낼 카테고리명
     * @return 해당 카테고리명을 갖는 카테고리
     */
    ProductCategory findByCategoryName(String categoryName);

    /**
     * 특정 카테고리명을 포함하는 모든 카테고리 리스트를 반환합니다.
     * @param pageable 페이징 처리를 위한 인자
     * @param categoryName 포함 여부를 확인할 카테고리명
     * @return 해당 카테고리명을 포함하는 모든 카테고리의 리스트
     */
    Page<ProductCategory> findAllByCategoryNameContaining(Pageable pageable, String categoryName);

    /**
     * 특정 카테고리의 1단계 하위 카테고리 리스트를 반환합니다.
     * @param parent 하위 카테고리를 찾을 상위 카테고리
     * @return 해당 상위 카테고리의 1단계 하위 카테고리 리스트
     */
    List<ProductCategory> findSubCategoriesByParentProductCategory(ProductCategory parent);
}
