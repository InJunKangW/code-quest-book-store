package com.nhnacademy.bookstoreinjun.feignclient;

import com.nhnacademy.bookstoreinjun.config.FeignConfig;
import com.nhnacademy.bookstoreinjun.dto.category.CategoryNodeResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "WEB-SERVICE", url = "${web.url}"+"/category/update", configuration = FeignConfig.class)
public interface WebClient {
    @PostMapping
    ResponseEntity<String> updateCategory(@RequestBody CategoryNodeResponseDto categoryNodeResponseDto);
}
