package com.nhnacademy.bookstoreinjun.service.book;

import com.nhnacademy.bookstoreinjun.dto.book.BookRegisterRequestDto;
import com.nhnacademy.bookstoreinjun.entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BookService {
    Page<Book> getBookPage(Pageable pageable);
    void saveBook(BookRegisterRequestDto bookRegisterRequestDto);
}
