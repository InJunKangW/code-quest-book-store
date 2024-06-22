package com.nhnacademy.bookstoreinjun.dto.tag;

import java.time.LocalDateTime;
import lombok.Builder;

@Builder
public record TagUpdateResponseDto(
        String previousTagName,
        String newTagName,
        LocalDateTime updateTime
)
{ }
