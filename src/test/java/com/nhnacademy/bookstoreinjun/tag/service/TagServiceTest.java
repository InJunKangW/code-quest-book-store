package com.nhnacademy.bookstoreinjun.tag.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.nhnacademy.bookstoreinjun.dto.tag.TagRegisterRequestDto;
import com.nhnacademy.bookstoreinjun.entity.Category;
import com.nhnacademy.bookstoreinjun.entity.Tag;
import com.nhnacademy.bookstoreinjun.exception.DuplicateException;
import com.nhnacademy.bookstoreinjun.exception.NotFoundNameException;
import com.nhnacademy.bookstoreinjun.repository.TagRepository;
import com.nhnacademy.bookstoreinjun.service.tag.TagServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class TagServiceTest {

    @InjectMocks
    private TagServiceImpl tagService;

    @Mock
    private TagRepository tagRepository;


    @Test
    public void contextLoads() {}

    @Test
    public void saveTagTest(){
        Tag tag = Tag.builder()
                .tagName("test tag1")
                .build();

        TagRegisterRequestDto dto = TagRegisterRequestDto.builder()
                .tagName(tag.getTagName())
                .build();

        when(tagRepository.save(any(Tag.class))).thenReturn(tag);
        tagService.saveTag(dto);

        verify(tagRepository, times(1)).save(any(Tag.class));
    }

    @Test
    public void saveTagTest2(){
        when(tagRepository.existsByTagName("test tag1")).thenReturn(true);

        Tag tag = Tag.builder()
                .tagName("test tag1")
                .build();

        TagRegisterRequestDto dto = TagRegisterRequestDto.builder()
                .tagName(tag.getTagName())
                .build();

        assertThrows(DuplicateException.class, () -> tagService.saveTag(dto));
    }
}
