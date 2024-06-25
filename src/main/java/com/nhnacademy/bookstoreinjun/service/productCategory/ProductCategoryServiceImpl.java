package com.nhnacademy.bookstoreinjun.service.productCategory;

import com.nhnacademy.bookstoreinjun.dto.category.CategoryGetResponseDto;
import com.nhnacademy.bookstoreinjun.dto.category.CategoryRegisterRequestDto;
import com.nhnacademy.bookstoreinjun.dto.category.CategoryRegisterResponseDto;
import com.nhnacademy.bookstoreinjun.dto.category.CategoryUpdateRequestDto;
import com.nhnacademy.bookstoreinjun.dto.category.CategoryUpdateResponseDto;
import com.nhnacademy.bookstoreinjun.dto.page.PageRequestDto;
import com.nhnacademy.bookstoreinjun.entity.ProductCategory;
import com.nhnacademy.bookstoreinjun.exception.DuplicateException;
import com.nhnacademy.bookstoreinjun.exception.NotFoundNameException;
import com.nhnacademy.bookstoreinjun.exception.PageOutOfRangeException;
import com.nhnacademy.bookstoreinjun.repository.ProductCategoryRepository;
import com.nhnacademy.bookstoreinjun.util.MakePageableUtil;
import com.nhnacademy.bookstoreinjun.util.SortCheckUtil;
import jakarta.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductCategoryServiceImpl implements ProductCategoryService {
    private final ProductCategoryRepository productCategoryRepository;

    private final String TYPE = "productCategory";

    private final int DEFAULT_PAGE_SIZE = 10;

    private final String DEFAULT_SORT = "productCategoryId";

    public CategoryRegisterResponseDto saveCategory(CategoryRegisterRequestDto categoryRegisterRequestDto) {
        String parentCategoryName = categoryRegisterRequestDto.parentCategoryName();
        String categoryName = categoryRegisterRequestDto.categoryName();

        if (parentCategoryName != null && !productCategoryRepository.existsByCategoryName(parentCategoryName)){
            throw new NotFoundNameException(TYPE, parentCategoryName);
        }else if (productCategoryRepository.existsByCategoryName(categoryName)){
            throw new DuplicateException(TYPE);
        }else {
            ProductCategory parentProductCategory = productCategoryRepository.findByCategoryName(parentCategoryName);
            productCategoryRepository.save(ProductCategory.builder()
                    .categoryName(categoryName)
                    .parentProductCategory(parentProductCategory)
                    .build());
            return new CategoryRegisterResponseDto(categoryName, parentCategoryName);
        }
    }

    public CategoryUpdateResponseDto updateCategory(CategoryUpdateRequestDto categoryUpdateRequestDto) {
        String currentCategoryName = categoryUpdateRequestDto.currentCategoryName();
        String newCategoryName = categoryUpdateRequestDto.newCategoryName();

        if (!productCategoryRepository.existsByCategoryName(currentCategoryName)){
            throw new NotFoundNameException(TYPE, currentCategoryName);
        }else if (productCategoryRepository.existsByCategoryName(newCategoryName)){
            throw new DuplicateException(TYPE);
        }else {
            ProductCategory currentProductCategory = productCategoryRepository.findByCategoryName(currentCategoryName);
            currentProductCategory.setCategoryName(newCategoryName);
            productCategoryRepository.save(currentProductCategory);

            return CategoryUpdateResponseDto.builder()
                    .previousCategoryName(currentCategoryName)
                    .newCategoryName(newCategoryName)
                    .updateTime(LocalDateTime.now())
                    .build();
        }
    }


    private CategoryGetResponseDto makeCategoryGetResponseDtoFromProductCategory(ProductCategory productCategory) {
        return CategoryGetResponseDto.builder()
                .productCategoryId(productCategory.getProductCategoryId())
                .categoryName(productCategory.getCategoryName())
                .parentProductCategory(productCategory.getParentProductCategory())
                .build();
    }

    private Page<CategoryGetResponseDto> makeCategoryGetResponseDtoPage(Pageable pageable, Page<ProductCategory> productCategoryPage) {
        int total = productCategoryPage.getTotalPages();
        int maxPage = pageable.getPageNumber() + 1;

        if (total < maxPage){
            throw new PageOutOfRangeException(total, maxPage);
        }
        return productCategoryPage.map(this::makeCategoryGetResponseDtoFromProductCategory);
    }


    public List<CategoryGetResponseDto> getAllCategories() {
        return productCategoryRepository.findAll().stream()
                .map(category -> CategoryGetResponseDto.builder()
                        .categoryName(category.getCategoryName())
                        .parentProductCategory(category.getParentProductCategory())
                        .build())
                .collect(Collectors.toList());
    }


    public Page<CategoryGetResponseDto> getAllCategoryPage(@Valid PageRequestDto pageRequestDto) {
        Pageable pageable = MakePageableUtil.makePageable(pageRequestDto, DEFAULT_PAGE_SIZE, DEFAULT_SORT);

        try{
            Page<ProductCategory> productCategoryPage = productCategoryRepository.findAll(pageable);
            return makeCategoryGetResponseDtoPage(pageable, productCategoryPage);
        }catch (PropertyReferenceException e) {
           throw SortCheckUtil.sortExceptionHandle(pageable);
        }
    }


    public List<CategoryGetResponseDto> getNameContainingCategories(String categoryName) {
        return productCategoryRepository.findAllByCategoryNameContaining(categoryName).stream()
                .map(category -> CategoryGetResponseDto.builder()
                        .categoryName(category.getCategoryName())
                        .parentProductCategory(category.getParentProductCategory())
                        .build())
                .collect(Collectors.toList());
    }


    public Page<CategoryGetResponseDto> getNameContainingCategoryPage(@Valid PageRequestDto pageRequestDto, String categoryName) {
        Pageable pageable = MakePageableUtil.makePageable(pageRequestDto, DEFAULT_PAGE_SIZE, DEFAULT_SORT);
        try{
            Page<ProductCategory> productCategoryPage = productCategoryRepository.findAllByCategoryNameContaining(pageable, categoryName);
            return makeCategoryGetResponseDtoPage(pageable, productCategoryPage);
        }catch (PropertyReferenceException e) {
            throw SortCheckUtil.sortExceptionHandle(pageable);
        }
    }


    public List<CategoryGetResponseDto> getSubCategories(String categoryName) {
        ProductCategory parent = productCategoryRepository.findByCategoryName(categoryName);
        if (parent == null) {
            throw new NotFoundNameException(TYPE, categoryName);
        }else {
            return productCategoryRepository.findSubCategoriesByParentProductCategory(parent).stream()
                    .map(category -> CategoryGetResponseDto.builder()
                            .categoryName(category.getCategoryName())
                            .parentProductCategory(category.getParentProductCategory())
                            .build())
                    .collect(Collectors.toList());
        }
    }

    public Page<CategoryGetResponseDto> getSubCategoryPage(@Valid PageRequestDto pageRequestDto, String categoryName) {
        ProductCategory parent = productCategoryRepository.findByCategoryName(categoryName);
        if (parent == null) {
            throw new NotFoundNameException(TYPE, categoryName);
        }else {
            Pageable pageable = MakePageableUtil.makePageable(pageRequestDto, DEFAULT_PAGE_SIZE, DEFAULT_SORT);

            try{
                Page<ProductCategory> productCategoryPage = productCategoryRepository.findAllByParentProductCategory(pageable, parent);
                return makeCategoryGetResponseDtoPage(pageable, productCategoryPage);
            }catch (PropertyReferenceException e) {
                throw SortCheckUtil.sortExceptionHandle(pageable);
            }
        }
    }


    public CategoryGetResponseDto getCategoryDtoByName(String categoryName) {
        ProductCategory productCategory = productCategoryRepository.findByCategoryName(categoryName);
        if (productCategory == null){
            throw new NotFoundNameException(TYPE, categoryName);
        }else{
            return CategoryGetResponseDto.builder()
                    .categoryName(categoryName)
                    .parentProductCategory(productCategory.getParentProductCategory())
                    .build();
        }
    }
}
