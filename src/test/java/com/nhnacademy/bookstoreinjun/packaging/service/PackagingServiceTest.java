package com.nhnacademy.bookstoreinjun.packaging.service;


import com.nhnacademy.bookstoreinjun.dto.packaging.PackagingGetResponseDto;
import com.nhnacademy.bookstoreinjun.dto.packaging.PackagingRegisterRequestDto;
import com.nhnacademy.bookstoreinjun.dto.packaging.PackagingUpdateRequestDto;
import com.nhnacademy.bookstoreinjun.dto.product.ProductRegisterResponseDto;
import com.nhnacademy.bookstoreinjun.dto.product.ProductUpdateResponseDto;
import com.nhnacademy.bookstoreinjun.entity.Packaging;
import com.nhnacademy.bookstoreinjun.entity.Product;
import com.nhnacademy.bookstoreinjun.exception.InconsistentEntityException;
import com.nhnacademy.bookstoreinjun.exception.NotFoundIdException;
import com.nhnacademy.bookstoreinjun.repository.PackagingRepository;
import com.nhnacademy.bookstoreinjun.repository.ProductRepository;
import com.nhnacademy.bookstoreinjun.service.packaging.PackagingServiceImpl;
import com.nhnacademy.bookstoreinjun.util.ProductCheckUtil;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@Slf4j
@ExtendWith(MockitoExtension.class)
@DisplayName("포장지 서비스 테스트")
class PackagingServiceTest {

    @InjectMocks
    private PackagingServiceImpl packagingService;

    @Mock
    private PackagingRepository packagingRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductCheckUtil productCheckUtil;

    private Product product;

    @BeforeEach
    void setUp() {
        product = Product.builder()
                .productId(1L)
                .productName("test product")
                .productDescription("test description")
                .productState(0)
                .productPriceStandard(3L)
                .productPriceSales(2L)
                .productInventory(10L)
                .productThumbnailUrl("test image")
                .productRegisterDate(LocalDateTime.of(2000, 1, 10, 16, 30))
                .build();
    }

    @DisplayName("포장지 조회 성공 테스트 - 개별")
    @Test
    void getPackageInfoSuccessTest(){
        Packaging packaging = Packaging.builder()
                .product(product)
                .packageId(2L)
                .packageName("test packaging")
                .build();

        when(packagingRepository.findByProduct_ProductId(1L)).thenReturn(Optional.of(packaging));

        PackagingGetResponseDto responseDto = packagingService.getPackageInfoByProductId(1L);

        assertNotNull(responseDto);
        assertEquals(1L, responseDto.getProductId());
        assertEquals(2L, responseDto.getPackagingId());
        assertEquals("test product", responseDto.getProductName());
        assertEquals("test packaging", responseDto.getPackagingName());
        assertEquals("test description", responseDto.getProductDescription());
        assertEquals(0, responseDto.getProductState());
        assertEquals(3L, responseDto.getProductPriceStandard());
        assertEquals(2L, responseDto.getProductPriceSales());
        assertEquals(10L, responseDto.getProductInventory());
        assertEquals("test image", responseDto.getPackagingImage());
    }


    @DisplayName("포장지 조회 실패 테스트 - 개별 : 존재하지 않는 포장지")
    @Test
    void getPackageInfoFailureTest(){
        when(packagingRepository.findByProduct_ProductId(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundIdException.class, () -> packagingService.getPackageInfoByProductId(1L));
    }

    @DisplayName("포장지 신규 등록 성공 테스트")
    @Test
    void registerPackageSuccessTest(){
        PackagingRegisterRequestDto requestDto = PackagingRegisterRequestDto.builder().build();
        when(productRepository.save(any(Product.class))).thenReturn(product);
        ProductRegisterResponseDto responseDto = packagingService.registerPackage(requestDto);

        assertNotNull(responseDto);
        assertEquals(LocalDateTime.of(2000,1,10, 16, 30), responseDto.registerTime());
    }

    @DisplayName("포장지 업데이트 성공 테스트")
    @Test
    void updatePackageSuccessTest(){
        Packaging packaging = Packaging.builder()
                .product(product)
                .packageId(2L)
                .packageName("test packaging")
                .build();

        when(packagingRepository.findByProduct_ProductId(1L)).thenReturn(Optional.of(packaging));
        when(productRepository.findByProductId(any())).thenReturn(product);
        when(productRepository.save(any(Product.class))).thenReturn(product);
        when(packagingRepository.save(any(Packaging.class))).thenReturn(packaging);

        PackagingUpdateRequestDto requestDto = PackagingUpdateRequestDto.builder()
                .productId(1L)
                .build();
        ProductUpdateResponseDto responseDto = packagingService.updatePackageInfo(requestDto);

        assertNotNull(responseDto);
        assertTrue(responseDto.updateTime().isAfter(LocalDateTime.now().minusMinutes(1)));
    }

    @DisplayName("포장지 업데이트 실패 테스트 - 존재하지 않는 상품")
    @Test
    void updatePackageFailureTest1(){
        when(productRepository.findByProductId(1L)).thenReturn(product);
        doThrow(NotFoundIdException.class).when(productCheckUtil).checkProduct(product);

        PackagingUpdateRequestDto requestDto = PackagingUpdateRequestDto.builder()
                .productId(1L)
                .build();

        assertThrows(NotFoundIdException.class, () -> packagingService.updatePackageInfo(requestDto));
    }

    @DisplayName("포장지 업데이트 실패 테스트 - 일치하지 않는 상품 데이터")
    @Test
    void updatePackageFailureTest2(){
        when(productRepository.findByProductId(1L)).thenReturn(product);
        doThrow(InconsistentEntityException.class).when(productCheckUtil).checkProduct(product);

        PackagingUpdateRequestDto requestDto = PackagingUpdateRequestDto.builder()
                .productId(1L)
                .build();

        assertThrows(InconsistentEntityException.class, () -> packagingService.updatePackageInfo(requestDto));
    }

    @DisplayName("포장지 업데이트 실패 테스트 - 존재하지 않는 포장지")
    @Test
    void updatePackageFailureTest3(){
        when(productRepository.findByProductId(any())).thenReturn(product);
        when(productRepository.save(any(Product.class))).thenReturn(product);
        when(packagingRepository.findByProduct_ProductId(1L)).thenReturn(Optional.empty());

        PackagingUpdateRequestDto requestDto = PackagingUpdateRequestDto.builder()
                .productId(1L)
                .build();

        assertThrows(NotFoundIdException.class, () -> packagingService.updatePackageInfo(requestDto));
    }

    @DisplayName("포장지 조회 성공 테스트 - 모든 리스트")
    @Test
    void getAllPackagesSuccessTest1(){
        when(packagingRepository.findAll()).thenReturn(Arrays.asList(
                Packaging.builder()
                        .product(product)
                        .packageId(1L)
                        .packageName("test packaging1")
                        .build(),
                Packaging.builder()
                        .product(product)
                        .packageId(2L)
                        .packageName("test packaging2")
                        .build()
        ));
        List<PackagingGetResponseDto> responseDto = packagingService.getAllPackages(null);
        assertNotNull(responseDto);
        assertEquals(2, responseDto.size());
        verify(packagingRepository, times(1)).findAll();
    }

    @DisplayName("포장지 조회 성공 테스트 - 판매중인 모든 리스트")
    @Test
    void getAllPackagesSuccessTest2(){
        when(packagingRepository.findByProduct_ProductState(0)).thenReturn(Arrays.asList(
                Packaging.builder()
                        .product(product)
                        .packageId(1L)
                        .packageName("test packaging1")
                        .build(),
                Packaging.builder()
                        .product(product)
                        .packageId(2L)
                        .packageName("test packaging2")
                        .build()
        ));
        List<PackagingGetResponseDto> responseDto = packagingService.getAllPackages(0);
        assertNotNull(responseDto);
        assertEquals(2, responseDto.size());
        verify(packagingRepository, times(1)).findByProduct_ProductState(0);
    }

    @DisplayName("포장지 조회 성공 테스트 - 페이지")
    @Test
    void getPackagesPageSuccessTest1(){
        when(packagingRepository.findAll(any(PageRequest.class))).thenReturn(new PageImpl<>(Arrays.asList(
                        Packaging.builder()
                                .product(product)
                                .packageId(1L)
                                .packageName("test packaging1")
                                .build(),
                        Packaging.builder()
                                .product(product)
                                .packageId(2L)
                                .packageName("test packaging2")
                                .build()
                )));
        Page<PackagingGetResponseDto> responseDto = packagingService.getPackagesPage(null,1, 1);
        assertNotNull(responseDto);
        assertEquals(2, responseDto.getTotalElements());
        verify(packagingRepository, times(1)).findAll(any(PageRequest.class));
    }

    @DisplayName("포장지 조회 성공 테스트 - 판매중인 페이지")
    @Test
    void getPackagesPageSuccessTest2(){
        when(packagingRepository.findByProduct_ProductState(eq(0), any(PageRequest.class))).thenReturn(new PageImpl<>(Arrays.asList(
                Packaging.builder()
                        .product(product)
                        .packageId(1L)
                        .packageName("test packaging1")
                        .build(),
                Packaging.builder()
                        .product(product)
                        .packageId(2L)
                        .packageName("test packaging2")
                        .build()
        )));
        Page<PackagingGetResponseDto> responseDto = packagingService.getPackagesPage(0,1, 1);
        assertNotNull(responseDto);
        assertEquals(2, responseDto.getTotalElements());
        verify(packagingRepository, times(1)).findByProduct_ProductState(eq(0), any(PageRequest.class));
    }
}
