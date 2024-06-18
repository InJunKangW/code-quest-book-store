package com.nhnacademy.bookstoreinjun.service;

import com.nhnacademy.bookstoreinjun.entity.Category;
import com.nhnacademy.bookstoreinjun.exception.DuplicateIdException;
import com.nhnacademy.bookstoreinjun.exception.NotFoundIdException;
import com.nhnacademy.bookstoreinjun.repository.CategoryRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final String DUPLICATE_TYPE = "category";

    public Category createCategory(Category category) {
        if (categoryRepository.existsById(category.getCategoryId())){
            throw new DuplicateIdException(DUPLICATE_TYPE);
        }else{
            return categoryRepository.save(category);
        }
    }

    public Category updateCategory(Category category) {
        if (!categoryRepository.existsById(category.getCategoryId())){
            throw new NotFoundIdException(DUPLICATE_TYPE, category.getCategoryId());
        }else{
            return categoryRepository.save(category);
        }
    }
}
