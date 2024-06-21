package com.nhnacademy.bookstoreinjun.exception;

public class InvalidSortNameException extends RuntimeException {
    public InvalidSortNameException(String sortName) {
        super("Invalid sort name : " + sortName);
    }
}
