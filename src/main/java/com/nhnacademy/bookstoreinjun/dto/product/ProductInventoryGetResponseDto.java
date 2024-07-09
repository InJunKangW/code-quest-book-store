package com.nhnacademy.bookstoreinjun.dto.product;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record ProductInventoryGetResponseDto (
        @NotNull
        @Min(1)
        Long productId,

        @NotNull
        @Min(0)
        Long productInventory
)
{}