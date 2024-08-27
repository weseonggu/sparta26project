package com.sparta26.baemin.exception.exceptionsdefined;

public class MemberStoreLimitExceededException extends RuntimeException {
    public MemberStoreLimitExceededException(String message) {
        super(message);
    }
}
