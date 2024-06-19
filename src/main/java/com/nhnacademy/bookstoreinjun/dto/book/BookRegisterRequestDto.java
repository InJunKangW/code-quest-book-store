package com.nhnacademy.bookstoreinjun.dto.book;

import com.nhnacademy.bookstoreinjun.entity.Product;
import java.time.LocalDate;
import lombok.Builder;

@Builder
public record BookRegisterRequestDto (
        String title,
        String publisher,
        String author,
        LocalDate pubDate,
        String isbn,
        String isbn13,
        boolean packable,
        Product product
){}
