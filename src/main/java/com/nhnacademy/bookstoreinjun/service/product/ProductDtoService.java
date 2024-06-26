package com.nhnacademy.bookstoreinjun.service.product;

import com.nhnacademy.bookstoreinjun.dto.page.PageRequestDto;
import com.nhnacademy.bookstoreinjun.dto.product.ProductGetResponseDto;
import com.nhnacademy.bookstoreinjun.dto.product.ProductLikeResponseDto;
import com.nhnacademy.bookstoreinjun.entity.Product;
import java.util.List;
import java.util.Set;
import org.springframework.data.domain.Page;

public interface ProductDtoService {
    Page<ProductGetResponseDto> findAllPage(PageRequestDto pageRequestDto);
    Page<ProductGetResponseDto> findNameContainingPage(PageRequestDto pageRequestDto, String productName);
    ProductLikeResponseDto saveProductLike(Long clientId, Long productId);
}
