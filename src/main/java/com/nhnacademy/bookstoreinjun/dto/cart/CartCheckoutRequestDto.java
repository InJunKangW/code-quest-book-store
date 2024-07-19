package com.nhnacademy.bookstoreinjun.dto.cart;


import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;
import lombok.Builder;

@Builder
public record CartCheckoutRequestDto (
        @NotNull
        Long clientId,

        @Size(min = 1)
        List<Long> productIdList
){
}
