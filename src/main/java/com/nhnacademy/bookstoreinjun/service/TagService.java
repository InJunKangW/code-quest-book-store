package com.nhnacademy.bookstoreinjun.service;

import com.nhnacademy.bookstoreinjun.entity.Tag;
import com.nhnacademy.bookstoreinjun.exception.DuplicateIdException;
import com.nhnacademy.bookstoreinjun.exception.NotFoundIdException;
import com.nhnacademy.bookstoreinjun.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class TagService {
    private final TagRepository tagRepository;
    private final String DUPLICATE_TYPE = "tag";


    public Tag createTag(Tag tag) {
        if (tagRepository.existsById(tag.getTagId())) {
            throw new DuplicateIdException(DUPLICATE_TYPE);
        }else{
            return tagRepository.save(tag);
        }
    }

    public Tag updateTag(Tag tag) {
        if (!tagRepository.existsById(tag.getTagId())) {
            throw new NotFoundIdException(DUPLICATE_TYPE, tag.getTagId());
        }else{
            return tagRepository.save(tag);
        }
    }
}
