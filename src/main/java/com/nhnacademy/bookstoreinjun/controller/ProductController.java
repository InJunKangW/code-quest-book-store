package com.nhnacademy.bookstoreinjun.controller;


import com.nhnacademy.bookstoreinjun.dto.page.PageRequestDto;
import com.nhnacademy.bookstoreinjun.dto.product.ProductGetResponseDto;
import com.nhnacademy.bookstoreinjun.service.product.ProductDtoService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/product")
public class ProductController {
    private final ProductDtoService productDtoService;

    private final HttpHeaders header = new HttpHeaders() {{
        setContentType(MediaType.APPLICATION_JSON);
    }};

    @GetMapping("/page/all")
    public ResponseEntity<Page<ProductGetResponseDto>> getAllProducts(
            @Valid @ModelAttribute PageRequestDto pageRequestDto

//            @RequestParam(name = "page", required = false) Integer page,
//            @RequestParam(name = "desc", required = false) Boolean desc,
//            @RequestParam(name = "sort", required = false) String sort
    ) {
//        PageRequestDto pageRequestDto = PageRequestDto.builder().page(page).desc(desc).sort(sort).build();
        return new ResponseEntity<>(productDtoService.findAllPage(pageRequestDto), header, HttpStatus.OK);
    }

    @GetMapping("/page/containing")
    public ResponseEntity<Page<ProductGetResponseDto>> getAllProductsNameContaining(
            @Valid @ModelAttribute PageRequestDto pageRequestDto,
//            @RequestParam(name = "page", required = false) Integer page,
//            @RequestParam(name = "desc", required = false) Boolean desc,
//            @RequestParam(name = "sort", required = false) String sort,
            @NotBlank @RequestParam(name = "productName") String productName) {
//        PageRequestDto pageRequestDto = PageRequestDto.builder().page(page).desc(desc).sort(sort).build();
        return new ResponseEntity<>(productDtoService.findNameContainingPage(pageRequestDto, productName), header, HttpStatus.OK);
    }
}
