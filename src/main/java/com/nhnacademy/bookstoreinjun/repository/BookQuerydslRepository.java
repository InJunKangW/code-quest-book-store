package com.nhnacademy.bookstoreinjun.repository;

import com.nhnacademy.bookstoreinjun.dto.book.BookProductGetResponseDto;
import com.nhnacademy.bookstoreinjun.entity.Product;
import java.util.Map;
import java.util.Set;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BookQuerydslRepository {
    BookProductGetResponseDto findBookByBookId(Long bookId);
    Page<BookProductGetResponseDto> findAllBookPage(Pageable pageable, int productState);

    Page<BookProductGetResponseDto> findNameContainingBookPage(Pageable pageable, String title, int productState);
    /**
     * 판매 중인 도서 상품 중 특정 태그를 갖는 도서의 페이지를 반환합니다.
     * @param tags 필터링할 태그명의 set 입니다.
     * @param conditionIsAnd 결과로 나올 도서가 지정된 태그를 모두 가지고 있어야 하는 지 아니면 태그 중 하나라도 가지고 있으면 되는 지를 결정합니다.
     *                       true 면 모두 가지고 있어야 하고, null 이거나 false 라면 하나만 가지고 있어도 됩니다.
     * @param pageable 요청한 페이지. offset 과 limit, 정렬조건, 오름차순/내림차순을 결정합니다.
     * @return 태그명으로 필터링된 도서 상품 페이지
     */
    Page<BookProductGetResponseDto> findBooksByTagFilter(Set<String> tags, Boolean conditionIsAnd, Pageable pageable);


    /**
     *
     * @param categoryName
     * @param pageable
     * @return
     */
    Page<BookProductGetResponseDto> findBooksByCategoryFilter(String categoryName, Pageable pageable);


    /**
     * 특정 상품에 달린 모든 태그명을 반환합니다.
     *
     * @param realProduct 검색할 상품
     * @return 해당 상품에 달린 모든 태그명
     */
    Map<Long, String> getTagMapOfIdAndName(Product realProduct);


    /**
     * 특정 상품에 달린 모든 카테고리명을 반환합니다.
     * @param realProduct 검색할 상품
     * @return 해당 상품에 달린 모든 카테고리명
     */
    Map<Long, String> getCategoryMapOfIdAndName(Product realProduct);
}
