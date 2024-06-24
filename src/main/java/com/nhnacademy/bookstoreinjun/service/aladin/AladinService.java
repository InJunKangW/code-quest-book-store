package com.nhnacademy.bookstoreinjun.service.aladin;

import com.nhnacademy.bookstoreinjun.dto.book.aladin.AladinBookListResponseDto;
import com.nhnacademy.bookstoreinjun.dto.book.aladin.AladinBookResponseDto;
import com.nhnacademy.bookstoreinjun.dto.page.PageRequestDto;
import org.springframework.data.domain.Page;

public interface AladinService {
    /**
     * 알라딘 api 를 호출하여 특정 도서명을 포함하는 최대 100개의 도서 리스트를 반환합니다.
     * @param title 포함 여부를 확인할 도서명
     * @return 해당 도서명을 포함하는 최대 사이즈 100의 도서 리스트
     */
    Page<AladinBookResponseDto> getAladdinBookPage(PageRequestDto pageRequestDto, String title);
}
