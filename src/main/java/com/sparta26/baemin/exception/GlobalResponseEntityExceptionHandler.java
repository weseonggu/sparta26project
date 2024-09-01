package com.sparta26.baemin.exception;

import com.sparta26.baemin.common.util.CurrentTime;
import com.sparta26.baemin.dto.error.ErrorResponse;
import com.sparta26.baemin.dto.response.FailMessage;
import com.sparta26.baemin.exception.exceptionsdefined.*;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import io.lettuce.core.RedisCommandTimeoutException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
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
    /**
     * 레디스 응답이 없어 시간 초과시
     * @param ex
     * @param request
     * @return
     * @throws Exception
     */
    @ExceptionHandler(RedisCommandTimeoutException.class)
    public final ResponseEntity<FailMessage> handeleRedisCommandTimeoutException(Exception ex, WebRequest request) throws Exception{
        FailMessage message = new FailMessage(rTime.getTime(), request.getDescription(false), List.of("잠시후 다시 시도해주세요"));
        return new ResponseEntity<FailMessage>(message,HttpStatus.BAD_REQUEST);
    }

    /**
     * 타인의 데이터에 접근시
     * @param ex
     * @param request
     * @return
     * @throws Exception
     */
    @ExceptionHandler(NoAccessToOtherPeopleData.class)
    public final ResponseEntity<FailMessage> handeleNoAccessToOtherPeopleDataException(Exception ex, WebRequest request) throws Exception{
        FailMessage message = new FailMessage(rTime.getTime(), request.getDescription(false), List.of(ex.getMessage()));
        return new ResponseEntity<FailMessage>(message,HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(AlreadyDeletedException.class)
    public final ResponseEntity<FailMessage> handeleAlreadyDeletedExceptionException(Exception ex, WebRequest request) throws Exception{
        FailMessage message = new FailMessage(rTime.getTime(), request.getDescription(false), List.of(ex.getMessage()));
        return new ResponseEntity<FailMessage>(message,HttpStatus.BAD_REQUEST);
    }

    /**
     * 이미 중복이 불가한 데이터를 저장 할 때 웅답
     * @param ex
     * @param request
     * @return
     * @throws Exception
     */
    @ExceptionHandler(DuplicateKeyException.class)
    public final ResponseEntity<FailMessage> handeleUsernameDuplicateKeyException(Exception ex, WebRequest request) throws Exception{
        FailMessage message = new FailMessage(rTime.getTime(), request.getDescription(false), List.of(ex.getMessage()));
        return new ResponseEntity<FailMessage>(message,HttpStatus.BAD_REQUEST);
    }

    /**
     * 로그인 실패 응답
     * @param ex
     * @param request
     * @return
     * @throws Exception
     */
    @ExceptionHandler(LoginFailException.class)
    public final ResponseEntity<FailMessage> handeleLoginFailException(Exception ex, WebRequest request) throws Exception{
        FailMessage message = new FailMessage(rTime.getTime(), request.getDescription(false), List.of(ex.getMessage()));
        return new ResponseEntity<FailMessage>(message,HttpStatus.UNAUTHORIZED);
    }

    /**
     * 인가 실패:  Invalid JWT signature, 유효하지 않는 JWT 서명 입니다.
     * @param ex
     * @param request
     * @return
     * @throws Exception
     */
    @ExceptionHandler({SecurityException.class, MalformedJwtException.class, SignatureException.class})
    public final ResponseEntity<FailMessage> handeleAuthenticationFailedException(Exception ex, WebRequest request) throws Exception{
        FailMessage message = new FailMessage(rTime.getTime(), request.getDescription(false), List.of(ex.getMessage()));
        return new ResponseEntity<FailMessage>(message,HttpStatus.UNAUTHORIZED);
    }

    /**
     * 만료된 토큰
     * @param ex
     * @param request
     * @return
     * @throws Exception
     */
    @ExceptionHandler(ExpiredJwtException.class)
    public final ResponseEntity<FailMessage> handeleExpiredJwtException(Exception ex, WebRequest request) throws Exception{
        List<String> list = new ArrayList<>();
        list.add(ex.getMessage());
        list.add("54826");// 토큰이 만료 되었다는 의미
        FailMessage message = new FailMessage(rTime.getTime(), request.getDescription(false), list);
        return new ResponseEntity<FailMessage>(message,HttpStatus.UNAUTHORIZED);
    }

    /**
     * Unsupported JWT token, 지원되지 않는 JWT 토큰 입니다.
     * @param ex
     * @param request
     * @return
     * @throws Exception
     */
    @ExceptionHandler(UnsupportedJwtException.class)
    public final ResponseEntity<FailMessage> handeleUnsupportedJwtException(Exception ex, WebRequest request) throws Exception{
        FailMessage message = new FailMessage(rTime.getTime(), request.getDescription(false), List.of(ex.getMessage()));
        return new ResponseEntity<FailMessage>(message,HttpStatus.UNAUTHORIZED);
    }

    /**
     * IllegalArgumentException 예외 처리
     * @param ex
     * @param request
     * @return
     * @throws Exception
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public final ResponseEntity<FailMessage> handeleIllegalArgumentException(Exception ex, WebRequest request) throws Exception{
        FailMessage message = new FailMessage(rTime.getTime(), request.getDescription(false), List.of(ex.getMessage()));
        return new ResponseEntity<FailMessage>(message,HttpStatus.BAD_REQUEST);
    }

    private String getStackTraceAsString(Throwable throwable) {
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        throwable.printStackTrace(printWriter);
        return stringWriter.toString();
    }

    @ExceptionHandler(ForbiddenAccessException.class)
    public ResponseEntity<ErrorResponse> handlerForbidden(ForbiddenAccessException ex){
        String stackTrace = getStackTraceAsString(ex);
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.FORBIDDEN.value(), "Unauthorized user", ex.getMessage(),stackTrace);
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
    }

    @ExceptionHandler({
            BadRequestException.class,
            MemberStoreLimitExceededException.class,
            UuidFormatException.class,
            ClientException.class
    })
    public ResponseEntity<ErrorResponse> handlerBadRequest(Exception ex){
        String stackTrace = getStackTraceAsString(ex);
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), "Bad Request", ex.getMessage(),stackTrace);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler({
            NotFoundException.class,
            ProductNotFoundException.class,
            AiNotFoundException.class,
            MemberNotFoundException.class,
            StoreNotFoundException.class
    })
    public ResponseEntity<ErrorResponse> handlerNotFound(Exception ex){
        String stackTrace = getStackTraceAsString(ex);
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.NOT_FOUND.value(), "Not Found", ex.getMessage(),stackTrace);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ErrorResponse> handlerUnauthorized(Exception ex){
        String stackTrace = getStackTraceAsString(ex);
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.UNAUTHORIZED.value(), "Unauthorized", ex.getMessage(),stackTrace);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
    }

    @ExceptionHandler(DeliveryZoneNotFoundException.class)
    public ResponseEntity<ErrorResponse> handlerSessionNotFound(DeliveryZoneNotFoundException ex){
        String stackTrace = getStackTraceAsString(ex);
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), "Not Found", ex.getMessage(),stackTrace);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(CategoryNotFoundException.class)
    public ResponseEntity<ErrorResponse> handlerSessionNotFound(CategoryNotFoundException ex){
        String stackTrace = getStackTraceAsString(ex);
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), "Not Found", ex.getMessage(),stackTrace);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

}
