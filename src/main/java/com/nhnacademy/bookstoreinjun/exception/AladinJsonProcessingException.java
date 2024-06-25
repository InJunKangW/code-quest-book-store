package com.nhnacademy.bookstoreinjun.exception;

public class AladinJsonProcessingException extends RuntimeException {
    public AladinJsonProcessingException(String title) {
        super("making aladin_result to json processing failed, with title" + title);
    }
}
