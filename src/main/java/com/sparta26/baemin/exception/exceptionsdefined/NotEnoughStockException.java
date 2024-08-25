package com.sparta26.baemin.exception.exceptionsdefined;

/**
 * stock 이상의 수를 감소 할때 발생하는 Exception
 */
public class NotEnoughStockException extends RuntimeException {

    public NotEnoughStockException(String message) {
        super(message);
    }
}
