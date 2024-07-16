package com.nhnacademy.bookstoreinjun.dto.tag;

import lombok.Builder;

@Builder
public record TagGetResponseDto (
        Long tagId,
        String tagName,
        Long productCount
){
}
