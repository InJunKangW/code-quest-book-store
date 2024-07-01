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
        }else if (!clientIdOfHeader.equals(cartRequestDto.clientId())){
            throw new XUserIdNotFoundException();
        }
        Long productId = cartRequestDto.productId();
        return productRepository.findById(productId).orElseThrow(() -> new NotFoundIdException("product", productId));
    }

    public SaveCartResponseDto addCartItem(Long clientIdOfHeader, CartRequestDto cartRequestDto) {
        Product product = getProduct(clientIdOfHeader, cartRequestDto);
        long productInventory = product.getProductInventory();

        Long productId = cartRequestDto.productId();
        Cart cart = cartRepository.findByClientIdAndProduct_ProductId(clientIdOfHeader, productId);

        Long requestQuantity = cartRequestDto.quantity();
        if (cart == null){
            log.info("it s new cart");
            if (productInventory < requestQuantity){
                requestQuantity = productInventory;
            }
            cartRepository.save(Cart.builder()
                    .clientId(cartRequestDto.clientId())
                    .product(product)
                    .quantity(requestQuantity)
                    .build());
        }else {
            log.info("cart exist!, current quantity is {}, id : {}", cart.getQuantity(), cart.getCartId());
            requestQuantity= cart.getQuantity() + requestQuantity;
            if (productInventory < requestQuantity){
                requestQuantity = productInventory;
            }
            cart.setQuantity(requestQuantity);
            cartRepository.save(cart);
        }

        //필요한 거.. 재고 이상 담기 방지. - 인 요청도 가능 (1개씩 빼기 (0으론 못만들게). 몇개 추가 말고 몇개 지정 기능 추가.. 숫자가 바뀌고 적용 버튼 누르면 set이 설정되게 하면 - 요청은 필요 없지 않을까
        return new SaveCartResponseDto();
    }

    public SaveCartResponseDto setCartItemQuantity(Long clientIdOfHeader, CartRequestDto cartRequestDto) {
        Product product = getProduct(clientIdOfHeader, cartRequestDto);
        long productInventory = product.getProductInventory();


        Long productId = cartRequestDto.productId();
        Cart cart = cartRepository.findByClientIdAndProduct_ProductId(clientIdOfHeader, productId);

        if (cart == null){
            throw new NotFoundIdException("cart product", productId);
        } else{
            Long requestQuantity = cartRequestDto.quantity();
            if (productInventory < requestQuantity){
                requestQuantity = productInventory;
            }
            cart.setQuantity(requestQuantity);
            cartRepository.save(cart);
            return new SaveCartResponseDto();
        }
    }



    public void deleteCartItem(Long clientIdOfHeader, Long clientId, Long productId) {
        if (clientIdOfHeader == -1){
            throw new XUserIdNotFoundException();
        }else if (!clientIdOfHeader.equals(clientId)){
            throw new XUserIdNotFoundException();
        }
        Cart cart = cartRepository.findByClientIdAndProduct_ProductId(clientIdOfHeader, productId);
        if (cart == null){
            throw new NotFoundIdException("cart product", clientIdOfHeader);
        }
        cartRepository.delete(cart);
    }

    public List<CartGetResponseDto> getCart(Long clientIdOfHeader, Long clientId) {
        if (clientIdOfHeader == -1){
            throw new XUserIdNotFoundException();
        }else if (!clientIdOfHeader.equals(clientId)){
            throw new XUserIdNotFoundException();
        }
        List<Cart> cartList = cartRepository.findAllByClientId(clientIdOfHeader);
        return cartList.stream()
                .map(this::getCartResponseDto)
                .toList();
    }

    private CartGetResponseDto getCartResponseDto(Cart cart) {
        Product product = cart.getProduct();
        return CartGetResponseDto.builder()
                .productId(product.getProductId())
                .productName(product.getProductName())
                .productPriceStandard(product.getProductPriceStandard())
                .productPriceSales(product.getProductPriceSales())
                .productQuantity(cart.getQuantity())
                .productThumbnailImage(product.getProductThumbnailUrl())
                .build();
    }

    public void clearAllCart(Long clientIdOfHeader, Long clientId) {
        if (clientIdOfHeader == -1){
            throw new XUserIdNotFoundException();
        }else if (!clientIdOfHeader.equals(clientId)){
            throw new XUserIdNotFoundException();
        }
        cartRepository.deleteByClientId(clientId);
    }
}
