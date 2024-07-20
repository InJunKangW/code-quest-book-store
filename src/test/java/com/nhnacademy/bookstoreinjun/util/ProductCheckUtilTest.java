package com.nhnacademy.bookstoreinjun.util;

import static org.junit.jupiter.api.Assertions.assertThrows;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.when;

import com.nhnacademy.bookstoreinjun.entity.Product;
import com.nhnacademy.bookstoreinjun.exception.InconsistentEntityException;
import com.nhnacademy.bookstoreinjun.exception.NotFoundIdException;
import com.nhnacademy.bookstoreinjun.repository.ProductRepository;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName("상품 체크 유틸 테스트")
class ProductCheckUtilTest {

    @InjectMocks
    private ProductCheckUtil productCheckUtil;

    @Mock
    private ProductRepository productRepository;

    @DisplayName("상품 체크 테스트 - 문제 없음")
    @Test
    void checkProductTest1(){
        Product product = Product.builder()
                .productId(1L)
                .build();

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        assertDoesNotThrow(() -> productCheckUtil.checkProduct(product));
    }

    @DisplayName("상품 체크 테스트 - 상품이 Null 일 경우")
    @Test
    void checkProductTest2(){
        assertThrows(NotFoundIdException.class, () -> productCheckUtil.checkProduct(null));
    }

    @DisplayName("상품 체크 테스트 - 상품 Id가 Null 일 경우")
    @Test
    void checkProductTest3(){
        Product product = new Product();
        assertThrows(NotFoundIdException.class, () -> productCheckUtil.checkProduct(product));
    }

    @DisplayName("상품 체크 테스트 - 상품이 데이터베이스에 존재하지 않을 경우")
    @Test
    void checkProductTest4(){
        Product product = Product.builder()
                .productId(1L)
                .build();
        when(productRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(NotFoundIdException.class, () -> productCheckUtil.checkProduct(product));
    }


    @DisplayName("상품 체크 테스트 - 상품과, 데이터베이스로 조회한 상품이 일치하지 않을 경우")
    @Test
    void checkProductTest5(){
        Product product = Product.builder()
                .productId(1L)
                .build();

        Product anoutherProduct = Product.builder().build();

        when(productRepository.findById(1L)).thenReturn(Optional.of(anoutherProduct));
        assertThrows(InconsistentEntityException.class, () -> productCheckUtil.checkProduct(product));
    }
}
