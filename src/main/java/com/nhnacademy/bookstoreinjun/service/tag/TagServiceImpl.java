package com.nhnacademy.bookstoreinjun.service.tag;

import com.nhnacademy.bookstoreinjun.dto.category.CategoryGetResponseDto;
import com.nhnacademy.bookstoreinjun.dto.page.PageRequestDto;
import com.nhnacademy.bookstoreinjun.dto.tag.TagGetResponseDto;
import com.nhnacademy.bookstoreinjun.dto.tag.TagRegisterRequestDto;
import com.nhnacademy.bookstoreinjun.dto.tag.TagRegisterResponseDto;
import com.nhnacademy.bookstoreinjun.dto.tag.TagUpdateRequestDto;
import com.nhnacademy.bookstoreinjun.dto.tag.TagUpdateResponseDto;
import com.nhnacademy.bookstoreinjun.entity.ProductCategory;
import com.nhnacademy.bookstoreinjun.entity.Tag;
import com.nhnacademy.bookstoreinjun.exception.DuplicateException;
import com.nhnacademy.bookstoreinjun.exception.NotFoundNameException;
import com.nhnacademy.bookstoreinjun.exception.PageOutOfRangeException;
import com.nhnacademy.bookstoreinjun.repository.ProductTagRepository;
import com.nhnacademy.bookstoreinjun.repository.TagRepository;
import com.nhnacademy.bookstoreinjun.util.MakePageableUtil;
import com.nhnacademy.bookstoreinjun.util.SortCheckUtil;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class TagServiceImpl implements TagService {
    private final TagRepository tagRepository;


    private final String DUPLICATE_TYPE = "tag";

    private final int DEFAULT_PAGE_SIZE = 10;

    private final String DEFAULT_SORT = "TagId";


    public TagRegisterResponseDto saveTag(TagRegisterRequestDto tagRegisterRequestDto) {
        if (tagRepository.existsByTagName(tagRegisterRequestDto.tagName())) {
            throw new DuplicateException(DUPLICATE_TYPE);
        }else{
            Tag tag = tagRepository.save(Tag.builder()
                    .tagName(tagRegisterRequestDto.tagName())
                    .build());

            return TagRegisterResponseDto.builder()
                    .tagId(tag.getTagId())
                    .tagName(tag.getTagName())
                    .build();
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
            return TagUpdateResponseDto.builder()
                    .previousTagName(currentTagName)
                    .newTagName(newTagName)
                    .updateTime(LocalDateTime.now())
                    .build();
        }
    }

    public List<TagGetResponseDto> getAllTags() {
        return tagRepository.findAll().stream()
                .map(tag -> TagGetResponseDto.builder()
                        .tagName(tag.getTagName())
                        .build())
                .toList();
    }


    public Page<TagGetResponseDto> getAllTagPage(PageRequestDto pageRequestDto) {
        Pageable pageable = MakePageableUtil.makePageable(pageRequestDto, DEFAULT_PAGE_SIZE, DEFAULT_SORT);
        try {
            Page<Tag> tagPage = tagRepository.findAll(pageable);
            return makeCategoryGetResponseDtoPage(pageable, tagPage);
        }catch (PropertyReferenceException e) {
            throw SortCheckUtil.sortExceptionHandle(pageable);
        }
    }


    public List<TagGetResponseDto> getNameContainingTags(String tagName) {
        return tagRepository.findAllByTagNameContaining(tagName).stream()
                .map(tag -> TagGetResponseDto.builder()
                        .tagName(tag.getTagName())
                        .build())
                .toList();
    }

    public Page<TagGetResponseDto> getNameContainingTagPage(PageRequestDto pageRequestDto, String tagName) {
        Pageable pageable = MakePageableUtil.makePageable(pageRequestDto, DEFAULT_PAGE_SIZE, DEFAULT_SORT);
        try {
            Page<Tag> tagPage = tagRepository.findAllByTagNameContaining(pageable, tagName);
            return makeCategoryGetResponseDtoPage(pageable, tagPage);
        }catch (PropertyReferenceException e) {
            throw SortCheckUtil.sortExceptionHandle(pageable);
        }
    }

    public TagGetResponseDto getTagDtoByTagName(String tagName) {
        Tag tag = tagRepository.findByTagName(tagName);
        if (tag == null) {
            throw new NotFoundNameException(DUPLICATE_TYPE, tagName);
        }else{
            return makeTagGetResponseDtoFromTag(tag);
        }
    }

    private Page<TagGetResponseDto> makeCategoryGetResponseDtoPage(Pageable pageable, Page<Tag> tagPage) {
        int total = tagPage.getTotalPages();
        int maxPage = pageable.getPageNumber() + 1;

        if (total < maxPage){
            throw new PageOutOfRangeException(total, maxPage);
        }
        return tagPage.map(this::makeTagGetResponseDtoFromTag);
    }

    private TagGetResponseDto makeTagGetResponseDtoFromTag(Tag tag) {
        return TagGetResponseDto.builder()
                .tagName(tag.getTagName())
                .build();
    }
}
