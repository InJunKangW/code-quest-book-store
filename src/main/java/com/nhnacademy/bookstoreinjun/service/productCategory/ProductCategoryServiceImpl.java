package com.nhnacademy.bookstoreinjun.service.productCategory;

import com.nhnacademy.bookstoreinjun.dto.category.CategoryGetResponseDto;
import com.nhnacademy.bookstoreinjun.dto.category.CategoryRegisterRequestDto;
import com.nhnacademy.bookstoreinjun.dto.category.CategoryRegisterResponseDto;
import com.nhnacademy.bookstoreinjun.entity.ProductCategory;
import com.nhnacademy.bookstoreinjun.exception.DuplicateException;
import com.nhnacademy.bookstoreinjun.exception.NotFoundNameException;
import com.nhnacademy.bookstoreinjun.repository.CategoryRepository;
import com.nhnacademy.bookstoreinjun.repository.ProductCategoryRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductCategoryServiceImpl implements ProductCategoryService {
    private final CategoryRepository categoryRepository;
    private final ProductCategoryRepository productCategoryRepository;

    private final String TYPE = "productCategory";

    public CategoryRegisterResponseDto saveCategory(CategoryRegisterRequestDto categoryRegisterRequestDto) {
        String parentCategoryName = categoryRegisterRequestDto.parentCategoryName();
        String categoryName = categoryRegisterRequestDto.categoryName();

        if (parentCategoryName != null && !categoryRepository.existsByCategoryName(parentCategoryName)){
            throw new NotFoundNameException(TYPE, parentCategoryName);
        }else if (categoryRepository.existsByCategoryName(categoryName)){
            throw new DuplicateException(TYPE);
        }else {
            ProductCategory parentProductCategory = categoryRepository.findByCategoryName(parentCategoryName);
            categoryRepository.save(ProductCategory.builder()
                    .categoryName(categoryName)
                    .parentProductCategory(parentProductCategory)
                    .build());
            return new CategoryRegisterResponseDto(categoryName, parentCategoryName);
        }
    }

//    public ProductCategory updateCategory(ProductCategory productCategory) {
//        if (!categoryRepository.existsById(productCategory.getCategoryId())){
//            throw new NotFoundIdException(TYPE, productCategory.getCategoryId());
//        }else{
//            return categoryRepository.save(productCategory);
//        }
//    }

    public List<CategoryGetResponseDto> getAllCategories() {
        return categoryRepository.findAll().stream()
                .map(category -> CategoryGetResponseDto.builder()
                        .categoryName(category.getCategoryName())
                        .parentProductCategory(category.getParentProductCategory())
                        .build())
                .collect(Collectors.toList());
    }


    public List<CategoryGetResponseDto> getNameContainingCategories(String categoryName) {
        return categoryRepository.findAllByCategoryNameContaining(categoryName).stream()
                .map(category -> CategoryGetResponseDto.builder()
                        .categoryName(category.getCategoryName())
                        .parentProductCategory(category.getParentProductCategory())
                        .build())
                .collect(Collectors.toList());
    }

    public List<CategoryGetResponseDto> getSubCategories(String categoryName) {
        ProductCategory parent = categoryRepository.findByCategoryName(categoryName);
        if (parent == null) {
            throw new NotFoundNameException(TYPE, categoryName);
        }else {
            return categoryRepository.findSubCategoriesByParent(parent).stream()
                    .map(category -> CategoryGetResponseDto.builder()
                            .categoryName(category.getCategoryName())
                            .parentProductCategory(category.getParentProductCategory())
                            .build())
                    .collect(Collectors.toList());
        }
    }

    public CategoryGetResponseDto getCategoryDtoByName(String categoryName) {
        ProductCategory productCategory = categoryRepository.findByCategoryName(categoryName);
        if (productCategory == null){
            throw new NotFoundNameException(TYPE, categoryName);
        }else{
            return CategoryGetResponseDto.builder()
                    .categoryName(categoryName)
                    .parentProductCategory(productCategory.getParentProductCategory())
                    .build();
        }
    }

    //밑의 둘은 내부적으로만 호출.
    public ProductCategory getCategoryByName(String categoryName){
        ProductCategory productCategory = categoryRepository.findByCategoryName(categoryName);
        if (productCategory == null){
            throw new NotFoundNameException(TYPE, categoryName);
        }else{
            return productCategory;
        }
    }
}
