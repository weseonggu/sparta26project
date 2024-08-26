package com.sparta26.baemin.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

/**
 * Member관련 로그 작성하는 곳
 */
@Aspect
@Component
@Slf4j(topic = "MemberTopic")
public class LoggingAspectOfJoinMember {
    /**
     * 회원 가입시 회가입 정보 로깅
     * @param joinPoint
     * @return
     * @throws Throwable
     */
    @Around("execution(* com.sparta26.baemin.member.service.MemberService.createMember(..))")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
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
            // 회원가입 성공 로그
            log.info("Method {} in class {} executed successfully with return value: {}",
                    methodName, className, result);
        } catch (Exception ex) {
            // 회원가입 실패 로그
            log.warn("Method {} in class {} threw an exception: {}",
                    methodName, className, ex.getMessage());
            // 예외를 다시 던져 전역 예외 처리기로 넘김
            throw ex;
        }
        return result;
    }
}
