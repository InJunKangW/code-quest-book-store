package com.nhnacademy.bookstoreinjun.controller;


import com.nhnacademy.bookstoreinjun.dto.page.PageRequestDto;
import com.nhnacademy.bookstoreinjun.dto.product.ProductGetResponseDto;
import com.nhnacademy.bookstoreinjun.dto.product.ProductLikeRequestDto;
import com.nhnacademy.bookstoreinjun.dto.product.ProductLikeResponseDto;
import com.nhnacademy.bookstoreinjun.service.product.ProductService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang.math.NumberUtils;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/product")
public class ProductController {
    private static final String ID_HEADER = "X-User-Id";

    private final ProductService productService;

    private final HttpHeaders header = new HttpHeaders() {{
        setContentType(MediaType.APPLICATION_JSON);
    }};

    @GetMapping("/admin/page/all")
    public ResponseEntity<Page<ProductGetResponseDto>> getAllProducts(
            @Valid @ModelAttribute PageRequestDto pageRequestDto
    ) {
        return new ResponseEntity<>(productService.findAllPage(pageRequestDto), header, HttpStatus.OK);
    }

    @GetMapping("/admin/page/containing")
    public ResponseEntity<Page<ProductGetResponseDto>> getAllProductsNameContaining(
            @Valid @ModelAttribute PageRequestDto pageRequestDto,
            @NotBlank @RequestParam(name = "productName") String productName) {
        return new ResponseEntity<>(productService.findNameContainingPage(pageRequestDto, productName), header, HttpStatus.OK);
    }

    @PostMapping("/client/like")
    public ResponseEntity<ProductLikeResponseDto> saveBookProductLike(
            @RequestHeader HttpHeaders httpHeaders,
            @RequestBody @Valid ProductLikeRequestDto productLikeRequestDto
    ){
        return new ResponseEntity<>(productService.saveProductLike(NumberUtils.toLong(httpHeaders.getFirst(ID_HEADER), -1L), productLikeRequestDto), header, HttpStatus.OK);
    }
}
