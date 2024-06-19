package com.nhnacademy.bookstoreinjun.service;

import com.nhnacademy.bookstoreinjun.dto.product.ProductRegisterRequestDto;
import com.nhnacademy.bookstoreinjun.entity.Product;
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
    private final String DUPLICATE_TYPE = "product";


    public Product saveProduct(ProductRegisterRequestDto productRegisterRequestDto) {
        return productRepository.save(
                Product.builder()
                    .productName(productRegisterRequestDto.getProductName())
                    .productPriceStandard(productRegisterRequestDto.getProductPriceStandard())
                    .productPriceSales(productRegisterRequestDto.getProductPriceSales())
                    .productInventory(productRegisterRequestDto.getProductInventory())
                    .productThumbnailUrl(productRegisterRequestDto.getProductThumbnailUrl())
                    .productDescription(productRegisterRequestDto.getProductDescription())
                    .build());
    }

    public Product updateProduct(Product product) {
        if (!productRepository.existsByProductId(product.getProductId())) {
            throw new NotFoundIdException(DUPLICATE_TYPE, product.getProductId());
        }else{
            return productRepository.save(product);
        }
    }
}
