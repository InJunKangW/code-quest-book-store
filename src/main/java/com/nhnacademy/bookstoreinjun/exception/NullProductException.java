package com.nhnacademy.bookstoreinjun.exception;

public class NullProductException extends RuntimeException {
    public NullProductException() {
        super("Product is null");
    }
}
