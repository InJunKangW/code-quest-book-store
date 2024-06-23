package com.nhnacademy.bookstoreinjun.dto.tag;

import lombok.Builder;

@Builder
public record TagUpdateRequestDto (
        String currentTagName,
        String newTagName
)
{}
