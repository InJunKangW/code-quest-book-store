package com.nhnacademy.bookstoreinjun.service.productCategory;

import com.nhnacademy.bookstoreinjun.dto.category.CategoryGetResponseDto;
import com.nhnacademy.bookstoreinjun.dto.category.CategoryRegisterRequestDto;
import com.nhnacademy.bookstoreinjun.dto.category.CategoryRegisterResponseDto;
import com.nhnacademy.bookstoreinjun.dto.category.CategoryUpdateRequestDto;
import com.nhnacademy.bookstoreinjun.dto.category.CategoryUpdateResponseDto;
import java.util.List;

public interface ProductCategoryService {
    CategoryRegisterResponseDto saveCategory(CategoryRegisterRequestDto requestDto);

    CategoryUpdateResponseDto updateCategory(CategoryUpdateRequestDto categoryUpdateRequestDto);

    List<CategoryGetResponseDto> getAllCategories();

    List<CategoryGetResponseDto> getNameContainingCategories(String categoryName);

    List<CategoryGetResponseDto> getSubCategories(String categoryName);

    CategoryGetResponseDto getCategoryDtoByName(String categoryName);
}
