package com.nhnacademy.bookstoreinjun.util;

import com.nhnacademy.bookstoreinjun.entity.ProductCategory;
import com.nhnacademy.bookstoreinjun.exception.NotFoundNameException;
import com.nhnacademy.bookstoreinjun.repository.ProductCategoryRepository;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FindAllSubCategoriesUtil {
    private final ProductCategoryRepository productCategoryRepository;

    public void findAllSubcategories(String parentName, Set<ProductCategory> categoryNames) {
        ProductCategory parentCategory = productCategoryRepository.findByCategoryName(parentName);
        if (parentCategory == null) {
            throw new NotFoundNameException("category", parentName);
        }else {
            categoryNames.add(parentCategory);

            List<ProductCategory> subcategories = productCategoryRepository.findSubCategoriesByParentProductCategory(parentCategory);
            for (ProductCategory subcategory : subcategories) {
                findAllSubcategories(subcategory.getCategoryName(), categoryNames);
            }
        }
    }
}
