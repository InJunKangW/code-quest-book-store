package com.nhnacademy.bookstoreinjun.service.cart;

import com.nhnacademy.bookstoreinjun.dto.cart.CartRequestDto;
import com.nhnacademy.bookstoreinjun.dto.cart.CartGetResponseDto;
import com.nhnacademy.bookstoreinjun.dto.cart.SaveCartResponseDto;
import java.util.List;

public interface CartService {
    List<CartRequestDto> restoreClientCartList(Long clientIdOfHeader);
    SaveCartResponseDto addClientCartItem(Long clientIdOfHeader, CartRequestDto cartRequestDto);
    SaveCartResponseDto checkCartRequestOfGuest(CartRequestDto cartRequestDto);
    SaveCartResponseDto setClientCartItemQuantity(Long clientIdOfHeader, CartRequestDto cartRequestDto);
    List<CartGetResponseDto> getClientCart(Long clientIdOfHeader);
    List<CartGetResponseDto> getGuestCart(List<CartRequestDto> cartRequestDtoList);
    void deleteCartItem(Long clientIdOfHeader, Long productId);
    void checkOutCart(String message);
    void clearAllCart(Long clientIdOfHeader);
}
