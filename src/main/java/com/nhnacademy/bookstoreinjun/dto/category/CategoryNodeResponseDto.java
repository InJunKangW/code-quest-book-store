package com.nhnacademy.bookstoreinjun.dto.category;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"productCategoryId"})
public class CategoryNodeResponseDto implements Comparable<CategoryNodeResponseDto> {
    private Long productCategoryId;
    private Integer nodeLevel;
    private String categoryName;
    private List<CategoryNodeResponseDto> children;

    @Override
    public int compareTo(CategoryNodeResponseDto o) {
        return this.productCategoryId.compareTo(o.productCategoryId);
    }
}
