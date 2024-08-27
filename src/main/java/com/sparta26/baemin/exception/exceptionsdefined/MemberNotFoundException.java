package com.sparta26.baemin.exception.exceptionsdefined;

/**
 * 회원 없을 때 예외
 */
public class MemberNotFoundException extends RuntimeException {
    public MemberNotFoundException(String message) {
        super(message);
    }
}
