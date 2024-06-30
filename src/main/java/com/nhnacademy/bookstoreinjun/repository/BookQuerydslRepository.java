package com.nhnacademy.bookstoreinjun.repository;

import com.nhnacademy.bookstoreinjun.dto.book.BookProductGetResponseDto;
import com.nhnacademy.bookstoreinjun.dto.page.PageRequestDto;
import com.nhnacademy.bookstoreinjun.entity.Book;
import com.nhnacademy.bookstoreinjun.entity.Product;
import com.nhnacademy.bookstoreinjun.entity.ProductTag;
import java.util.List;
import java.util.Set;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BookQuerydslRepository {
    Page<BookProductGetResponseDto> findBooksByTagFilter(Set<String> tags, Boolean conditionIsAnd, Pageable pageable);
    Page<BookProductGetResponseDto> findBooksByCategoryFilter(Set<String> categories, Boolean conditionIsAnd, Pageable pageable);
    List<String> getAllTagName(Product realProduct);
    List<String> getAllProductCategoryName(Product realProduct);
}
