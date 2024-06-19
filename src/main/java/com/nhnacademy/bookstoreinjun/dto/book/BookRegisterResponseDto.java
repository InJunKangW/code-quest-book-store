package com.nhnacademy.bookstoreinjun.dto.book;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BookRegisterResponseDto {
    Long id;
    LocalDateTime registerTime;
}
