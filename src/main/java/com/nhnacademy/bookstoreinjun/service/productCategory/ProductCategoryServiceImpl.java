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
import com.nhnacademy.bookstoreinjun.util.FindAllSubCategoriesUtil;
import com.nhnacademy.bookstoreinjun.util.PageableUtil;
import com.nhnacademy.bookstoreinjun.util.SortCheckUtil;
import jakarta.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ProductCategoryServiceImpl implements ProductCategoryService {
    private final ProductCategoryRepository productCategoryRepository;

    private final FindAllSubCategoriesUtil findAllSubCategoriesUtil;

    private final String TYPE = "productCategory";

    private final int DEFAULT_PAGE_SIZE = 10;

    private final String DEFAULT_SORT = "productCategoryId";

    private CategoryGetResponseDto makeCategoryGetResponseDtoFromProductCategory(ProductCategory productCategory) {
        return CategoryGetResponseDto.builder()
                .productCategoryId(productCategory.getProductCategoryId())
                .categoryName(productCategory.getCategoryName())
                .parentProductCategory(productCategory.getParentProductCategory())
                .build();
    }

    private Page<CategoryGetResponseDto> makeCategoryGetResponseDtoPage(Pageable pageable, Page<ProductCategory> productCategoryPage) {
        int total = productCategoryPage.getTotalPages();
        int requestPage = pageable.getPageNumber() + 1;

        if (total != 0 && total < requestPage){
            throw new PageOutOfRangeException(total, requestPage);
        }
        return productCategoryPage.map(this::makeCategoryGetResponseDtoFromProductCategory);
    }


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

    public CategoryGetResponseDto getCategoryDtoByName(String categoryName) {
        ProductCategory productCategory = productCategoryRepository.findByCategoryName(categoryName);
        if (productCategory == null){
            throw new NotFoundNameException(TYPE, categoryName);
        }else{
            return makeCategoryGetResponseDtoFromProductCategory(productCategory);
        }
    }

    public Page<CategoryGetResponseDto> getAllCategoryPage(PageRequestDto pageRequestDto) {
        Pageable pageable = PageableUtil.makePageable(pageRequestDto, DEFAULT_PAGE_SIZE, DEFAULT_SORT);
        SortCheckUtil.pageSortCheck(ProductCategory.class, pageable);

        Page<ProductCategory> productCategoryPage = productCategoryRepository.findAll(pageable);
        return makeCategoryGetResponseDtoPage(pageable, productCategoryPage);
    }


    public Page<CategoryGetResponseDto> getNameContainingCategoryPage(PageRequestDto pageRequestDto, String categoryName) {
        Pageable pageable = PageableUtil.makePageable(pageRequestDto, DEFAULT_PAGE_SIZE, DEFAULT_SORT);
        SortCheckUtil.pageSortCheck(ProductCategory.class, pageable);

        Page<ProductCategory> productCategoryPage = productCategoryRepository.findAllByCategoryNameContaining(pageable, categoryName);
        return makeCategoryGetResponseDtoPage(pageable, productCategoryPage);
    }

    public Page<CategoryGetResponseDto> getSubCategoryPage(@Valid PageRequestDto pageRequestDto, String categoryName) {
        Pageable pageable = PageableUtil.makePageable(pageRequestDto, DEFAULT_PAGE_SIZE, DEFAULT_SORT);
        SortCheckUtil.pageSortCheck(ProductCategory.class, pageable);

        Set<ProductCategory> categorySet = findAllSubCategoriesUtil.getAllSubcategorySet(categoryName);
        List<ProductCategory> categoryList = categorySet.stream().toList();

        Page<ProductCategory> productCategoryPage = new PageImpl<>(categoryList, pageable, categoryList.size());
        return makeCategoryGetResponseDtoPage(pageable, productCategoryPage);
    }
}
