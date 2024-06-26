package com.nhnacademy.bookstoreinjun.exception;

public class XUserIdNotFoundException extends RuntimeException {
    public XUserIdNotFoundException() {
        super("X_USER_ID Not Found on Request Header");
    }
}
