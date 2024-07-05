package com.nhnacademy.bookstoreinjun.dto.book;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.Set;
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

        @NotNull(message = "{must.have.category}")
        @Size(min = 1, message = "{must.have.category}")
        @Size(max = 10, message = "{too.much.category}")
        Set<String> categories,

        Set<String> tags
)
{
}
