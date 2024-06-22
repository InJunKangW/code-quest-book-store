package com.nhnacademy.bookstoreinjun.controller;

import com.nhnacademy.bookstoreinjun.dto.book.AladinBookListResponseDto;
import com.nhnacademy.bookstoreinjun.dto.book.AladinBookResponseDto;
import com.nhnacademy.bookstoreinjun.dto.book.BookProductGetResponseDto;
import com.nhnacademy.bookstoreinjun.dto.book.BookProductRegisterRequestDto;
import com.nhnacademy.bookstoreinjun.dto.page.BookPageRequestDto;
import com.nhnacademy.bookstoreinjun.dto.product.ProductRegisterResponseDto;
import com.nhnacademy.bookstoreinjun.dto.book.BookRegisterRequestDto;
import com.nhnacademy.bookstoreinjun.dto.error.ErrorResponseDto;
import com.nhnacademy.bookstoreinjun.dto.product.ProductRegisterRequestDto;
import com.nhnacademy.bookstoreinjun.entity.Book;
import com.nhnacademy.bookstoreinjun.entity.ProductCategory;
import com.nhnacademy.bookstoreinjun.entity.Product;
import com.nhnacademy.bookstoreinjun.entity.ProductCategoryRelation;
import com.nhnacademy.bookstoreinjun.entity.ProductTag;
import com.nhnacademy.bookstoreinjun.entity.Tag;
import com.nhnacademy.bookstoreinjun.exception.AladinJsonProcessingException;
import com.nhnacademy.bookstoreinjun.exception.PageOutOfRangeException;
import com.nhnacademy.bookstoreinjun.feignclient.BookRegisterClient;
import com.nhnacademy.bookstoreinjun.service.aladin.AladinService;
import com.nhnacademy.bookstoreinjun.service.book.BookService;
import com.nhnacademy.bookstoreinjun.service.category.CategoryService;
import com.nhnacademy.bookstoreinjun.service.ProductCategoryService;
import com.nhnacademy.bookstoreinjun.service.ProductService;
import com.nhnacademy.bookstoreinjun.service.ProductTagService;
import com.nhnacademy.bookstoreinjun.service.tag.TagService;
import jakarta.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
//@RestController
@Controller
@RequestMapping("/api")
@RequiredArgsConstructor
public class BookController {

    private final AladinService aladinService;

    private final BookService bookService;

    private final ProductService productService;

    private final CategoryService categoryService;

    private final ProductCategoryService productCategoryService;

    private final TagService tagService;

    private final ProductTagService productTagService;

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
        AladinBookListResponseDto aladinBookListResponseDto = aladinService.getAladdinBookList(title);
        return new ResponseEntity<>(aladinBookListResponseDto, header, HttpStatus.OK);
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
    @PostMapping("/admin/book/register")
    public ResponseEntity<ProductRegisterResponseDto> saveBookProduct(@Valid @RequestBody BookProductRegisterRequestDto bookProductRegisterRequestDto){

        Product product = productService.saveProduct(getProductRegisterRequestDto(bookProductRegisterRequestDto));

        bookService.saveBook(getBookRegisterRequestDto(bookProductRegisterRequestDto, product));


        List<String> categories = bookProductRegisterRequestDto.categories();
        if (categories != null){
            for (String categoryName : categories) {
                ProductCategory productCategory = categoryService.getCategoryByName(categoryName);
                ProductCategoryRelation productCategoryRelation = ProductCategoryRelation.builder()
                        .productCategory(productCategory)
                        .product(product)
                        .build();

                productCategoryService.saveProductCategory(productCategoryRelation);
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

        return new ResponseEntity<>(dto, header, HttpStatus.CREATED);
    }

    @GetMapping("/books")
    public ResponseEntity<Page<BookProductGetResponseDto>> getAllBookPage(
           @Valid @RequestBody BookPageRequestDto bookPageRequestDto
    ){
        int page = Objects.requireNonNullElse(bookPageRequestDto.page(),1);
        int size = Objects.requireNonNullElse(bookPageRequestDto.size(),5);
        boolean desc = Objects.requireNonNullElse(bookPageRequestDto.desc(), true);
        String sort =  Objects.requireNonNullElse(bookPageRequestDto.sort(), "productProductRegisterDate");

        log.info("page {}, size {}, desc {}, sort {}", page, size, desc, sort);

        Pageable pageable = PageRequest.of(page -1, size,
                Sort.by(desc ? Sort.Direction.DESC : Sort.Direction.ASC, sort));
        Page<Book> bookPage = bookService.getBookPage(pageable);

        int total = bookPage.getTotalPages();
        if (total < page){
            throw new PageOutOfRangeException(total, page);
        }
        //state 가 0이 아닌 북은 없애야.. 반환 값에 포함 안 되게 해야..
        return new ResponseEntity<>
                (bookPage.map(
                book -> BookProductGetResponseDto.builder()
                        .title(book.getTitle())
                        .publisher(book.getPublisher())
                        .author(book.getAuthor())
                        .pubDate(book.getPubDate())
                        .isbn(book.getIsbn())
                        .isbn13(book.getIsbn13())
                        .cover(book.getProduct().getProductThumbnailUrl())
                        .packable(book.isPackable())
                        .productDescription(book.getProduct().getProductDescription())
                        .productRegisterDate(book.getProduct().getProductRegisterDate())
                        .productState(book.getProduct().getProductState())
                        .productViewCount(book.getProduct().getProductViewCount())
                        .productPriceStandard(book.getProduct().getProductPriceStandard())
                        .productPriceSales(book.getProduct().getProductPriceSales())
                        .productInventory(book.getProduct().getProductInventory())
                        .categories(categoryService.getCategoriesByProduct(book.getProduct()).stream()
                                .map(ProductCategory ::getCategoryName)
                                .collect(Collectors.toList()))
                        .tags(tagService.getTagsByProduct(book.getProduct()).stream()
                                .map(Tag::getTagName)
                                .collect(Collectors.toList()))
                        .build())

                        , header, HttpStatus.OK
        );
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
