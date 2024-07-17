package com.nhnacademy.bookstoreinjun.service.product;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import com.nhnacademy.bookstoreinjun.entity.Product;
import com.nhnacademy.bookstoreinjun.entity.ProductLike;
import com.nhnacademy.bookstoreinjun.exception.DuplicateException;
import com.nhnacademy.bookstoreinjun.exception.NotFoundIdException;
import com.nhnacademy.bookstoreinjun.exception.PageOutOfRangeException;
import com.nhnacademy.bookstoreinjun.exception.XUserIdNotFoundException;
import com.nhnacademy.bookstoreinjun.repository.ProductLikeRepository;
import com.nhnacademy.bookstoreinjun.repository.ProductRepository;
import com.nhnacademy.bookstoreinjun.repository.QuerydslRepository;
import com.nhnacademy.bookstoreinjun.util.PageableUtil;
import com.nhnacademy.bookstoreinjun.util.SortCheckUtil;
import jakarta.validation.Valid;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    private final ProductLikeRepository productLikeRepository;

    private final QuerydslRepository querydslRepository;

    private final int DEFAULT_PAGE_SIZE = 10;

    private final String DEFAULT_SORT = "productId";

    private final ObjectMapper objectMapper;

    private ProductGetResponseDto makeProductGetResponseDtoFromProduct(Product product) {
        return ProductGetResponseDto.builder()
                .productId(product.getProductId())
                .productName(product.getProductName())
                .productState(product.getProductState())
                .productPriceStandard(product.getProductPriceStandard())
                .productPriceSales(product.getProductPriceSales())
                .productThumbNailImage(product.getProductThumbnailUrl())
                .build();
    }

    private Page<ProductGetResponseDto> makeProductGetResponseDtoPage(Pageable pageable, Page<Product> productPage) {
        int total = productPage.getTotalPages();
        int maxPage = pageable.getPageNumber() + 1;

        if (total != -0 && total < maxPage){
            throw new PageOutOfRangeException(total, maxPage);
        }

        return productPage.map(this::makeProductGetResponseDtoFromProduct);
    }


    @Override
    public Page<ProductGetResponseDto> findAllPage(@Valid PageRequestDto pageRequestDto) {
        Pageable pageable = PageableUtil.makePageable(pageRequestDto, DEFAULT_PAGE_SIZE, DEFAULT_SORT);

        try {
            Page<Product> productPage = productRepository.findAll(pageable);
            return makeProductGetResponseDtoPage(pageable, productPage);
        }catch (PropertyReferenceException e) {
            throw SortCheckUtil.ThrowInvalidSortNameException(pageable);
        }
    }

    public Page<ProductGetResponseDto> findNameContainingPage(@Valid PageRequestDto pageRequestDto, String productName) {
        Pageable pageable = PageableUtil.makePageable(pageRequestDto, DEFAULT_PAGE_SIZE, DEFAULT_SORT);

        try {
            Page<Product> productPage = productRepository.findByProductNameContaining(pageable, productName);
            return makeProductGetResponseDtoPage(pageable, productPage);
        }catch (PropertyReferenceException e) {
            throw SortCheckUtil.ThrowInvalidSortNameException(pageable);
        }
    }


    @Override
    public ProductLikeResponseDto saveProductLike(Long clientIdOfHeader, ProductLikeRequestDto productLikeRequestDto){
        if (clientIdOfHeader ==- 1){
            throw new XUserIdNotFoundException();
        }

        log.info("Saving product like request: {} with id {}", productLikeRequestDto, clientIdOfHeader);

        Long productId = productLikeRequestDto.productId();
        Optional<Product> optionalProduct = productRepository.findById(productId);
        if(optionalProduct.isPresent()){
            log.info("Saving product. found product!: {}", productLikeRequestDto);
            Product product = optionalProduct.get();
            if (productLikeRepository.existsByClientIdAndProduct(clientIdOfHeader, product)){
                throw new DuplicateException("Product Like");
            }
            productLikeRepository.save(ProductLike.builder()
                    .clientId(clientIdOfHeader)
                    .product(product)
                    .build());
        }else {
            log.warn("product not found with {}", productLikeRequestDto.productId());
            throw new NotFoundIdException("product", productId);
        }
        return new ProductLikeResponseDto();
    }


    @Override
    public ProductLikeResponseDto deleteProductLike(Long clientIdOfHeader, Long productId) {
        if (clientIdOfHeader ==- 1){
            throw new XUserIdNotFoundException();
        }
        Product product = productRepository.findById(productId).orElseThrow(() -> new NotFoundIdException("product", productId));
        if (!productLikeRepository.existsByClientIdAndProduct(clientIdOfHeader, product)){
            throw new DuplicateException("Product Like");
        }else {
            ProductLike productLike = productLikeRepository.findByClientIdAndProduct(clientIdOfHeader, product);
            productLikeRepository.delete(productLike);
        }
        return new ProductLikeResponseDto();
    }


    @Override
    public ProductUpdateResponseDto updateProductState(ProductStateUpdateRequestDto productStateUpdateRequestDto) {
        long result = querydslRepository.setProductState(productStateUpdateRequestDto.productId(), productStateUpdateRequestDto.productState());
        log.info("update state called, dto : {}", productStateUpdateRequestDto);
        if (result != 1){
            throw new NotFoundIdException("product", productStateUpdateRequestDto.productId());
        }else {
            return new ProductUpdateResponseDto(LocalDateTime.now());
        }
    }


    @Override
    public List<ProductInventoryGetResponseDto> getInventoryOfProductList(Set<Long> productIdSet) {
        List<ProductInventoryGetResponseDto> result = new ArrayList<>();
        for (Long productId : productIdSet) {
            Product product = productRepository.findById(productId).orElseThrow(() -> new NotFoundIdException("product", productId));
            ProductInventoryGetResponseDto responseDto = ProductInventoryGetResponseDto.builder()
                    .productId(productId)
                    .productInventory(product.getProductInventory())
                    .build();
            result.add(responseDto);
        }
        return result;
    }

//    @Override
//    public ResponseEntity<Void> decreaseProductInventory(List<InventoryDecreaseRequestDto> inventoryDecreaseRequestDtoList){
//        try {
//            long updatedRow = querydslRepository.decreaseProductInventory(inventoryDecreaseRequestDtoList);
//            if (updatedRow != inventoryDecreaseRequestDtoList.size()){
//                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
//            }
//            return new ResponseEntity<>(HttpStatus.OK);
//        }catch (JpaSystemException e) {
//            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//        }
//    }
//
//    @Override
//    public ResponseEntity<Void> increaseProductInventory(List<InventoryIncreaseRequestDto> inventoryIncreaseRequestDtoList) {
//        try {
//            long updatedRow = querydslRepository.increaseProductInventory(inventoryIncreaseRequestDtoList);
//            if (updatedRow != inventoryIncreaseRequestDtoList.size()){
//                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
//            }
//            return new ResponseEntity<>(HttpStatus.OK);
//        }catch (JpaSystemException e) {
//            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//        }
//    }

    @RabbitListener(queues = "${rabbit.inventory.decrease.queue.name}")
    @Override
    public void decreaseProductInventoryQueue(String message){
        List<InventoryDecreaseRequestDto> inventoryDecreaseRequestDtoList;
        log.info("decrease product inventory queue: {}", message);
        try {
            inventoryDecreaseRequestDtoList = objectMapper.readValue(message, new TypeReference<List<InventoryDecreaseRequestDto>>() {});
            long updatedRow = querydslRepository.decreaseProductInventory(inventoryDecreaseRequestDtoList);
            if (updatedRow != inventoryDecreaseRequestDtoList.size()){
                log.error("decrease product inventory queue failed");
            }else {
                log.info("decrease product inventory queue success");
            }
        } catch (JsonProcessingException e) {
            log.error("decrease product inventory queue failed", e);
        } catch (JpaSystemException e) {
            log.error("decrease product inventory queue failed ", e);
        }
    }



    @RabbitListener(queues = "${rabbit.inventory.increase.queue.name}")
    @Override
    public void increaseProductInventoryQueue(String message){
        List<InventoryIncreaseRequestDto> inventoryIncreaseRequestDtoList;
        log.info("increase product inventory queue: {}", message);
        try {
            inventoryIncreaseRequestDtoList = objectMapper.readValue(message, new TypeReference<List<InventoryIncreaseRequestDto>>() {});
            long updatedRow = querydslRepository.increaseProductInventory(inventoryIncreaseRequestDtoList);
            if (updatedRow != inventoryIncreaseRequestDtoList.size()){
                log.error("increase product inventory queue failed. updatedRow: {}, size: {}, request : {}", updatedRow, inventoryIncreaseRequestDtoList.size(), inventoryIncreaseRequestDtoList);
            }else {
                log.info("increase product inventory queue success");
            }
        } catch (JsonProcessingException e) {
            log.error("increase product inventory queue failed", e);
        } catch (JpaSystemException e) {
            log.error("increase product inventory queue failed ", e);
        }
    }

    @Override
    public ResponseEntity<Void> setProductInventory(InventorySetRequestDto inventorySetRequestDto) {
        long updatedRow = querydslRepository.setProductInventory(inventorySetRequestDto);
        if (updatedRow != 1){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
