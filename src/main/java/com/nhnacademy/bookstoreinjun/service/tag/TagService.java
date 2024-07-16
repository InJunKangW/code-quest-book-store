package com.nhnacademy.bookstoreinjun.service.tag;

import com.nhnacademy.bookstoreinjun.dto.page.PageRequestDto;
import com.nhnacademy.bookstoreinjun.dto.tag.TagGetResponseDto;
import com.nhnacademy.bookstoreinjun.dto.tag.TagRegisterRequestDto;
import com.nhnacademy.bookstoreinjun.dto.tag.TagRegisterResponseDto;
import com.nhnacademy.bookstoreinjun.dto.tag.TagUpdateRequestDto;
import com.nhnacademy.bookstoreinjun.dto.tag.TagUpdateResponseDto;
import com.nhnacademy.bookstoreinjun.exception.InvalidSortNameException;
import com.nhnacademy.bookstoreinjun.exception.PageOutOfRangeException;
import org.springframework.data.domain.Page;
import com.nhnacademy.bookstoreinjun.exception.DuplicateException;
import com.nhnacademy.bookstoreinjun.exception.NotFoundNameException;
import org.springframework.http.ResponseEntity;

public interface TagService {
    /**
     * 새로운 태그를 등록합니다.
     * @param tagRegisterRequestDto 등록할 태그명이 포함된 Dto
     * @return 등록된 태그의 id, 태그명
     * @throws DuplicateException 데이터베이스에 이미 해당 태그명의 태그가 존재할 경우 발생합니다.
     */
    TagRegisterResponseDto saveTag(TagRegisterRequestDto tagRegisterRequestDto);

    /**
     * 기존의 태그명을 수정합니다.
     * @param tagUpdateRequestDto 기존의 태그명, 변경할 태그명이 포함된 Dto
     * @return 기존의 태그명, 변경된 태그명, 업데이트 시간이 포함된 Dto
     * @throws NotFoundNameException 데이터베이스에 기존 태그명의 태그가 존재하지 않을 경우 발생합니다.
     * @throws DuplicateException 데이터베이스에 변경할 태그명의 태그가 존재할 경우 발생합니다.
     */
    TagUpdateResponseDto updateTag(TagUpdateRequestDto tagUpdateRequestDto);

    ResponseEntity<Void> deleteTag(Long tagId);

    /**
     * 페이징 요청에 따른 모든 태그의 페이지를 반환합니다.
     * @param pageRequestDto 페이징 요청 (int page (페이지 넘버), int size (페이지 당 사이즈), String sort (정렬할 조건), boolean desc (오름차순/내림차순 여부))
     * @return 해당 요청에 따라 반환되는 태그 페이지
     * @throws PageOutOfRangeException 요청의 page 가, total page 를 초과할 때 발생합니다.
     * @throws InvalidSortNameException 요청의 sort 가, tag 를 정렬하기에 부적절한 경우 발생합니다.
     */
    Page<TagGetResponseDto> getAllTagPage(PageRequestDto pageRequestDto);

    /**
     * 태그명에 특정 문자열을 포함하는 태그의 페이지를 반환합니다.
     * @param pageRequestDto 페이징 요청 (int page (페이지 넘버), int size (페이지 당 사이즈), String sort (정렬할 조건), boolean desc (오름차순/내림차순 여부))
     * @param tagName 포함되어야 할 태그명
     * @return 해당 요청에 따라 반환되는 태그 페이지
     * @throws PageOutOfRangeException 요청의 page 가, total page 를 초과할 때 발생합니다.
     * @throws InvalidSortNameException 요청의 sort 가, tag 를 정렬하기에 부적절한 경우 발생합니다.
     */
    Page<TagGetResponseDto> getNameContainingTagPage(PageRequestDto pageRequestDto, String tagName);
}
