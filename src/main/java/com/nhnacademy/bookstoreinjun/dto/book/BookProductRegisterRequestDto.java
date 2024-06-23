package com.nhnacademy.bookstoreinjun.dto.book;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;
import lombok.Builder;
import org.hibernate.validator.constraints.Length;

@Builder
public record BookProductRegisterRequestDto(
        @NotBlank
        @NotNull
        String title,

        @NotNull
        String publisher,

        @NotNull
        String author,

        @NotNull
        LocalDate pubDate,

        @NotNull
        @Length(min = 10, max =10) String isbn,

        @NotNull
        @Length(min = 13, max =13) String isbn13,

        @NotNull
        String cover,

        @NotNull
        @NotBlank
        @Length(min = 2)
        String productName,

        boolean packable,

        @NotNull
        String productDescription,

        @Min(0)
        long productPriceStandard,

        @Min(0)
        long productPriceSales,

        @Min(0)
        long productInventory,

        List<String> categories,
        List<String> tags
) {}
