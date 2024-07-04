package com.nhnacademy.bookstoreinjun.util;

import com.nhnacademy.bookstoreinjun.dto.page.PageRequestDto;
import com.nhnacademy.bookstoreinjun.exception.PageOutOfRangeException;
import java.util.Objects;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class PageableUtil {

    public PageableUtil() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * pageRequestDto 에 따른 Pageable 을 반환합니다.
     * @param pageRequestDto 페이징 요청 (Integer page (페이지 넘버), Integer size (페이지 당 사이즈), String sort (정렬할 조건), Boolean desc (오름차순/내림차순 여부))
     * @param defaultSize size 가 null 일 경우에 적용될 기본 페이지 사이즈
     * @param defaultSort sort 가 null 일 경우에 적용될 기본 정렬 조건
     * @return Pageable 객체
     */
    public static Pageable makePageable(PageRequestDto pageRequestDto, int defaultSize, String defaultSort) {
        int page = Objects.requireNonNullElse(pageRequestDto.page(),1);
        int size = Objects.requireNonNullElse(pageRequestDto.size(),defaultSize);
        boolean desc = Objects.requireNonNullElse(pageRequestDto.desc(), true);

        String sort = pageRequestDto.sort();
        if (sort == null || sort.trim().isEmpty()) {
            sort = defaultSort;
        }

        return PageRequest.of(page -1, size, Sort.by(desc ? Sort.Direction.DESC : Sort.Direction.ASC, sort));
    }

    public static void pageNumCheck(Page<?> page, Pageable pageable) {
        int totalPages = page.getTotalPages();
        int requestPage = pageable.getPageNumber() + 1;
        if (totalPages != 0 && totalPages < requestPage){
            throw new PageOutOfRangeException(totalPages, requestPage);
        }
    }
}
