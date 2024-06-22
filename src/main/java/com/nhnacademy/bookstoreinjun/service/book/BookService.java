package com.nhnacademy.bookstoreinjun.service.book;

import com.nhnacademy.bookstoreinjun.dto.book.BookProductGetResponseDto;
import com.nhnacademy.bookstoreinjun.dto.book.BookProductRegisterRequestDto;
import com.nhnacademy.bookstoreinjun.dto.book.BookProductUpdateRequestDto;
import com.nhnacademy.bookstoreinjun.dto.page.BookPageRequestDto;
import com.nhnacademy.bookstoreinjun.dto.product.ProductRegisterResponseDto;
import com.nhnacademy.bookstoreinjun.dto.product.ProductUpdateResponseDto;
import org.springframework.data.domain.Page;

public interface BookService {
    BookProductGetResponseDto getBookByBookId(Long bookId);
    Page<BookProductGetResponseDto> getBookPage(BookPageRequestDto bookPageRequestDto);
    ProductRegisterResponseDto saveBook(BookProductRegisterRequestDto bookProductRegisterRequestDto);
    ProductUpdateResponseDto updateBook(BookProductUpdateRequestDto bookProductUpdateRequestDto);
}
