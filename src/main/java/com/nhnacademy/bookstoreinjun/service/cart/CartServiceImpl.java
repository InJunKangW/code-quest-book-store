package com.nhnacademy.bookstoreinjun.service.cart;
import com.nhnacademy.bookstoreinjun.dto.cart.CartRequestDto;
import com.nhnacademy.bookstoreinjun.dto.cart.CartGetResponseDto;
import com.nhnacademy.bookstoreinjun.dto.cart.SaveCartResponseDto;
import com.nhnacademy.bookstoreinjun.entity.Cart;
import com.nhnacademy.bookstoreinjun.entity.Product;
import com.nhnacademy.bookstoreinjun.exception.NotFoundIdException;
import com.nhnacademy.bookstoreinjun.exception.XUserIdNotFoundException;
import com.nhnacademy.bookstoreinjun.repository.QuerydslRepository;
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

    private final QuerydslRepository querydslRepository;

    private Product getProduct(Long clientIdOfHeader, CartRequestDto cartRequestDto) {
        if (clientIdOfHeader == -1){
            throw new XUserIdNotFoundException();
        }
        Long productId = cartRequestDto.productId();
        return productRepository.findById(productId).orElseThrow(() -> new NotFoundIdException("product", productId));
    }

    @Override
    public List<CartRequestDto> restoreClientCartList(Long clientIdOfHeader) {
        if (clientIdOfHeader == -1){
            throw new XUserIdNotFoundException();
        }
        log.info("Restore client cart list with id {}", clientIdOfHeader);
        List<Cart> cartList = cartRepository.findAllByClientId(clientIdOfHeader);
        log.info("list found. {}", cartList);
        return cartList.stream().map(cart -> CartRequestDto.builder()
                        .productId(cart.getProduct().getProductId())
                        .quantity(cart.getQuantity())
                        .build())
                .toList();
    }

    @Override
    public SaveCartResponseDto addClientCartItem(Long clientIdOfHeader, CartRequestDto cartRequestDto) {
        Product product = getProduct(clientIdOfHeader, cartRequestDto);
        long productInventory = product.getProductInventory();

        Long productId = cartRequestDto.productId();
        Cart cart = cartRepository.findByClientIdAndProduct_ProductId(clientIdOfHeader, productId);

        Long quantity = cartRequestDto.quantity();
        if (cart == null){
            quantity = Math.min(quantity, productInventory);
            cartRepository.save(Cart.builder()
                    .clientId(clientIdOfHeader)
                    .product(product)
                    .quantity(quantity)
                    .build());
        }else {
            quantity = Math.min(cart.getQuantity() + quantity, productInventory);
            cart.setQuantity(quantity);
            cartRepository.save(cart);
        }
        return new SaveCartResponseDto(quantity);
    }

    @Override
    public SaveCartResponseDto checkCartRequestOfGuest(CartRequestDto cartRequestDto){
        Product product = productRepository.findById(cartRequestDto.productId()).orElseThrow(() -> new NotFoundIdException("product", cartRequestDto.productId()));
        return new SaveCartResponseDto(Math.min(cartRequestDto.quantity(), product.getProductInventory()));
    }


    @Override
    public SaveCartResponseDto setClientCartItemQuantity(Long clientIdOfHeader, CartRequestDto cartRequestDto) {
        Product product = getProduct(clientIdOfHeader, cartRequestDto);
        long productInventory = product.getProductInventory();

        Long productId = cartRequestDto.productId();
        Cart cart = cartRepository.findByClientIdAndProduct_ProductId(clientIdOfHeader, productId);

        if (cart == null){
            throw new NotFoundIdException("cart product", productId);
        } else{
            Long quantity = Math.min(cartRequestDto.quantity(), productInventory);
            cart.setQuantity(quantity);
            cartRepository.save(cart);
            return new SaveCartResponseDto(quantity);
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
    public List<CartGetResponseDto> getClientCart(Long clientIdOfHeader) {
        if (clientIdOfHeader == -1){
            throw new XUserIdNotFoundException();
        }
        List<Cart> cartList = cartRepository.findAllByClientId(clientIdOfHeader);
        return cartList.stream()
                .map(cart -> getCartResponseDto(cart.getProduct(), cart.getQuantity()))
                .toList();
    }

    @Override
    public List<CartGetResponseDto> getGuestCart(List<CartRequestDto> cartRequestDtoList) {
        List<CartGetResponseDto> responseDtoList = new ArrayList<>();
        for (CartRequestDto cartRequestDto : cartRequestDtoList) {
            Product product = productRepository.findById(cartRequestDto.productId()).orElseThrow(() -> new NotFoundIdException("product", cartRequestDto.productId()));
            responseDtoList.add(getCartResponseDto(product, cartRequestDto.quantity()));
       }
    return responseDtoList;
    }


    private CartGetResponseDto getCartResponseDto(Product product, Long productCartQuantity) {
        return CartGetResponseDto.builder()
                .productId(product.getProductId())
                .productName(product.getProductName())
                .productPriceStandard(product.getProductPriceStandard())
                .productPriceSales(product.getProductPriceSales())
                .productQuantityOfCart(productCartQuantity)
                .productInventory(product.getProductInventory())
                .productState(product.getProductState())
                .productThumbnailImage(product.getProductThumbnailUrl())
                .categorySet(querydslRepository.getCategorySet(product))
                .tagSet(querydslRepository.getTagSet(product))
                .build();
    }

    @Override
    public void clearAllCart(Long clientIdOfHeader) {
        if (clientIdOfHeader == -1){
            throw new XUserIdNotFoundException();
        }
        cartRepository.deleteByClientId(clientIdOfHeader);
    }
}
