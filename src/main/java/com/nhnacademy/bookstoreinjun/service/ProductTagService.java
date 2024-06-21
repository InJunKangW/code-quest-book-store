package com.nhnacademy.bookstoreinjun.service;

import com.nhnacademy.bookstoreinjun.entity.Product;
import com.nhnacademy.bookstoreinjun.entity.ProductTag;
import com.nhnacademy.bookstoreinjun.entity.Tag;
import com.nhnacademy.bookstoreinjun.exception.NotFoundIdException;
import com.nhnacademy.bookstoreinjun.repository.ProductRepository;
import com.nhnacademy.bookstoreinjun.repository.ProductTagRepository;
import com.nhnacademy.bookstoreinjun.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductTagService {
    private final ProductTagRepository productTagRepository;

    private final ProductRepository productRepository;

    private final TagRepository tagRepository;

    public void saveProductTag(ProductTag productTag) {
        Product product = productTag.getProduct();
        if (product == null || product.getProductId() == null) {
            throw new RuntimeException();
        } else if (!productRepository.existsByProductId(product.getProductId())) {
            throw new NotFoundIdException("product", product.getProductId());
        } else if (!product.equals(productRepository.findByProductId(product.getProductId()))) {
            throw new NotFoundIdException("product", product.getProductId());
        }
        Tag tag = productTag.getTag();
        if (tag == null || tag.getTagId() == null) {
            throw new RuntimeException();
        } else if (!tagRepository.existsById(tag.getTagId())) {
            throw new NotFoundIdException("tag", tag.getTagId());
        }
        productTagRepository.save(productTag);
    }
}
