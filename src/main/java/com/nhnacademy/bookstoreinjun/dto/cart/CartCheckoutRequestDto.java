package com.nhnacademy.bookstoreinjun.dto.cart;


import jakarta.validation.constraints.Size;
import java.util.List;

public record CartCheckoutRequestDto (
        @Size(min = 1)
        List<Long> productIdList
){
}
