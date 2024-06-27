package com.nhnacademy.bookstoreinjun.repository;

import com.nhnacademy.bookstoreinjun.dto.page.PageRequestDto;
import com.nhnacademy.bookstoreinjun.entity.Book;
import java.util.List;
import java.util.Set;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BookQuerydslRepository {
    Page<Book> findBooksByTagFilter(Set<String> tags, Boolean conditionIsAnd, Pageable pageable);
    List<Book> findBooksByCategoryFilter(Set<String> categories, Boolean conditionIsAnd);
}
