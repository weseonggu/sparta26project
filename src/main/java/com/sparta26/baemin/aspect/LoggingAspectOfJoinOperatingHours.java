package com.sparta26.baemin.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

/**
 * OperatingHours 관련 로그 작성하는 곳
 */
@Aspect
@Component
@Slf4j(topic = "OperatingHours")
public class LoggingAspectOfJoinOperatingHours {

    // 두 패키지의 모든 클래스와 메서드에 적용될 포인트컷 정의
    @Pointcut("execution(* com.sparta26.baemin.operatinghours..*(..))")
    public void applicationPackagePointcut() {
    }

    // 메서드 실행 전후로 로그 기록 (메서드 실행 시간 측정)
    @Around("applicationPackagePointcut()")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getSignature().getDeclaringTypeName();

        long startTime = System.currentTimeMillis();
        log.info("메서드 실행 시작 - 클래스명: {}, 메서드명: {}", className, methodName);

        Object result = null;
        try {
            result = joinPoint.proceed(); // 실제 메서드 실행
        } catch (Throwable ex) {
            log.error("예외 발생 - 클래스명: {}, 메서드명: {}, 예외 메시지: {}", className, methodName, ex.getMessage());
            throw ex; // 예외를 다시 던져줍니다.
        }

        long elapsedTime = System.currentTimeMillis() - startTime;
        log.info("메서드 실행 종료 - 클래스명: {}, 메서드명: {}, 실행 시간: {}ms", className, methodName, elapsedTime);

        return result;
    }
}
