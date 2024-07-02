package com.nhnacademy.bookstoreinjun.service.cart;

import com.nhnacademy.bookstoreinjun.dto.cart.CartRequestDto;
import com.nhnacademy.bookstoreinjun.dto.cart.CartGetResponseDto;
import com.nhnacademy.bookstoreinjun.dto.cart.SaveCartResponseDto;
import java.util.List;

public interface CartService {
    SaveCartResponseDto addCartItem(Long clientIdOfHeader, CartRequestDto cartRequestDto);
    SaveCartResponseDto setCartItemQuantity(Long clientIdOfHeader, CartRequestDto cartRequestDto);
    List<CartGetResponseDto> getCart(Long clientIdOfHeader);
    List<CartGetResponseDto> getGuestCart(List<CartRequestDto> cartRequestDtoList);
    void deleteCartItem(Long clientIdOfHeader, Long productId);
    void clearAllCart(Long clientIdOfHeader);
}
