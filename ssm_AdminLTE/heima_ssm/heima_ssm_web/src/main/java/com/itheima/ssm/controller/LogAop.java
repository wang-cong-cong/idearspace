package com.itheima.ssm.controller;

import com.itheima.ssm.ISysLogService;
import com.itheima.ssm.domain.SysLog;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Component

public class LogAop {

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private ISysLogService iSysLogService;



    public Object around(ProceedingJoinPoint pjp) {

        Date visitTime = new Date();//获取开始时间

        //使用SecurityContextHolder(上下文)获得SecurityContext，在使用context获得令牌从令牌中获得详情，从详情中获取用户名
        SecurityContext context = SecurityContextHolder.getContext();
        User principal = (User) context.getAuthentication().getPrincipal();
        String username = principal.getUsername();

        //获取URI
        String uri = request.getRequestURI();

        //获取ip地址

        String ip = request.getRemoteAddr();

        //获得方法名
        Signature signature = pjp.getSignature();
        String method = signature.getName();
        Class<? extends Signature> aClass = signature.getClass();


        long start = System.nanoTime();

        long executionTime = 0;
        try {
            Object proceed = pjp.proceed();
            return proceed;
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            throw new RuntimeException(throwable);
        } finally {
            long end = System.nanoTime();
            executionTime = end - start;

            SysLog sysLog = new SysLog();

            sysLog.setVisitTime(visitTime);
            sysLog.setUrl(uri);
            sysLog.setIp(ip);
            sysLog.setMethod("[类名] " + aClass + "[方法名] " + method);
            sysLog.setUsername(username);
            sysLog.setExecutionTime(executionTime);

            iSysLogService.save(sysLog);

        }
    }
}
