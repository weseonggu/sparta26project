package com.sparta26.baemin.exception.exceptionsdefined;

public class BadRequestException extends RuntimeException{

    public BadRequestException(String message) {
        super(message);
    }
}
