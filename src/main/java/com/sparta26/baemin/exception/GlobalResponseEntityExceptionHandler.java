package com.sparta26.baemin.exception;

import com.sparta26.baemin.common.util.CurrentTime;
import com.sparta26.baemin.dto.response.FailMessage;
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

import java.sql.SQLException;
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
}
