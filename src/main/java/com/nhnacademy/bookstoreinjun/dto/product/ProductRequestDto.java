package com.nhnacademy.bookstoreinjun.dto.product;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Builder
@Getter
@Setter
public class ProductRequestDto {
    //조회수랑 상품 등록 시간은 알아서 디폴트로 들어가게..

    @NotBlank
    @Builder.Default
    private String productName = "제목 없음";

    @Min(0)
    @Builder.Default
    private long productPriceStandard = 999999999;

    @Min(0)
    @Builder.Default
    private long productPriceSales = 999999999;

    @Min(0)
    @Builder.Default
    private long productInventory = 100;

    @Builder.Default
    private String productThumbnailUrl = "no image";

    @Builder.Default
    private String productDescription = "상품입니다.";

}
