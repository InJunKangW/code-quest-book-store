package com.nhnacademy.bookstoreinjun.exception;

public class DuplicateException extends RuntimeException {
    public DuplicateException(String type) {
        super("Duplicate " + type);
    }
}
