package com.nhnacademy.bookstoreinjun.dto.page;

import jakarta.validation.constraints.Min;
import lombok.Builder;

@Builder
public record PageRequestDto(
       @Min(1) Integer page,
       @Min(1) Integer size,
        String sort,
        Boolean desc
) {
}
