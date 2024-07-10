package com.nhnacademy.bookstoreinjun.controller;


import com.nhnacademy.bookstoreinjun.dto.page.PageRequestDto;
import com.nhnacademy.bookstoreinjun.dto.product.InventoryDecreaseRequestDto;
import com.nhnacademy.bookstoreinjun.dto.product.InventoryIncreaseRequestDto;
import com.nhnacademy.bookstoreinjun.dto.product.InventorySetRequestDto;
import com.nhnacademy.bookstoreinjun.dto.product.ProductGetResponseDto;
import com.nhnacademy.bookstoreinjun.dto.product.ProductInventoryGetResponseDto;
import com.nhnacademy.bookstoreinjun.dto.product.ProductLikeRequestDto;
import com.nhnacademy.bookstoreinjun.dto.product.ProductLikeResponseDto;
import com.nhnacademy.bookstoreinjun.dto.product.ProductStateUpdateRequestDto;
import com.nhnacademy.bookstoreinjun.dto.product.ProductUpdateResponseDto;
import com.nhnacademy.bookstoreinjun.service.product.ProductService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang.math.NumberUtils;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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

    @GetMapping("/admin/getInventory")
    public ResponseEntity<List<ProductInventoryGetResponseDto>> getInventoryOfProductList(
            @Size(min = 1) @RequestParam(name = "productId") Set<Long> productIdSet) {
        return new ResponseEntity<>(productService.getInventoryOfProductList(productIdSet), header, HttpStatus.OK);
    }

    @PutMapping("/admin/update/state")
    public ResponseEntity<ProductUpdateResponseDto> updateState(
            @RequestBody @Valid ProductStateUpdateRequestDto productStateUpdateRequestDto){
        return new ResponseEntity<>(productService.updateProductState(productStateUpdateRequestDto), header, HttpStatus.OK);
    }

    @PostMapping("/client/like")
    public ResponseEntity<ProductLikeResponseDto> saveBookProductLike(
            @RequestHeader HttpHeaders httpHeaders,
            @RequestBody @Valid ProductLikeRequestDto productLikeRequestDto
    ){
        return new ResponseEntity<>(productService.saveProductLike(NumberUtils.toLong(httpHeaders.getFirst(ID_HEADER), -1L), productLikeRequestDto), header, HttpStatus.CREATED);
    }

    @DeleteMapping("/client/unlike")
    public ResponseEntity<ProductLikeResponseDto> deleteBookProductLike(
            @RequestHeader HttpHeaders httpHeaders,
            @RequestParam("productId") Long productId
    ){
        return new ResponseEntity<>(productService.deleteProductLike(NumberUtils.toLong(httpHeaders.getFirst(ID_HEADER), -1L), productId), header, HttpStatus.OK);
    }


    @PutMapping("/inventory/decrease")
    public ResponseEntity<Void> decreaseProductInventory(
            @RequestBody @Valid List<InventoryDecreaseRequestDto> inventoryDecreaseRequestDtoList
    ){
        return productService.decreaseInventoryOfProductList(inventoryDecreaseRequestDtoList);
    }

    @PutMapping("/inventory/increase")
    public ResponseEntity<Void> increaseProductInventory(
            @RequestBody @Valid List<InventoryIncreaseRequestDto> inventoryIncreaseRequestDtoList
    ){
        return productService.increaseProductInventory(inventoryIncreaseRequestDtoList);
    }

    @PutMapping("/admin/inventory/set")
    public ResponseEntity<Void> setProductInventory(
            @RequestBody @Valid InventorySetRequestDto inventorySetRequestDtoList
    ){
        return productService.setProductInventory(inventorySetRequestDtoList);
    }
}
