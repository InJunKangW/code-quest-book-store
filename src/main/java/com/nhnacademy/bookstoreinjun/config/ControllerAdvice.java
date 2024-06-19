package com.nhnacademy.bookstoreinjun.config;


import com.nhnacademy.bookstoreinjun.dto.error.ErrorResponseDto;
import com.nhnacademy.bookstoreinjun.exception.DuplicateException;
import com.nhnacademy.bookstoreinjun.exception.NotFoundIdException;
import com.nhnacademy.bookstoreinjun.exception.NotFoundNameException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@org.springframework.web.bind.annotation.ControllerAdvice
public class ControllerAdvice {

    @ExceptionHandler(DuplicateException.class)
    public ResponseEntity<ErrorResponseDto> duplicateExceptionHandler(DuplicateException ex) {
        ErrorResponseDto dto = new ErrorResponseDto(ex.getMessage());
        log.error(dto.message());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(dto);
    }

    @ExceptionHandler({NotFoundNameException.class, NotFoundIdException.class})
    public ResponseEntity<ErrorResponseDto> notFoundNameExceptionHandler(Exception ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponseDto(ex.getMessage()));
    }
}
