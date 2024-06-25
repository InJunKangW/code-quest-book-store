package com.nhnacademy.bookstoreinjun.dto.product;

public record ProductGetResponseDto (
        Long productId,
        String productName,
        int productState,
        int productPriceStandard,
        int productPriceSales,
        String productThumbNailImage
        
){
}
