package com.nhnacademy.bookstoreinjun.dto.category;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;

public record CategoryRegisterRequestDto (
        @NotBlank String categoryName,
        @Nullable String parentCategoryName
)
{}
