package com.nhnacademy.bookstoreinjun.dto.category;

import lombok.Builder;

@Builder
public record CategoryRegisterResponseDto (
        String categoryName,
        String parentCategoryName
)
{}
