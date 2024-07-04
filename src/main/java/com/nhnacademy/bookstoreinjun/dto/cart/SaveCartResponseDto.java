package com.nhnacademy.bookstoreinjun.dto.cart;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record SaveCartResponseDto(
        @NotNull
        @Min(0)
        Long savedCartQuantity
) {
}
