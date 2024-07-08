package com.nhnacademy.bookstoreinjun.service.product;

import com.nhnacademy.bookstoreinjun.dto.page.PageRequestDto;
import com.nhnacademy.bookstoreinjun.dto.product.ProductGetResponseDto;
import com.nhnacademy.bookstoreinjun.dto.product.ProductLikeRequestDto;
import com.nhnacademy.bookstoreinjun.dto.product.ProductLikeResponseDto;
import com.nhnacademy.bookstoreinjun.dto.product.ProductStateUpdateRequestDto;
import com.nhnacademy.bookstoreinjun.dto.product.ProductUpdateResponseDto;
import com.nhnacademy.bookstoreinjun.entity.Product;
import com.nhnacademy.bookstoreinjun.entity.ProductLike;
import com.nhnacademy.bookstoreinjun.exception.DuplicateException;
import com.nhnacademy.bookstoreinjun.exception.NotFoundIdException;
import com.nhnacademy.bookstoreinjun.exception.PageOutOfRangeException;
import com.nhnacademy.bookstoreinjun.exception.XUserIdNotFoundException;
import com.nhnacademy.bookstoreinjun.repository.ProductLikeRepository;
import com.nhnacademy.bookstoreinjun.repository.ProductRepository;
import com.nhnacademy.bookstoreinjun.repository.QuerydslRepository;
import com.nhnacademy.bookstoreinjun.util.PageableUtil;
import com.nhnacademy.bookstoreinjun.util.SortCheckUtil;
import jakarta.validation.Valid;
import java.time.LocalDateTime;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    private final ProductLikeRepository productLikeRepository;

    private final QuerydslRepository querydslRepository;

    private final int DEFAULT_PAGE_SIZE = 10;

    private final String DEFAULT_SORT = "productId";

    private ProductGetResponseDto makeProductGetResponseDtoFromProduct(Product product) {
        return ProductGetResponseDto.builder()
                .productId(product.getProductId())
                .productName(product.getProductName())
                .productState(product.getProductState())
                .productPriceStandard(product.getProductPriceStandard())
                .productPriceSales(product.getProductPriceSales())
                .productThumbNailImage(product.getProductThumbnailUrl())
                .build();
    }

    private Page<ProductGetResponseDto> makeProductGetResponseDtoPage(Pageable pageable, Page<Product> productPage) {
        int total = productPage.getTotalPages();
        int maxPage = pageable.getPageNumber() + 1;

        if (total != -0 && total < maxPage){
            throw new PageOutOfRangeException(total, maxPage);
        }

        return productPage.map(this::makeProductGetResponseDtoFromProduct);
    }

    public Page<ProductGetResponseDto> findAllPage(@Valid PageRequestDto pageRequestDto) {
        Pageable pageable = PageableUtil.makePageable(pageRequestDto, DEFAULT_PAGE_SIZE, DEFAULT_SORT);

        try {
            Page<Product> productPage = productRepository.findAll(pageable);
            return makeProductGetResponseDtoPage(pageable, productPage);
        }catch (PropertyReferenceException e) {
            throw SortCheckUtil.ThrowInvalidSortNameException(pageable);
        }
    }

    public Page<ProductGetResponseDto> findNameContainingPage(@Valid PageRequestDto pageRequestDto, String productName) {
        Pageable pageable = PageableUtil.makePageable(pageRequestDto, DEFAULT_PAGE_SIZE, DEFAULT_SORT);

        try {
            Page<Product> productPage = productRepository.findByProductNameContaining(pageable, productName);
            return makeProductGetResponseDtoPage(pageable, productPage);
        }catch (PropertyReferenceException e) {
            throw SortCheckUtil.ThrowInvalidSortNameException(pageable);
        }
    }



    public ProductLikeResponseDto saveProductLike(Long clientIdOfHeader, ProductLikeRequestDto productLikeRequestDto){
        if (clientIdOfHeader ==- 1){
            throw new XUserIdNotFoundException();
        }

        log.info("Saving product like request: {} with id {}", productLikeRequestDto, clientIdOfHeader);

        Long productId = productLikeRequestDto.productId();
        Optional<Product> optionalProduct = productRepository.findById(productId);
        if(optionalProduct.isPresent()){
            log.info("Saving product. found product!: {}", productLikeRequestDto);
            Product product = optionalProduct.get();
            if (productLikeRepository.existsByClientIdAndProduct(clientIdOfHeader, product)){
                throw new DuplicateException("Product Like");
            }
            productLikeRepository.save(ProductLike.builder()
                    .clientId(clientIdOfHeader)
                    .product(product)
                    .build());
        }else {
            log.warn("product not found with {}", productLikeRequestDto.productId());
            throw new NotFoundIdException("product", productId);
        }
        return new ProductLikeResponseDto();
    }

    public ProductLikeResponseDto deleteProductLike(Long clientIdOfHeader, Long productId) {
        if (clientIdOfHeader ==- 1){
            throw new XUserIdNotFoundException();
        }
        Product product = productRepository.findById(productId).orElseThrow(() -> new NotFoundIdException("product", productId));
        if (!productLikeRepository.existsByClientIdAndProduct(clientIdOfHeader, product)){
            throw new DuplicateException("Product Like");
        }else {
            ProductLike productLike = productLikeRepository.findByClientIdAndProduct(clientIdOfHeader, product);
            productLikeRepository.delete(productLike);
        }
        return new ProductLikeResponseDto();
    }

    public ProductUpdateResponseDto updateProductState(ProductStateUpdateRequestDto productStateUpdateRequestDto) {
        long result = querydslRepository.setProductState(productStateUpdateRequestDto.productId(), productStateUpdateRequestDto.productState());
        if (result == -1){
            throw new NotFoundIdException("product", productStateUpdateRequestDto.productId());
        }else {
            return new ProductUpdateResponseDto(LocalDateTime.now());
        }
    }
}
