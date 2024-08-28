package com.sparta26.baemin.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

/**
 * 주문 관련 로그 작성하는 곳
 */
@Aspect
@Component
@Slf4j(topic = "OrderTopic")
public class LoggingAspectOfJoinOrder {

    @Around("execution(* com.sparta26.baemin.order.service.OrderService.createOrder(..)) || "
            + "execution(* com.sparta26.baemin.order.service.OrderService.updateOrder(..)) || "
            + "execution(* com.sparta26.baemin.order.service.OrderService.deleteOrder(..))")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
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
