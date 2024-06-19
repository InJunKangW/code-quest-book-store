package com.nhnacademy.bookstoreinjun.service;

import com.nhnacademy.bookstoreinjun.dto.tag.TagRegisterRequestDto;
import com.nhnacademy.bookstoreinjun.entity.Tag;
import com.nhnacademy.bookstoreinjun.exception.DuplicateException;
import com.nhnacademy.bookstoreinjun.exception.NotFoundIdException;
import com.nhnacademy.bookstoreinjun.repository.TagRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class TagService {
    private final TagRepository tagRepository;
    private final String DUPLICATE_TYPE = "tag";


    public Tag createTag(TagRegisterRequestDto tagRegisterRequestDto) {
        if (tagRepository.existsByTagName(tagRegisterRequestDto.tagName())) {
            throw new DuplicateException(DUPLICATE_TYPE);
        }else{
            return tagRepository.save(Tag.builder()
                    .tagName(tagRegisterRequestDto.tagName())
                    .build());
        }
    }

    public Tag updateTag(Tag tag) {
        if (!tagRepository.existsById(tag.getTagId())) {
            throw new NotFoundIdException(DUPLICATE_TYPE, tag.getTagId());
        }else{
            return tagRepository.save(tag);
        }
    }

    public List<Tag> getAllTags() {
        return tagRepository.findAll();
    }

    public List<Tag> getTagsContaining(String tagName) {
        return tagRepository.findAllByTagNameContaining(tagName);
    }

    public Tag getTagByTagName(String tagName) {
        return tagRepository.findByTagName(tagName);
    }
}
