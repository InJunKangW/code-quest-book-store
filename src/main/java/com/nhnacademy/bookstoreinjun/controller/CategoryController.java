package com.nhnacademy.bookstoreinjun.controller;

import com.nhnacademy.bookstoreinjun.dto.category.CategoryGetResponseDto;
import com.nhnacademy.bookstoreinjun.dto.category.CategoryRegisterRequestDto;
import com.nhnacademy.bookstoreinjun.dto.category.CategoryRegisterResponseDto;
import com.nhnacademy.bookstoreinjun.entity.Category;
import com.nhnacademy.bookstoreinjun.service.category.CategoryService;
import com.nhnacademy.bookstoreinjun.service.category.CategoryServiceImpl;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/admin/category")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    @GetMapping("/list/all")
    public ResponseEntity<List<CategoryGetResponseDto>> getAllCategories() {
        log.info("Called list all");
        return ResponseEntity.ok(categoryService.getAllCategories());
    }

    @GetMapping("/list/containing")
    public ResponseEntity<List<CategoryGetResponseDto>> getNameContainingCategories(@RequestParam("categoryName") String categoryName){
        return  ResponseEntity.ok(categoryService.getNameContainingCategories(categoryName));
    }

    @GetMapping("/list/sub")
    public ResponseEntity<List<CategoryGetResponseDto>> getSubCategories(@RequestParam("categoryName") String categoryName){
        return ResponseEntity.ok(categoryService.getSubCategories(categoryName));
    }

    @PostMapping("/register")
    public ResponseEntity<CategoryRegisterResponseDto> createCategory(@RequestBody CategoryRegisterRequestDto categoryRegisterRequestDto) {

//        CategoryRegisterResponseDto dto = categoryServiceImpl.createCategory(categoryRegisterRequestDto);
//        Category parentCategory = category.getParentCategory();
//        String parentCategoryName = null;
//
//        if (parentCategory != null) {
//            parentCategoryName = parentCategory.getCategoryName();
//        }
//
//        CategoryRegisterResponseDto dto = CategoryRegisterResponseDto.builder()
//                .categoryName(category.getCategoryName())
//                .parentCategoryName(parentCategoryName)
//                .build();

        return new ResponseEntity<>(categoryService.saveCategory(categoryRegisterRequestDto), HttpStatus.CREATED);
    }
}
