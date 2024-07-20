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
import com.nhnacademy.bookstoreinjun.exception.NotFoundIdException;
import com.nhnacademy.bookstoreinjun.exception.NotFoundNameException;
import com.nhnacademy.bookstoreinjun.exception.PageOutOfRangeException;
import com.nhnacademy.bookstoreinjun.repository.ProductTagRepository;
import com.nhnacademy.bookstoreinjun.repository.TagRepository;
import com.nhnacademy.bookstoreinjun.service.tag.TagServiceImpl;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
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
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
@DisplayName("태그 서비스 테스트")
class TagServiceTest {

    @InjectMocks
    private TagServiceImpl tagService;

    @Mock
    private TagRepository tagRepository;

    @Mock
    private ProductTagRepository productTagRepository;

    private final String TEST_TAG_NAME = "Test Tag";

    @DisplayName("태그 신규 등록 성공 테스트")
    @Test
    void saveTagTest(){
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

    @DisplayName("태그 신규 등록 실패 테스트 - 중복된 태그명")
    @Test
    void saveTagTest2(){
        when(tagRepository.existsByTagName(TEST_TAG_NAME)).thenReturn(true);

        Tag tag = Tag.builder()
                .tagName(TEST_TAG_NAME)
                .build();

        TagRegisterRequestDto dto = TagRegisterRequestDto.builder()
                .tagName(tag.getTagName())
                .build();

        assertThrows(DuplicateException.class, () -> tagService.saveTag(dto));
    }

    @DisplayName("태그 수정 성공 테스트")
    @Test
    void updateTagTestSuccess(){
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

    @DisplayName("태그 수정 실패 테스트 - 존재하지 않는 현재 태그명")
    @Test
    void updateTagTestFailureByNotExistingTagName(){
        when(tagRepository.existsByTagName(TEST_TAG_NAME)).thenReturn(false);

        TagUpdateRequestDto tagUpdateRequestDto = TagUpdateRequestDto.builder()
                .currentTagName(TEST_TAG_NAME)
                .newTagName("new" + TEST_TAG_NAME)
                .build();

        assertThrows(NotFoundNameException.class, () -> tagService.updateTag(tagUpdateRequestDto));
    }

    @DisplayName("태그 수정 실패 테스트 - 중복되는 태그명")
    @Test
    void updateTagTestFailureByDuplicateTagName(){
        when(tagRepository.existsByTagName(TEST_TAG_NAME)).thenReturn(true);
        when(tagRepository.existsByTagName("new" + TEST_TAG_NAME)).thenReturn(true);

        TagUpdateRequestDto tagUpdateRequestDto = TagUpdateRequestDto.builder()
                .currentTagName(TEST_TAG_NAME)
                .newTagName("new" + TEST_TAG_NAME)
                .build();

        assertThrows(DuplicateException.class, () -> tagService.updateTag(tagUpdateRequestDto));
    }


    @DisplayName("태그 페이지 조회 성공 테스트 - 모든 태그")
    @Test
    void getAllTagPageTestSuccess(){
        PageRequestDto pageRequestDto = PageRequestDto.builder()
                .build();

        when(tagRepository.findAll(any(Pageable.class))).thenReturn(new PageImpl<>(Arrays.asList(
                Tag.builder()
                        .tagName(TEST_TAG_NAME)
                        .build(),
                Tag.builder()
                        .tagName(TEST_TAG_NAME)
                        .build()
        )));
        Page<TagGetResponseDto> testTagPage = tagService.getAllTagPage(pageRequestDto);

        verify(tagRepository, times(1)).findAll(any(Pageable.class));
        assertNotNull(testTagPage);
        assertEquals(2, testTagPage.getTotalElements());
    }

    @DisplayName("태그 페이지 조회 실패 테스트 - 모든 태그 : 잘못된 정렬 조건")
    @Test
    void getAllTagPageTestFailureByWrongSort(){
        PageRequestDto pageRequestDto = PageRequestDto.builder().build();

        when(tagRepository.findAll(any(Pageable.class))).thenThrow(new PropertyReferenceException("wrong", TypeInformation.COLLECTION, new ArrayList<>()));

        assertThrows(InvalidSortNameException.class, () -> tagService.getAllTagPage(pageRequestDto));

    }

    @DisplayName("태그 페이지 조회 실패 테스트 - 모든 태그 : 초과된 페이지")
    @Test
    void getAllTagPageTestFailureByOutOfPageRange(){
        PageRequestDto pageRequestDto = PageRequestDto.builder()
                .page(100)
                .build();

        when(tagRepository.findAll(any(Pageable.class))).thenReturn(new PageImpl<>(Arrays.asList(
                Tag.builder()
                        .tagName(TEST_TAG_NAME)
                        .build(),
                Tag.builder()
                        .tagName(TEST_TAG_NAME)
                        .build()
        )));

        assertThrows(PageOutOfRangeException.class, () -> tagService.getAllTagPage(pageRequestDto));
    }


    @DisplayName("태그 페이지 조회 성공 테스트 - 특정 이름 포함")
    @Test
    void getNameContainingTagPageTestSuccess(){
        PageRequestDto pageRequestDto = PageRequestDto.builder()
                .build();

        when(tagRepository.findAllByTagNameContaining(any(Pageable.class), eq("test"))).thenReturn(new PageImpl<>(Arrays.asList(
                Tag.builder()
                        .tagName(TEST_TAG_NAME)
                        .build(),
                Tag.builder()
                        .tagName(TEST_TAG_NAME)
                        .build(),
                Tag.builder()
                        .tagName(TEST_TAG_NAME)
                        .build()
        )));

        Page<TagGetResponseDto> testTagPage = tagService.getNameContainingTagPage(pageRequestDto, "test");

        verify(tagRepository, times(1)).findAllByTagNameContaining(any(Pageable.class), eq("test"));
        assertNotNull(testTagPage);
        assertEquals(3, testTagPage.getTotalElements());
    }


    @DisplayName("태그 페이지 조회 실패 테스트 - 특정 이름 포함 : 잘못된 정렬 조건")
    @Test
    void getNameContainingTagPageTestFailure(){
        PageRequestDto pageRequestDto = PageRequestDto.builder()
                .sort("wrong")
                .build();

        when(tagRepository.findAllByTagNameContaining(any(Pageable.class), eq("test"))).thenThrow(new PropertyReferenceException("wrong", TypeInformation.COLLECTION, new ArrayList<>()));

        assertThrows(InvalidSortNameException.class, () -> tagService.getNameContainingTagPage(pageRequestDto, "test"));
    }

    @DisplayName("태그 페이지 조회 실패 테스트 - 특정 이름 포함 : 초과된 페이지")
    @Test
    void getNameContainingTagPageTestFailure2(){
        PageRequestDto pageRequestDto = PageRequestDto.builder()
                .page(100)
                .build();

        when(tagRepository.findAllByTagNameContaining(any(Pageable.class), eq("test"))).thenReturn(new PageImpl<>(Arrays.asList(
                Tag.builder()
                        .tagName(TEST_TAG_NAME)
                        .build(),
                Tag.builder()
                        .tagName(TEST_TAG_NAME)
                        .build(),
                Tag.builder()
                        .tagName(TEST_TAG_NAME)
                        .build()
        )));

        assertThrows(PageOutOfRangeException.class, () -> tagService.getNameContainingTagPage(pageRequestDto, "test"));
    }


    @DisplayName("태그 삭제 성공 테스트")
    @Test
    void deleteTagTestSuccess(){
        Tag tag = Tag.builder()
                .tagName(TEST_TAG_NAME)
                .build();
        when(tagRepository.findById(1L)).thenReturn(Optional.of(tag));
        when(productTagRepository.existsByTag(tag)).thenReturn(false);

        ResponseEntity<Void> response = tagService.deleteTag(1L);
        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        verify(tagRepository, times(1)).delete(tag);
    }

    @DisplayName("태그 삭제 실패 테스트 - 존재하지 않는 태그")
    @Test
    void deleteTagTestFailure1(){
        when(tagRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundIdException.class, () -> tagService.deleteTag(1L));
    }

    @DisplayName("태그 삭제 실패 테스트 - 존재하는 상품-태그 관계")
    @Test
    void deleteTagTestFailure2(){
        Tag tag = Tag.builder()
                .tagName(TEST_TAG_NAME)
                .build();
        when(tagRepository.findById(1L)).thenReturn(Optional.of(tag));
        when(productTagRepository.existsByTag(tag)).thenReturn(true);

        ResponseEntity<Void> response = tagService.deleteTag(1L);
        assertNotNull(response);
        assertEquals(409, response.getStatusCode().value());
        verify(tagRepository, times(0)).delete(tag);
    }
}
