package com.sparta26.baemin.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

/**
 * 주문상품 관련 로그 작성하는 곳
 */
@Aspect
@Component
@Slf4j(topic = "OrderProductTopic")
public class LoggingAspectOfJoinOrderProduct {
}
