package com.tc.web.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

//@Aspect
//@Component
public class TimeAspect {

    @Around("execution(* com.tc.web.controller.UserController.*(..))")
    public Object handleControllerMethod(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {

        System.out.println("time aspect start");

        Object[] args = proceedingJoinPoint.getArgs();
        for (Object arg:args
             ) {
            System.out.println("arg is" + arg);
        }

        long start = System.currentTimeMillis();

        Object object = proceedingJoinPoint.proceed();

        System.out.println("time aspect 耗时：" + (System.currentTimeMillis() - start));

        System.out.println("time aspect end");

        return object;
    }



}
