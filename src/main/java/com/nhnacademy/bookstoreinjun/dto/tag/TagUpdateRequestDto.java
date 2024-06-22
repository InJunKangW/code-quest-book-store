package com.nhnacademy.bookstoreinjun.dto.tag;

public record TagUpdateRequestDto (
        String currentTagName,
        String newTagName
)
{}
