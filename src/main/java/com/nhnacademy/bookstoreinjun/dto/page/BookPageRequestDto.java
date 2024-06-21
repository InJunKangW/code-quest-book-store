package com.nhnacademy.bookstoreinjun.dto.page;

import jakarta.validation.constraints.Min;

public record BookPageRequestDto(
       @Min(1) Integer page,
       @Min(1) Integer size,
        String sort,
        Boolean desc
) {
}
