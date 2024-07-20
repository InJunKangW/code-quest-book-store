package com.nhnacademy.bookstoreinjun.querydsl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.nhnacademy.bookstoreinjun.dto.book.BookProductGetResponseDto;
import com.nhnacademy.bookstoreinjun.dto.cart.CartCheckoutRequestDto;
import com.nhnacademy.bookstoreinjun.dto.page.PageRequestDto;
import com.nhnacademy.bookstoreinjun.dto.product.InventoryDecreaseRequestDto;
import com.nhnacademy.bookstoreinjun.dto.product.InventoryIncreaseRequestDto;
import com.nhnacademy.bookstoreinjun.repository.QuerydslRepositoryImpl;
import com.nhnacademy.bookstoreinjun.util.PageableUtil;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;


@Slf4j
@SpringBootTest
@ExtendWith(SpringExtension.class)
@TestPropertySource(locations = "classpath:application-dev.properties")
@ActiveProfiles("dev")
@Sql(scripts = "/querydsl-test-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("querydsl repository 테스트")
class QuerydslRepositoryTest{

    @Autowired
    private QuerydslRepositoryImpl querydslRepository;

    @DisplayName("개별 도서 조회 테스트")
    @Order(0)
    @Test
    void testFindBookByProductId() {
        BookProductGetResponseDto responseDto=  querydslRepository.findBookByProductId(1L,1L);

        assertNotNull(responseDto);
        assertTrue(responseDto.hasLike());
        assertEquals(11, responseDto.productViewCount());
        assertEquals(2, responseDto.categorySet().size());
        assertEquals(2, responseDto.tagSet().size());
        log.info("response dto: {}", responseDto);
    }

    @DisplayName("도서 페이지 조회 테스트 - 판매 중 상태, 재고순 정렬")
    @Order(0)
    @Test
    void testFindAllBookPage(){
        PageRequestDto pageRequestDto = PageRequestDto.builder().page(1).size(20).sort("product.productInventory").build();
        Pageable pageable = PageableUtil.makePageable(pageRequestDto, 10, null);
        Page<BookProductGetResponseDto> responseDtoPage = querydslRepository.findAllBookPage(1L, pageable, 0);

        assertNotNull(responseDtoPage);
        assertEquals(3, responseDtoPage.getTotalElements());
    }

    @DisplayName("도서 페이지 조회 테스트 - 모든 상태")
    @Order(0)
    @Test
    void testFindAllBookPage2(){
        PageRequestDto pageRequestDto = PageRequestDto.builder().page(1).size(20).sort("product.productId").build();
        Pageable pageable = PageableUtil.makePageable(pageRequestDto, 10, null);
        Page<BookProductGetResponseDto> responseDtoPage = querydslRepository.findAllBookPage(1L, pageable, null);

        assertNotNull(responseDtoPage);
        assertEquals(6, responseDtoPage.getTotalElements());
    }


    @DisplayName("도서 페이지 조회 테스트 - 모든 상태, 재고순 정렬")
    @Order(0)
    @Test
    void testFindAllBookPage3(){
        PageRequestDto pageRequestDto = PageRequestDto.builder().page(1).size(20).sort("product.productInventory").build();
        Pageable pageable = PageableUtil.makePageable(pageRequestDto, 10, null);
        Page<BookProductGetResponseDto> responseDtoPage = querydslRepository.findAllBookPage(1L, pageable, null);

        assertNotNull(responseDtoPage);
        assertEquals(4, responseDtoPage.getTotalElements());
    }


    @DisplayName("좋아요 누른 도서 페이지 조회 테스트 - 판매 중 상태, 재고순 정렬")
    @Order(0)
    @Test
    void testFindLikeBookPage1(){
        PageRequestDto pageRequestDto = PageRequestDto.builder().page(1).size(20).sort("product.productInventory").build();
        Pageable pageable = PageableUtil.makePageable(pageRequestDto, 10, null);
        Page<BookProductGetResponseDto> responseDtoPage = querydslRepository.findLikeBooks(1L, pageable, 0);

        assertNotNull(responseDtoPage);
        assertEquals(2, responseDtoPage.getTotalElements());
    }


    @DisplayName("좋아요 누른 도서 페이지 조회 테스트 - 모든 상태, 재고순 정렬")
    @Order(0)
    @Test
    void testFindLikeBookPage2(){
        PageRequestDto pageRequestDto = PageRequestDto.builder().page(1).size(20).sort("product.productInventory").build();
        Pageable pageable = PageableUtil.makePageable(pageRequestDto, 10, null);
        Page<BookProductGetResponseDto> responseDtoPage = querydslRepository.findLikeBooks(1L, pageable, null);

        assertNotNull(responseDtoPage);
        assertEquals(3, responseDtoPage.getTotalElements());
    }


    @DisplayName("도서 페이지 조회 테스트 - 태그 필터, 모든 상태")
    @Order(0)
    @Test
    void testTagFilterBookPage1(){
        PageRequestDto pageRequestDto = PageRequestDto.builder().page(1).size(20).sort("product.productId").build();
        Pageable pageable = PageableUtil.makePageable(pageRequestDto, 10, null);
        Page<BookProductGetResponseDto> responseDtoPage = querydslRepository.findBooksByTagFilter(1L, Set.of("test tag1"), null,  pageable, null);

        assertNotNull(responseDtoPage);
        assertEquals(5, responseDtoPage.getTotalElements());
    }

    @DisplayName("도서 페이지 조회 테스트 - 태그 필터, 판매중  상태")
    @Order(0)
    @Test
    void testTagFilterBookPage2(){
        PageRequestDto pageRequestDto = PageRequestDto.builder().page(1).size(20).sort("product.productId").build();
        Pageable pageable = PageableUtil.makePageable(pageRequestDto, 10, null);
        Page<BookProductGetResponseDto> responseDtoPage = querydslRepository.findBooksByTagFilter(1L, Set.of("test tag1"), null,  pageable, 0);

        assertNotNull(responseDtoPage);
        assertEquals(3, responseDtoPage.getTotalElements());
    }

    @DisplayName("도서 페이지 조회 테스트 - 태그 필터 (2개 중 하나라도 가지고 있는)")
    @Order(0)
    @Test
    void testTagFilterBookPage3(){
        PageRequestDto pageRequestDto = PageRequestDto.builder().page(1).size(20).sort("product.productId").build();
        Pageable pageable = PageableUtil.makePageable(pageRequestDto, 10, null);
        Page<BookProductGetResponseDto> responseDtoPage = querydslRepository.findBooksByTagFilter(1L, Set.of("test tag1", "test tag2"), null,  pageable, null);

        assertNotNull(responseDtoPage);
        assertEquals(6, responseDtoPage.getTotalElements());
    }


    @DisplayName("도서 페이지 조회 테스트 - 태그 필터 (2개 모두를 가지고 있는)")
    @Order(0)
    @Test
    void testTagFilterBookPage4(){
        PageRequestDto pageRequestDto = PageRequestDto.builder().page(1).size(20).sort("product.productId").build();
        Pageable pageable = PageableUtil.makePageable(pageRequestDto, 10, null);
        Page<BookProductGetResponseDto> responseDtoPage = querydslRepository.findBooksByTagFilter(1L, Set.of("test tag1", "test tag2"), true,  pageable, null);

        assertNotNull(responseDtoPage);
        assertEquals(1, responseDtoPage.getTotalElements());
    }


    @DisplayName("도서 페이지 조회 테스트 - 이름 포함, 모든 상태")
    @Order(0)
    @Test
    void testNameContainingBookPage1(){
        PageRequestDto pageRequestDto = PageRequestDto.builder().page(1).size(20).sort("product.productId").build();
        Pageable pageable = PageableUtil.makePageable(pageRequestDto, 10, null);
        Page<BookProductGetResponseDto> responseDtoPage = querydslRepository.findNameContainingBookPage(1L, pageable, "new", null);

        assertNotNull(responseDtoPage);
        assertEquals(3, responseDtoPage.getTotalElements());
    }

    @DisplayName("도서 페이지 조회 테스트 - 이름 포함, 판매 중 상태")
    @Order(0)
    @Test
    void testNameContainingBookPage2(){
        PageRequestDto pageRequestDto = PageRequestDto.builder().page(1).size(20).sort("product.productId").build();
        Pageable pageable = PageableUtil.makePageable(pageRequestDto, 10, null);
        Page<BookProductGetResponseDto> responseDtoPage = querydslRepository.findNameContainingBookPage(1L, pageable, "new", 0);

        assertNotNull(responseDtoPage);
        assertEquals(2, responseDtoPage.getTotalElements());
    }


    @DisplayName("도서 페이지 조회 테스트 - 카테고리 필터, 모든 상태")
    @Test
    void testCategoryFilterBookPage1(){
        PageRequestDto pageRequestDto = PageRequestDto.builder().page(1).size(20).sort("product.productId").build();
        Pageable pageable = PageableUtil.makePageable(pageRequestDto, 10, null);
        Page<BookProductGetResponseDto> responseDtoPage = querydslRepository.findBooksByCategoryFilter(1L, 1L,pageable, null);

        assertNotNull(responseDtoPage);
        assertEquals(3, responseDtoPage.getTotalElements());
    }


    @DisplayName("도서 페이지 조회 테스트 - 카테고리 필터, 판매 중 상태")
    @Order(0)
    @Test
    void testCategoryFilterBookPage2(){
        PageRequestDto pageRequestDto = PageRequestDto.builder().page(1).size(20).sort("product.productId").build();
        Pageable pageable = PageableUtil.makePageable(pageRequestDto, 10, null);
        Page<BookProductGetResponseDto> responseDtoPage = querydslRepository.findBooksByCategoryFilter(1L, 1L,pageable, 0);

        assertNotNull(responseDtoPage);
        assertEquals(2, responseDtoPage.getTotalElements());
    }

    @DisplayName("상품 상태 변경 테스트 - 존재하는 상품")
    @Order(1)
    @Test
    void setProductStateTest1(){
        BookProductGetResponseDto responseDto=  querydslRepository.findBookByProductId(1L,1L);

        long updated = querydslRepository.setProductState(1L, 1);
        BookProductGetResponseDto updatedResponseDto=  querydslRepository.findBookByProductId(1L,1L);

        assertEquals(1L, updated);
        assertEquals(0, responseDto.productState());
        assertEquals(1, updatedResponseDto.productState());

    }

    @DisplayName("상품 상태 변경 테스트 - 존재하지 않는 상품")
    @Order(0)
    @Test
    void setProductStateTest2(){
        long updated = querydslRepository.setProductState(10L, 1);
        assertEquals(0L, updated);
    }

    @DisplayName("상품 재고 감소 테스트")
    @Order(1)
    @Test
    void decreaseProductInventoryTest1(){
        BookProductGetResponseDto responseDto1=  querydslRepository.findBookByProductId(1L,1L);
        BookProductGetResponseDto responseDto2=  querydslRepository.findBookByProductId(1L,2L);

        assertEquals(10L, responseDto1.productInventory());
        assertEquals(20L, responseDto2.productInventory());


        Map<Long, Long> decreaseInfo = new LinkedHashMap<>();
        decreaseInfo.put(1L, 5L);
        decreaseInfo.put(2L, 30L);
        InventoryDecreaseRequestDto requestDto = InventoryDecreaseRequestDto.builder()
                .orderId(1L)
                .decreaseInfo(decreaseInfo)
                .build();

        long updated = querydslRepository.decreaseProductInventory(requestDto);
        assertEquals(2L, updated);

        BookProductGetResponseDto decreasedResponseDto1=  querydslRepository.findBookByProductId(1L,1L);
        BookProductGetResponseDto decreasedResponseDto2=  querydslRepository.findBookByProductId(1L,2L);

        assertEquals(5L, decreasedResponseDto1.productInventory());
        assertEquals(0L, decreasedResponseDto2.productInventory());
    }


    @DisplayName("상품 재고 증가 테스트")
    @Order(2)
    @Test
    void increaseProductInventoryTest1(){
        BookProductGetResponseDto responseDto1=  querydslRepository.findBookByProductId(1L,1L);
        BookProductGetResponseDto responseDto2=  querydslRepository.findBookByProductId(1L,2L);

        assertEquals(5L, responseDto1.productInventory());
        assertEquals(0L, responseDto2.productInventory());

        List<InventoryIncreaseRequestDto> requestDtoList = Arrays.asList(
                InventoryIncreaseRequestDto.builder()
                        .productId(1L)
                        .quantityToIncrease(100L)
                        .build(),
                InventoryIncreaseRequestDto.builder()
                        .productId(2L)
                        .quantityToIncrease(200L)
                        .build()
        );

        long updated = querydslRepository.increaseProductInventory(requestDtoList);
        assertEquals(2L, updated);

        BookProductGetResponseDto increasedResponseDto1=  querydslRepository.findBookByProductId(1L,1L);
        BookProductGetResponseDto increasedResponseDto2=  querydslRepository.findBookByProductId(1L,2L);

        assertEquals(105L, increasedResponseDto1.productInventory());
        assertEquals(200L, increasedResponseDto2.productInventory());
    }


    @DisplayName("장바구니 처리 테스트 - 구매로 인한 비우기")
    @Sql("/querydsl-cart-data.sql")
    @Test
    void checkoutCartTest(){
        CartCheckoutRequestDto requestDto = CartCheckoutRequestDto.builder()
                .clientId(1L)
                .productIdList(Arrays.asList(1L,2L,3L))
                .build();

        long updated = querydslRepository.checkOutCart(requestDto);

        assertEquals(2L, updated);
    }


    @DisplayName("장바구니 처리 테스트 - 개별 상품 직접 삭제 - 장바구니에 담긴 상품")
    @Sql("/querydsl-cart-data.sql")
    @Test
    void deleteCartItemTest1(){
        assertTrue(querydslRepository.deleteCartItem(1L, 1L));
    }

    @DisplayName("장바구니 처리 테스트 - 개별 상품 직접 삭제 - 장바구니에 담겨있지 않은 상품")
    @Sql("/querydsl-cart-data.sql")
    @Test
    void deleteCartItemTest2(){
        assertFalse(querydslRepository.deleteCartItem(1L, 3L));
    }


    @DisplayName("장바구니 처리 테스트 - 장바구니 상품 모두 직접 비우기 - 장바구니에 상품이 담겨있는 유저")
    @Sql("/querydsl-cart-data.sql")
    @Test
    void deleteAllCartTest(){
        assertTrue(querydslRepository.deleteAllCart(1L));
    }

    @DisplayName("장바구니 처리 테스트 - 장바구니 상품 모두 직접 비우기 - 장바구니에 상품이 담겨있지 않은 유저")
    @Sql("/querydsl-cart-data.sql")
    @Test
    void deleteAllCartTest2(){
        assertFalse(querydslRepository.deleteAllCart(2L));
    }
}
