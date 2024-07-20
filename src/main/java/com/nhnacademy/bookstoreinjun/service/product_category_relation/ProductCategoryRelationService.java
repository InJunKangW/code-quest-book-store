package com.nhnacademy.bookstoreinjun.service.product_category_relation;

import com.nhnacademy.bookstoreinjun.entity.Product;
import com.nhnacademy.bookstoreinjun.entity.ProductCategoryRelation;
import com.nhnacademy.bookstoreinjun.exception.NullProductException;
import com.nhnacademy.bookstoreinjun.exception.InconsistentEntityException;
import com.nhnacademy.bookstoreinjun.exception.NotFoundIdException;

public interface ProductCategoryRelationService {
    /**
     * 특정 상품에 대한 카테고리의 관계를 저장합니다.
     * @param productCategoryRelation 저장할 상품_카테고리_관계
     *                                상품 id와 카테고리 id를 갖습니다.
     * @throws NullProductException 요청의 상품이 null 이거나, id가 null 일 때 발생합니다.
     * @throws NotFoundIdException 요청의 상품 id에 해당하는 상품이 데이터베이스에 존재하지 않을 때 발생합니다.
     * @throws InconsistentEntityException 상품의 id에 해당하는 상품이, 요청의 상품과 일치하지 않을 때 발생합니다.
     */
    void saveProductCategoryRelation(ProductCategoryRelation productCategoryRelation);

    /**
     * 특정 상품에 대한 카테고리의 관계를 초기화합니다.
     * @param product 카테고리 관계를 초기화할 상품
     * @throws NullProductException 상품이 null 이거나, id가 null 일 때 발생합니다.
     * @throws NotFoundIdException 상품 id에 해당하는 상품이 데이터베이스에 존재하지 않을 때 발생합니다.
     * @throws InconsistentEntityException 상품의 id에 해당하는 상품이, 상품과 일치하지 않을 때 발생합니다.
     */
    void clearProductCategoryRelationsByProduct(Product product);
}
