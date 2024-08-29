package com.sparta26.baemin.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * Member관련 로그 작성하는 곳
 */
@Aspect
@Component
@Slf4j(topic = "MemberTopic")
public class LoggingAspectOfJoinMember {
    private final AspectCommonLogic aspectCommonLogic;

    public LoggingAspectOfJoinMember(AspectCommonLogic aspectCommonLogic) {
        this.aspectCommonLogic = aspectCommonLogic;
    }



    /**
     * 회원 가입시 회가입 정보 로깅
     * @param joinPoint
     * @return
     * @throws Throwable
     */
    @Around("execution(* com.sparta26.baemin.member.service.MemberService.createMember(..))")
    public Object createMemberAround(ProceedingJoinPoint joinPoint) throws Throwable {
        return aspectCommonLogic.ChangeRedundantMethods(joinPoint);
    }

    /**
     * 회원정보 수정
     * @param joinPoint
     * @return
     * @throws Throwable
     */
    @Around("execution(* com.sparta26.baemin.member.service.MemberCacheService.updateMemberInfo(..))")
    public Object updateMemberInfoAround(ProceedingJoinPoint joinPoint) throws Throwable {

        return aspectCommonLogic.ChangeRedundantMethods(joinPoint);
    }
    /**
     * 회원정보  공개여부 변경
     * @param joinPoint
     * @return
     * @throws Throwable
     */
    @Around("execution(* com.sparta26.baemin.member.service.MemberCacheService.deleteMember(..))")
    public Object deleteMemberAround(ProceedingJoinPoint joinPoint) throws Throwable {

        return aspectCommonLogic.ChangeRedundantMethods(joinPoint);
    }

    @Pointcut("execution(* com.sparta26.baemin.member.controller..*(..))")
    public void serviceMethods() {}

    @Pointcut("execution(* com.sparta26.baemin.member.controller.MemberController.signUp(..)) || execution(* com.sparta26.baemin.member.controller.MemberController.logIn(..))")
    public void excludedMethod() {}

    @Around("serviceMethods() && !excludedMethod()")
    public Object aroundAdvice(ProceedingJoinPoint joinPoint) throws Throwable {

        return aspectCommonLogic.ResponseRedundantMethods(joinPoint);
    }


}
