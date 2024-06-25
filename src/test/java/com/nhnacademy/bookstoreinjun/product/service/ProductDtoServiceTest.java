package com.nhnacademy.bookstoreinjun.product.service;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.nhnacademy.bookstoreinjun.dto.page.PageRequestDto;
import com.nhnacademy.bookstoreinjun.dto.product.ProductGetResponseDto;
import com.nhnacademy.bookstoreinjun.entity.Product;
import com.nhnacademy.bookstoreinjun.repository.ProductRepository;
import com.nhnacademy.bookstoreinjun.service.product.ProductDtoServiceImpl;
import java.util.ArrayList;
import java.util.Arrays;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProductDtoServiceTest {
    @InjectMocks
    private ProductDtoServiceImpl productService;

    @Mock
    private ProductRepository productRepository;

    @Test
    public void contextLoads() {}

    @Test
    public void getAllProductPageTestSuccess(){
        when(productRepository.findAll(any(Pageable.class))).thenReturn(new PageImpl<>(Arrays.asList(
                Product.builder().build(),
                Product.builder().build(),
                Product.builder().build()
        )));

        PageRequestDto pageRequestDto = PageRequestDto.builder().build();

        Page<ProductGetResponseDto> responseDtoPage=  productService.findAllPage(pageRequestDto);

        assertNotNull(responseDtoPage);

        assertNotNull(responseDtoPage.getContent());

        assertEquals(responseDtoPage.getTotalElements(), 3);
    }
}
