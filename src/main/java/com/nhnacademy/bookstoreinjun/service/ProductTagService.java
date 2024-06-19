package com.nhnacademy.bookstoreinjun.service;

import com.nhnacademy.bookstoreinjun.entity.Product;
import com.nhnacademy.bookstoreinjun.entity.ProductTag;
import com.nhnacademy.bookstoreinjun.exception.NotFoundIdException;
import com.nhnacademy.bookstoreinjun.repository.ProductRepository;
import com.nhnacademy.bookstoreinjun.repository.ProductTagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductTagService {
    private final ProductTagRepository productTagRepository;

    private final ProductRepository productRepository;

    public ProductTag saveProductTag(ProductTag productTag) {
        Product product = productTag.getProduct();
        if (product == null || product.getProductId() == null) {
            throw new RuntimeException();
        } else if (!productRepository.existsByProductId(product.getProductId())) {
            throw new NotFoundIdException("product", product.getProductId());
        } else if (!product.equals(productRepository.findByProductId(product.getProductId()))) {
            throw new NotFoundIdException("product", product.getProductId());
        }

        return productTagRepository.save(productTag);
    }
}
