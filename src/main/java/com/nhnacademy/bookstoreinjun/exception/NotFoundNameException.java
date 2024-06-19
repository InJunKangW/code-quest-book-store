package com.nhnacademy.bookstoreinjun.exception;

public class NotFoundNameException extends RuntimeException {
    public NotFoundNameException(String type, String name) {
        super("Could not find " + type + " with id " + name);
    }
}
