package com.nhnacademy.bookstoreinjun.dto.book;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.Builder;
import org.hibernate.validator.constraints.Length;

@Builder
public record BookProductUpdateRequestDto (
        @NotNull
        Long bookId,

        @NotNull
        @Length(min = 2)
        String productName,
        boolean packable,

        @NotNull
        @NotBlank
        String productDescription,
        long productPriceSales,
        long productInventory,
        int productState,
        List<String> categories,
        List<String> tags
)
{
}
