package com.nhnacademy.bookstoreinjun.service;

import com.nhnacademy.bookstoreinjun.repository.ProductCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductCategoryService {
    private final ProductCategoryRepository productCategoryRepository;
}
