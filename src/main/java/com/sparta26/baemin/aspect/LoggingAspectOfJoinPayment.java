package com.sparta26.baemin.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/**
 * 결제 관련 로그 작성하는 곳
 */
@Aspect
@Component
@Slf4j(topic = "PaymentTopic")
public class LoggingAspectOfJoinPayment {
    @Around("execution(* com.sparta26.baemin.payment.service.PaymentService.pay(..)) || "
            + "execution(* com.sparta26.baemin.payment.service.PaymentService.updatePayment(..)) || "
            + "execution(* com.sparta26.baemin.payment.service.PaymentService.deletePayment(..))")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        log.info("Authentication detail: {}", authentication.getName());

        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getTarget().getClass().getSimpleName();
        Object[] args = joinPoint.getArgs();
        log.info("Method {} in class {} is about to be executed with arguments: {}",
                methodName, className, args);
        Object result;
        try {
            result = joinPoint.proceed();
            log.info("Method {} in class {} executed successfully with return value: {}",
                    methodName, className, result);
        } catch (Exception ex) {
            log.warn("Method {} in class {} threw an exception: {} with arguments: {}",
                    methodName, className, ex.getMessage(), args);
            throw ex;
        }
        return result;
    }
}