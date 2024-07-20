package com.nhnacademy.bookstoreinjun.feignclient;

import com.nhnacademy.bookstoreinjun.config.FeignConfig;
import com.nhnacademy.bookstoreinjun.dto.category.CategoryNodeResponseDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "WEB-SERVICE", configuration = FeignConfig.class)
public interface WebClient {
    @Value("${web.url}")
    String URL = "";

    @PostMapping(URL + "/category/update")
    ResponseEntity<String> updateCategory(@RequestBody CategoryNodeResponseDto categoryNodeResponseDto);
}
