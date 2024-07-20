package com.nhnacademy.bookstoreinjun.cart.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.nhnacademy.bookstoreinjun.entity.Cart;
import com.nhnacademy.bookstoreinjun.entity.CartRemoveType;
import com.nhnacademy.bookstoreinjun.entity.Product;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;

@DataJpaTest
@TestPropertySource(locations = "classpath:application-dev.properties")
@DisplayName("장바구니 엔티티 테스트")
class CartEntityTest {
    @PersistenceContext
    private EntityManager entityManager;

    private Product product;

    private CartRemoveType cartRemoveType;

    @BeforeEach
    void setUp() {
        product = Product.builder().build();
        entityManager.persist(product);
        entityManager.flush();

        cartRemoveType = CartRemoveType.builder()
                .cartRemoveTypeName("test type")
                .build();
        entityManager.persist(cartRemoveType);
        entityManager.flush();
    }

    @DisplayName("장바구니 엔티티 저장 테스트")
    @Test
    void test1(){
        Cart cart = Cart.builder()
                .clientId(1L)
                .product(product)
                .quantity(1L)
                .addedToCartAt(LocalDateTime.now())
                .cartRemoveType(cartRemoveType)
                .build();

        Cart savedCart = entityManager.merge(cart);

        assertNotNull(savedCart);
        assertNotNull(savedCart.getCartId());
        assertEquals("test type", savedCart.getCartRemoveType().getCartRemoveTypeName());
        assertEquals(product, savedCart.getProduct());
    }
}
