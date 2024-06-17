package com.nhnacademy.bookstoreinjun.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
    private Long id;

    @Column(nullable = false)
    @Builder.Default
    @ColumnDefault("'신규 상품'")
    private String productName = "신규 상품";
//근데 디폴트 값이 있을 일이 있나..? 에러 날 때말곤 없을 것 같은데.

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

    @Builder.Default
    @Column(nullable = false)
    @ColumnDefault("'상품입니다.'")
    private String productDescription = "상품입니다.";

    public void addViewCount(){
        this.productViewCount++;
    }
}
