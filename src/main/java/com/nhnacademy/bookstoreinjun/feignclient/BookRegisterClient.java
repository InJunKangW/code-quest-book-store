package com.nhnacademy.bookstoreinjun.feignclient;


import com.nhnacademy.bookstoreinjun.dto.book.AladinBookListResponseDto;
import com.nhnacademy.bookstoreinjun.dto.book.BookProductRegisterRequestDto;
import com.nhnacademy.bookstoreinjun.dto.product.ProductRegisterResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

//웹에 있을 거
@FeignClient(name = "ADMIN-SERVICE", url = "http://localhost:8003/api/admin/book")
public interface BookRegisterClient {
    @GetMapping
    ResponseEntity<AladinBookListResponseDto> getBookList(@RequestParam("title") String title);

    @PostMapping("/register")
    ResponseEntity<ProductRegisterResponseDto> registerBook(@RequestBody BookProductRegisterRequestDto bookProductRegisterRequestDto);
}