package com.nhnacademy.bookstoreinjun.dto.category;

public record CategoryUpdateRequestDto (
        String currentCategoryName,
        String newCategoryName
)
{}
