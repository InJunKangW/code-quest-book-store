package com.nhnacademy.bookstoreinjun.exception;

public class NotFoundIdException extends RuntimeException {
    public NotFoundIdException(String type, Long id) {
        super("Could not find " + type + " with id " + id);
    }
}
