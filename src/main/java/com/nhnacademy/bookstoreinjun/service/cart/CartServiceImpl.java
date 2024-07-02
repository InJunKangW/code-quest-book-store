package com.nhnacademy.bookstoreinjun.service.cart;
import com.nhnacademy.bookstoreinjun.dto.cart.CartRequestDto;
import com.nhnacademy.bookstoreinjun.dto.cart.CartGetResponseDto;
import com.nhnacademy.bookstoreinjun.dto.cart.SaveCartResponseDto;
import com.nhnacademy.bookstoreinjun.entity.Cart;
import com.nhnacademy.bookstoreinjun.entity.Product;
import com.nhnacademy.bookstoreinjun.exception.NotFoundIdException;
import com.nhnacademy.bookstoreinjun.exception.XUserIdNotFoundException;
import com.nhnacademy.bookstoreinjun.repository.CartRepository;
import com.nhnacademy.bookstoreinjun.repository.ProductRepository;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;

    private final ProductRepository productRepository;

    private Product getProduct(Long clientIdOfHeader, CartRequestDto cartRequestDto) {
        if (clientIdOfHeader == -1){
            throw new XUserIdNotFoundException();
        }
        Long productId = cartRequestDto.productId();
        return productRepository.findById(productId).orElseThrow(() -> new NotFoundIdException("product", productId));
    }


    @Override
    public SaveCartResponseDto addCartItem(Long clientIdOfHeader, CartRequestDto cartRequestDto) {
        Product product = getProduct(clientIdOfHeader, cartRequestDto);
        long productInventory = product.getProductInventory();

        Long productId = cartRequestDto.productId();
        Cart cart = cartRepository.findByClientIdAndProduct_ProductId(clientIdOfHeader, productId);

        Long requestQuantity = cartRequestDto.quantity();
        if (cart == null){
            requestQuantity = Math.min(requestQuantity, productInventory);
            cartRepository.save(Cart.builder()
                    .clientId(clientIdOfHeader)
                    .product(product)
                    .quantity(requestQuantity)
                    .build());
        }else {
            requestQuantity = Math.min(cart.getQuantity() + requestQuantity, productInventory);
            cart.setQuantity(requestQuantity);
            cartRepository.save(cart);
        }
        return new SaveCartResponseDto();
    }

    @Override
    public SaveCartResponseDto setCartItemQuantity(Long clientIdOfHeader, CartRequestDto cartRequestDto) {
        Product product = getProduct(clientIdOfHeader, cartRequestDto);
        long productInventory = product.getProductInventory();

        Long productId = cartRequestDto.productId();
        Cart cart = cartRepository.findByClientIdAndProduct_ProductId(clientIdOfHeader, productId);

        if (cart == null){
            throw new NotFoundIdException("cart product", productId);
        } else{
            Long requestQuantity = Math.min(cartRequestDto.quantity(), productInventory);
            cart.setQuantity(requestQuantity);
            cartRepository.save(cart);
            return new SaveCartResponseDto();
        }
    }

    @Override
    public void deleteCartItem(Long clientIdOfHeader, Long productId) {
        if (clientIdOfHeader == -1){
            throw new XUserIdNotFoundException();
        }
        Cart cart = cartRepository.findByClientIdAndProduct_ProductId(clientIdOfHeader, productId);
        if (cart == null){
            throw new NotFoundIdException("cart product", productId);
        }
        cartRepository.delete(cart);
    }

    @Override
    public List<CartGetResponseDto> getCart(Long clientIdOfHeader) {
        if (clientIdOfHeader == -1){
            throw new XUserIdNotFoundException();
        }
        List<Cart> cartList = cartRepository.findAllByClientId(clientIdOfHeader);
        return cartList.stream()
                .map(this::getCartResponseDto)
                .toList();
    }

    @Override
    public List<CartGetResponseDto> getGuestCart(List<CartRequestDto> cartRequestDtoList) {
        List<CartGetResponseDto> responseDtoList = new ArrayList<>();
        for (CartRequestDto cartRequestDto : cartRequestDtoList) {
            Product product = productRepository.findById(cartRequestDto.productId()).orElseThrow(() -> new NotFoundIdException("product", cartRequestDto.productId()));
            CartGetResponseDto cartGetResponseDto = CartGetResponseDto.builder()
                   .productId(product.getProductId())
                   .productName(product.getProductName())
                   .productPriceStandard(product.getProductPriceStandard())
                   .productPriceSales(product.getProductPriceSales())
                   .productQuantityOfCart(cartRequestDto.quantity())
                   .productInventory(product.getProductInventory())
                   .productThumbnailImage(product.getProductThumbnailUrl())
                   .build();
            responseDtoList.add(cartGetResponseDto);
       }
    return responseDtoList;
    }

    private CartGetResponseDto getCartResponseDto(Cart cart) {
        Product product = cart.getProduct();
        return CartGetResponseDto.builder()
                .productId(product.getProductId())
                .productName(product.getProductName())
                .productPriceStandard(product.getProductPriceStandard())
                .productPriceSales(product.getProductPriceSales())
                .productQuantityOfCart(cart.getQuantity())
                .productInventory(product.getProductInventory())
                .productThumbnailImage(product.getProductThumbnailUrl())
                .build();
    }

    public void clearAllCart(Long clientIdOfHeader) {
        if (clientIdOfHeader == -1){
            throw new XUserIdNotFoundException();
        }
        cartRepository.deleteByClientId(clientIdOfHeader);
    }
}
