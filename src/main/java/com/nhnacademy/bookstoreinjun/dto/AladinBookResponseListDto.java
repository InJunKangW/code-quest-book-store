package com.nhnacademy.bookstoreinjun.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import java.util.List;

import com.nhnacademy.bookstoreinjun.dto.book.aladin.AladinBookResponseDto;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class AladinBookResponseListDto {
    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "item")
    private List<AladinBookResponseDto> books;
}
