package com.nhnacademy.bookstoreinjun.service.category;

import com.nhnacademy.bookstoreinjun.dto.category.*;
import com.nhnacademy.bookstoreinjun.dto.page.PageRequestDto;
import org.springframework.data.domain.Page;
import com.nhnacademy.bookstoreinjun.exception.DuplicateException;
import com.nhnacademy.bookstoreinjun.exception.NotFoundNameException;
import com.nhnacademy.bookstoreinjun.exception.PageOutOfRangeException;
import com.nhnacademy.bookstoreinjun.exception.InvalidSortNameException;
import org.springframework.http.ResponseEntity;


public interface ProductCategoryService {
    /**
     * 신규 카테고리를 등록합니다.
     * @param requestDto 카테고리명, 상위 카테고리명을 포함합니다.
     * @return 등록된 카테고리의 카테고리명, 해당 카테고리의 상위 카테고리명이 포함된 Dto
     * @throws DuplicateException 등록 요청의 카테고리명이 이미 데이터베이스에 존재할 경우 발생합니다
     * @throws NotFoundNameException 등록 요청의 상위 카테고리명이 데이터베이스에 존재하지 않을 경우 발생합니다
     */
    CategoryRegisterResponseDto saveCategory(CategoryRegisterRequestDto requestDto);

    /**
     * 기존 카테고리의 이름을 수정합니다.
     * @param categoryUpdateRequestDto 기존의 카테고리명과 변경할 카테고리명이 포함된 Dto
     * @return 기존의 카테고리명, 변경된 카테고리명, 업데이트 시간이 포함된 Dto
     * @throws DuplicateException 변경할 카테고리명이 이미 데이터베이스에 존재할 경우 발생합니다
     * @throws NotFoundNameException 기존 카테고리명이 데이터베이스에 존재하지 않을 경우 발생합니다
     */
    CategoryUpdateResponseDto updateCategory(CategoryUpdateRequestDto categoryUpdateRequestDto);

    ResponseEntity<Void> deleteCategory(Long categoryId);


    CategoryGetResponseDto getCategoryDtoByName(String categoryName);

    /**
     * 모든 카테고리의 페이지를 반환합니다.
     * @param pageRequestDto 페이징 요청 (int page (페이지 넘버), int size (페이지 당 사이즈), String sort (정렬할 조건), boolean desc (오름차순/내림차순 여부))
     * @return 요청에 따른 페이지
     * @throws PageOutOfRangeException 요청의 page 가, total page 를 초과할 때 발생합니다.
     * @throws InvalidSortNameException 요청의 sort 가, book 을 정렬하기에 부적절한 경우 발생합니다.
     */
    Page<CategoryGetResponseDto> getAllCategoryPage(PageRequestDto pageRequestDto);

    /**
     * 카테고리명에 특정 문자열이 포함된 카테고리의 페이지를 반환합니다.
     * @param pageRequestDto 페이징 요청 (int page (페이지 넘버), int size (페이지 당 사이즈), String sort (정렬할 조건), boolean desc (오름차순/내림차순 여부))
     * @param categoryName 포함해야 하는 문자열
     * @return 요청에 따른 페이지
     * @throws PageOutOfRangeException 요청의 page 가, total page 를 초과할 때 발생합니다.
     * @throws InvalidSortNameException 요청의 sort 가, book 을 정렬하기에 부적절한 경우 발생합니다.
     */
    Page<CategoryGetResponseDto> getNameContainingCategoryPage(PageRequestDto pageRequestDto, String categoryName);



    Page<CategoryGetResponseDto> getSubCategoryPage(PageRequestDto pageRequestDto, Long parentId);

    CategoryNodeResponseDto getCategoryTree();
}
