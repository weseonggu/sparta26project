package com.sparta26.baemin.exception.exceptionsdefined;

public class ProductNotFoundException extends RuntimeException {
    public ProductNotFoundException(String message) {
        super(message);
    }
}
