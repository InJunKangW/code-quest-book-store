package com.nhnacademy.bookstoreinjun.dto.product;

import java.time.LocalDateTime;

public record ProductRegisterResponseDto(
        Long id,
        LocalDateTime registerTime
){}