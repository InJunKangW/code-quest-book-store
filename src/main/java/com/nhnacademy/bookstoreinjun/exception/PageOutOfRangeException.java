package com.nhnacademy.bookstoreinjun.exception;

public class PageOutOfRangeException extends RuntimeException {
    public PageOutOfRangeException(int limitPageNum, int requestPageNum) {
        super(String.format("Page out of range [limit : %d, requested : %d]", limitPageNum, requestPageNum));
    }
}
