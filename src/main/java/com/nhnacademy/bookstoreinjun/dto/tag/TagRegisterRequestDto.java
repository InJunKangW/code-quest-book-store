package com.nhnacademy.bookstoreinjun.dto.tag;

import lombok.Builder;

@Builder
public record TagRegisterRequestDto(
        String tagName
){}
