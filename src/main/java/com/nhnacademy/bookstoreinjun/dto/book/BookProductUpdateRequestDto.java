package com.nhnacademy.bookstoreinjun.dto.book;

import java.util.List;
import lombok.Builder;

@Builder
public record BookProductUpdateRequestDto (
        Long bookId,
        boolean packable,
        String productDescription,
        long productPriceSales,
        long productInventory,
        int productState,
        List<String> categories,
        List<String> tags
)
{
}
