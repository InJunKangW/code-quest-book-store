package com.nhnacademy.bookstoreinjun.controller;

import com.nhnacademy.bookstoreinjun.dto.book.AladinBookListResponseDto;
import com.nhnacademy.bookstoreinjun.dto.book.AladinBookResponseDto;
import com.nhnacademy.bookstoreinjun.dto.book.BookRegisterRequestDto;
import com.nhnacademy.bookstoreinjun.dto.book.BookRegisterResponseDto;
import com.nhnacademy.bookstoreinjun.dto.error.ErrorResponseDto;
import com.nhnacademy.bookstoreinjun.dto.product.ProductRegisterRequestDto;
import com.nhnacademy.bookstoreinjun.entity.Book;
import com.nhnacademy.bookstoreinjun.entity.Product;
import com.nhnacademy.bookstoreinjun.exception.AladinJsonProcessingException;
import com.nhnacademy.bookstoreinjun.exception.DuplicateIdException;
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
import org.springframework.web.bind.annotation.RestController;

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
    public ResponseEntity<BookRegisterResponseDto> saveBookProduct(@RequestBody BookRegisterRequestDto bookRegisterRequestDto){
        Product product = productService.saveProduct(ProductRegisterRequestDto.builder()
                                                        .productName(bookRegisterRequestDto.getTitle())
                                                        .productDescription(bookRegisterRequestDto.getProductDescription())
                                                        .productInventory(bookRegisterRequestDto.getProductInventory())
                                                        .productThumbnailUrl(bookRegisterRequestDto.getCover())
                                                        .productPriceStandard(bookRegisterRequestDto.getProductPriceStandard())
                                                        .productPriceSales(bookRegisterRequestDto.getProductPriceSales())
                                                        .build());

        Book savedBook = bookService.saveBook(Book.builder()
                                                        .title(bookRegisterRequestDto.getTitle())
                                                        .author(bookRegisterRequestDto.getAuthor())
                                                        .publisher(bookRegisterRequestDto.getPublisher())
                                                        .isbn(bookRegisterRequestDto.getIsbn())
                                                        .isbn13(bookRegisterRequestDto.getIsbn13())
                                                        .pubDate(bookRegisterRequestDto.getPubDate())
                                                        .packable(bookRegisterRequestDto.isPackable())
                                                        .product(product)
                                                        .build());

        BookRegisterResponseDto dto = new BookRegisterResponseDto(savedBook.getBookId(), savedBook.getProduct().getProductRegisterDate());

        return new ResponseEntity<>(dto, HttpStatus.CREATED);
    }
}
//BookRegisterDto에 태그랑 카테고리 필드도 추가해야.. 이름이 unique하다고 하면 그냥 List<String> 두 개로 하면 될 것 같기도 하고..
