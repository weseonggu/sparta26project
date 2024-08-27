package com.sparta26.baemin.exception;

import com.sparta26.baemin.common.util.CurrentTime;
import com.sparta26.baemin.dto.error.ErrorResponse;
import com.sparta26.baemin.dto.response.FailMessage;
import com.sparta26.baemin.exception.exceptionsdefined.*;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    private final CurrentTime rTime;


    /**
     * 유효성 검증 실패시 예외처리
     * @param ex
     * @param headers
     * @param status
     * @param request
     * @return
     */
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers, HttpStatusCode status, WebRequest request) {

        BindingResult bindingResult = ex.getBindingResult();
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();

        List<String> errorMessages = new ArrayList<>();
        for (FieldError fieldError : fieldErrors) {
            errorMessages.add(fieldError.getDefaultMessage());
        }

        FailMessage message = new FailMessage(rTime.getTime(), request.getDescription(false), errorMessages);
        return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(DuplicateKeyException.class)
    public final ResponseEntity<FailMessage> handeleUsernameNotFoundException(Exception ex, WebRequest request) throws Exception{
        FailMessage message = new FailMessage(rTime.getTime(), request.getDescription(false), List.of(ex.getMessage()));
        return new ResponseEntity<FailMessage>(message,HttpStatus.BAD_REQUEST);
    }

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

    @ExceptionHandler(ForbiddenAccessException.class)
    public ResponseEntity<ErrorResponse> handlerSessionNotFound(ForbiddenAccessException ex){
        String stackTrace = getStackTraceAsString(ex);
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.FORBIDDEN.value(), "Unauthorized user", ex.getMessage(),stackTrace);
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
    }

    @ExceptionHandler(AiNotFoundException.class)
    public ResponseEntity<ErrorResponse> handlerSessionNotFound(AiNotFoundException ex){
        String stackTrace = getStackTraceAsString(ex);
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), "Not Found", ex.getMessage(),stackTrace);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }
}
