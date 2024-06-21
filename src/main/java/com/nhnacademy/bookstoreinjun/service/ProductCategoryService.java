package com.nhnacademy.bookstoreinjun.service;

import com.nhnacademy.bookstoreinjun.entity.ProductCategory;
import com.nhnacademy.bookstoreinjun.entity.Product;
import com.nhnacademy.bookstoreinjun.entity.ProductCategoryRelation;
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

    public void saveProductCategory(ProductCategoryRelation productCategoryRelation) {
        Product product = productCategoryRelation.getProduct();
        if (product == null || product.getProductId() == null) {
            throw new RuntimeException();
        } else if (!productRepository.existsByProductId(product.getProductId())) {
            throw new NotFoundIdException("product", product.getProductId());
        } else if (!product.equals(productRepository.findByProductId(product.getProductId()))) {
            throw new NotFoundIdException("product", product.getProductId());
        }
        ProductCategory productCategory = productCategoryRelation.getProductCategory();
        if (productCategory == null || productCategory.getProductCategoryId() == null) {
            throw new RuntimeException();
        } else if (!categoryRepository.existsById(productCategory.getProductCategoryId())) {
            throw new NotFoundIdException("productCategory", productCategory.getProductCategoryId());
        }
        productCategoryRepository.save(productCategoryRelation);
    }
}
