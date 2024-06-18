package com.nhnacademy.bookstoreinjun.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.nhnacademy.bookstoreinjun.dto.AladinBookListResponseDto;
import com.nhnacademy.bookstoreinjun.dto.AladinBookResponseDto;
import com.nhnacademy.bookstoreinjun.feignclient.BookRegisterClient;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Slf4j
@Controller
@RequestMapping("/api/book")
@RequiredArgsConstructor
public class BookController {

    private final int PAGE_SIZE = 5;
    private final RestTemplate restTemplate;


    private final BookRegisterClient bookRegisterClient;

    @GetMapping
    @RequestMapping("/register")
    public String home() {
        return "registerForm";
    }


    @GetMapping
    public
    ResponseEntity<AladinBookListResponseDto> getBooks(@RequestParam("title")String title) throws JsonProcessingException {
        log.error("test called");

//       검색할 제목
        byte[] bytes = title.getBytes(StandardCharsets.UTF_8);
        String utf8EncodedString = new String(bytes, StandardCharsets.UTF_8);


        URI uri = UriComponentsBuilder
                .fromUriString("http://www.aladin.co.kr")
                .path("/ttb/api/ItemSearch.aspx")
                .queryParam("TTBKey","ttbjasmine066220924001")
                .queryParam("Query",utf8EncodedString)
                .queryParam("QueryType","Title")
                .queryParam("MaxResults", 100)
                .encode()
                .build()
                .toUri();
        //제목 검색 - 리스트 보기.


        ResponseEntity<String> responseEntity = restTemplate.exchange(RequestEntity.get(uri).build(), String.class);
        String responseBody = responseEntity.getBody();

        XmlMapper xmlMapper = new XmlMapper();
        AladinBookListResponseDto aladinBookListResponseDto = xmlMapper.readValue(responseBody, AladinBookListResponseDto.class);

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, "application/json");
        return new ResponseEntity<>(aladinBookListResponseDto, headers, HttpStatus.OK);
    }


    //웹에 있을 거
    @GetMapping
    @RequestMapping("/test/test")
    public String test(@RequestParam("title")String title, Model model) {
        log.error("test called + title : {}", title);
        ResponseEntity<AladinBookListResponseDto> aladinBookListResponseDtoResponseEntity = bookRegisterClient.getBookList(title);
        AladinBookListResponseDto aladinBookListResponseDto = aladinBookListResponseDtoResponseEntity.getBody();
        List<AladinBookResponseDto> bookList = aladinBookListResponseDto.getBooks();
        model.addAttribute("bookList", bookList);

        return "test";
    }

    @PostMapping
    public String post(@ModelAttribute AladinBookResponseDto AladinBookResponseDto, Model model) throws JsonProcessingException {
        log.error("test called");
        log.info("title : {}, author : {}, isbn : {} cover : {}, priceStandard : {}, isbn13: {}, pubdate :{}, publisher : {}"
                ,AladinBookResponseDto.getTitle(), AladinBookResponseDto.getAuthor(), AladinBookResponseDto.getIsbn(), AladinBookResponseDto.getCover(), AladinBookResponseDto.getPriceStandard(), AladinBookResponseDto.getIsbn13(), AladinBookResponseDto.getPubDate(), AladinBookResponseDto.getPublisher());
        model.addAttribute("book", AladinBookResponseDto);
        return "eachBook";
    }
}
