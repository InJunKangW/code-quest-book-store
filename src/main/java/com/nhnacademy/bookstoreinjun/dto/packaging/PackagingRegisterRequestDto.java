package com.nhnacademy.bookstoreinjun.dto.packaging;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class PackagingRegisterRequestDto {
    @NotBlank
    @NotNull
    private String packagingName;

    @NotBlank
    @NotNull
    private String productName;

    @NotBlank
    @NotNull
    String productThumbnailUrl;

    @NotBlank
    @NotNull
    String productDescription;

    @Min(0)
    long productPriceStandard;

    @Min(0)
    long productPriceSales;

    @Min(0)
    long productInventory;
}
