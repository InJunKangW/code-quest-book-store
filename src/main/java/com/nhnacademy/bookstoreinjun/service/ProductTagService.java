package com.nhnacademy.bookstoreinjun.service;

import com.nhnacademy.bookstoreinjun.repository.ProductTagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductTagService {
    private final ProductTagRepository productTagRepository;
}
