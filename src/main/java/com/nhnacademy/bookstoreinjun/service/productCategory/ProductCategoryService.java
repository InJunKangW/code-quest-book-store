package com.nhnacademy.bookstoreinjun.service.productCategory;

import com.nhnacademy.bookstoreinjun.dto.category.CategoryGetResponseDto;
import com.nhnacademy.bookstoreinjun.dto.category.CategoryRegisterRequestDto;
import com.nhnacademy.bookstoreinjun.dto.category.CategoryRegisterResponseDto;
import com.nhnacademy.bookstoreinjun.dto.category.CategoryUpdateRequestDto;
import com.nhnacademy.bookstoreinjun.dto.category.CategoryUpdateResponseDto;
import com.nhnacademy.bookstoreinjun.dto.page.PageRequestDto;
import java.util.List;
import org.springframework.data.domain.Page;

public interface ProductCategoryService {
    CategoryRegisterResponseDto saveCategory(CategoryRegisterRequestDto requestDto);

    CategoryUpdateResponseDto updateCategory(CategoryUpdateRequestDto categoryUpdateRequestDto);

    CategoryGetResponseDto getCategoryDtoByName(String categoryName);

    List<CategoryGetResponseDto> getAllCategoryList();

    Page<CategoryGetResponseDto> getAllCategoryPage(PageRequestDto pageRequestDto);

    List<CategoryGetResponseDto> getNameContainingCategoryList(String categoryName);

    Page<CategoryGetResponseDto> getNameContainingCategoryPage(PageRequestDto pageRequestDto, String categoryName);

    List<CategoryGetResponseDto> getSubCategoryList(String categoryName);

    Page<CategoryGetResponseDto> getSubCategoryPage(PageRequestDto pageRequestDto, String categoryName);
}
