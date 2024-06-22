package com.nhnacademy.bookstoreinjun.dto.tag;

import java.time.LocalDateTime;

public record TagUpdateResponseDto(
        String previousTagName,
        String changedName,
        LocalDateTime updateTime
)
{ }
