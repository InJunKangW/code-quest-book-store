package com.nhnacademy.bookstoreinjun.exception;

public class InconsistentEntityException extends RuntimeException {
    public InconsistentEntityException(String entityType) {
        super(String.format("Entity of type %s is not consistent", entityType));
    }
}
