package com.nhnacademy.bookstoreinjun.service.productCategory;

import com.nhnacademy.bookstoreinjun.dto.category.CategoryGetResponseDto;
import com.nhnacademy.bookstoreinjun.dto.category.CategoryRegisterRequestDto;
import com.nhnacademy.bookstoreinjun.dto.category.CategoryRegisterResponseDto;
import com.nhnacademy.bookstoreinjun.dto.category.CategoryUpdateRequestDto;
import com.nhnacademy.bookstoreinjun.dto.category.CategoryUpdateResponseDto;
import com.nhnacademy.bookstoreinjun.entity.ProductCategory;
import com.nhnacademy.bookstoreinjun.exception.DuplicateException;
import com.nhnacademy.bookstoreinjun.exception.NotFoundNameException;
import com.nhnacademy.bookstoreinjun.repository.ProductCategoryRepository;
import com.nhnacademy.bookstoreinjun.repository.ProductCategoryRelationRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductCategoryServiceImpl implements ProductCategoryService {
    private final ProductCategoryRepository productCategoryRepository;
    private final ProductCategoryRelationRepository productCategoryRelationRepository;

    private final String TYPE = "productCategory";

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

    public List<CategoryGetResponseDto> getAllCategories() {
        return productCategoryRepository.findAll().stream()
                .map(category -> CategoryGetResponseDto.builder()
                        .categoryName(category.getCategoryName())
                        .parentProductCategory(category.getParentProductCategory())
                        .build())
                .collect(Collectors.toList());
    }


    public List<CategoryGetResponseDto> getNameContainingCategories(String categoryName) {
        return productCategoryRepository.findAllByCategoryNameContaining(categoryName).stream()
                .map(category -> CategoryGetResponseDto.builder()
                        .categoryName(category.getCategoryName())
                        .parentProductCategory(category.getParentProductCategory())
                        .build())
                .collect(Collectors.toList());
    }

    public List<CategoryGetResponseDto> getSubCategories(String categoryName) {
        ProductCategory parent = productCategoryRepository.findByCategoryName(categoryName);
        if (parent == null) {
            throw new NotFoundNameException(TYPE, categoryName);
        }else {
            return productCategoryRepository.findSubCategoriesByParent(parent).stream()
                    .map(category -> CategoryGetResponseDto.builder()
                            .categoryName(category.getCategoryName())
                            .parentProductCategory(category.getParentProductCategory())
                            .build())
                    .collect(Collectors.toList());
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
