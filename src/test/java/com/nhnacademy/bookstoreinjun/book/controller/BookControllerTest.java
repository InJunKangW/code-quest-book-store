package com.nhnacademy.bookstoreinjun.book.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.bookstoreinjun.config.SecurityConfig;
import com.nhnacademy.bookstoreinjun.controller.BookController;
import com.nhnacademy.bookstoreinjun.dto.book.BookProductRegisterRequestDto;
import com.nhnacademy.bookstoreinjun.entity.Product;
import com.nhnacademy.bookstoreinjun.exception.AladinJsonProcessingException;
//import com.nhnacademy.bookstoreinjun.filter.EmailHeaderFilter;
import com.nhnacademy.bookstoreinjun.service.productCategoryRelation.ProductCategoryRelationServiceImpl;
import com.nhnacademy.bookstoreinjun.service.ProductService;
import com.nhnacademy.bookstoreinjun.service.productTag.ProductTagServiceImpl;
import com.nhnacademy.bookstoreinjun.service.aladin.AladinService;
import com.nhnacademy.bookstoreinjun.service.book.BookService;
import com.nhnacademy.bookstoreinjun.service.productCategory.ProductCategoryService;
import com.nhnacademy.bookstoreinjun.service.tag.TagService;
import java.util.Arrays;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@WebMvcTest(BookController.class)
@Import(SecurityConfig.class)
public class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;


    @MockBean
    AladinService aladinService;

    @MockBean
    BookService bookService;

    @MockBean
    ProductService productService;

    @MockBean
    ProductCategoryService productCategoryService;

    @MockBean
    ProductCategoryRelationServiceImpl productCategoryRelationServiceImpl;

    @MockBean
    TagService tagService;

    @MockBean
    ProductTagServiceImpl productTagServiceImpl;

//    @MockBean
//    EmailHeaderFilter emailHeaderFilter;


    @Test
    public void contextLoads() {
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void test1() throws Exception {
        mockMvc.perform(get("/api/admin/book")
                        .param("title","이해"))
                .andExpect(status().isOk())
                .andExpect(header().stringValues("Content-Type", "application/json"));

        verify(aladinService,times(1)).getAladdinBookList("이해");
    }

    @DisplayName("도서 상품 등록 테스트")
    @Test
    @WithMockUser(roles = "ADMIN")
    public void test2() throws Exception {
        when(productService.saveProduct(any())).thenReturn(Product.builder().productId(123L).build());

        BookProductRegisterRequestDto bookProductRegisterRequestDto = BookProductRegisterRequestDto.builder()
                .isbn("123456789a")
                .isbn13("123456789abcd")
                .categories(Arrays.asList("test category1","test category2"))
                .tags(Arrays.asList("test tag1","test tag2"))
                .build();

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(bookProductRegisterRequestDto);

        mockMvc.perform(post("/api/admin/book/register")
                .content(json)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value("123"))
                .andDo(print());

        verify(productService,times(1)).saveProduct(any());
        verify(bookService,times(1)).saveBook(any());
        verify(productCategoryService, times(2)).getCategoryByName(any());
        verify(productCategoryRelationServiceImpl, times(2)).saveProductCategory(any());
        verify(tagService, times(2)).getTagByTagName(any());
        verify(productTagServiceImpl, times(2)).saveProductTag(any());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void test3() throws Exception {
        when(aladinService.getAladdinBookList(any())).thenThrow(new AladinJsonProcessingException(any()));

        mockMvc.perform(get("/api/admin/book")
                        .param("title","이해"))
                .andExpect(status().is5xxServerError());
    }
}
