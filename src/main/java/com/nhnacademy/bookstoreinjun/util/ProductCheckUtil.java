package com.nhnacademy.bookstoreinjun.util;

import com.nhnacademy.bookstoreinjun.entity.Product;
import com.nhnacademy.bookstoreinjun.exception.InconsistentEntityException;
import com.nhnacademy.bookstoreinjun.exception.NotFoundIdException;
import com.nhnacademy.bookstoreinjun.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProductCheckUtil {

    private final ProductRepository productRepository;

    private static final String TYPE = "product";

    public void checkProduct(Product product){
        if (product == null || product.getProductId() == null) {
            throw new NotFoundIdException(TYPE, null);
        } else{
            Product foundProduct = productRepository.findById(product.getProductId()).orElseThrow(() -> new NotFoundIdException(TYPE, product.getProductId()));
            if (!product.equals(foundProduct)) {
                throw new InconsistentEntityException(TYPE);
            }
        }
    }
}
