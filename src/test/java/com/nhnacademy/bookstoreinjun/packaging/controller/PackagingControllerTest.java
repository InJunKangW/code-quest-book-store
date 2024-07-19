package com.nhnacademy.bookstoreinjun.packaging.controller;

import com.nhnacademy.bookstoreinjun.config.SecurityConfig;
import com.nhnacademy.bookstoreinjun.controller.PackagingController;
import com.nhnacademy.bookstoreinjun.dto.packaging.PackagingGetResponseDto;
import com.nhnacademy.bookstoreinjun.service.packaging.PackagingService;
import io.github.classgraph.PackageInfo;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@WebMvcTest(PackagingController.class)
@Import(SecurityConfig.class)
@DisplayName("도서 컨트롤러 테스트")
class PackagingControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    PackagingService packagingService;

    @Test
    void getSinglePackagingSuccessTest() throws Exception {
        when(packagingService.getPackageInfoByProductId(1L)).thenReturn(new PackagingGetResponseDto());
        mockMvc.perform(get("/api/product/packaging/single/byProduct/1"))
                .andExpect(status().isOk());

        verify(packagingService, times(1)).getPackageInfoByProductId(1L);
    }

    @Test
    void getSinglePackagingFailureTest() throws Exception {
        when(packagingService.getPackageInfoByProductId(1L)).thenReturn(null);
        mockMvc.perform(get("/api/product/packaging/single/byProduct/1"))
                .andExpect(status().isNotFound());

        verify(packagingService, times(1)).getPackageInfoByProductId(1L);
    }

//    @Test
//    void getSinglePackagingFailureTest() throws Exception {
//        when(packagingService.getPackageInfoByProductId(1L)).thenReturn(null);
//        mockMvc.perform(get("/api/product/packaging/single/byProduct/1"))
//                .andExpect(status().isNotFound());
//
//        verify(packagingService, times(1)).getPackageInfoByProductId(1L);
//    }

}
