package com.nhnacademy.bookstoreinjun.controller;

import com.nhnacademy.bookstoreinjun.dto.category.CategoryGetResponseDto;
import com.nhnacademy.bookstoreinjun.dto.category.CategoryRegisterRequestDto;
import com.nhnacademy.bookstoreinjun.dto.category.CategoryRegisterResponseDto;
import com.nhnacademy.bookstoreinjun.dto.category.CategoryUpdateRequestDto;
import com.nhnacademy.bookstoreinjun.dto.category.CategoryUpdateResponseDto;
import com.nhnacademy.bookstoreinjun.dto.page.PageRequestDto;
import com.nhnacademy.bookstoreinjun.service.productCategory.ProductCategoryService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/product")
@RequiredArgsConstructor
public class CategoryController {
    private final ProductCategoryService productCategoryService;


    private final HttpHeaders header = new HttpHeaders() {{
        setContentType(MediaType.APPLICATION_JSON);
    }};


    @PostMapping("/admin/category/register")
    public ResponseEntity<CategoryRegisterResponseDto> createCategory(@Valid @RequestBody CategoryRegisterRequestDto categoryRegisterRequestDto) {
        return new ResponseEntity<>(productCategoryService.saveCategory(categoryRegisterRequestDto), header, HttpStatus.CREATED);
    }

    @PutMapping("/admin/category/update")
    public ResponseEntity<CategoryUpdateResponseDto> updateCategory(@Valid @RequestBody CategoryUpdateRequestDto categoryUpdateRequestDto) {
        return new ResponseEntity<>(productCategoryService.updateCategory(categoryUpdateRequestDto), header, HttpStatus.OK);
    }


    @GetMapping("/categories/all")
    public ResponseEntity<Page<CategoryGetResponseDto>> getAllCategories(
            @Valid @ModelAttribute PageRequestDto pageRequestDto) {
        return new ResponseEntity<>(productCategoryService.getAllCategoryPage(pageRequestDto), header, HttpStatus.OK);
    }

    @GetMapping("/categories/containing")
    public ResponseEntity<Page<CategoryGetResponseDto>> getNameContainingCategories(
            @Valid @ModelAttribute PageRequestDto pageRequestDto,
            @NotBlank @RequestParam("categoryName") String categoryName) {
        log.info("Called list containing");
        return new ResponseEntity<>(productCategoryService.getNameContainingCategoryPage(pageRequestDto, categoryName), header, HttpStatus.OK);
    }

    @GetMapping("/categories/sub")
    public ResponseEntity<Page<CategoryGetResponseDto>> getSubCategories(
            @Valid @ModelAttribute PageRequestDto pageRequestDto,
            @NotBlank @RequestParam("categoryName") String categoryName) {
        return new ResponseEntity<>(productCategoryService.getSubCategoryPage(pageRequestDto, categoryName), header, HttpStatus.OK);
    }

}
