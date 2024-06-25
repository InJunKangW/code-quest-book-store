package com.nhnacademy.bookstoreinjun.controller;

import com.nhnacademy.bookstoreinjun.dto.category.CategoryGetResponseDto;
import com.nhnacademy.bookstoreinjun.dto.category.CategoryRegisterRequestDto;
import com.nhnacademy.bookstoreinjun.dto.category.CategoryRegisterResponseDto;
import com.nhnacademy.bookstoreinjun.dto.category.CategoryUpdateRequestDto;
import com.nhnacademy.bookstoreinjun.dto.category.CategoryUpdateResponseDto;
import com.nhnacademy.bookstoreinjun.dto.page.PageRequestDto;
import com.nhnacademy.bookstoreinjun.service.productCategory.ProductCategoryService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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

    @GetMapping("/category/list/all")
    public ResponseEntity<List<CategoryGetResponseDto>> getAllCategories() {
        log.info("Called list all");
        return new ResponseEntity<>(productCategoryService.getAllCategories(), header, HttpStatus.OK);
    }


    @GetMapping("/category/list/containing")
    public ResponseEntity<List<CategoryGetResponseDto>> getNameContainingCategories(@RequestParam("categoryName") String categoryName){
        return new ResponseEntity<>(productCategoryService.getNameContainingCategories(categoryName), header, HttpStatus.OK);
    }

    @GetMapping("/category/list/sub")
    public ResponseEntity<List<CategoryGetResponseDto>> getSubCategories(@RequestParam("categoryName") String categoryName){
        return new ResponseEntity<>(productCategoryService.getSubCategories(categoryName), header, HttpStatus.OK);
    }

    @PostMapping("/admin/category/register")
    public ResponseEntity<CategoryRegisterResponseDto> createCategory(@RequestBody CategoryRegisterRequestDto categoryRegisterRequestDto) {
        return new ResponseEntity<>(productCategoryService.saveCategory(categoryRegisterRequestDto), header, HttpStatus.CREATED);
    }

    @PutMapping("/admin/category/update")
    public ResponseEntity<CategoryUpdateResponseDto> updateCategory(@RequestBody CategoryUpdateRequestDto categoryUpdateRequestDto) {
        return new ResponseEntity<>(productCategoryService.updateCategory(categoryUpdateRequestDto), header, HttpStatus.OK);
    }

    @GetMapping("/categories/all")
    public ResponseEntity<Page<CategoryGetResponseDto>> getAllCategories(@RequestParam(name = "page", required = false) Integer page, @RequestParam(name = "desc", required = false) Boolean desc, @RequestParam(name = "sort", required = false) String sort) {
        PageRequestDto pageRequestDto = PageRequestDto.builder().page(page).desc(desc).sort(sort).build();
        return new ResponseEntity<>(productCategoryService.getAllCategoryPage(pageRequestDto), header, HttpStatus.OK);
    }

    @GetMapping("/categories/containing")
    public ResponseEntity<Page<CategoryGetResponseDto>> getNameContainingCategories(@RequestParam(name = "page", required = false) Integer page, @RequestParam(name = "desc", required = false) Boolean desc, @RequestParam(name = "sort", required = false) String sort, @RequestParam("categoryName") String categoryName) {
        PageRequestDto pageRequestDto = PageRequestDto.builder().page(page).desc(desc).sort(sort).build();
        return new ResponseEntity<>(productCategoryService.getNameContainingCategoryPage(pageRequestDto, categoryName), header, HttpStatus.OK);
    }

    @GetMapping("/categories/sub")
    public ResponseEntity<Page<CategoryGetResponseDto>> getSubCategories(@RequestParam(name = "page", required = false) Integer page, @RequestParam(name = "desc", required = false) Boolean desc,  @RequestParam(name = "sort", required = false) String sort, @RequestParam("categoryName") String categoryName) {
        PageRequestDto pageRequestDto = PageRequestDto.builder().page(page).desc(desc).sort(sort).build();
        return new ResponseEntity<>(productCategoryService.getSubCategoryPage(pageRequestDto, categoryName), header, HttpStatus.OK);
    }

}
