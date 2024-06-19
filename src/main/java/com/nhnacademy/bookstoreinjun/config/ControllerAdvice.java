package com.nhnacademy.bookstoreinjun.config;


import com.nhnacademy.bookstoreinjun.dto.error.ErrorResponseDto;
import com.nhnacademy.bookstoreinjun.exception.DuplicateIdException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

@org.springframework.web.bind.annotation.ControllerAdvice
public class ControllerAdvice {

    @ExceptionHandler(DuplicateIdException.class)
    public ResponseEntity<ErrorResponseDto> exceptionHandler(DuplicateIdException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorResponseDto(ex.getMessage()));
    }
}
