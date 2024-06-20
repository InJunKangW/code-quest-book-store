package com.nhnacademy.bookstoreinjun.dto.book;

import java.time.LocalDate;
import java.util.List;
import lombok.Builder;
import org.hibernate.validator.constraints.Length;

@Builder
public record BookProductRegisterRequestDto(
        String title,
        String publisher,
        String author,
        LocalDate pubDate,
        @Length(min = 10, max =10) String isbn,
        @Length(min = 13, max =13) String isbn13,

        String cover,
        boolean packable,
        String productDescription,
        long productPriceStandard,
        long productPriceSales,
        long productInventory,
        List<String> categories,
        List<String> tags
) {}
