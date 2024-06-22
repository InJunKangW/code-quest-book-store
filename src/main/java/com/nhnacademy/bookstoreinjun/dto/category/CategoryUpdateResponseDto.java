package com.nhnacademy.bookstoreinjun.dto.category;

import java.time.LocalDateTime;
import lombok.Builder;

@Builder
public record CategoryUpdateResponseDto(
        String previousCategoryName,
        String newCategoryName,
        LocalDateTime updateTime
) {
}
