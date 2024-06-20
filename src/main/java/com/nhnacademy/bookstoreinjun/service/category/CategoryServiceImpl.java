package com.nhnacademy.bookstoreinjun.service.category;

import com.nhnacademy.bookstoreinjun.dto.category.CategoryGetResponseDto;
import com.nhnacademy.bookstoreinjun.dto.category.CategoryRegisterRequestDto;
import com.nhnacademy.bookstoreinjun.dto.category.CategoryRegisterResponseDto;
import com.nhnacademy.bookstoreinjun.entity.Category;
import com.nhnacademy.bookstoreinjun.exception.DuplicateException;
import com.nhnacademy.bookstoreinjun.exception.NotFoundIdException;
import com.nhnacademy.bookstoreinjun.exception.NotFoundNameException;
import com.nhnacademy.bookstoreinjun.repository.CategoryRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final String TYPE = "category";

    public CategoryRegisterResponseDto saveCategory(CategoryRegisterRequestDto categoryRegisterRequestDto) {
        String parentCategoryName = categoryRegisterRequestDto.parentCategoryName();
        String categoryName = categoryRegisterRequestDto.categoryName();

        if (parentCategoryName != null && !categoryRepository.existsByCategoryName(parentCategoryName)){
            throw new NotFoundNameException(TYPE, parentCategoryName);
        }else if (categoryRepository.existsByCategoryName(categoryName)){
            throw new DuplicateException(TYPE);
        }else {
            Category parentCategory = categoryRepository.findByCategoryName(parentCategoryName);
            categoryRepository.save(Category.builder()
                    .categoryName(categoryName)
                    .parentCategory(parentCategory)
                    .build());
            return new CategoryRegisterResponseDto(categoryName, parentCategoryName);
        }
    }

//    public Category updateCategory(Category category) {
//        if (!categoryRepository.existsById(category.getCategoryId())){
//            throw new NotFoundIdException(TYPE, category.getCategoryId());
//        }else{
//            return categoryRepository.save(category);
//        }
//    }

    public List<CategoryGetResponseDto> getAllCategories() {
        return categoryRepository.findAll().stream()
                .map(category -> CategoryGetResponseDto.builder()
                        .categoryName(category.getCategoryName())
                        .parentCategory(category.getParentCategory())
                        .build())
                .collect(Collectors.toList());
    }


    public List<CategoryGetResponseDto> getCategoriesContaining(String categoryName) {
        return categoryRepository.findAllByCategoryNameContaining(categoryName).stream()
                .map(category -> CategoryGetResponseDto.builder()
                        .categoryName(category.getCategoryName())
                        .parentCategory(category.getParentCategory())
                        .build())
                .collect(Collectors.toList());
    }

    public List<CategoryGetResponseDto> getSubCategories(String categoryName) {
        Category parent = categoryRepository.findByCategoryName(categoryName);
        if (parent == null) {
            throw new NotFoundNameException(TYPE, categoryName);
        }else {
            return categoryRepository.findSubCategoriesByParent(parent).stream()
                    .map(category -> CategoryGetResponseDto.builder()
                            .categoryName(category.getCategoryName())
                            .parentCategory(category.getParentCategory())
                            .build())
                    .collect(Collectors.toList());
        }
    }

    public CategoryGetResponseDto getCategoryDtoByName(String categoryName) {
        Category category = categoryRepository.findByCategoryName(categoryName);
        if (category == null){
            throw new NotFoundNameException(TYPE, categoryName);
        }else{
            return CategoryGetResponseDto.builder()
                    .categoryName(categoryName)
                    .parentCategory(category.getParentCategory())
                    .build();
        }
    }

    public Category getCategoryByName(String categoryName){
        Category category = categoryRepository.findByCategoryName(categoryName);
        if (category == null){
            throw new NotFoundNameException(TYPE, categoryName);
        }else{
            return category;
        }
    }
}
