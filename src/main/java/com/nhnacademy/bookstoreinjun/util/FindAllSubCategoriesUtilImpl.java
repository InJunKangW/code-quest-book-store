package com.nhnacademy.bookstoreinjun.util;

import com.nhnacademy.bookstoreinjun.entity.ProductCategory;
import com.nhnacademy.bookstoreinjun.exception.NotFoundIdException;
import com.nhnacademy.bookstoreinjun.repository.ProductCategoryRepository;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class FindAllSubCategoriesUtilImpl implements FindAllSubCategoriesUtil {
    private final ProductCategoryRepository productCategoryRepository;


    private void findAllSubcategories(Long parentId, Set<ProductCategory> categoryNames) {
        ProductCategory parentCategory = productCategoryRepository.findById(parentId).orElseThrow(() -> new NotFoundIdException("category", parentId));
        categoryNames.add(parentCategory);
        List<ProductCategory> subcategories = productCategoryRepository.findSubCategoriesByParentProductCategory(parentCategory);
        for (ProductCategory subcategory : subcategories) {
            findAllSubcategories(subcategory.getProductCategoryId(), categoryNames);
        }
    }

    public Set<ProductCategory> getAllSubcategorySet(Long parentId) {
        Set<ProductCategory> subCategorySet = new LinkedHashSet<>();
        findAllSubcategories(parentId, subCategorySet);
        return subCategorySet;
    }
}
