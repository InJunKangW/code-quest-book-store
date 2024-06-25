package com.nhnacademy.bookstoreinjun.dto.category;

import com.nhnacademy.bookstoreinjun.entity.ProductCategory;
import lombok.Builder;

@Builder
public record CategoryGetResponseDto (
        String categoryName,
        ProductCategory parentProductCategory
){
}
