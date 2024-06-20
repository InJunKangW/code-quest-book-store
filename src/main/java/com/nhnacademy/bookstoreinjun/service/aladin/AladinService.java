package com.nhnacademy.bookstoreinjun.service.aladin;

import com.nhnacademy.bookstoreinjun.dto.book.AladinBookListResponseDto;

public interface AladinService {
    AladinBookListResponseDto getAladdinBookList(String title);
}
