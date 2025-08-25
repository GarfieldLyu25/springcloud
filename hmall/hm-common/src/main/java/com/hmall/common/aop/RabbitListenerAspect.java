package com.hmall.common.aop;

import com.hmall.common.utils.UserContext;
import lombok.extern.slf4j.Slf4j;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.amqp.core.Message;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class RabbitListenerAspect {

    @Around("@annotation(org.springframework.amqp.rabbit.annotation.RabbitListener)")
    public Object handleRabbitListener(ProceedingJoinPoint joinPoint) throws Throwable {
        Message message = null;
        for (Object arg : joinPoint.getArgs()) {
            if (arg instanceof Message) {
                message = (Message) arg;
                break;
            }
        }
        if(message != null){
            Long userId = message.getMessageProperties().getHeader("userId");
            if(userId != null){
                log.debug("放置userid到tl");
                UserContext.setUser(userId);
            }
        }
        try {
            return joinPoint.proceed();
        } finally {
            // 确保清理 ThreadLocal
            log.info("mq调用清除ThreadLocal");
            UserContext.removeUser();
        }
    }
}