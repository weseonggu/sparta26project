package com.sparta26.baemin.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

/**
 * 가게 관련 로그 작성하는 곳
 */
@Aspect
@Component
@Slf4j(topic = "StoreTopic")
public class LoggingAspectOfJoinStore {

    // 두 패키지의 모든 클래스와 메서드에 적용될 포인트컷 정의
    @Pointcut("execution(* com.sparta26.baemin.store.service..*(..)) || execution(* com.sparta26.baemin.store.controller..*(..))")
    public void applicationPackagePointcut() {
    }

    // 메서드 실행 전 로그 기록
    @Before("applicationPackagePointcut()")
    public void logBefore(ProceedingJoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getSignature().getDeclaringTypeName();
        log.info("메서드 실행 전 로그 - 클래스명: {}, 메서드명: {}", className, methodName);
    }

    // 메서드 실행 후 로그 기록
    @After("applicationPackagePointcut()")
    public void logAfter(ProceedingJoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getSignature().getDeclaringTypeName();
        log.info("메서드 실행 후 로그 - 클래스명: {}, 메서드명: {}", className, methodName);
    }

    // 메서드 실행 전후로 로그 기록 (메서드 실행 시간 측정)
    @Around("applicationPackagePointcut()")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getSignature().getDeclaringTypeName();

        long startTime = System.currentTimeMillis();
        log.info("메서드 실행 시작 - 클래스명: {}, 메서드명: {}", className, methodName);

        Object result = joinPoint.proceed(); // 실제 메서드 실행

        long elapsedTime = System.currentTimeMillis() - startTime;
        log.info("메서드 실행 종료 - 클래스명: {}, 메서드명: {}, 실행 시간: {}ms", className, methodName, elapsedTime);

        return result;
    }
}
