package com.nhnacademy.bookstoreinjun.util;

import com.nhnacademy.bookstoreinjun.entity.Product;
import com.nhnacademy.bookstoreinjun.exception.NotFoundIdException;
import com.nhnacademy.bookstoreinjun.repository.ProductRepository;

public class ProductCheckUtil {

    private static ProductRepository productRepository;

    public ProductCheckUtil() {
        throw new RuntimeException("This is a utility class and cannot be instantiated");
    }

    public static void checkProduct(Product product) {
        if (product == null || product.getProductId() == null) {
            throw new RuntimeException();
        } else if (!productRepository.existsByProductId(product.getProductId())) {
            throw new NotFoundIdException("product", product.getProductId());
        } else if (!product.equals(productRepository.findByProductId(product.getProductId()))) {
            throw new NotFoundIdException("product", product.getProductId());
        }
    }
}
