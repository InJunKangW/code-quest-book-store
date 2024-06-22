package com.nhnacademy.bookstoreinjun.service.tag;

import com.nhnacademy.bookstoreinjun.dto.category.CategoryGetResponseDto;
import com.nhnacademy.bookstoreinjun.dto.tag.TagGetResponseDto;
import com.nhnacademy.bookstoreinjun.dto.tag.TagRegisterRequestDto;
import com.nhnacademy.bookstoreinjun.dto.tag.TagRegisterResponseDto;
import com.nhnacademy.bookstoreinjun.dto.tag.TagUpdateRequestDto;
import com.nhnacademy.bookstoreinjun.dto.tag.TagUpdateResponseDto;
import com.nhnacademy.bookstoreinjun.entity.Product;
import com.nhnacademy.bookstoreinjun.entity.ProductCategory;
import com.nhnacademy.bookstoreinjun.entity.ProductCategoryRelation;
import com.nhnacademy.bookstoreinjun.entity.ProductTag;
import com.nhnacademy.bookstoreinjun.entity.Tag;
import com.nhnacademy.bookstoreinjun.exception.DuplicateException;
import com.nhnacademy.bookstoreinjun.exception.NotFoundIdException;
import com.nhnacademy.bookstoreinjun.exception.NotFoundNameException;
import com.nhnacademy.bookstoreinjun.repository.ProductTagRepository;
import com.nhnacademy.bookstoreinjun.repository.TagRepository;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class TagServiceImpl implements TagService {
    private final TagRepository tagRepository;

    private final ProductTagRepository productTagRepository;

    private final String DUPLICATE_TYPE = "tag";


    public TagRegisterResponseDto saveTag(TagRegisterRequestDto tagRegisterRequestDto) {
        if (tagRepository.existsByTagName(tagRegisterRequestDto.tagName())) {
            throw new DuplicateException(DUPLICATE_TYPE);
        }else{
            Tag tag = tagRepository.save(Tag.builder()
                    .tagName(tagRegisterRequestDto.tagName())
                    .build());
            return new TagRegisterResponseDto(tag.getTagId(), tag.getTagName());
        }
    }



    public TagUpdateResponseDto updateTag(TagUpdateRequestDto tagUpdateRequestDto) {
        String currentTagName = tagUpdateRequestDto.currentTagName();
        String newTagName = tagUpdateRequestDto.newTagName();
        if (!tagRepository.existsByTagName(currentTagName)) {
            throw new NotFoundNameException(DUPLICATE_TYPE, currentTagName);
        }else if (tagRepository.existsByTagName(newTagName)){
            throw new DuplicateException(DUPLICATE_TYPE);
        }else{
            Tag currentTag = tagRepository.findByTagName(currentTagName);
            currentTag.setTagName(newTagName);
            tagRepository.save(currentTag);
            return new TagUpdateResponseDto(currentTagName, newTagName, LocalDateTime.now());
        }
    }

    public List<TagGetResponseDto> getAllTags() {
        return tagRepository.findAll().stream()
                .map(tag -> TagGetResponseDto.builder()
                        .tagName(tag.getTagName())
                        .build())
                .toList();
    }

    public List<TagGetResponseDto> getTagsContaining(String tagName) {
        return tagRepository.findAllByTagNameContaining(tagName).stream()
                .map(tag -> TagGetResponseDto.builder()
                        .tagName(tag.getTagName())
                        .build())
                .toList();
    }

    public TagGetResponseDto getTagDtoByTagName(String tagName) {
        Tag tag = tagRepository.findByTagName(tagName);
        if (tag == null) {
            throw new NotFoundNameException(DUPLICATE_TYPE, tagName);
        }else{
            return TagGetResponseDto.builder()
                    .tagName(tag.getTagName())
                    .build();
        }
    }


    //이 서비스 내부적으로만 호출됩니다.
    public Tag getTagByTagName(String tagName) {
        Tag tag = tagRepository.findByTagName(tagName);
        if (tag == null) {
            throw new NotFoundNameException(DUPLICATE_TYPE, tagName);
        }else{
            return tag;
        }
    }
}
