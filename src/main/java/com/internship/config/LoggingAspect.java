package com.internship.config;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.stream.Collectors;

@Slf4j
@Aspect
@Component
public class LoggingAspect {
    @Pointcut("execution(* com.internship.service.DocumentService.*(..))")
    private void callAtDocumentService() {
    }

    @Pointcut("execution(* com.internship.service.DocumentGroupService.*(..))")
    private void callAtDocumentGroupService() {
    }

    @Pointcut("execution(* com.internship.service.DocumentTypeService.*(..))")
    private void callAtDocumentTypeService() {
    }

    @Before("callAtDocumentService() || callAtDocumentGroupService() || callAtDocumentTypeService()")
    public void beforeServiceMethodAdvice(JoinPoint joinPoint) {
        log.info("{}, args=[{}]", joinPoint, getArgs(joinPoint));
    }

    @AfterThrowing(pointcut = "callAtDocumentService() || callAtDocumentGroupService() || callAtDocumentTypeService()",
            throwing = "exception")
    public void afterThrowingServiceMethodAdvice(JoinPoint joinPoint, Throwable exception) {
        log.error("{}, args=[{}]", joinPoint, getArgs(joinPoint));
        log.error(exception.toString());
    }

    private String getArgs(JoinPoint joinPoint) {
        return Arrays.stream(joinPoint.getArgs())
                .map(Object::toString)
                .collect(Collectors.joining(","));
    }
}
