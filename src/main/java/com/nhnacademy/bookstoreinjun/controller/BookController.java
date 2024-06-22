package com.nhnacademy.bookstoreinjun.controller;

import com.nhnacademy.bookstoreinjun.dto.book.aladin.AladinBookListResponseDto;
import com.nhnacademy.bookstoreinjun.dto.book.BookProductGetResponseDto;
import com.nhnacademy.bookstoreinjun.dto.book.BookProductRegisterRequestDto;
import com.nhnacademy.bookstoreinjun.dto.book.BookProductUpdateRequestDto;
import com.nhnacademy.bookstoreinjun.dto.page.BookPageRequestDto;
import com.nhnacademy.bookstoreinjun.dto.product.ProductRegisterResponseDto;
import com.nhnacademy.bookstoreinjun.dto.error.ErrorResponseDto;
import com.nhnacademy.bookstoreinjun.dto.product.ProductUpdateResponseDto;
import com.nhnacademy.bookstoreinjun.exception.AladinJsonProcessingException;
import com.nhnacademy.bookstoreinjun.service.aladin.AladinService;
import com.nhnacademy.bookstoreinjun.service.book.BookService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Slf4j
//@RestController
@Controller
@RequestMapping("/api")
@RequiredArgsConstructor
public class BookController {

    private final AladinService aladinService;

    private final BookService bookService;

    private final HttpHeaders header = new HttpHeaders() {{
        setContentType(MediaType.APPLICATION_JSON);
    }};

    @ExceptionHandler(AladinJsonProcessingException.class)
    public ResponseEntity<ErrorResponseDto> exceptionHandler(AladinJsonProcessingException ex) {
        return new ResponseEntity<>(new ErrorResponseDto(ex.getMessage()), header, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // feign 이 호출하는 메서드.
    @GetMapping("/admin/book")
    public ResponseEntity<AladinBookListResponseDto> getBooks(@RequestParam("title")String title){
        return new ResponseEntity<>(aladinService.getAladdinBookList(title), header, HttpStatus.OK);
    }

    @PostMapping("/admin/book/register")
    public ResponseEntity<ProductRegisterResponseDto> saveBookProduct(@Valid @RequestBody BookProductRegisterRequestDto bookProductRegisterRequestDto){
        return new ResponseEntity<>(bookService.saveBook(bookProductRegisterRequestDto), header, HttpStatus.CREATED);
    }

    //페이지 조회. 기본적으로 판매 중인 책만 조회합니다.
    @GetMapping("/books")
    public ResponseEntity<Page<BookProductGetResponseDto>> getAllBookPage(@Valid @RequestBody BookPageRequestDto bookPageRequestDto){
        return new ResponseEntity<>(bookService.getBookPage(bookPageRequestDto), header, HttpStatus.OK);
    }

    @GetMapping("/book/{bookId}")
    public ResponseEntity<BookProductGetResponseDto> getSingleBookInfo(@PathVariable long bookId){
        return new ResponseEntity<>(bookService.getBookByBookId(bookId), header, HttpStatus.OK);
    }


    @PutMapping("/admin/book")
    public ResponseEntity<ProductUpdateResponseDto> updateBook(@Valid @RequestBody BookProductUpdateRequestDto bookProductUpdateRequestDto){
        return new ResponseEntity<>(bookService.updateBook(bookProductUpdateRequestDto), header, HttpStatus.OK);
    }


//    //웹에 있을 거
//    private final BookRegisterClient bookRegisterClient;
//
//    // 유레카, 게이트웨이, 웹 다 켜놓고 테스트하기 번거로울 때 주석 풀고 쓰려고 냅뒀습니다.
//    @GetMapping
//    @RequestMapping("/register")
//    public String home() {
//        return "registerForm";
//    }
//
//    //    웹에 있을 거
//    @GetMapping
//    @RequestMapping("/test/test")
//    public String test(@RequestParam("title")String title, Model model) {
//        log.error("test called + title : {}", title);
//        ResponseEntity<AladinBookListResponseDto> aladinBookListResponseDtoResponseEntity = bookRegisterClient.getBookList(title);
//
//        AladinBookListResponseDto aladinBookListResponseDto = aladinBookListResponseDtoResponseEntity.getBody();
//
//        if (aladinBookListResponseDto == null) {
//            log.info("dto is null");
//            model.addAttribute("bookList", new ArrayList<>());
//        }else{
//            log.info("dto isn't null");
//
//            List<AladinBookResponseDto> bookList = aladinBookListResponseDto.getBooks();
//            model.addAttribute("bookList", bookList);
//        }
//
//        return "test";
//    }
}
