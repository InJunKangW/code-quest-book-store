package com.nhnacademy.bookstoreinjun.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productId;

    @Column(nullable = false)
    @Builder.Default
    @ColumnDefault("'신규 상품'")
    private String productName = "신규 상품";
//근데 디폴트 값이 있을 일이 있나..? 에러 날 때말곤 없을 것 같은데. 기본은 도서 이름으로 하되 readonly 빼면 될 듯..

    @Builder.Default
    @Column(nullable = false)
    @ColumnDefault("999999999")
    private long productPriceStandard = 999999999;

    @Builder.Default
    @Column(nullable = false)
    @ColumnDefault("999999999")
    private long productPriceSales = 999999999;

    @Builder.Default
    @Column(nullable = false)
    @ColumnDefault("0")
    private long productViewCount = 0;

    @Builder.Default
    @Column(nullable = false)
    @ColumnDefault("100")
    private long productInventory = 100;

    @Builder.Default
    @Column(nullable = false)
    @ColumnDefault("'https://i.postimg.cc/fbT2n5jH/Pngtree-man-face-6836758.png'")
    private String productThumbnailUrl = "https://i.postimg.cc/fbT2n5jH/Pngtree-man-face-6836758.png";
    //이미지 없음 이라는 이미지로 바꾸기. 지금 아주 간단한 파일 써놨음.
    //디비 자체에는 No image 를 적고 front 에서 저 파일을 띄우는 게 맞을지도.

    @Builder.Default
    @Column(nullable = false)
    @ColumnDefault("'상품입니다.'")
    private String productDescription = "상품입니다.";

    //조회수랑 얘 둘은 굳이.. 어드민이 입력하게 해야할까?
    @Builder.Default
    @Column(nullable = false)
    @ColumnDefault("CURRENT_TIMESTAMP")
    private LocalDateTime productRegisterDate = LocalDateTime.now();


    public void addViewCount(){
        this.productViewCount++;
    }
}
