package com.nhnacademy.bookstoreinjun.dto.product;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record InventorySetRequestDto(
        @NotNull
        @Min(1)
        Long productId,

        @NotNull
        @Min(1)
        Long quantityToSet
) {
}