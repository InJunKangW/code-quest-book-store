package com.nhnacademy.bookstoreinjun.dto.tag;

import lombok.Builder;

@Builder
public record TagGetResponseDto (
        String tagName
){
}
