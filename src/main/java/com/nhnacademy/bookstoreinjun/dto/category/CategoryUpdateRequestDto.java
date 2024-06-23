package com.nhnacademy.bookstoreinjun.dto.category;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record CategoryUpdateRequestDto (
        @NotNull
        @NotBlank
        String currentCategoryName,

        @NotNull
        @NotBlank
        String newCategoryName
)
{}
