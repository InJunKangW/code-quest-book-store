package com.nhnacademy.bookstoreinjun.book.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.nhnacademy.bookstoreinjun.dto.book.BookProductRegisterRequestDto;
import com.nhnacademy.bookstoreinjun.entity.Book;
import com.nhnacademy.bookstoreinjun.entity.Product;
import com.nhnacademy.bookstoreinjun.repository.BookRepository;
import com.nhnacademy.bookstoreinjun.service.book.BookServiceImpl;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class BookServiceTest {

    @InjectMocks
    private BookServiceImpl bookService;

    @Mock
    private BookRepository bookRepository;

    @Test
    public void saveTest(){
        Product product = Product.builder().build();
        BookProductRegisterRequestDto dto = BookProductRegisterRequestDto.builder()
                .title("test Title")
                .publisher("test Publisher")
                .author("test Author")
                .isbn("123456789a")
                .isbn13("123456789abcd")
                .pubDate(LocalDate.of(1999,9,9))
                .build();

        bookService.saveBook(dto);

        verify(bookRepository,times(1)).save(any(Book.class));
    }
}
