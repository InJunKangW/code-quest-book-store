package com.nhnacademy.bookstoreinjun.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.nhnacademy.bookstoreinjun.dto.book.AladinBookListResponseDto;
import com.nhnacademy.bookstoreinjun.exception.AladinJsonProcessingException;
import com.nhnacademy.bookstoreinjun.feignclient.AladinClient;
import java.nio.charset.StandardCharsets;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AladinService {
    private final AladinClient aladinClient;

    public AladinBookListResponseDto getAladdinBookList(String title){
        try {
            byte[] bytes = title.getBytes(StandardCharsets.UTF_8);
            String utf8EncodedString = new String(bytes, StandardCharsets.UTF_8);
            String responseBody = aladinClient.getBooks("ttbjasmine066220924001",utf8EncodedString,"Title",100);

            XmlMapper xmlMapper = new XmlMapper();
            return xmlMapper.readValue(responseBody, AladinBookListResponseDto.class);
        }catch (JsonProcessingException e){
            throw new AladinJsonProcessingException(title);
        }

    }
}
