package com.nhnacademy.bookstoreinjun.book.repository;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.nhnacademy.bookstoreinjun.entity.Book;
import com.nhnacademy.bookstoreinjun.entity.Product;
import com.nhnacademy.bookstoreinjun.repository.BookRepository;
import com.nhnacademy.bookstoreinjun.repository.ProductRepository;
import java.time.LocalDate;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;

@Slf4j
@DataJpaTest
@TestPropertySource(locations = "classpath:application-dev.properties")
public class BookRepositoryTest {
    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private ProductRepository productRepository;

    private Product product;

    @BeforeEach
    public void setUp() {
        product = Product.builder().build();
        productRepository.save(product);
    }

    @Test
    public void bookSaveTest() {
        Book book = Book.builder()
                .title("test Title")
                .publisher("test Publisher")
                .author("test Author")
                .isbn("123456789a")
                .isbn13("123456789abcd")
                .pubDate(LocalDate.of(1999,9,9))
                .product(product)
                .build();

        Book savedBook = bookRepository.save(book);

        assertNotNull(savedBook);
        assertEquals(savedBook.getTitle(), "test Title");
        assertEquals(savedBook.getAuthor(), "test Author");
        assertEquals(savedBook.getIsbn(), "123456789a");
        assertEquals(savedBook.getIsbn13(), "123456789abcd");
        assertEquals(savedBook.getPubDate(), LocalDate.of(1999, 9, 9));
        assertEquals(savedBook.getProduct(), product);

//        assertTrue(bookRepository.existsByIsbn13("123456789abcd"));
    }

}
