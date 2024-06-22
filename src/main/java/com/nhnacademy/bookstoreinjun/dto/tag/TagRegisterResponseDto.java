package com.nhnacademy.bookstoreinjun.dto.tag;

import lombok.Builder;

@Builder
public record TagRegisterResponseDto(
        Long tagId,
        String tagName
)
{}
