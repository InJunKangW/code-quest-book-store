package com.nhnacademy.bookstoreinjun.repository;

import com.nhnacademy.bookstoreinjun.entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

//    public Book findByIsbn13(String isbn13);
//
//    public Book findByBookId(Long bookId);
//
//    public Page<Book> findByTitleContaining(String title, Pageable pageable);
//
//    public Page<Book> findByAuthorContaining(String author, Pageable pageable);
//
//    public boolean existsByBookId(Long bookId);

    @Query("select b from Book b join b.product p where p.productState = 0")
    Page<Book> findBooksByProductState(Pageable pageable);

    boolean existsByIsbn13(String isbn13);
}
