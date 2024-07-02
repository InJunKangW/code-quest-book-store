package com.nhnacademy.bookstoreinjun.controller;

import com.nhnacademy.bookstoreinjun.dto.cart.CartRequestDto;
import com.nhnacademy.bookstoreinjun.dto.cart.CartGetResponseDto;
import com.nhnacademy.bookstoreinjun.dto.cart.SaveCartResponseDto;
import com.nhnacademy.bookstoreinjun.service.cart.CartService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang.math.NumberUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/product")
public class CartController {
    private final CartService cartService;

    private static final String ID_HEADER = "X-User-Id";


    private final HttpHeaders header = new HttpHeaders() {{
        setContentType(MediaType.APPLICATION_JSON);
    }};

    @GetMapping("/client/cart")
    public ResponseEntity<List<CartGetResponseDto>> getCartList(
            @RequestHeader HttpHeaders httpHeaders) {
        return new ResponseEntity<>(cartService.getCart(NumberUtils.toLong(httpHeaders.getFirst(ID_HEADER), -1L)), header, HttpStatus.OK);
    }

    @PostMapping("/guest/cart")
    public ResponseEntity<List<CartGetResponseDto>> getCartList(
            @RequestBody List<@Valid CartRequestDto> cartRequestDtoList) {
        return new ResponseEntity<>(cartService.getGuestCart(cartRequestDtoList), header, HttpStatus.OK);
    }


    @PostMapping("/client/cart/add")
    public ResponseEntity<SaveCartResponseDto> saveCartItem(
            @RequestHeader HttpHeaders httpHeaders,
            @RequestBody @Valid CartRequestDto cartRequestDto){
        return new ResponseEntity<>(cartService.addCartItem(NumberUtils.toLong(httpHeaders.getFirst(ID_HEADER), -1L), cartRequestDto), header, HttpStatus.OK);
    }

    @PutMapping("/client/cart/update")
    public ResponseEntity<SaveCartResponseDto> updateCartItem(
            @RequestHeader HttpHeaders httpHeaders,
            @RequestBody @Valid CartRequestDto cartRequestDto
    ){
        return new ResponseEntity<>(cartService.setCartItemQuantity(NumberUtils.toLong(httpHeaders.getFirst(ID_HEADER), -1L), cartRequestDto), header, HttpStatus.OK);
    }

    @DeleteMapping("/client/cart/items/{productId}")
    public ResponseEntity<Void> deleteCartItem(
            @RequestHeader HttpHeaders httpHeaders,
            @PathVariable Long productId){
        cartService.deleteCartItem(NumberUtils.toLong(httpHeaders.getFirst(ID_HEADER), -1L), productId);
        return new ResponseEntity<>(null, header, HttpStatus.OK);
    }

    @DeleteMapping("/client/cart/all")
    public ResponseEntity<Void> clearAllCart(
            @RequestHeader HttpHeaders httpHeaders
    ){
        cartService.clearAllCart(NumberUtils.toLong(httpHeaders.getFirst(ID_HEADER), -1L));
        return new ResponseEntity<>(null, header, HttpStatus.OK);
    }

}
