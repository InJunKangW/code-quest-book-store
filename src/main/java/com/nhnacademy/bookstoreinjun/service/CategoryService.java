package com.nhnacademy.bookstoreinjun.service;

import com.nhnacademy.bookstoreinjun.dto.category.CategoryRegisterRequestDto;
import com.nhnacademy.bookstoreinjun.entity.Category;
import com.nhnacademy.bookstoreinjun.exception.DuplicateException;
import com.nhnacademy.bookstoreinjun.exception.NotFoundIdException;
import com.nhnacademy.bookstoreinjun.exception.NotFoundNameException;
import com.nhnacademy.bookstoreinjun.repository.CategoryRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final String DUPLICATE_TYPE = "category";

    public Category createCategory(CategoryRegisterRequestDto categoryRegisterRequestDto) {
        String parentCategoryName = categoryRegisterRequestDto.parentCategoryName();
        if (parentCategoryName != null && !categoryRepository.existsByCategoryName(parentCategoryName)){
            throw new NotFoundNameException(DUPLICATE_TYPE, categoryRegisterRequestDto.parentCategoryName());
        }else if (categoryRepository.existsByCategoryName(categoryRegisterRequestDto.categoryName())){
            throw new DuplicateException(DUPLICATE_TYPE);
        }else {
            Category parentCategory = categoryRepository.findByCategoryName(categoryRegisterRequestDto.parentCategoryName());
            return categoryRepository.save(Category.builder()
                    .categoryName(categoryRegisterRequestDto.categoryName())
                    .parentCategory(parentCategory)
                    .build());
        }
    }

    public Category updateCategory(Category category) {
        if (!categoryRepository.existsById(category.getCategoryId())){
            throw new NotFoundIdException(DUPLICATE_TYPE, category.getCategoryId());
        }else{
            return categoryRepository.save(category);
        }
    }

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public List<Category> getCategoriesContaining(String categoryName) {
        return categoryRepository.findAllByCategoryNameContaining(categoryName);
    }

    public List<Category> getSubCategories(String categoryName) {
        Category parent = categoryRepository.findByCategoryName(categoryName);
        return categoryRepository.findSubCategoriesByParent(parent);
    }

}
