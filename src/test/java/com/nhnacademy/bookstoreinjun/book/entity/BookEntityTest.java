package com.nhnacademy.bookstoreinjun.book.entity;

import com.nhnacademy.bookstoreinjun.entity.Book;
import com.nhnacademy.bookstoreinjun.entity.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.test.context.TestPropertySource;

@DataJpaTest
@TestPropertySource(locations = "classpath:application-dev.properties")
@DisplayName("도서 엔티티 테스트")
class BookEntityTest {

    @PersistenceContext
    private EntityManager entityManager;

    private Product product;


    @BeforeEach
    public void setUp() {
        product = Product.builder().build();
        entityManager.persist(product);
        entityManager.flush();
    }

    @DisplayName("도서 엔티티 저장 테스트")
    @Test
    void testSaveBook() {
        Book book = Book.builder()
                .bookId(1L)
                .title("test Title")
                .author("test Author")
                .publisher("test Publisher")
                .isbn("123456789a")
                .isbn13("123456789abed")
                .pubDate(LocalDate.of(1960, 7, 11))
                .product(product)
                .build();

        Book savedBook = entityManager.merge(book);

        assertNotNull(savedBook);
        assertNotNull(savedBook.getBookId());
        assertEquals("test Title", savedBook.getTitle());
        assertEquals("test Author", savedBook.getAuthor());
        assertEquals("test Publisher", savedBook.getPublisher());
        assertEquals("123456789a", savedBook.getIsbn());
        assertEquals("123456789abed", savedBook.getIsbn13());
        assertEquals(LocalDate.of(1960, 7, 11), savedBook.getPubDate());
        assertEquals(product, savedBook.getProduct());
    }

    @DisplayName("도서 엔티티 업데이트 테스트")
    @Test
    void testUpdateBook() {
        Book book = Book.builder()
                .bookId(1L)
                .title("test Title")
                .author("test Author")
                .publisher("test Publisher")
                .isbn("123456789a")
                .isbn13("123456789abed")
                .pubDate(LocalDate.of(1960, 7, 11))
                .product(product)
                .build();

        Book savedBook = entityManager.merge(book);

        savedBook.setTitle("new test Title");
        savedBook.setAuthor("new test Author");
        savedBook.setPublisher("new test Publisher");
        savedBook.setIsbn("23456789ab");
        savedBook.setIsbn13("23456789abide");
        savedBook.setPubDate(LocalDate.of(1999, 7, 11));
        savedBook.setProduct(product);

        entityManager.flush();

        Book updatedBook = entityManager.merge(savedBook);
        assertEquals("new test Title", updatedBook.getTitle());
        assertEquals("new test Author", updatedBook.getAuthor());
        assertEquals("new test Publisher", updatedBook.getPublisher());
        assertEquals("23456789ab", updatedBook.getIsbn());
        assertEquals("23456789abide", updatedBook.getIsbn13());
        assertEquals(LocalDate.of(1999, 7, 11), updatedBook.getPubDate());
        assertEquals(product, updatedBook.getProduct());
    }
}
