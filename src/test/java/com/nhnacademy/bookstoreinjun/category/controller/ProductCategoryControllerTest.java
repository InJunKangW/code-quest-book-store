package com.nhnacademy.bookstoreinjun.category.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.bookstoreinjun.config.SecurityConfig;
import com.nhnacademy.bookstoreinjun.controller.CategoryController;
import com.nhnacademy.bookstoreinjun.dto.category.CategoryRegisterRequestDto;
import com.nhnacademy.bookstoreinjun.dto.category.CategoryUpdateRequestDto;
import com.nhnacademy.bookstoreinjun.exception.DuplicateException;
import com.nhnacademy.bookstoreinjun.exception.NotFoundNameException;
import com.nhnacademy.bookstoreinjun.repository.ProductCategoryRepository;
import com.nhnacademy.bookstoreinjun.service.productCategory.ProductCategoryService;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;



@WebMvcTest(CategoryController.class)
@Import(SecurityConfig.class)
public class ProductCategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductCategoryService productCategoryService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @DisplayName("카테고리 신규 등록 성공 테스트")
    @Test
    @WithMockUser(roles = "ADMIN")
    public void saveCategoryTest1() throws Exception {
        CategoryRegisterRequestDto dto = CategoryRegisterRequestDto.builder()
                        .categoryName("test productCategory 1")
                        .build();
        String json = objectMapper.writeValueAsString(dto);

        mockMvc.perform(post("/api/product/admin/category/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isCreated());

        verify(productCategoryService,times(1)).saveCategory(any(CategoryRegisterRequestDto.class));
    }

    @DisplayName("카테고리 신규 등록 실패 테스트 - 중복 카테고리명")
    @Test
    @WithMockUser(roles = "ADMIN")
    public void saveCategoryTest2() throws Exception {

        CategoryRegisterRequestDto dto = CategoryRegisterRequestDto.builder()
                .categoryName("duplicate productCategory")
                .build();
        String json = objectMapper.writeValueAsString(dto);

        when(productCategoryService.saveCategory(dto)).thenThrow(DuplicateException.class);

        mockMvc.perform(post("/api/product/admin/category/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isConflict());

        verify(productCategoryService,times(1)).saveCategory(any(CategoryRegisterRequestDto.class));
    }

    @DisplayName("카테고리 신규 등록 실패 테스트 - 존재하지 않는 상위 카테고리명")
    @Test
    @WithMockUser(roles = "ADMIN")
    public void saveCategoryTest3() throws Exception {
        CategoryRegisterRequestDto dto = CategoryRegisterRequestDto.builder()
                .categoryName("test productCategory 1")
                .parentCategoryName("not exist productCategory")
                .build();
        String json = objectMapper.writeValueAsString(dto);

        when(productCategoryService.saveCategory(dto)).thenThrow(NotFoundNameException.class);


        mockMvc.perform(post("/api/product/admin/category/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isNotFound());

        verify(productCategoryService,times(1)).saveCategory(any(CategoryRegisterRequestDto.class));
    }

    @DisplayName("카테고리 리스트 찾아오기 - 모두")
    @Test
    @WithMockUser(roles = "ADMIN")
    public void getAllCategoriesTest() throws Exception {
        mockMvc.perform(get("/api/product/category/list/all"))
                .andExpect(status().isOk());

        verify(productCategoryService,times(1)).getAllCategories();
    }

    @DisplayName("카테고리 리스트 찾아오기 - 이름 포함")
    @Test
    @WithMockUser(roles = "ADMIN")
    public void getAllContainingCategoriesTest() throws Exception {
        mockMvc.perform(get("/api/product/category/list/containing")
                .param("categoryName", "test productCategory"))
                .andExpect(status().isOk());

        verify(productCategoryService,times(1)).getNameContainingCategories("test productCategory");
    }

    @DisplayName("카테고리 리스트 찾아오기 - 하위")
    @Test
    @WithMockUser(roles = "ADMIN")
    public void getAllSubCategoriesTest() throws Exception {
        mockMvc.perform(get("/api/product/category/list/sub")
                        .param("categoryName", "test parent productCategory"))
                .andExpect(status().isOk());

        verify(productCategoryService,times(1)).getSubCategories("test parent productCategory");
    }

    @DisplayName("카테고리 업데이트 성공 테스트")
    @Test
    @WithMockUser(roles = "ADMIN")
    public void updateCategoryTestSuccess() throws Exception {
        CategoryUpdateRequestDto dto = CategoryUpdateRequestDto.builder()
                .currentCategoryName("test category1")
                .newCategoryName("new test category1")
                .build();

        String json = objectMapper.writeValueAsString(dto);

        mockMvc.perform(put("/api/product/admin/category/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk());

        verify(productCategoryService,times(1)).updateCategory(dto);
    }
}
