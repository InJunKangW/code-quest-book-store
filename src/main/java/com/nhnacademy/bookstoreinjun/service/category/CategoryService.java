package com.nhnacademy.bookstoreinjun.service.category;

import com.nhnacademy.bookstoreinjun.dto.category.CategoryGetResponseDto;
import com.nhnacademy.bookstoreinjun.dto.category.CategoryRegisterRequestDto;
import com.nhnacademy.bookstoreinjun.dto.category.CategoryRegisterResponseDto;
import com.nhnacademy.bookstoreinjun.entity.Product;
import com.nhnacademy.bookstoreinjun.entity.ProductCategory;
import java.util.List;

public interface CategoryService {
    CategoryRegisterResponseDto saveCategory(CategoryRegisterRequestDto requestDto);

    List<CategoryGetResponseDto> getAllCategories();

    List<CategoryGetResponseDto> getNameContainingCategories(String categoryName);

    List<CategoryGetResponseDto> getSubCategories(String categoryName);

    CategoryGetResponseDto getCategoryDtoByName(String categoryName);

    //내부적으로만 호출됨.
    ProductCategory getCategoryByName(String categoryName);

    //내부적으로만 호출됨.
    List<ProductCategory> getCategoriesByProduct(Product product);
}
