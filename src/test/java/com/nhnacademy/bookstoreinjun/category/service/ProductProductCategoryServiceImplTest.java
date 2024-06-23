package com.nhnacademy.bookstoreinjun.category.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


import com.nhnacademy.bookstoreinjun.dto.category.CategoryGetResponseDto;
import com.nhnacademy.bookstoreinjun.dto.category.CategoryRegisterRequestDto;
import com.nhnacademy.bookstoreinjun.dto.category.CategoryRegisterResponseDto;
import com.nhnacademy.bookstoreinjun.dto.category.CategoryUpdateRequestDto;
import com.nhnacademy.bookstoreinjun.dto.category.CategoryUpdateResponseDto;
import com.nhnacademy.bookstoreinjun.dto.page.PageRequestDto;
import com.nhnacademy.bookstoreinjun.entity.ProductCategory;
import com.nhnacademy.bookstoreinjun.entity.Tag;
import com.nhnacademy.bookstoreinjun.exception.DuplicateException;
import com.nhnacademy.bookstoreinjun.exception.InvalidSortNameException;
import com.nhnacademy.bookstoreinjun.exception.NotFoundNameException;
import com.nhnacademy.bookstoreinjun.exception.PageOutOfRangeException;
import com.nhnacademy.bookstoreinjun.repository.ProductCategoryRepository;
import com.nhnacademy.bookstoreinjun.service.productCategory.ProductCategoryServiceImpl;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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

@ExtendWith(MockitoExtension.class)
public class ProductProductCategoryServiceImplTest {

    @InjectMocks
    private ProductCategoryServiceImpl categoryService;

    @Mock
    private ProductCategoryRepository productCategoryRepository;

    private final String TEST_CATEGORY_NAME = "Test ProductCategory";

    private final String TEST_PARENT_CATEGORY_NAME = "Test Parent ProductCategory";

    private final CategoryRegisterRequestDto requestDto = CategoryRegisterRequestDto.builder()
            .categoryName(TEST_CATEGORY_NAME)
            .build();


    @DisplayName("카테고리 신규 등록 성공 테스트")
    @Test
    public void saveCategoryTestSuccess(){
        CategoryRegisterResponseDto resultDto =  categoryService.saveCategory(requestDto);

        assertNotNull(resultDto);

        verify(productCategoryRepository, times(1)).save(any(ProductCategory.class));
    }

    @DisplayName("카테고리 신규 등록 실패 테스트 - 존재하지 않는 상위 카테고리명")
    @Test
    public void saveCategoryTestFailureByNotFoundParentCategoryName(){
        when(productCategoryRepository.existsByCategoryName(TEST_PARENT_CATEGORY_NAME)).thenReturn(false);

        CategoryRegisterRequestDto dto = CategoryRegisterRequestDto.builder()
                .categoryName(TEST_PARENT_CATEGORY_NAME)
                .parentCategoryName(TEST_PARENT_CATEGORY_NAME)
                .build();

        assertThrows(NotFoundNameException.class, () -> categoryService.saveCategory(dto));
    }

    @DisplayName("카테고리 신규 등록 실패 테스트 - 중복되는 카테고리명")
    @Test
    public void saveCategoryTestFailureByExistingCategoryName(){
        when(productCategoryRepository.existsByCategoryName(TEST_CATEGORY_NAME)).thenReturn(true);

        assertThrows(DuplicateException.class, () -> categoryService.saveCategory(requestDto));
    }

    @DisplayName("카테고리 업데이트 성공 테스트")
    @Test
    public void updateCategoryTestSuccess(){
        when(productCategoryRepository.existsByCategoryName(TEST_CATEGORY_NAME)).thenReturn(true);
        when(productCategoryRepository.existsByCategoryName("new" + TEST_CATEGORY_NAME)).thenReturn(false);
        when(productCategoryRepository.findByCategoryName(TEST_CATEGORY_NAME)).thenReturn(new ProductCategory());

        CategoryUpdateRequestDto dto = CategoryUpdateRequestDto.builder()
                .currentCategoryName(TEST_CATEGORY_NAME)
                .newCategoryName("new" + TEST_CATEGORY_NAME)
                .build();

        CategoryUpdateResponseDto resultDto = categoryService.updateCategory(dto);

        assertNotNull(resultDto);
        assertEquals(resultDto.previousCategoryName(), TEST_CATEGORY_NAME);
        assertEquals(resultDto.newCategoryName(),"new" + TEST_CATEGORY_NAME);
        assertNotNull(resultDto.updateTime());
    }

    @DisplayName("카테고리 업데이트 실패 테스트 - 바뀌기 이전의 카테고리명이 현재 존재하지 않음")
    @Test
    public void updateCategoryTestFailureByNotFoundCategoryName(){
        when(productCategoryRepository.existsByCategoryName(TEST_CATEGORY_NAME)).thenReturn(false);

        CategoryUpdateRequestDto dto = CategoryUpdateRequestDto.builder()
                .currentCategoryName(TEST_CATEGORY_NAME)
                .newCategoryName("new" + TEST_CATEGORY_NAME)
                .build();

        assertThrows(NotFoundNameException.class, () -> categoryService.updateCategory(dto));
    }

    @DisplayName("카테고리 업데이트 실패 테스트 - 바뀌기 이후의 카테고리명이 중복됨")
    @Test
    public void updateCategoryTestFailureByExistingCategoryName(){
        when(productCategoryRepository.existsByCategoryName(TEST_CATEGORY_NAME)).thenReturn(true);
        when(productCategoryRepository.existsByCategoryName("new" + TEST_CATEGORY_NAME)).thenReturn(true);


        CategoryUpdateRequestDto dto = CategoryUpdateRequestDto.builder()
                .currentCategoryName(TEST_CATEGORY_NAME)
                .newCategoryName("new" + TEST_CATEGORY_NAME)
                .build();

        assertThrows(DuplicateException.class, () -> categoryService.updateCategory(dto));
    }

    @DisplayName("카테고리 조회 성공 테스트")
    @Test
    public void getCategoryDtoTestSuccess(){
        when(productCategoryRepository.findByCategoryName(TEST_CATEGORY_NAME)).thenReturn(new ProductCategory());

        CategoryGetResponseDto dto = categoryService.getCategoryDtoByName(TEST_CATEGORY_NAME);

        verify(productCategoryRepository, times(1)).findByCategoryName(TEST_CATEGORY_NAME);

        assertNotNull(dto);
    }

    @DisplayName("카테고리 조회 실패 테스트 - 존재하지 않는 카테고리명")
    @Test
    public void getCategoryDtoTestFailureByNotFoundCategoryName(){
        when(productCategoryRepository.findByCategoryName(TEST_CATEGORY_NAME)).thenReturn(null);

        assertThrows(NotFoundNameException.class, () -> categoryService.getCategoryDtoByName(TEST_CATEGORY_NAME));

        verify(productCategoryRepository, times(1)).findByCategoryName(TEST_CATEGORY_NAME);
    }


    @DisplayName("카테고리 리스트 조회 테스트 - 모든 카테고리")
    @Test
    public void getAllCategoriesTest(){
        when(productCategoryRepository.findAll()).thenReturn(
                Arrays.asList(
                        ProductCategory.builder()
                                .categoryName(TEST_CATEGORY_NAME + 1)
                                .build(),
                        ProductCategory.builder()
                                .categoryName(TEST_CATEGORY_NAME + 2)
                                .build()
                ));
        List<CategoryGetResponseDto> dto = categoryService.getAllCategories();
        assertNotNull(dto);
        assertEquals(dto.size(), 2);
        verify(productCategoryRepository, times(1)).findAll();
    }

    @DisplayName("카테고리 리스트 조회 테스트 - 특정 이름 포함")
    @Test
    public void getCategoriesContaining(){
        when(productCategoryRepository.findAllByCategoryNameContaining("test")).thenReturn(
                Arrays.asList(
                ProductCategory.builder()
                        .categoryName(TEST_CATEGORY_NAME + 1)
                        .build(),
                ProductCategory.builder()
                        .categoryName(TEST_CATEGORY_NAME + 2)
                        .build()));
        List<CategoryGetResponseDto> dto = categoryService.getNameContainingCategories("test");
        assertNotNull(dto);
        assertEquals(dto.size(), 2);
        verify(productCategoryRepository, times(1)).findAllByCategoryNameContaining("test");
    }


    @DisplayName("특정 카테고리의 하위 카테고리 리스트 조회 성공 테스트")
    @Test
    public void getSubCategoriesTestFailureByNotFoundCategoryName(){
        ProductCategory testCategory = new ProductCategory();
        when(productCategoryRepository.findByCategoryName("test")).thenReturn(testCategory);

        when(productCategoryRepository.findSubCategoriesByParent(testCategory)).thenReturn(
                Arrays.asList(
                        ProductCategory.builder()
                                .categoryName(TEST_CATEGORY_NAME + 1)
                                .build(),
                        ProductCategory.builder()
                                .categoryName(TEST_CATEGORY_NAME + 2)
                                .build()));

        List<CategoryGetResponseDto> dto = categoryService.getSubCategories("test");
        assertNotNull(dto);
        assertEquals(dto.size(), 2);

        verify(productCategoryRepository, times(1)).findByCategoryName("test");
        verify(productCategoryRepository, times(1)).findSubCategoriesByParent(testCategory);
    }

    @DisplayName("특정 카테고리의 하위 카테고리 리스트 조회 실패 테스트 - 존재하지 않는 상위 카테고리")
    @Test
    public void getSubCategoriesTestSuccess(){
        when(productCategoryRepository.findByCategoryName(TEST_PARENT_CATEGORY_NAME)).thenReturn(null);
        assertThrows(NotFoundNameException.class, () -> categoryService.getSubCategories(TEST_PARENT_CATEGORY_NAME));
    }

    @DisplayName("모든 카테고리 페이지 조회 성공 테스트")
    @Test
    public void getAllCategoryPageTestSuccess(){
        PageRequestDto pageRequestDto = PageRequestDto.builder().build();
        when(productCategoryRepository.findAll(any(Pageable.class))).thenReturn(new PageImpl<>(Arrays.asList(
                ProductCategory.builder()
                       .categoryName(TEST_CATEGORY_NAME)
                       .build(),
                ProductCategory.builder()
                        .categoryName(TEST_CATEGORY_NAME)
                        .build()
        )));

        Page<CategoryGetResponseDto> dto = categoryService.getAllCategoryPage(pageRequestDto);
        assertNotNull(dto);

        assertEquals(dto.getTotalElements(), 2);
    }



    @DisplayName("모든 카테고리 페이지 조회 실패 테스트 - 잘못된 정렬 조건")
    @Test
    public void getAllCategoryPageTestFailureByWrongSort(){
        PageRequestDto pageRequestDto = PageRequestDto.builder().build();

        when(productCategoryRepository.findAll(any(Pageable.class))).thenThrow(new PropertyReferenceException("wrong", TypeInformation.COLLECTION, new ArrayList<>()));

        assertThrows(InvalidSortNameException.class, () -> categoryService.getAllCategoryPage(pageRequestDto));
    }

    @DisplayName("모든 카테고리 페이지 조회 실패 테스트 - 초과된 페이지 넘버")
    @Test
    public void getAllTagPageTestFailureByOutOfPageRange(){
        PageRequestDto pageRequestDto = PageRequestDto.builder()
                .page(100)
                .build();

        when(productCategoryRepository.findAll(any(Pageable.class))).thenReturn(new PageImpl<>(Arrays.asList(
                ProductCategory.builder()
                        .categoryName(TEST_CATEGORY_NAME)
                        .build(),
                ProductCategory.builder()
                        .categoryName(TEST_CATEGORY_NAME)
                        .build()
        )));

        assertThrows(PageOutOfRangeException.class, () -> categoryService.getAllCategoryPage(pageRequestDto));
    }

    @DisplayName("특정 이름이 포함된 카테고리의 페이지 조회 성공 테스트")
    @Test
    public void getNameContainingCategoryPageTestSuccess(){
        PageRequestDto pageRequestDto = PageRequestDto.builder().build();
        when(productCategoryRepository.findAllByCategoryNameContaining(any(Pageable.class), eq(TEST_CATEGORY_NAME))).thenReturn(new PageImpl<>(Arrays.asList(
                ProductCategory.builder()
                        .categoryName(TEST_CATEGORY_NAME)
                        .build(),
                ProductCategory.builder()
                        .categoryName(TEST_CATEGORY_NAME)
                        .build()
        )));

        Page<CategoryGetResponseDto> dto = categoryService.getNameContainingCategoryPage(pageRequestDto, TEST_CATEGORY_NAME);

        assertNotNull(dto);

        assertEquals(dto.getTotalElements(), 2);
    }

    @DisplayName("특정 이름이 포함된 카테고리의 페이지 조회 실패 테스트 - 잘못된 정렬 조건")
    @Test
    public void getNameContainingCategoryPageTestFailureByWrongSort(){
        PageRequestDto pageRequestDto = PageRequestDto.builder().build();

        when(productCategoryRepository.findAllByCategoryNameContaining(any(Pageable.class), eq(TEST_CATEGORY_NAME))).thenThrow(new PropertyReferenceException("wrong", TypeInformation.COLLECTION, new ArrayList<>()));

        assertThrows(InvalidSortNameException.class, () -> categoryService.getNameContainingCategoryPage(pageRequestDto, TEST_CATEGORY_NAME));
    }

    @DisplayName("특정 카테고리의 하위 카테고리 페이지 조회 성공 테스트")
    @Test
    public void getSubCategoryPageTestSuccess(){
        PageRequestDto pageRequestDto = PageRequestDto.builder().build();
        when(productCategoryRepository.findAllByParentProductCategory(any(Pageable.class), any(ProductCategory.class))).thenReturn(new PageImpl<>(Arrays.asList(
                ProductCategory.builder()
                        .categoryName(TEST_CATEGORY_NAME)
                        .build(),
                ProductCategory.builder()
                        .categoryName(TEST_CATEGORY_NAME)
                        .build()
        )));

        when(productCategoryRepository.findByCategoryName(TEST_CATEGORY_NAME)).thenReturn(new ProductCategory());

        Page<CategoryGetResponseDto> dto = categoryService.getSubCategoryPage(pageRequestDto, TEST_CATEGORY_NAME);

        assertNotNull(dto);

        assertEquals(dto.getTotalElements(), 2);
    }

    @DisplayName("특정 카테고리의 하위 카테고리 페이지 조회 실패 테스트 - 존재하지 않는 상위 카테고리")
    @Test
    public void getSubCategoryPageTestFailureByNotExistingParentCategory(){
        PageRequestDto pageRequestDto = PageRequestDto.builder().build();

        when(productCategoryRepository.findByCategoryName(TEST_CATEGORY_NAME)).thenReturn(null);

        assertThrows(NotFoundNameException.class, () -> categoryService.getSubCategoryPage(pageRequestDto, TEST_CATEGORY_NAME));
    }

    @DisplayName("특정 카테고리의 하위 카테고리 페이지 조회 실패 테스트 - 잘못된 정렬 조건")
    @Test
    public void getSubCategoryPageTestFailureByWrongSort(){
        PageRequestDto pageRequestDto = PageRequestDto.builder().build();

        when(productCategoryRepository.findByCategoryName(TEST_CATEGORY_NAME)).thenReturn(new ProductCategory());

        when(productCategoryRepository.findAllByParentProductCategory(any(Pageable.class), any(ProductCategory.class))).thenThrow(new PropertyReferenceException("wrong", TypeInformation.COLLECTION, new ArrayList<>()));

        assertThrows(InvalidSortNameException.class, () -> categoryService.getSubCategoryPage(pageRequestDto, TEST_CATEGORY_NAME));
    }
}
