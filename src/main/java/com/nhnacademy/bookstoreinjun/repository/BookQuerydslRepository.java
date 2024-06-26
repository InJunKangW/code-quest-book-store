package com.nhnacademy.bookstoreinjun.repository;

import com.nhnacademy.bookstoreinjun.entity.Book;
import java.util.List;
import java.util.Set;

public interface BookQuerydslRepository {
    List<Book> findBooksByTagFilter(Set<String> tags, Boolean conditionIsAnd);
    List<Book> findBooksByCategoryFilter(Set<String> categories, Boolean conditionIsAnd);
}
