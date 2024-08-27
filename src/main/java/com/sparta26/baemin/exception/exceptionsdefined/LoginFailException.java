package com.sparta26.baemin.exception.exceptionsdefined;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.UNAUTHORIZED)
public class LoginFailException extends BadCredentialsException {
    public LoginFailException(String msg) {
        super(msg);
    }
}
