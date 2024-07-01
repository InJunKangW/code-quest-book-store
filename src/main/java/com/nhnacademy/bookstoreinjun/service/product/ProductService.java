package com.nhnacademy.bookstoreinjun.service.product;

import com.nhnacademy.bookstoreinjun.dto.page.PageRequestDto;
import com.nhnacademy.bookstoreinjun.dto.product.ProductGetResponseDto;
import com.nhnacademy.bookstoreinjun.dto.product.ProductLikeRequestDto;
import com.nhnacademy.bookstoreinjun.dto.product.ProductLikeResponseDto;
import org.springframework.data.domain.Page;

public interface ProductService {
    /**
     * 모든 상품의 Dto 페이지를 반환합니다.
     * Dto 는 다음 필드를 갖습니다.
     *         Long productId  - 상품의 아이디
     *         String productName - 상품명
     *         int productState - 상품의 상태 (판매 중 / 판매 중지 등)
     *         long productPriceStandard - 상품의 정가
     *         long productPriceSales - 상품의 판매가
     *         String productThumbNailImage - 상품의 대표 이미지
     * @param pageRequestDto 페이징 요청 (int page (페이지 넘버), int size (페이지 당 사이즈), String sort (정렬할 조건), boolean desc (오름차순/내림차순 여부))
     * @return 요청에 따른 상품 Dto 의 페이지
     */
    Page<ProductGetResponseDto> findAllPage(PageRequestDto pageRequestDto);

    /**
     * 특정 상품 명을 포함하는 상품의 Dto 페이지를 반환합니다.
     * @param pageRequestDto 페이징 요청 (int page (페이지 넘버), int size (페이지 당 사이즈), String sort (정렬할 조건), boolean desc (오름차순/내림차순 여부))
     * @param productName 포함되어야 할 상품명
     * @return 요청에 따른 상품 Dto 의 페이지
     */
    Page<ProductGetResponseDto> findNameContainingPage(PageRequestDto pageRequestDto, String productName);

    /**
     * 상품의 좋아요를 남깁니다.
     * @param clientId 좋아요를 누른 고객의 아이디
     * @param productLikeRequestDto 좋아요의 요청. 다음 필드를 갖습니다.
     *          Long clientId  - @NotNull @Min(1) - 혹여나 요청을 누른 유저 아이디와 일치하지 않을 경우를 방지하기 위해 확인합니다.
     *          Long productId - @NotNull @Min(1) - 고객이 좋아요를 누른 상품의 아이디
     * @return 좋아요를 누른 기록. 아직은 아무 필드가 없습니다. - Void 타입으로 반환할까 하다가 추후 필요한 필드가 생길 경우를 대비해 일단 빈 Dto 를 반환하기로 했습니다
     */
    ProductLikeResponseDto saveProductLike(Long clientId, ProductLikeRequestDto productLikeRequestDto);
}
