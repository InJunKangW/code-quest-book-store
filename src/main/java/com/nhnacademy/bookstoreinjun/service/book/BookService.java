package com.nhnacademy.bookstoreinjun.service.book;

import com.nhnacademy.bookstoreinjun.dto.book.BookRegisterRequestDto;
import com.nhnacademy.bookstoreinjun.entity.Book;

public interface BookService {
    void saveBook(BookRegisterRequestDto bookRegisterRequestDto);
}
