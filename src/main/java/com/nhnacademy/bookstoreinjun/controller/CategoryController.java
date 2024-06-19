package com.nhnacademy.bookstoreinjun.controller;

import com.nhnacademy.bookstoreinjun.dto.category.CategoryRegisterRequestDto;
import com.nhnacademy.bookstoreinjun.dto.category.CategoryRegisterResponseDto;
import com.nhnacademy.bookstoreinjun.entity.Category;
import com.nhnacademy.bookstoreinjun.service.CategoryService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/category")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    @GetMapping("/list")
    public ResponseEntity<List<Category>> getAllCategories() {
        return ResponseEntity.ok(categoryService.getAllCategories());
    }

    @GetMapping("/list/containing")
    public ResponseEntity<List<Category>> getCategoriesContaining(@RequestParam("categoryName") String categoryName){
        return  ResponseEntity.ok(categoryService.getCategoriesContaining(categoryName));
    }

    @GetMapping("/list/sub")
    public ResponseEntity<List<Category>> getSubCategories(@RequestParam("categoryName") String categoryName){
        return ResponseEntity.ok(categoryService.getSubCategories(categoryName));
//        return null;
    }

    @PostMapping("/register")
    public ResponseEntity<CategoryRegisterResponseDto> createCategory(@RequestBody CategoryRegisterRequestDto categoryRegisterRequestDto) {

        Category category = categoryService.createCategory(categoryRegisterRequestDto);
        Category parentCategory = category.getParentCategory();
        String parentCategoryName = null;

        if (parentCategory != null) {
            parentCategoryName = parentCategory.getCategoryName();
        }

        CategoryRegisterResponseDto dto = CategoryRegisterResponseDto.builder()
                .categoryName(category.getCategoryName())
                .parentCategoryName(parentCategoryName)
                .build();

        return new ResponseEntity<>(dto, HttpStatus.CREATED);
    }
}
