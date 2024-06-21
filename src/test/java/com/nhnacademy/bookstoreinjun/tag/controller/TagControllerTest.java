package com.nhnacademy.bookstoreinjun.tag.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.bookstoreinjun.controller.TagController;
import com.nhnacademy.bookstoreinjun.dto.tag.TagRegisterRequestDto;
import com.nhnacademy.bookstoreinjun.service.tag.TagService;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TagController.class)
public class TagControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TagService tagService;

    @Test
    public void contextLoads() throws Exception {
    }

    @Test
    public void testCreateTag() throws Exception {
        TagRegisterRequestDto dto = TagRegisterRequestDto.builder()
                        .tagName("test tag")
                        .build();

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(dto);

        mockMvc.perform(post("/api/admin/tag")
                .content(json)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        verify(tagService,times(1)).saveTag(dto);
    }
}
