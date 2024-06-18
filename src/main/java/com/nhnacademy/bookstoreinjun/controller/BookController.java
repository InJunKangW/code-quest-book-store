package com.nhnacademy.bookstoreinjun.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.nhnacademy.bookstoreinjun.dto.book.AladinBookListResponseDto;
import com.nhnacademy.bookstoreinjun.dto.book.AladinBookResponseDto;
import com.nhnacademy.bookstoreinjun.dto.book.BookRegisterRequestDto;
import com.nhnacademy.bookstoreinjun.dto.product.ProductRequestDto;
import com.nhnacademy.bookstoreinjun.entity.Book;
import com.nhnacademy.bookstoreinjun.entity.Product;
import com.nhnacademy.bookstoreinjun.exception.DuplicateIdException;
import com.nhnacademy.bookstoreinjun.feignclient.BookRegisterClient;
import com.nhnacademy.bookstoreinjun.service.BookService;
import com.nhnacademy.bookstoreinjun.service.ProductService;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Slf4j
@Controller
@RequestMapping("/api/admin/book")
@RequiredArgsConstructor
public class BookController {

    private final int PAGE_SIZE = 5;
    private final RestTemplate restTemplate;

    private final BookRegisterClient bookRegisterClient;

    private final BookService bookService;
    private final ProductService productService;


    @ExceptionHandler(DuplicateIdException.class)
    public ResponseEntity<Void> exceptionHandler(DuplicateIdException ex, Model model) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
    }

    @GetMapping
    @RequestMapping("/register")
    public String home() {
        return "registerForm";
    }

    // feign 이 호출하는 메서드.
    @GetMapping
    public ResponseEntity<AladinBookListResponseDto> getBooks(@RequestParam("title")String title) throws JsonProcessingException {
        log.info("getBooks");
//       검색할 제목
        byte[] bytes = title.getBytes(StandardCharsets.UTF_8);
        String utf8EncodedString = new String(bytes, StandardCharsets.UTF_8);


        URI uri = UriComponentsBuilder
                .fromUriString("https://www.aladin.co.kr")
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
        log.info("{}",responseBody);

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
        if (aladinBookListResponseDto == null) {
            log.info("dto is null");
            model.addAttribute("bookList", new ArrayList<>());
        }else{
            log.info("dto isn't null");

            List<AladinBookResponseDto> bookList = aladinBookListResponseDto.getBooks();
            model.addAttribute("bookList", bookList);
        }
        return "test";
    }

//    @PostMapping("/register")
//    public String post(@ModelAttribute AladinBookResponseDto AladinBookResponseDto,@RequestParam("pubDate")String pubDate, Model model) throws JsonProcessingException {
//        log.info("title : {}, author : {}, isbn : {} cover : {}, priceStandard : {}, isbn13: {}, pubdate :{}, publisher : {}"
//                ,AladinBookResponseDto.getTitle(), AladinBookResponseDto.getAuthor(), AladinBookResponseDto.getIsbn(), AladinBookResponseDto.getCover(), AladinBookResponseDto.getPriceStandard(), AladinBookResponseDto.getIsbn13(), AladinBookResponseDto.getPubDate(), AladinBookResponseDto.getPublisher());
//        model.addAttribute("book", AladinBookResponseDto);
//        return "eachBook";
//    }

    @Transactional
    @PostMapping("/register")
    public ResponseEntity<Void> saveBookProduct(@RequestBody BookRegisterRequestDto bookRegisterRequestDto){
        log.info("register called");
        ProductRequestDto productRequestDto = ProductRequestDto
                .builder()
                .productName(bookRegisterRequestDto.getTitle())
                .productDescription(bookRegisterRequestDto.getProductDescription())
                .productInventory(bookRegisterRequestDto.getProductInventory())
                .productThumbnailUrl(bookRegisterRequestDto.getCover())
                .productPriceStandard(bookRegisterRequestDto.getProductPriceStandard())
                .productPriceSales(bookRegisterRequestDto.getProductPriceSales())
                .build();

        log.info("productRequestDto : {}", productRequestDto);

        Product product = productService.createProduct(productRequestDto);
        Book book = Book.builder()
                .title(bookRegisterRequestDto.getTitle())
                .author(bookRegisterRequestDto.getAuthor())
                .publisher(bookRegisterRequestDto.getPublisher())
                .isbn(bookRegisterRequestDto.getIsbn())
                .isbn13(bookRegisterRequestDto.getIsbn13())
                .pubDate(bookRegisterRequestDto.getPubDate())
                .packable(bookRegisterRequestDto.isPackable())
                .product(product)
                .build();
        bookService.saveBook(book);

//        return ResponseEntity.status(204).build();
        return new ResponseEntity<>(null, HttpStatus.CREATED);
    }
}
