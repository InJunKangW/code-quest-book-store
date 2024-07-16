package com.nhnacademy.bookstoreinjun.controller;

import com.nhnacademy.bookstoreinjun.service.product.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@WebMvcTest(ProductController.class)
class ProductControllerTest {

    @MockBean
    ProductService productService;
    @Test
    void contextLoads(){
    }
}