package com.nhnacademy.bookstoreinjun.service.book;

import com.nhnacademy.bookstoreinjun.dto.book.BookRegisterRequestDto;
import com.nhnacademy.bookstoreinjun.entity.Book;
import com.nhnacademy.bookstoreinjun.exception.DuplicateException;
import com.nhnacademy.bookstoreinjun.exception.InvalidSortNameException;
import com.nhnacademy.bookstoreinjun.repository.BookRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final String TYPE = "book";


    //내부적으로만 호출됨
    public Page<Book> getBookPage(Pageable pageable) {
        try{
            return bookRepository.findAll(pageable);
        }catch (IllegalArgumentException e) {
            StringBuilder stringBuilder = new StringBuilder();
            Sort sort = pageable.getSort();
            List<Sort.Order> orders = sort.toList();

            for (Sort.Order order : orders) {
                stringBuilder.append(order.getProperty());
                if (order != orders.getLast()){
                    stringBuilder.append(", ");
                }
            }
            throw new InvalidSortNameException(stringBuilder.toString());
        }
    }


    public void saveBook(BookRegisterRequestDto bookRegisterRequestDto) {
        if (bookRepository.existsByIsbn13(bookRegisterRequestDto.isbn13())){
            throw new DuplicateException(TYPE);
        }else{
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
