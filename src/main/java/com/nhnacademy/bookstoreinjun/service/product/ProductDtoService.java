package com.nhnacademy.bookstoreinjun.service.product;

import com.nhnacademy.bookstoreinjun.dto.page.PageRequestDto;
import com.nhnacademy.bookstoreinjun.dto.product.ProductGetResponseDto;
import org.springframework.data.domain.Page;

public interface ProductDtoService {
    Page<ProductGetResponseDto> findAllPage(PageRequestDto pageRequestDto);
    Page<ProductGetResponseDto> findNameContainingPage(PageRequestDto pageRequestDto, String productName);
}
