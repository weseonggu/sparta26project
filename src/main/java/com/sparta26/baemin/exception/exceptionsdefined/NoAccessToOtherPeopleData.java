package com.sparta26.baemin.exception.exceptionsdefined;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_GATEWAY)
public class NoAccessToOtherPeopleData extends RuntimeException{

    public NoAccessToOtherPeopleData(String msg) {
        super(msg);
    }

}
