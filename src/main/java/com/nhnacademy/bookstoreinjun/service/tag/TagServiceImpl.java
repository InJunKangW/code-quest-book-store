package com.nhnacademy.bookstoreinjun.service.tag;

import com.nhnacademy.bookstoreinjun.dto.page.PageRequestDto;
import com.nhnacademy.bookstoreinjun.dto.tag.TagGetResponseDto;
import com.nhnacademy.bookstoreinjun.dto.tag.TagRegisterRequestDto;
import com.nhnacademy.bookstoreinjun.dto.tag.TagRegisterResponseDto;
import com.nhnacademy.bookstoreinjun.dto.tag.TagUpdateRequestDto;
import com.nhnacademy.bookstoreinjun.dto.tag.TagUpdateResponseDto;
import com.nhnacademy.bookstoreinjun.entity.Tag;
import com.nhnacademy.bookstoreinjun.exception.DuplicateException;
import com.nhnacademy.bookstoreinjun.exception.NotFoundIdException;
import com.nhnacademy.bookstoreinjun.exception.NotFoundNameException;
import com.nhnacademy.bookstoreinjun.exception.PageOutOfRangeException;
import com.nhnacademy.bookstoreinjun.repository.ProductTagRepository;
import com.nhnacademy.bookstoreinjun.repository.TagRepository;
import com.nhnacademy.bookstoreinjun.util.PageableUtil;
import com.nhnacademy.bookstoreinjun.util.SortCheckUtil;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class TagServiceImpl implements TagService {
    private final TagRepository tagRepository;

    private final ProductTagRepository productTagRepository;

    private static final String TYPE = "tag";

    private static final int DEFAULT_PAGE_SIZE = 10;

    private static final String DEFAULT_SORT = "tagId";

    private TagGetResponseDto makeTagGetResponseDtoFromTag(Tag tag) {
        return TagGetResponseDto.builder()
                .tagId(tag.getTagId())
                .tagName(tag.getTagName())
                .productCount(productTagRepository.countByTag(tag))
                .build();
    }

    private Page<TagGetResponseDto> makeTagGetResponseDtoPage(Pageable pageable, Page<Tag> tagPage) {
        int total = tagPage.getTotalPages();
        int requestPage = pageable.getPageNumber() + 1;

        if (total != 0 && total < requestPage){
            throw new PageOutOfRangeException(total, requestPage);
        }
        return tagPage.map(this::makeTagGetResponseDtoFromTag);
    }


    public TagRegisterResponseDto saveTag(TagRegisterRequestDto tagRegisterRequestDto) {
        if (tagRepository.existsByTagName(tagRegisterRequestDto.tagName())) {
            throw new DuplicateException(TYPE);
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
            throw new NotFoundNameException(TYPE, currentTagName);
        }else if (tagRepository.existsByTagName(newTagName)){
            throw new DuplicateException(TYPE);
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

    public ResponseEntity<Void> deleteTag(Long tagId) {
        Tag tag = tagRepository.findById(tagId).orElseThrow(() -> new NotFoundIdException(TYPE, tagId));
        if (productTagRepository.existsByTag(tag)){
            return ResponseEntity.status(409).build();
        }else {
            tagRepository.delete(tag);
            return ResponseEntity.ok().build();
        }
    }


    public Page<TagGetResponseDto> getAllTagPage(PageRequestDto pageRequestDto) {
        Pageable pageable = PageableUtil.makePageable(pageRequestDto, DEFAULT_PAGE_SIZE, DEFAULT_SORT);
        try {
            Page<Tag> tagPage = tagRepository.findAll(pageable);

            PageableUtil.pageNumCheck(tagPage, pageable);

            return makeTagGetResponseDtoPage(pageable, tagPage);
        }catch (PropertyReferenceException e) {
            throw SortCheckUtil.ThrowInvalidSortNameException(pageable);
        }
    }


    public Page<TagGetResponseDto> getNameContainingTagPage(PageRequestDto pageRequestDto, String tagName) {
        Pageable pageable = PageableUtil.makePageable(pageRequestDto, DEFAULT_PAGE_SIZE, DEFAULT_SORT);
        try {
            Page<Tag> tagPage = tagRepository.findAllByTagNameContaining(pageable, tagName);

            PageableUtil.pageNumCheck(tagPage, pageable);

            return makeTagGetResponseDtoPage(pageable, tagPage);
        }catch (PropertyReferenceException e) {
            throw SortCheckUtil.ThrowInvalidSortNameException(pageable);
        }
    }

}
