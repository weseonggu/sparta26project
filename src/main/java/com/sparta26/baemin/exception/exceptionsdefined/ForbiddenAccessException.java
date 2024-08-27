package com.sparta26.baemin.exception.exceptionsdefined;

public class ForbiddenAccessException extends RuntimeException {
    public ForbiddenAccessException(String message) {
        super(message);
    }
}
