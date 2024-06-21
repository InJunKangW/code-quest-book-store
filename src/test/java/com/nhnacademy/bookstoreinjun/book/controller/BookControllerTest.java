package com.nhnacademy.bookstoreinjun.book.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.bookstoreinjun.controller.BookController;
import com.nhnacademy.bookstoreinjun.dto.book.BookProductRegisterRequestDto;
import com.nhnacademy.bookstoreinjun.entity.Product;
import com.nhnacademy.bookstoreinjun.feignclient.AladinClient;
import com.nhnacademy.bookstoreinjun.service.ProductCategoryService;
import com.nhnacademy.bookstoreinjun.service.ProductService;
import com.nhnacademy.bookstoreinjun.service.ProductTagService;
import com.nhnacademy.bookstoreinjun.service.aladin.AladinService;
import com.nhnacademy.bookstoreinjun.service.book.BookService;
import com.nhnacademy.bookstoreinjun.service.category.CategoryService;
import com.nhnacademy.bookstoreinjun.service.tag.TagService;
import java.util.Arrays;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@WebMvcTest(BookController.class)
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
    CategoryService categoryService;

    @MockBean
    ProductCategoryService productCategoryService;

    @MockBean
    TagService tagService;

    @MockBean
    ProductTagService productTagService;


    @Test
    public void contextLoads() {
    }

    @Test
    public void test1() throws Exception {
        mockMvc.perform(get("/api/admin/book")
                        .param("title","이해"))
                .andExpect(status().isOk())
                .andExpect(header().stringValues("Content-Type", "application/json"));

        verify(aladinService,times(1)).getAladdinBookList("이해");
    }

    @DisplayName("도서 상품 등록 테스트")
    @Test
    public void test2() throws Exception {
        when(productService.saveProduct(any())).thenReturn(Product.builder().productId(123L).build());

        BookProductRegisterRequestDto bookProductRegisterRequestDto = BookProductRegisterRequestDto.builder()
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
                .andExpect(jsonPath("$.id").value("123"));

        verify(productService,times(1)).saveProduct(any());
        verify(bookService,times(1)).saveBook(any());
        verify(categoryService, times(2)).getCategoryByName(any());
        verify(productCategoryService, times(2)).saveProductCategory(any());
        verify(tagService, times(2)).getTagByTagName(any());
        verify(productTagService, times(2)).saveProductTag(any());
    }
}
