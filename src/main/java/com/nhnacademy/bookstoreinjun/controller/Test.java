package com.nhnacademy.bookstoreinjun.controller;

import com.nhnacademy.bookstoreinjun.repository.JPQLTest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class Test {
    private final JPQLTest jpqlTest;
    @GetMapping("/test")
    public String test() {
        jpqlTest.test();
        return "test";
    }
}
