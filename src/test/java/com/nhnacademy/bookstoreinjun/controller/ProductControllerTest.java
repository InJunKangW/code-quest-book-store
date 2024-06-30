package com.nhnacademy.bookstoreinjun.controller;

import static org.junit.jupiter.api.Assertions.*;

import com.nhnacademy.bookstoreinjun.service.product.ProductDtoService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@WebMvcTest(ProductController.class)
class ProductControllerTest {

    @MockBean
    ProductDtoService productDtoService;
    @Test
    void name() throws InterruptedException {

        Thread.sleep(2000);
    }
}