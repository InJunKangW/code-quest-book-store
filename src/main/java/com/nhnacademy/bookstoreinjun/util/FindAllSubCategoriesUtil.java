package com.nhnacademy.bookstoreinjun.util;

import com.nhnacademy.bookstoreinjun.entity.ProductCategory;
import java.util.Set;
import com.nhnacademy.bookstoreinjun.exception.NotFoundNameException;

public interface FindAllSubCategoriesUtil {
    /**
     * 해당 카테고리명의 카테고리의 모든 하위 카테고리 set 을 반환합니다.
     * @param parentName 하위 카테고리를 찾을 카테고리의 카테고리명
     * @return 해당 카테고리명의 카테고리의 모든 하위 카테고리 set
     * @throws NotFoundNameException parentName 에 해당하는 카테고리가 데이터베이스에 존재하지 않을 경우 발생합니다
     */
    Set<ProductCategory> getAllSubcategorySet(String parentName);
}
