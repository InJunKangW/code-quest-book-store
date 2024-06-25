package com.nhnacademy.bookstoreinjun.tag.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.nhnacademy.bookstoreinjun.dto.page.PageRequestDto;
import com.nhnacademy.bookstoreinjun.dto.tag.TagGetResponseDto;
import com.nhnacademy.bookstoreinjun.dto.tag.TagRegisterRequestDto;
import com.nhnacademy.bookstoreinjun.dto.tag.TagUpdateRequestDto;
import com.nhnacademy.bookstoreinjun.dto.tag.TagUpdateResponseDto;
import com.nhnacademy.bookstoreinjun.entity.Tag;
import com.nhnacademy.bookstoreinjun.exception.DuplicateException;
import com.nhnacademy.bookstoreinjun.exception.InvalidSortNameException;
import com.nhnacademy.bookstoreinjun.exception.NotFoundNameException;
import com.nhnacademy.bookstoreinjun.exception.PageOutOfRangeException;
import com.nhnacademy.bookstoreinjun.repository.TagRepository;
import com.nhnacademy.bookstoreinjun.service.tag.TagServiceImpl;
import com.nhnacademy.bookstoreinjun.util.SortCheckUtil;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.data.util.TypeInformation;

@ExtendWith(MockitoExtension.class)
public class TagServiceTest {

    @InjectMocks
    private TagServiceImpl tagService;

    @Mock
    private TagRepository tagRepository;

    @Mock
    private SortCheckUtil sortCheckUtil;

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

    @Test
    public void updateTagTestSuccess(){
        when(tagRepository.existsByTagName(TEST_TAG_NAME)).thenReturn(true);
        when(tagRepository.existsByTagName("new" + TEST_TAG_NAME)).thenReturn(false);
        when(tagRepository.findByTagName(TEST_TAG_NAME)).thenReturn(new Tag());

        TagUpdateRequestDto tagUpdateRequestDto = TagUpdateRequestDto.builder()
                .currentTagName(TEST_TAG_NAME)
                .newTagName("new" + TEST_TAG_NAME)
                .build();

        TagUpdateResponseDto tagUpdateResponseDto = tagService.updateTag(tagUpdateRequestDto);

        assertNotNull(tagUpdateResponseDto);

        verify(tagRepository, times(1)).save(any(Tag.class));

        verify(tagRepository, times(1)).findByTagName(TEST_TAG_NAME);
    }

    @Test
    public void updateTagTestFailureByNotExistingTagName(){
        when(tagRepository.existsByTagName(TEST_TAG_NAME)).thenReturn(false);

        TagUpdateRequestDto tagUpdateRequestDto = TagUpdateRequestDto.builder()
                .currentTagName(TEST_TAG_NAME)
                .newTagName("new" + TEST_TAG_NAME)
                .build();

        assertThrows(NotFoundNameException.class, () -> tagService.updateTag(tagUpdateRequestDto));
    }

    @Test
    public void updateTagTestFailureByDuplicateTagName(){
        when(tagRepository.existsByTagName(TEST_TAG_NAME)).thenReturn(true);
        when(tagRepository.existsByTagName("new" + TEST_TAG_NAME)).thenReturn(true);

        TagUpdateRequestDto tagUpdateRequestDto = TagUpdateRequestDto.builder()
                .currentTagName(TEST_TAG_NAME)
                .newTagName("new" + TEST_TAG_NAME)
                .build();

        assertThrows(DuplicateException.class, () -> tagService.updateTag(tagUpdateRequestDto));
    }

    @Test
    public void getTagDtoByTagNameTestSuccess(){
        when(tagRepository.findByTagName(TEST_TAG_NAME)).thenReturn(new Tag());

        TagGetResponseDto response = tagService.getTagDtoByTagName(TEST_TAG_NAME);

        assertNotNull(response);

        verify(tagRepository, times(1)).findByTagName(TEST_TAG_NAME);
    }

    @Test
    public void getTagDtoByTagNameTestFailure(){
        when(tagRepository.findByTagName(TEST_TAG_NAME)).thenReturn(null);

        assertThrows(NotFoundNameException.class, () -> tagService.getTagDtoByTagName(TEST_TAG_NAME));
    }


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
    public void getAllTagPageTestSuccess(){
        PageRequestDto pageRequestDto = PageRequestDto.builder()
                .build();

        when(tagRepository.findAll(any(Pageable.class))).thenReturn(new PageImpl<>(Arrays.asList(
                Tag.builder()
                        .tagName("test tag")
                        .build(),
                Tag.builder()
                        .tagName("test tag")
                        .build()
        )));
        Page<TagGetResponseDto> testTagPage = tagService.getAllTagPage(pageRequestDto);

        verify(tagRepository, times(1)).findAll(any(Pageable.class));
        assertNotNull(testTagPage);
        assertEquals(testTagPage.getTotalElements(),2);
    }

    @Test
    public void getAllTagPageTestFailureByWrongSort(){
        PageRequestDto pageRequestDto = PageRequestDto.builder().build();

        when(tagRepository.findAll(any(Pageable.class))).thenThrow(new PropertyReferenceException("wrong", TypeInformation.COLLECTION, new ArrayList<>()));

        assertThrows(InvalidSortNameException.class, () -> tagService.getAllTagPage(pageRequestDto));
    }

    @Test
    public void getAllTagPageTestFailureByOutOfPageRange(){
        PageRequestDto pageRequestDto = PageRequestDto.builder()
                .page(100)
                .build();

        when(tagRepository.findAll(any(Pageable.class))).thenReturn(new PageImpl<>(Arrays.asList(
                Tag.builder()
                        .tagName("test tag")
                        .build(),
                Tag.builder()
                        .tagName("test tag")
                        .build()
        )));

        assertThrows(PageOutOfRangeException.class, () -> tagService.getAllTagPage(pageRequestDto));
    }

    @Test
    public void getNameContainingTagPageTestSuccess(){
        PageRequestDto pageRequestDto = PageRequestDto.builder()
                .build();

        when(tagRepository.findAllByTagNameContaining(any(Pageable.class), eq("test"))).thenReturn(new PageImpl<>(Arrays.asList(
                Tag.builder()
                        .tagName("test tag")
                        .build(),
                Tag.builder()
                        .tagName("test tag")
                        .build(),
                Tag.builder()
                        .tagName("test tag")
                        .build()
        )));

        Page<TagGetResponseDto> testTagPage = tagService.getNameContainingTagPage(pageRequestDto, "test");

        verify(tagRepository, times(1)).findAllByTagNameContaining(any(Pageable.class), eq("test"));
        assertNotNull(testTagPage);
        assertEquals(testTagPage.getTotalElements(),3);
    }

    @Test
    public void getNameContainingTagPageTestFailure(){
        PageRequestDto pageRequestDto = PageRequestDto.builder()
                .sort("wrong")
                .build();

        when(tagRepository.findAllByTagNameContaining(any(Pageable.class), eq("test"))).thenThrow(new PropertyReferenceException("wrong", TypeInformation.COLLECTION, new ArrayList<>()));

        assertThrows(InvalidSortNameException.class, () -> tagService.getNameContainingTagPage(pageRequestDto, "test"));
    }

    @Test
    public void getNameContainingTagPageTestFailure2(){
        PageRequestDto pageRequestDto = PageRequestDto.builder()
                .page(100)
                .build();

        when(tagRepository.findAllByTagNameContaining(any(Pageable.class), eq("test"))).thenReturn(new PageImpl<>(Arrays.asList(
                Tag.builder()
                        .tagName("test tag")
                        .build(),
                Tag.builder()
                        .tagName("test tag")
                        .build(),
                Tag.builder()
                        .tagName("test tag")
                        .build()
        )));

        assertThrows(PageOutOfRangeException.class, () -> tagService.getNameContainingTagPage(pageRequestDto, "test"));
    }


    @Test
    public void getNameContainingTags(){
        when(tagRepository.findAllByTagNameContaining("test")).thenReturn(
                Arrays.asList(
                        Tag.builder()
                                .tagName(TEST_TAG_NAME + 1)
                                .build(),
                        Tag.builder().tagName(TEST_TAG_NAME +2)
                                .build()
                ));

        List<TagGetResponseDto> dto = tagService.getNameContainingTags("test");
        assertNotNull(dto);
        assertEquals(dto.size(),2);
    }

}
