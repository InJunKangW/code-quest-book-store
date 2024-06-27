package com.nhnacademy.bookstoreinjun.service.product;

import com.nhnacademy.bookstoreinjun.dto.page.PageRequestDto;
import com.nhnacademy.bookstoreinjun.dto.product.ProductGetResponseDto;
import com.nhnacademy.bookstoreinjun.dto.product.ProductLikeRequestDto;
import com.nhnacademy.bookstoreinjun.dto.product.ProductLikeResponseDto;
import com.nhnacademy.bookstoreinjun.entity.Product;
import com.nhnacademy.bookstoreinjun.entity.ProductLike;
import com.nhnacademy.bookstoreinjun.exception.DuplicateException;
import com.nhnacademy.bookstoreinjun.exception.NotFoundIdException;
import com.nhnacademy.bookstoreinjun.exception.PageOutOfRangeException;
import com.nhnacademy.bookstoreinjun.exception.XUserIdNotFoundException;
import com.nhnacademy.bookstoreinjun.repository.ProductCategoryRelationRepository;
import com.nhnacademy.bookstoreinjun.repository.ProductCategoryRepository;
import com.nhnacademy.bookstoreinjun.repository.ProductLikeRepository;
import com.nhnacademy.bookstoreinjun.repository.ProductRepository;
import com.nhnacademy.bookstoreinjun.repository.ProductTagRepository;
import com.nhnacademy.bookstoreinjun.repository.TagRepository;
import com.nhnacademy.bookstoreinjun.util.MakePageableUtil;
import com.nhnacademy.bookstoreinjun.util.SortCheckUtil;
import jakarta.validation.Valid;
import java.util.Objects;
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
public class ProductDtoServiceImpl implements ProductDtoService {

    private final ProductRepository productRepository;

    private final ProductLikeRepository productLikeRepository;

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
        Pageable pageable = MakePageableUtil.makePageable(pageRequestDto, DEFAULT_PAGE_SIZE, DEFAULT_SORT);

        try {
            Page<Product> productPage = productRepository.findAll(pageable);
            return makeProductGetResponseDtoPage(pageable, productPage);
        }catch (PropertyReferenceException e) {
            throw SortCheckUtil.ThrowInvalidSortNameException(pageable);
        }
    }

    public Page<ProductGetResponseDto> findNameContainingPage(@Valid PageRequestDto pageRequestDto, String productName) {
        Pageable pageable = MakePageableUtil.makePageable(pageRequestDto, DEFAULT_PAGE_SIZE, DEFAULT_SORT);

        try {
            Page<Product> productPage = productRepository.findByProductNameContaining(pageable, productName);
            return makeProductGetResponseDtoPage(pageable, productPage);
        }catch (PropertyReferenceException e) {
            throw SortCheckUtil.ThrowInvalidSortNameException(pageable);
        }
    }

    public ProductLikeResponseDto saveProductLike(Long clientId, ProductLikeRequestDto productLikeRequestDto){
        if (clientId ==- 1){
            throw new XUserIdNotFoundException();
        }
        Long requestClientId= productLikeRequestDto.clientId();
        if (!Objects.equals(requestClientId, clientId)){
            throw new XUserIdNotFoundException();
        }

        Long productId = productLikeRequestDto.productId();
        Optional<Product> optionalProduct = productRepository.findById(productId);
        if(optionalProduct.isPresent()){
            Product product = optionalProduct.get();
            if (productLikeRepository.existsByClientIdAndProduct(clientId, product)){
                throw new DuplicateException("Product Like");
            }
            productLikeRepository.save(ProductLike.builder()
                    .clientId(clientId)
                    .product(product)
                    .build());
        }else {
            throw new NotFoundIdException("product", productId);
        }
        return new ProductLikeResponseDto();
    }
}
