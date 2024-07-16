package com.nhnacademy.bookstoreinjun.cart.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.bookstoreinjun.config.SecurityConfig;
import com.nhnacademy.bookstoreinjun.controller.CartController;
import com.nhnacademy.bookstoreinjun.dto.cart.CartRequestDto;
import com.nhnacademy.bookstoreinjun.service.cart.CartService;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.security.test.context.support.WithMockUser;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CartController.class)
@Import(SecurityConfig.class)
class CartControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    CartService cartService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void contextLoads() {}


    @DisplayName("장바구니 복구 테스트")
    @Test
    void restoreTest() throws Exception {
        mockMvc.perform(get("/api/product/client/cart/restore")
                        .header("X-User-Id", 1))
                .andExpect(status().isOk());
    }

    @DisplayName("장바구니 조회 테스트 - 회원")
    @Test
    void getClientCartTest() throws Exception {
        mockMvc.perform(get("/api/product/client/cart")
                        .header("X-User-Id", 1))
                .andExpect(status().isOk());
    }

    @DisplayName("장바구니 조회 테스트 - 비회원")
    @Test
    void getGuestCartTest() throws Exception {
        List<CartRequestDto> requestDtoList = Arrays.asList(
                CartRequestDto.builder()
                        .productId(1L)
                        .quantity(3L)
                        .build(),
                CartRequestDto.builder()
                        .productId(2L)
                        .quantity(5L)
                        .build()
        );

        mockMvc.perform(post("/api/product/guest/cart")
//                        .header("X-User-Id", 1)
                                .content(objectMapper.writeValueAsString(requestDtoList))
                )
                .andExpect(status().isOk());
    }
}
