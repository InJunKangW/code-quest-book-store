package com.nhnacademy.bookstoreinjun.controller;

import com.nhnacademy.bookstoreinjun.dto.book.AladinBookListResponseDto;
import com.nhnacademy.bookstoreinjun.dto.book.AladinBookResponseDto;
import com.nhnacademy.bookstoreinjun.dto.book.BookProductRegisterRequestDto;
import com.nhnacademy.bookstoreinjun.dto.product.ProductRegisterResponseDto;
import com.nhnacademy.bookstoreinjun.dto.book.BookRegisterRequestDto;
import com.nhnacademy.bookstoreinjun.dto.error.ErrorResponseDto;
import com.nhnacademy.bookstoreinjun.dto.product.ProductRegisterRequestDto;
import com.nhnacademy.bookstoreinjun.entity.Category;
import com.nhnacademy.bookstoreinjun.entity.Product;
import com.nhnacademy.bookstoreinjun.entity.ProductCategory;
import com.nhnacademy.bookstoreinjun.entity.ProductTag;
import com.nhnacademy.bookstoreinjun.entity.Tag;
import com.nhnacademy.bookstoreinjun.exception.AladinJsonProcessingException;
import com.nhnacademy.bookstoreinjun.feignclient.BookRegisterClient;
import com.nhnacademy.bookstoreinjun.service.aladin.AladinService;
import com.nhnacademy.bookstoreinjun.service.book.BookService;
import com.nhnacademy.bookstoreinjun.service.category.CategoryService;
import com.nhnacademy.bookstoreinjun.service.ProductCategoryService;
import com.nhnacademy.bookstoreinjun.service.ProductService;
import com.nhnacademy.bookstoreinjun.service.ProductTagService;
import com.nhnacademy.bookstoreinjun.service.tag.TagService;
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


    private final AladinService aladinService;

    private final BookService bookService;

    private final ProductService productService;

    private final CategoryService categoryService;

    private final ProductCategoryService productCategoryService;

    private final TagService tagService;

    private final ProductTagService productTagService;

    @ExceptionHandler(AladinJsonProcessingException.class)
    public ResponseEntity<ErrorResponseDto> exceptionHandler(AladinJsonProcessingException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponseDto(ex.getMessage()));
    }

    // feign 이 호출하는 메서드.
    @GetMapping
    public ResponseEntity<AladinBookListResponseDto> getBooks(@RequestParam("title")String title){
        AladinBookListResponseDto aladinBookListResponseDto = aladinService.getAladdinBookList(title);
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, "application/json");
        log.info("{}",aladinBookListResponseDto);
        return new ResponseEntity<>(aladinBookListResponseDto, headers, HttpStatus.OK);
    }


    private ProductRegisterRequestDto getProductRegisterRequestDto(BookProductRegisterRequestDto bookProductRegisterRequestDto){
        return ProductRegisterRequestDto.builder()
                .productName(bookProductRegisterRequestDto.title())
                .productDescription(bookProductRegisterRequestDto.productDescription())
                .productInventory(bookProductRegisterRequestDto.productInventory())
                .productThumbnailUrl(bookProductRegisterRequestDto.cover())
                .productPriceStandard(bookProductRegisterRequestDto.productPriceStandard())
                .productPriceSales(bookProductRegisterRequestDto.productPriceSales())
                .build();
    }


    private BookRegisterRequestDto getBookRegisterRequestDto(BookProductRegisterRequestDto bookProductRegisterRequestDto, Product product){
        return BookRegisterRequestDto.builder()
                .title(bookProductRegisterRequestDto.title())
                .author(bookProductRegisterRequestDto.author())
                .publisher(bookProductRegisterRequestDto.publisher())
                .isbn(bookProductRegisterRequestDto.isbn())
                .isbn13(bookProductRegisterRequestDto.isbn13())
                .pubDate(bookProductRegisterRequestDto.pubDate())
                .packable(bookProductRegisterRequestDto.packable())
                .product(product)
                .build();
    }

    @Transactional
    @PostMapping("/register")
    public ResponseEntity<ProductRegisterResponseDto> saveBookProduct(@RequestBody BookProductRegisterRequestDto bookProductRegisterRequestDto){

        Product product = productService.saveProduct(getProductRegisterRequestDto(bookProductRegisterRequestDto));

        bookService.saveBook(getBookRegisterRequestDto(bookProductRegisterRequestDto, product));


        List<String> categories = bookProductRegisterRequestDto.categories();
        if (categories != null){
            for (String categoryName : categories) {
                Category category = categoryService.getCategoryByName(categoryName);
                ProductCategory productCategory = ProductCategory.builder()
                        .category(category)
                        .product(product)
                        .build();

                productCategoryService.saveProductCategory(productCategory);
            }
        }


        List<String> tags = bookProductRegisterRequestDto.tags();
        if (tags != null){
            for (String tagName : tags) {
                Tag tag = tagService.getTagByTagName(tagName);
                ProductTag productTag = ProductTag.builder()
                        .product(product)
                        .tag(tag)
                        .build();
                productTagService.saveProductTag(productTag);
            }
        }

        ProductRegisterResponseDto dto = new ProductRegisterResponseDto(product.getProductId(), product.getProductRegisterDate());

        return new ResponseEntity<>(dto, HttpStatus.CREATED);
    }

    //웹에 있을 거
//    private final int PAGE_SIZE = 5;
//
//    //웹에 있을 거
//    private final BookRegisterClient bookRegisterClient;

    // 유레카, 게이트웨이, 웹 다 켜놓고 테스트하기 그럴 때 쓰려고 냅뒀습니다.
    //웹에 있을 거
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
