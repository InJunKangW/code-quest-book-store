package com.nhnacademy.bookstoreinjun.dto.book;

import java.time.LocalDate;

//import java.time.LocalDate;
//import lombok.Builder;
//import lombok.Data;
//import lombok.Getter;
//import lombok.Setter;
//
//@Data
//@Builder
//@Getter
//@Setter
//public class BookProductRegisterRequestDto {
//
//    private String title;
//    private String publisher;
//    private String author;
//    private LocalDate pubDate;
//    private String isbn;
//    private String isbn13;
//    private String cover;
//
//    private boolean packable;
//
//    private String productDescription;
//
//    private long productPriceStandard;
//
//    private long productPriceSales;
//
//    private long productInventory;
//
//}
public record BookProductRegisterRequestDto(
        String title,
        String publisher,
        String author,
        LocalDate pubDate,
        String isbn,
        String isbn13,

        String cover,
        boolean packable,
        String productDescription,
        long productPriceStandard,
        long productPriceSales,
        long productInventory
) {}
