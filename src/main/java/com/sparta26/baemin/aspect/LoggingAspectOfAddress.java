package com.sparta26.baemin.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j(topic = "AddressTopic")
public class LoggingAspectOfAddress {

    private final AspectCommonLogic aspectCommonLogic;

    public LoggingAspectOfAddress(AspectCommonLogic aspectCommonLogic) {
        this.aspectCommonLogic = aspectCommonLogic;
    }
    /**
     * 회원 가입시 회가입 정보 로깅
     * @param joinPoint
     * @return
     * @throws Throwable
     */
    @Around("execution(* com.sparta26.baemin.address.service.AddressService.createAdressInfo(..))")
    public Object createMemberAround(ProceedingJoinPoint joinPoint) throws Throwable {
        return aspectCommonLogic.redundantMethods(joinPoint);
    }
}
