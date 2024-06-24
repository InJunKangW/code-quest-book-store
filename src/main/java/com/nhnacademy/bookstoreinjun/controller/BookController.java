package com.nhnacademy.bookstoreinjun.controller;

import com.nhnacademy.bookstoreinjun.dto.book.aladin.AladinBookListResponseDto;
import com.nhnacademy.bookstoreinjun.dto.book.BookProductGetResponseDto;
import com.nhnacademy.bookstoreinjun.dto.book.BookProductRegisterRequestDto;
import com.nhnacademy.bookstoreinjun.dto.book.BookProductUpdateRequestDto;
import com.nhnacademy.bookstoreinjun.dto.book.aladin.AladinBookResponseDto;
import com.nhnacademy.bookstoreinjun.dto.page.PageRequestDto;
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
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/product")
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
    public ResponseEntity<Page<AladinBookResponseDto>> getBooks(@RequestParam(name = "page", required = false) Integer page, @RequestParam(name = "sort", required = false) String sort, @RequestParam("title") String title){
        log.info("controller called");
        PageRequestDto pageRequestDto = PageRequestDto.builder().page(page).sort(sort).build();
        return new ResponseEntity<>(aladinService.getAladdinBookPage(pageRequestDto, title), header, HttpStatus.OK);
    }

    @PostMapping("/admin/book/register")
    public ResponseEntity<ProductRegisterResponseDto> saveBookProduct(@Valid @RequestBody BookProductRegisterRequestDto bookProductRegisterRequestDto){
        return new ResponseEntity<>(bookService.saveBook(bookProductRegisterRequestDto), header, HttpStatus.CREATED);
    }

    //페이지 조회. 기본적으로 판매 중인 책만 조회합니다.
    @GetMapping("/books")
    public ResponseEntity<Page<BookProductGetResponseDto>> getAllBookPage(@RequestParam(name = "page", required = false) Integer page, @RequestParam(name = "sort", required = false) String sort){
        PageRequestDto pageRequestDto = PageRequestDto.builder().page(page).sort(sort).build();
        return new ResponseEntity<>(bookService.getBookPage(pageRequestDto), header, HttpStatus.OK);
    }

    @GetMapping("/book/{bookId}")
    public ResponseEntity<BookProductGetResponseDto> getSingleBookInfo(@PathVariable long bookId){
        return new ResponseEntity<>(bookService.getBookByBookId(bookId), header, HttpStatus.OK);
    }


    @PutMapping("/admin/book/update")
    public ResponseEntity<ProductUpdateResponseDto> updateBook(@Valid @RequestBody BookProductUpdateRequestDto bookProductUpdateRequestDto){
        return new ResponseEntity<>(bookService.updateBook(bookProductUpdateRequestDto), header, HttpStatus.OK);

}
