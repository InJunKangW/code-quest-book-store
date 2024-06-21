package com.nhnacademy.bookstoreinjun.service.tag;

import com.nhnacademy.bookstoreinjun.dto.tag.TagGetResponseDto;
import com.nhnacademy.bookstoreinjun.dto.tag.TagRegisterRequestDto;
import com.nhnacademy.bookstoreinjun.dto.tag.TagRegisterResponseDto;
import com.nhnacademy.bookstoreinjun.entity.Product;
import com.nhnacademy.bookstoreinjun.entity.ProductTag;
import com.nhnacademy.bookstoreinjun.entity.Tag;
import java.util.List;

public interface TagService {
    TagRegisterResponseDto saveTag(TagRegisterRequestDto tagRegisterRequestDto);

    Tag getTagByTagName(String tagName);

    TagGetResponseDto getTagDtoByTagName(String tagName);

    List<TagGetResponseDto> getAllTags();

    List<TagGetResponseDto> getTagsContaining(String tagName);

    //내부적으로만 호출됨.
    List<Tag> getTagsByProduct(Product product);
}
