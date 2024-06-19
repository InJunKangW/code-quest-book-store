package com.nhnacademy.bookstoreinjun.dto.book;

import java.time.LocalDateTime;

public record BookProductRegisterResponseDto(
        Long id,
        LocalDateTime registerTime
){}