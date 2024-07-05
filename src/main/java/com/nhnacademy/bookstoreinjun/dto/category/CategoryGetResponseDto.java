package com.nhnacademy.bookstoreinjun.dto.category;

import com.nhnacademy.bookstoreinjun.entity.ProductCategory;
import java.util.List;
import lombok.Builder;

@Builder
public record CategoryGetResponseDto (
        Long productCategoryId,
        String categoryName,
        ProductCategory parentProductCategory,
        List<ProductCategory> childrenProductCategory
){
}
