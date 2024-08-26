package com.sparta26.baemin.exception;

import com.sparta26.baemin.dto.error.ErrorResponse;
import com.sparta26.baemin.exception.exceptionsdefined.MemberNotFoundException;
import com.sparta26.baemin.exception.exceptionsdefined.MemberStoreLimitExceededException;
import com.sparta26.baemin.exception.exceptionsdefined.StoreNotFoundException;
import com.sparta26.baemin.exception.exceptionsdefined.UuidFormatException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.io.PrintWriter;
import java.io.StringWriter;

@RestControllerAdvice
public class GlobalResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    private String getStackTraceAsString(Throwable throwable) {
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        throwable.printStackTrace(printWriter);
        return stringWriter.toString();
    }

    @ExceptionHandler(MemberNotFoundException.class)
    public ResponseEntity<ErrorResponse> handlerSessionNotFound(MemberNotFoundException ex){
        String stackTrace = getStackTraceAsString(ex);
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), "Not Found", ex.getMessage(),stackTrace);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(MemberStoreLimitExceededException.class)
    public ResponseEntity<ErrorResponse> handlerSessionNotFound(MemberStoreLimitExceededException ex){
        String stackTrace = getStackTraceAsString(ex);
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), "Not Found", ex.getMessage(),stackTrace);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(StoreNotFoundException.class)
    public ResponseEntity<ErrorResponse> handlerSessionNotFound(StoreNotFoundException ex){
        String stackTrace = getStackTraceAsString(ex);
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), "Not Found", ex.getMessage(),stackTrace);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(UuidFormatException.class)
    public ResponseEntity<ErrorResponse> handlerSessionNotFound(UuidFormatException ex){
        String stackTrace = getStackTraceAsString(ex);
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), "Not Found", ex.getMessage(),stackTrace);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }


}
