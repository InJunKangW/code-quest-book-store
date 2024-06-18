package com.nhnacademy.bookstoreinjun.feignclient;


import com.nhnacademy.bookstoreinjun.dto.AladinBookListResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

//웹에 있을 거
@FeignClient(name = "product-service", url = "http://localhost:8080/api/book")
public interface BookRegisterClient {
    @GetMapping
    public ResponseEntity<AladinBookListResponseDto> getBookList(@RequestParam("title") String title);

}
