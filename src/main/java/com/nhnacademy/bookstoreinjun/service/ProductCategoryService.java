package com.nhnacademy.bookstoreinjun.service;

import com.nhnacademy.bookstoreinjun.entity.Category;
import com.nhnacademy.bookstoreinjun.entity.Product;
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

    public void saveProductCategory(ProductCategory productCategory) {
        Product product = productCategory.getProduct();
        if (product == null || product.getProductId() == null) {
            throw new RuntimeException();
        } else if (!productRepository.existsByProductId(product.getProductId())) {
            throw new NotFoundIdException("product", product.getProductId());
        } else if (!product.equals(productRepository.findByProductId(product.getProductId()))) {
            throw new NotFoundIdException("product", product.getProductId());
        }
        Category category = productCategory.getCategory();
        if (category == null || category.getCategoryId() == null) {
            throw new RuntimeException();
        } else if (!categoryRepository.existsById(category.getCategoryId())) {
            throw new NotFoundIdException("category", category.getCategoryId());
        }
        productCategoryRepository.save(productCategory);
    }
}
