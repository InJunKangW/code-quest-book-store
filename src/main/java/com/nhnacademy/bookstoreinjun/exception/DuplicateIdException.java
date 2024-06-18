package com.nhnacademy.bookstoreinjun.exception;

public class DuplicateIdException extends RuntimeException {
    public DuplicateIdException(String type) {
        super("Duplicate " + type);
    }
}
