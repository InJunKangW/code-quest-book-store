package com.nhnacademy.bookstoreinjun.tag.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.nhnacademy.bookstoreinjun.dto.tag.TagGetResponseDto;
import com.nhnacademy.bookstoreinjun.dto.tag.TagRegisterRequestDto;
import com.nhnacademy.bookstoreinjun.entity.Tag;
import com.nhnacademy.bookstoreinjun.exception.DuplicateException;
import com.nhnacademy.bookstoreinjun.repository.TagRepository;
import com.nhnacademy.bookstoreinjun.service.tag.TagServiceImpl;
import java.util.Arrays;
import java.util.List;
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

    private final String TEST_TAG_NAME = "Test Tag";

    @Test
    public void contextLoads() {}

    @Test
    public void saveTagTest(){
        Tag tag = Tag.builder()
                .tagName(TEST_TAG_NAME)
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
        when(tagRepository.existsByTagName(TEST_TAG_NAME)).thenReturn(true);

        Tag tag = Tag.builder()
                .tagName(TEST_TAG_NAME)
                .build();

        TagRegisterRequestDto dto = TagRegisterRequestDto.builder()
                .tagName(tag.getTagName())
                .build();

        assertThrows(DuplicateException.class, () -> tagService.saveTag(dto));
    }

//    @Test
//    public void getTagByTagNameTest(){
//        when(tagRepository.findByTagName(TEST_TAG_NAME))
//    }

    @Test
    public void getAllTagsTest(){
        when(tagRepository.findAll()).thenReturn(
                Arrays.asList(
                    Tag.builder()
                            .tagName(TEST_TAG_NAME + 1)
                            .build(),
                    Tag.builder().tagName(TEST_TAG_NAME +2)
                            .build()
                ));

        List<TagGetResponseDto> dto = tagService.getAllTags();
        assertNotNull(dto);
        assertEquals(dto.size(),2);
        verify(tagRepository, times(1)).findAll();
    }

    @Test
    public void getTagsContaining(){
        when(tagRepository.findAllByTagNameContaining("test")).thenReturn(
                Arrays.asList(
                        Tag.builder()
                                .tagName(TEST_TAG_NAME + 1)
                                .build(),
                        Tag.builder().tagName(TEST_TAG_NAME +2)
                                .build()
                ));

        List<TagGetResponseDto> dto = tagService.getTagsContaining("test");
        assertNotNull(dto);
        assertEquals(dto.size(),2);
    }

}
