package com.nhnacademy.bookstoreinjun.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.nhnacademy.bookstoreinjun.dto.AladinBookResponseListDto;
import com.nhnacademy.bookstoreinjun.dto.AladinBookResponseDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
@RequestMapping("/test")
@RequiredArgsConstructor
public class BookController {

    private final int PAGE_SIZE = 5;
    private final RestTemplate restTemplate;

    @GetMapping
    @RequestMapping("/home")
    public String home() {
        return "home";
    }

    @GetMapping
    @RequestMapping("/another")
    public void test2(@RequestParam("title")String title, HttpServletRequest req, HttpServletResponse resp) throws Exception {
        byte[] bytes = title.getBytes(StandardCharsets.UTF_8);
        String utf8EncodedString = new String(bytes, StandardCharsets.UTF_8);


        URI uri = UriComponentsBuilder
                .fromUriString("http://www.aladin.co.kr")
                .path("/ttb/api/ItemSearch.aspx")
                .queryParam("TTBKey","ttbjasmine066220924001")
                .queryParam("Query",utf8EncodedString)
                .queryParam("QueryType","Title")
//                .queryParam("start",500)
                .queryParam("MaxResults", 100)
                .encode()
                .build()
                .toUri();
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> responseEntity = restTemplate.exchange(RequestEntity.get(uri).build(), String.class);

        String responseBody = responseEntity.getBody();
        XmlMapper xmlMapper = new XmlMapper();
        AladinBookResponseListDto AladinBookResponseListDto = xmlMapper.readValue(responseBody, AladinBookResponseListDto.class);

        List<AladinBookResponseDto> bookList = AladinBookResponseListDto.getBooks();

        req.setAttribute("bookList", bookList);
        req.getRequestDispatcher("/test/anotherlist").forward(req,resp);
    }

    @GetMapping
    @RequestMapping("anotherlist")
    public String anotherlist(HttpServletRequest req, Model model, @RequestParam(value = "page", defaultValue = "1") int page) throws Exception {
        List<AladinBookResponseDto> bookList = (List<AladinBookResponseDto>)req.getAttribute("bookList");
        int Maxpage = bookList.size()/PAGE_SIZE;
        List<Integer> pageNumbers = new ArrayList<>();
        for (int i = 0; i < Maxpage; i++) {
            pageNumbers.add(i);
        }
        model.addAttribute("pageNumbers", pageNumbers);
        model.addAttribute("bookList", bookList.subList((page-1)*PAGE_SIZE, page * PAGE_SIZE));
        log.info("request good! list: {}", bookList);
        return "test";
    }

    @GetMapping
    public String test(@RequestParam("title")String title, @RequestParam(value = "page", defaultValue = "1") int page, Model model) throws
            JsonProcessingException {
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


        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> responseEntity = restTemplate.exchange(RequestEntity.get(uri).build(), String.class);
        ObjectMapper objectMapper = new ObjectMapper();

        String responseBody = responseEntity.getBody();

        System.out.println("rep body : " + responseBody);

        XmlMapper xmlMapper = new XmlMapper();
        AladinBookResponseListDto
                AladinBookResponseListDto = xmlMapper.readValue(responseBody, AladinBookResponseListDto.class);

        List<AladinBookResponseDto> bookList = AladinBookResponseListDto.getBooks();

        System.out.println(bookList);
        Pageable pageable = PageRequest.of(page - 1,PAGE_SIZE);

        int totalItems = 0;

        if(bookList != null) {
            totalItems = bookList.size();
        }
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), totalItems);
        log.info("total items : " + totalItems);
        log.info("start: " + start);
        log.info("end: " + end);
//        int totalPages = totalItems/PAGE_SIZE;
        Page<AladinBookResponseDto> bookPage = new PageImpl<>(new ArrayList<>(), pageable, totalItems);

        int totalPages = bookPage.getTotalPages();
        log.info("total pages : " + totalPages);

        if(bookList != null) {
            try{
                bookPage = new PageImpl<>(bookList.subList(start, end), pageable, totalItems);
            }catch (IllegalArgumentException e) {
                e.printStackTrace();
            }

        }

        List<Integer> pageNumbers = new ArrayList<>();
        for(int i = 1; i <= totalPages; i++) {
            pageNumbers.add(i);
        }

        model.addAttribute("bookList", bookPage);
        model.addAttribute("pageNumbers", pageNumbers);
        model.addAttribute("title", title);
        return "test";
    }

    @PostMapping
    public String post(@ModelAttribute AladinBookResponseDto AladinBookResponseDto, Model model) throws JsonProcessingException {
        log.error("test called");
        log.info("title : {}, author : {}, isbn : {} cover : {}, priceStandard : {}, isbn13: {}, pubdate :{}, publisher : {}"
                ,AladinBookResponseDto.getTitle(), AladinBookResponseDto.getAuthor(), AladinBookResponseDto.getIsbn(), AladinBookResponseDto.getCover(), AladinBookResponseDto.getPriceStandard(), AladinBookResponseDto.getIsbn13(), AladinBookResponseDto.getPubdate(), AladinBookResponseDto.getPublisher());
        model.addAttribute("book", AladinBookResponseDto);
        return "eachBook";
    }
}
