package com.nhnacademy.bookstoreinjun.repository;

import com.nhnacademy.bookstoreinjun.entity.Book;
import com.nhnacademy.bookstoreinjun.entity.Product;
import java.util.Set;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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
    /**
     * @param pageable 웹에서 사용자가 전달할 pageable 인자.
     * @return productState = 0인 (현재 판매 중인) 도서의 페이지.
    */
    @Query("select b from Book b join b.product p where p.productState = 0")
    Page<Book> findBooksByProductState(Pageable pageable);

    @Query("select b from Book b join b.product p where p.productState = 0 and b.title like %:title%")
    Page<Book> findBooksByProductStateAndNameContaining(Pageable pageable, @Param("title") String title);

    /**
     * @param isbn13 도서의 isbn13 (각 도서별로 고유한 코드. 이걸로 세계 모든 도서를 구분할 수 있습니다.)
     * @return 현재 데이터베이스에 해당 isbn13에 해당하는 도서가 존재하는 지 여부.
     */
    boolean existsByIsbn13(String isbn13);

    /**
     * @param product 도서가 가지는 product.
     * @return 해당 product 에 대응되는 도서. 1대 1 관계이기 때문에 리스트가 아닌 단일 객체가 반환됩니다.
     */
    Book findByProduct(Product product);
}
