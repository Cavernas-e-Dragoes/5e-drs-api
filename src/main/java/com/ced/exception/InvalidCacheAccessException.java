package com.ced.exception;

public class InvalidCacheAccessException extends RuntimeException {
    public InvalidCacheAccessException(String message) {
        super(message);
    }
}
