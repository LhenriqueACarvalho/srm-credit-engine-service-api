package com.srm.credit.engine.SRM_Credit_Engine.config;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {

    private static final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);

    @Before("execution(* com.srm.credit.engine.SRM_Credit_Engine.controller..*(..))")
    public void logControllerEntry(JoinPoint joinPoint) {
        logger.info("Controller Entry: {} - Method: {}",
                joinPoint.getTarget().getClass().getSimpleName(),
                joinPoint.getSignature().getName());
    }

    @AfterReturning(pointcut = "execution(* com.srm.credit.engine.SRM_Credit_Engine.service..*(..))",
            returning = "result")
    public void logServiceExit(JoinPoint joinPoint, Object result) {
        logger.debug("Service Exit: {} - Method: {} - Result: {}",
                joinPoint.getTarget().getClass().getSimpleName(),
                joinPoint.getSignature().getName(),
                result != null ? result.getClass().getSimpleName() : "void");
    }

    @AfterThrowing(pointcut = "execution(* com.srm.credit.engine.SRM_Credit_Engine..*(..))",
            throwing = "ex")
    public void logException(JoinPoint joinPoint, Throwable ex) {
        logger.error("Exception in {} - Method: {} - Message: {}",
                joinPoint.getTarget().getClass().getSimpleName(),
                joinPoint.getSignature().getName(),
                ex.getMessage(), ex);
    }
}