package com.nhnacademy.bookstoreinjun.dto.book;

import java.time.LocalDateTime;

//@Data
//@Setter
//@AllArgsConstructor
//@NoArgsConstructor
//public class BookProductRegisterResponseDto {
//    Long id;
//    LocalDateTime registerTime;
//}


public record BookProductRegisterResponseDto(
        Long id,
        LocalDateTime registerTime
){}