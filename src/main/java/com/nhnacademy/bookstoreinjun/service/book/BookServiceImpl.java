package com.nhnacademy.bookstoreinjun.service.book;

import com.nhnacademy.bookstoreinjun.dto.book.BookRegisterRequestDto;
import com.nhnacademy.bookstoreinjun.entity.Book;
import com.nhnacademy.bookstoreinjun.exception.DuplicateException;
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
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final String TYPE = "book";


//    public Page<Book> getBookPage(Pageable pageable) {
//        return bookRepository.findAll(pageable);
//    }
//
//    public Page<Book> getBookPageByTitle(String title, Pageable pageable) {
//        return bookRepository.findByTitleContaining(title, pageable);
//    }
//
//    public Book getBookById(Long id) {
//        return bookRepository.findByBookId(id);
//    }

    public void saveBook(BookRegisterRequestDto bookRegisterRequestDto) {
        if (bookRepository.existsByIsbn13(bookRegisterRequestDto.isbn13())){
            throw new DuplicateException(TYPE);
        }else{
//            return
                    bookRepository.save(Book.builder()
                    .title(bookRegisterRequestDto.title())
                    .publisher(bookRegisterRequestDto.publisher())
                    .author(bookRegisterRequestDto.author())
                    .pubDate(bookRegisterRequestDto.pubDate())
                    .isbn(bookRegisterRequestDto.isbn())
                    .isbn13(bookRegisterRequestDto.isbn13())
                    .packable(bookRegisterRequestDto.packable())
                    .product(bookRegisterRequestDto.product())
                    .build());
        }
    }

//    public Book updateBook(Book book) {
//        if (!bookRepository.existsByBookId(book.getBookId())){
//            throw new NotFoundIdException(TYPE, book.getBookId());
//        }else {
//            return bookRepository.save(book);
//        }
//    }
}
