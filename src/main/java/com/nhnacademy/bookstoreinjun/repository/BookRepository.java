package com.nhnacademy.bookstoreinjun.repository;

import com.nhnacademy.bookstoreinjun.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    public Book findByIsbn13(String isbn13);

    public Book findByBookId(Long bookId);
}
