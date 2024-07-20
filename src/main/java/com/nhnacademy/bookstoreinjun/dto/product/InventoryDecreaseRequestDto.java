package com.nhnacademy.bookstoreinjun.dto.product;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.util.Map;
import lombok.Builder;

@Builder
public record InventoryDecreaseRequestDto (
        @NotNull
        @Min(1)
        Long orderId,
        Map<Long, Long> decreaseInfo
){
}
