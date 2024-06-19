package com.nhnacademy.bookstoreinjun.service;

import com.nhnacademy.bookstoreinjun.entity.Category;
import com.nhnacademy.bookstoreinjun.entity.ProductCategory;
import com.nhnacademy.bookstoreinjun.exception.NotFoundIdException;
import com.nhnacademy.bookstoreinjun.repository.CategoryRepository;
import com.nhnacademy.bookstoreinjun.repository.ProductCategoryRepository;
import com.nhnacademy.bookstoreinjun.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductCategoryService {
    private final ProductCategoryRepository productCategoryRepository;

    private final ProductRepository productRepository;

    private final CategoryRepository categoryRepository;

    public ProductCategory saveProductCategory(ProductCategory productCategory) {
        Long productId = productCategory.getProduct().getProductId();
        if (!productRepository.existsByProductId(productId)){
            throw new NotFoundIdException("product", productId);
        }
        Category category = productCategory.getCategory();
        Long categoryId = category.getCategoryId();
        if(!categoryRepository.existsById(categoryId)){
            throw new NotFoundIdException("category", categoryId);
        };

        return productCategoryRepository.save(productCategory);
    }
}
