package com.nhnacademy.bookstoreinjun.repository;

import com.nhnacademy.bookstoreinjun.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {

    /**
     * 특정 isbn10 (각 도서별로 고유한 코드. 이걸로 세계 모든 도서를 구분할 수 있습니다.)에 해당하는 도서를 반환합니다.
     * @param isbn 도서의 isbn10
     * @return 현재 데이터베이스에 해당 isbn10에 해당하는 도서가 존재하는 지 여부
     */
    boolean existsByIsbn(String isbn);
}
