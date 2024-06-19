package com.nhnacademy.bookstoreinjun.service;

import com.nhnacademy.bookstoreinjun.dto.category.CategoryRegisterRequestDto;
import com.nhnacademy.bookstoreinjun.entity.Category;
import com.nhnacademy.bookstoreinjun.exception.DuplicateException;
import com.nhnacademy.bookstoreinjun.exception.NotFoundIdException;
import com.nhnacademy.bookstoreinjun.exception.NotFoundNameException;
import com.nhnacademy.bookstoreinjun.repository.CategoryRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final String TYPE = "category";

    public Category createCategory(CategoryRegisterRequestDto categoryRegisterRequestDto) {
        String parentCategoryName = categoryRegisterRequestDto.parentCategoryName();
        String categoryName = categoryRegisterRequestDto.categoryName();

        if (parentCategoryName != null && !categoryRepository.existsByCategoryName(parentCategoryName)){
            throw new NotFoundNameException(TYPE, parentCategoryName);
        }else if (categoryRepository.existsByCategoryName(categoryName)){
            throw new DuplicateException(TYPE);
        }else {
            Category parentCategory = categoryRepository.findByCategoryName(parentCategoryName);
            return categoryRepository.save(Category.builder()
                    .categoryName(categoryName)
                    .parentCategory(parentCategory)
                    .build());
        }
    }

    public Category updateCategory(Category category) {
        if (!categoryRepository.existsById(category.getCategoryId())){
            throw new NotFoundIdException(TYPE, category.getCategoryId());
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

    public Category getCategoryByName(String categoryName) {
        Category category = categoryRepository.findByCategoryName(categoryName);
        if (category == null){
            throw new NotFoundNameException(TYPE, categoryName);
        }else{
            return category;
        }
    }
}
