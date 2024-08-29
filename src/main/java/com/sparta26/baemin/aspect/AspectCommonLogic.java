package com.sparta26.baemin.aspect;

import com.sparta26.baemin.jwt.JWTUtil;
import com.sparta26.baemin.member.service.MemberCacheService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.stereotype.Service;

@Service
@Slf4j(topic = "MemberTopic")
public class AspectCommonLogic {

    private final JWTUtil jwtUtil;
    private final MemberCacheService memberCacheService;

    public AspectCommonLogic(JWTUtil jwtUtil, MemberCacheService memberCacheService) {
        this.jwtUtil = jwtUtil;
        this.memberCacheService = memberCacheService;
    }

    /**
     * 공통 메소드
     * @param joinPoint
     * @return
     * @throws Throwable
     */
    public Object ChangeRedundantMethods(ProceedingJoinPoint joinPoint)throws Throwable{
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getTarget().getClass().getSimpleName();
        Object[] args = joinPoint.getArgs();
        // 회원 가입저장 전 저장할 데이터
        log.info("Method {} in class {} is about to be executed with arguments: {}",
                methodName, className, args);
        Object result;
        try {
            // 메소드 실행
            result = joinPoint.proceed();
            // 성공 로그
            log.info("Method {} in class {} executed successfully with return value: {}",
                    methodName, className, result);
        } catch (Exception ex) {
            // 실패 로그
            log.warn("Method {} in class {} threw an exception: {}",
                    methodName, className, ex.getMessage());
            // 예외를 다시 던져 전역 예외 처리기로 넘김
            throw ex;
        }
        return result;
    }

    /**
     * 응답 전 토큰검사
     * @param joinPoint
     * @return
     * @throws Throwable
     */
    public Object ResponseRedundantMethods(ProceedingJoinPoint joinPoint)throws Throwable{
        Object[] args = joinPoint.getArgs();


        Object result;
        try {
            // 메소드 실행
            result = joinPoint.proceed();
            // 토큰 검사
            HttpServletRequest request = null;
            for (Object arg : args) {
                if (arg instanceof HttpServletRequest) {
                    request = (HttpServletRequest) arg;
                    break;
                }
            }
            if (request != null) {
                String token = request.getHeader("Authorization");

                // 토큰 유효성 검증 로직
                isValidToken(token);
                log.info("응답 전 토큰 검사 완료");

            }

        } catch (Exception ex) {
            // 실패 로그
            log.info("토큰이 만료되었습니다.");
            // 예외를 다시 던져 전역 예외 처리기로 넘김
            throw ex;
        }
        return result;
    }
    private void isValidToken(String token) throws Exception{
        String tokenValue = jwtUtil.substringToken(token);
        jwtUtil.validateToken(tokenValue);
    }
}
