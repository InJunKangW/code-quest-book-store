package com.nhnacademy.bookstoreinjun.controller;


import com.nhnacademy.bookstoreinjun.service.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/tag")
@RequiredArgsConstructor
public class TagController {
    private final TagService tagService;
}
