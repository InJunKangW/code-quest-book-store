package com.nhnacademy.bookstoreinjun.controller;

import com.nhnacademy.bookstoreinjun.dto.book.AladinBookListResponseDto;
import com.nhnacademy.bookstoreinjun.dto.book.AladinBookResponseDto;
import com.nhnacademy.bookstoreinjun.dto.book.BookProductRegisterRequestDto;
import com.nhnacademy.bookstoreinjun.dto.book.BookProductRegisterResponseDto;
import com.nhnacademy.bookstoreinjun.dto.book.BookRegisterRequestDto;
import com.nhnacademy.bookstoreinjun.dto.error.ErrorResponseDto;
import com.nhnacademy.bookstoreinjun.dto.product.ProductRegisterRequestDto;
import com.nhnacademy.bookstoreinjun.entity.Book;
import com.nhnacademy.bookstoreinjun.entity.Product;
import com.nhnacademy.bookstoreinjun.exception.AladinJsonProcessingException;
import com.nhnacademy.bookstoreinjun.feignclient.BookRegisterClient;
import com.nhnacademy.bookstoreinjun.service.AladinService;
import com.nhnacademy.bookstoreinjun.service.BookService;
import com.nhnacademy.bookstoreinjun.service.ProductService;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
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

@Slf4j
//@RestController
@Controller
@RequestMapping("/api/admin/book")
@RequiredArgsConstructor
public class BookController {

    //웹에 있을 거
    private final int PAGE_SIZE = 5;

    //웹에 있을 거
    private final BookRegisterClient bookRegisterClient;

    private final AladinService aladinService;

    private final BookService bookService;

    private final ProductService productService;

    @ExceptionHandler(AladinJsonProcessingException.class)
    public ResponseEntity<ErrorResponseDto> exceptionHandler(AladinJsonProcessingException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponseDto(ex.getMessage()));
    }

    //웹에 있을 거
    @GetMapping
    @RequestMapping("/register")
    public String home() {
        return "registerForm";
    }


    // feign 이 호출하는 메서드.
    @GetMapping
    public ResponseEntity<AladinBookListResponseDto> getBooks(@RequestParam("title")String title){
        AladinBookListResponseDto aladinBookListResponseDto = aladinService.getAladdinBookList(title);
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, "application/json");
        return new ResponseEntity<>(aladinBookListResponseDto, headers, HttpStatus.OK);
    }


    /**
     *
     * @param title 검색할 제목
     * @param model 모델
     * @return string
     */
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

    @Transactional
    @PostMapping("/register")
    public ResponseEntity<BookProductRegisterResponseDto> saveBookProduct(@RequestBody
                                                                   BookProductRegisterRequestDto bookProductRegisterRequestDto){
//        Product product = productService.saveProduct(ProductRegisterRequestDto.builder()
//                                                        .productName(bookProductRegisterRequestDto.getTitle())
//                                                        .productDescription(bookProductRegisterRequestDto.getProductDescription())
//                                                        .productInventory(bookProductRegisterRequestDto.getProductInventory())
//                                                        .productThumbnailUrl(bookProductRegisterRequestDto.getCover())
//                                                        .productPriceStandard(bookProductRegisterRequestDto.getProductPriceStandard())
//                                                        .productPriceSales(bookProductRegisterRequestDto.getProductPriceSales())
//                                                        .build());
//
//        Book savedBook = bookService.saveBook(Book.builder()
//                                                        .title(bookProductRegisterRequestDto.getTitle())
//                                                        .author(bookProductRegisterRequestDto.getAuthor())
//                                                        .publisher(bookProductRegisterRequestDto.getPublisher())
//                                                        .isbn(bookProductRegisterRequestDto.getIsbn())
//                                                        .isbn13(bookProductRegisterRequestDto.getIsbn13())
//                                                        .pubDate(bookProductRegisterRequestDto.getPubDate())
//                                                        .packable(bookProductRegisterRequestDto.isPackable())
//                                                        .product(product)
//                                                        .build());

        Product product = productService.saveProduct(ProductRegisterRequestDto.builder()
                .productName(bookProductRegisterRequestDto.title())
                .productDescription(bookProductRegisterRequestDto.productDescription())
                .productInventory(bookProductRegisterRequestDto.productInventory())
                .productThumbnailUrl(bookProductRegisterRequestDto.cover())
                .productPriceStandard(bookProductRegisterRequestDto.productPriceStandard())
                .productPriceSales(bookProductRegisterRequestDto.productPriceSales())
                .build());


        Book savedBook = bookService.saveBook(BookRegisterRequestDto.builder()
                .title(bookProductRegisterRequestDto.title())
                .author(bookProductRegisterRequestDto.author())
                .publisher(bookProductRegisterRequestDto.publisher())
                .isbn(bookProductRegisterRequestDto.isbn())
                .isbn13(bookProductRegisterRequestDto.isbn13())
                .pubDate(bookProductRegisterRequestDto.pubDate())
                .packable(bookProductRegisterRequestDto.packable())
                .product(product)
                .build());

//        Book savedBook = bookService.saveBook(
//
//                Book.builder()
//                .title(bookProductRegisterRequestDto.title())
//                .author(bookProductRegisterRequestDto.author())
//                .publisher(bookProductRegisterRequestDto.publisher())
//                .isbn(bookProductRegisterRequestDto.isbn())
//                .isbn13(bookProductRegisterRequestDto.isbn13())
//                .pubDate(bookProductRegisterRequestDto.pubDate())
//                .packable(bookProductRegisterRequestDto.packable())
//                .product(product)
//                .build());

        BookProductRegisterResponseDto
                dto = new BookProductRegisterResponseDto(savedBook.getBookId(), savedBook.getProduct().getProductRegisterDate());

        return new ResponseEntity<>(dto, HttpStatus.CREATED);
    }
}
//BookRegisterDto에 태그랑 카테고리 필드도 추가해야.. 이름이 unique하다고 하면 그냥 Set<String> 두 개로 하면 될 것 같기도 하고..
