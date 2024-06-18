package com.nhnacademy.bookstoreinjun.service;

import com.nhnacademy.bookstoreinjun.dto.product.ProductRequestDto;
import com.nhnacademy.bookstoreinjun.entity.Product;
import com.nhnacademy.bookstoreinjun.exception.DuplicateIdException;
import com.nhnacademy.bookstoreinjun.exception.NotFoundIdException;
import com.nhnacademy.bookstoreinjun.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductService {
    private final ProductRepository productRepository;
    private final String DUPLICATE_TYPE = "category";


    public Product createProduct(ProductRequestDto productRequestDto) {
        Product product = Product.builder()
                .productName(productRequestDto.getProductName())
                .productPriceStandard(productRequestDto.getProductPriceStandard())
                .productPriceSales(productRequestDto.getProductPriceSales())
                .productInventory(productRequestDto.getProductInventory())
                .productThumbnailUrl(productRequestDto.getProductThumbnailUrl())
                .productDescription(productRequestDto.getProductDescription())
                .build();
        return productRepository.save(product);
    }

    public Product updateProduct(Product product) {
        if (!productRepository.existsByProductId(product.getProductId())) {
            throw new NotFoundIdException(DUPLICATE_TYPE, product.getProductId());
        }else{
            return productRepository.save(product);
        }
    }
}
