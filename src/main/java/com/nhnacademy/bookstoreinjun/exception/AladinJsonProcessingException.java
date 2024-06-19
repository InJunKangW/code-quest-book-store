package com.nhnacademy.bookstoreinjun.exception;

public class AladinJsonProcessingException extends RuntimeException {
    public AladinJsonProcessingException(String title) {
        super("json processing failed, with title" + title);
    }
}
