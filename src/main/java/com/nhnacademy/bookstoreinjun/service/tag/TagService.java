package com.nhnacademy.bookstoreinjun.service.tag;

import com.nhnacademy.bookstoreinjun.dto.page.PageRequestDto;
import com.nhnacademy.bookstoreinjun.dto.tag.TagGetResponseDto;
import com.nhnacademy.bookstoreinjun.dto.tag.TagRegisterRequestDto;
import com.nhnacademy.bookstoreinjun.dto.tag.TagRegisterResponseDto;
import com.nhnacademy.bookstoreinjun.dto.tag.TagUpdateRequestDto;
import com.nhnacademy.bookstoreinjun.dto.tag.TagUpdateResponseDto;
import java.util.List;
import org.springframework.data.domain.Page;

public interface TagService {
    TagRegisterResponseDto saveTag(TagRegisterRequestDto tagRegisterRequestDto);

    TagGetResponseDto getTagDtoByTagName(String tagName);

    TagUpdateResponseDto updateTag(TagUpdateRequestDto tagUpdateRequestDto);

    Page<TagGetResponseDto> getAllTagPage(PageRequestDto pageRequestDto);

    Page<TagGetResponseDto> getNameContainingTagPage(PageRequestDto pageRequestDto, String tagName);
}
