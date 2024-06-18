package com.nhnacademy.bookstoreinjun.service;

import com.nhnacademy.bookstoreinjun.entity.Book;
import com.nhnacademy.bookstoreinjun.exception.DuplicateIdException;
import com.nhnacademy.bookstoreinjun.exception.NotFoundIdException;
import com.nhnacademy.bookstoreinjun.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class BookService {
    private final BookRepository bookRepository;
    private final String DUPLICATE_TYPE = "book";


    public Page<Book> getBookPage(Pageable pageable) {
        return bookRepository.findAll(pageable);
    }

    public Page<Book> getBookPageByTitle(String title, Pageable pageable) {
        return bookRepository.findByTitleContaining(title, pageable);
    }

    public Book getBookById(Long id) {
        return bookRepository.findByBookId(id);
    }

    public Book saveBook(Book book) {
        if (bookRepository.existsByIsbn13(book.getIsbn13())){
            throw new DuplicateIdException(DUPLICATE_TYPE);
        }else{
            return bookRepository.save(book);
        }
    }

    public Book updateBook(Book book) {
        if (!bookRepository.existsByBookId(book.getBookId())){
            throw new NotFoundIdException(DUPLICATE_TYPE, book.getBookId());
        }else {
            return bookRepository.save(book);
        }
    }
}
