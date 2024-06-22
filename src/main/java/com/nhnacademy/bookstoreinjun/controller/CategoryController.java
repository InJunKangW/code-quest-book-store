package com.nhnacademy.bookstoreinjun.controller;

import com.nhnacademy.bookstoreinjun.dto.category.CategoryGetResponseDto;
import com.nhnacademy.bookstoreinjun.dto.category.CategoryRegisterRequestDto;
import com.nhnacademy.bookstoreinjun.dto.category.CategoryRegisterResponseDto;
import com.nhnacademy.bookstoreinjun.service.productCategory.ProductCategoryService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
    private final ProductCategoryService productCategoryService;

    private final HttpHeaders header = new HttpHeaders() {{
        setContentType(MediaType.APPLICATION_JSON);
    }};

    @GetMapping("/list/all")
    public ResponseEntity<List<CategoryGetResponseDto>> getAllCategories() {
        log.info("Called list all");
        return new ResponseEntity<>(productCategoryService.getAllCategories(), header, HttpStatus.OK);
    }

    @GetMapping("/list/containing")
    public ResponseEntity<List<CategoryGetResponseDto>> getNameContainingCategories(@RequestParam("categoryName") String categoryName){
        return new ResponseEntity<>(productCategoryService.getNameContainingCategories(categoryName), header, HttpStatus.OK);
    }

    @GetMapping("/list/sub")
    public ResponseEntity<List<CategoryGetResponseDto>> getSubCategories(@RequestParam("categoryName") String categoryName){
        return new ResponseEntity<>(productCategoryService.getSubCategories(categoryName), header, HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<CategoryRegisterResponseDto> createCategory(@RequestBody CategoryRegisterRequestDto categoryRegisterRequestDto) {
        return new ResponseEntity<>(productCategoryService.saveCategory(categoryRegisterRequestDto), header, HttpStatus.CREATED);
    }
}
