package com.nhnacademy.bookstoreinjun.util;

import com.nhnacademy.bookstoreinjun.entity.Product;
import com.nhnacademy.bookstoreinjun.exception.InconsistentEntityException;
import com.nhnacademy.bookstoreinjun.exception.NotFoundIdException;
import com.nhnacademy.bookstoreinjun.exception.NullProductException;
import com.nhnacademy.bookstoreinjun.repository.ProductRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProductCheckUtil {

    private final ProductRepository productRepository;

    public void checkProduct(Product product){
        if (product == null || product.getProductId() == null) {
            throw new NullProductException();
        } else{
            Optional<Product> optionalProduct = productRepository.findById(product.getProductId());
            if (optionalProduct.isEmpty()) {
                throw new NotFoundIdException("product", product.getProductId());
            } else if (!product.equals(optionalProduct.get())) {
                throw new InconsistentEntityException("product");
            }
        }
    }
}
