//package com.nhnacademy.bookstoreinjun.tag.controller;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.nhnacademy.bookstoreinjun.config.SecurityConfig;
//import com.nhnacademy.bookstoreinjun.controller.TagController;
//import com.nhnacademy.bookstoreinjun.dto.tag.TagRegisterRequestDto;
//import com.nhnacademy.bookstoreinjun.service.tag.TagService;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.context.annotation.Import;
//import org.springframework.http.MediaType;
//import org.springframework.security.test.context.support.WithMockUser;
//import org.springframework.test.web.servlet.MockMvc;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.times;
//import static org.mockito.Mockito.verify;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@WebMvcTest(TagController.class)
//@Import(SecurityConfig.class)
//public class TagControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @MockBean
//    private TagService tagService;
//
//    @DisplayName("태그 등록 성공 테스트")
//    @Test
//    @WithMockUser(roles = "ADMIN")
//    public void testCreateTagSuccess() throws Exception {
//        TagRegisterRequestDto dto = TagRegisterRequestDto.builder()
//                        .tagName("test tag")
//                        .build();
//
//        ObjectMapper objectMapper = new ObjectMapper();
//        String json = objectMapper.writeValueAsString(dto);
//
//        mockMvc.perform(post("/api/product/admin/tag/register")
//                .content(json)
//                .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isCreated());
//
//        verify(tagService,times(1)).saveTag(dto);
//    }
//
//    @DisplayName("태그 등록 실패 테스트 - 중복된 태그명")
//    @Test
//    @WithMockUser(roles = "ADMIN")
//    public void testCreateTagFailureByDuplicateTagName() throws Exception {
//        TagRegisterRequestDto dto = TagRegisterRequestDto.builder()
//                .tagName("test tag")
//                .build();
//
//        ObjectMapper objectMapper = new ObjectMapper();
//        String json = objectMapper.writeValueAsString(dto);
//
//
//        mockMvc.perform(post("/api/product/admin/tag/register")
//                        .content(json)
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isCreated());
//
//        verify(tagService,times(1)).saveTag(dto);
//    }
//
//}
