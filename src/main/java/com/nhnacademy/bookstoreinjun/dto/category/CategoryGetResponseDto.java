package com.nhnacademy.bookstoreinjun.dto.category;

import com.nhnacademy.bookstoreinjun.entity.Category;
import lombok.Builder;

@Builder
public record CategoryGetResponseDto (
        String categoryName,
        Category parentCategory
){
}
