package com.nhnacademy.bookstoreinjun.dto.category;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;

@Builder
public record CategoryUpdateRequestDto (
        @Pattern(regexp = "^(?!.*,).*$")
        @NotNull
        @NotBlank
        String currentCategoryName,

        @Pattern(regexp = "^(?!.*,).*$")
        @NotNull
        @NotBlank
        String newCategoryName
)
{}
