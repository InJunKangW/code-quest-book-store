package com.nhnacademy.bookstoreinjun.category.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


import com.nhnacademy.bookstoreinjun.dto.category.CategoryGetResponseDto;
import com.nhnacademy.bookstoreinjun.dto.category.CategoryRegisterRequestDto;
import com.nhnacademy.bookstoreinjun.entity.Category;
import com.nhnacademy.bookstoreinjun.exception.DuplicateException;
import com.nhnacademy.bookstoreinjun.exception.NotFoundNameException;
import com.nhnacademy.bookstoreinjun.repository.CategoryRepository;
import com.nhnacademy.bookstoreinjun.service.category.CategoryServiceImpl;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceTest {

    @InjectMocks
    private CategoryServiceImpl categoryService;

    @Mock
    private CategoryRepository categoryRepository;

    private final String TEST_CATEGORY_NAME = "Test Category";

    @Test
    public void saveCategoryTest(){
        Category category = Category.builder()
                .categoryName(TEST_CATEGORY_NAME)
                .build();

        CategoryRegisterRequestDto dto = CategoryRegisterRequestDto.builder()
                .categoryName(category.getCategoryName())
                .build();

        categoryService.saveCategory(dto);

        verify(categoryRepository, times(1)).save(any(Category.class));
    }

    @Test
    public void saveCategoryTest2(){
        when(categoryRepository.existsByCategoryName("parent category")).thenReturn(false);
        Category category = Category.builder()
                .categoryName(TEST_CATEGORY_NAME)
                .build();

        CategoryRegisterRequestDto dto = CategoryRegisterRequestDto.builder()
                .categoryName(category.getCategoryName())
                .parentCategoryName("parent category")
                .build();

        assertThrows(NotFoundNameException.class, () -> categoryService.saveCategory(dto));
    }

    @Test
    public void saveCategoryTest3(){
        when(categoryRepository.existsByCategoryName(any())).thenReturn(true);

        Category category = Category.builder()
                .categoryName(TEST_CATEGORY_NAME)
                .build();

        CategoryRegisterRequestDto dto = CategoryRegisterRequestDto.builder()
                .categoryName(category.getCategoryName())
                .build();

        assertThrows(DuplicateException.class, () -> categoryService.saveCategory(dto));
    }

    @Test
    public void getCategoryDtoTest1(){
        when(categoryRepository.findByCategoryName(TEST_CATEGORY_NAME)).thenReturn(new Category());

        CategoryGetResponseDto dto = categoryService.getCategoryDtoByName(TEST_CATEGORY_NAME);

        verify(categoryRepository, times(1)).findByCategoryName(TEST_CATEGORY_NAME);

        assertNotNull(dto);
    }

    @Test
    public void getCategoryDtoTest2(){
        assertThrows(NotFoundNameException.class, () -> categoryService.getCategoryDtoByName(TEST_CATEGORY_NAME));

        verify(categoryRepository, times(1)).findByCategoryName(TEST_CATEGORY_NAME);
    }

    @Test
    public void getCategoryTest1(){
        when(categoryRepository.findByCategoryName(TEST_CATEGORY_NAME)).thenReturn(new Category());

        Category category = categoryService.getCategoryByName(TEST_CATEGORY_NAME);

        verify(categoryRepository, times(1)).findByCategoryName(TEST_CATEGORY_NAME);

        assertNotNull(category);
    }

    @Test
    public void getCategoryTest2(){
        when(categoryRepository.findByCategoryName(TEST_CATEGORY_NAME)).thenReturn(null);

        assertThrows(NotFoundNameException.class, () -> categoryService.getCategoryByName(TEST_CATEGORY_NAME));

        verify(categoryRepository, times(1)).findByCategoryName(any());
    }

    @Test
    public void getAllCategoriesTest(){
        when(categoryRepository.findAll()).thenReturn(
                Arrays.asList(
                        Category.builder()
                                .categoryName(TEST_CATEGORY_NAME + 1)
                                .build(),
                        Category.builder()
                                .categoryName(TEST_CATEGORY_NAME + 2)
                                .build()
                ));
        List<CategoryGetResponseDto> dto = categoryService.getAllCategories();
        assertNotNull(dto);
        assertEquals(dto.size(), 2);
        verify(categoryRepository, times(1)).findAll();
    }

    @Test
    public void getCategoriesContaining(){
        when(categoryRepository.findAllByCategoryNameContaining("test")).thenReturn(
                Arrays.asList(
                Category.builder()
                        .categoryName(TEST_CATEGORY_NAME + 1)
                        .build(),
                Category.builder()
                        .categoryName(TEST_CATEGORY_NAME + 2)
                        .build()));
        List<CategoryGetResponseDto> dto = categoryService.getNameContainingCategories("test");
        assertNotNull(dto);
        assertEquals(dto.size(), 2);
        verify(categoryRepository, times(1)).findAllByCategoryNameContaining(any());
    }

    @Test
    public void getSubCategoriesTest1(){
        when(categoryRepository.findByCategoryName("test")).thenReturn(null);
        assertThrows(NotFoundNameException.class, () -> categoryService.getSubCategories("test"));
    }

    @Test
    public void getSubCategoriesTest2(){
        when(categoryRepository.findByCategoryName("test")).thenReturn(new Category());

        categoryService.getSubCategories("test");

        verify(categoryRepository, times(1)).findByCategoryName(any());
    }

}
