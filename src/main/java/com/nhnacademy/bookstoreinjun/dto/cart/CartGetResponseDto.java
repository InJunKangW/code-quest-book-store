package com.nhnacademy.bookstoreinjun.dto.cart;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import org.hibernate.validator.constraints.Length;

@Builder
public record CartGetResponseDto(
        @NotNull
        @Min(1)
        Long productId,

        @NotNull
        @Length(min = 2)
        String productName,

        @NotNull
        @Min(0)
        Long productPriceStandard,

        @NotNull
        @Min(0)
        Long productPriceSales,

        @NotNull
        @Min(1)
        Long productQuantity,

        @NotNull
        @NotBlank
        String productThumbnailImage
) {
}
