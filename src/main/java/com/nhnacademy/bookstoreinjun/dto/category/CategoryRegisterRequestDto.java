package com.nhnacademy.bookstoreinjun.dto.category;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record CategoryRegisterRequestDto (
        @NotBlank String categoryName,
        @Nullable String parentCategoryName
)
{}
