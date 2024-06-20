package com.nhnacademy.bookstoreinjun.category.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doCallRealMethod;
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
import java.util.ArrayList;
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


    @Test
    public void saveCategoryTest(){
        Category category = Category.builder()
                .categoryName("test category1")
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
                .categoryName("test category1")
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
                .categoryName("test category1")
                .build();

        CategoryRegisterRequestDto dto = CategoryRegisterRequestDto.builder()
                .categoryName(category.getCategoryName())
                .build();

        assertThrows(DuplicateException.class, () -> categoryService.saveCategory(dto));
    }

    @Test
    public void getCategoryDtoTest1(){
        when(categoryRepository.findByCategoryName(any())).thenReturn(new Category());

        CategoryGetResponseDto dto = categoryService.getCategoryDtoByName("test category");

        verify(categoryRepository, times(1)).findByCategoryName(any());

        assertNotNull(dto);
    }

    @Test
    public void getCategoryDtoTest2(){
        assertThrows(NotFoundNameException.class, () -> categoryService.getCategoryDtoByName("test category"));

        verify(categoryRepository, times(1)).findByCategoryName(any());
    }

    @Test
    public void getCategoryTest1(){
        when(categoryRepository.findByCategoryName(any())).thenReturn(new Category());

        Category category = categoryService.getCategoryByName("test category");

        verify(categoryRepository, times(1)).findByCategoryName(any());

        assertNotNull(category);
    }

    @Test
    public void getCategoryTest2(){
        when(categoryRepository.findByCategoryName(any())).thenReturn(null);

        assertThrows(NotFoundNameException.class, () -> categoryService.getCategoryByName("test category"));

        verify(categoryRepository, times(1)).findByCategoryName(any());
    }

    @Test
    public void getAllCategoriesTest(){
        when(categoryRepository.findAll()).thenReturn(
                Arrays.asList(
                        Category.builder()
                                .categoryName("test category1")
                                .build(),
                        Category.builder()
                                .categoryName("test category2")
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
                        .categoryName("test category1")
                        .build(),
                Category.builder()
                        .categoryName("test category2")
                        .build()));
        List<CategoryGetResponseDto> dto = categoryService.getCategoriesContaining("test");
        assertNotNull(dto);
        assertEquals(dto.size(), 2);
        verify(categoryRepository, times(1)).findAllByCategoryNameContaining(any());
    }

    @Test
    public void getSubCategoriesTest(){
        when(categoryRepository.findByCategoryName(any())).thenReturn(null);
        assertThrows(NotFoundNameException.class, () -> categoryService.getSubCategories("test"));
    }


}
