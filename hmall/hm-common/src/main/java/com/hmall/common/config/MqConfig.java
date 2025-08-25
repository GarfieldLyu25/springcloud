package com.hmall.common.config;


import com.hmall.common.utils.UserContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnClass(RabbitTemplate.class)
@Slf4j
public class MqConfig {
    @Bean
    public MessageConverter messageConverter(){
        // 1.定义消息转换器
        Jackson2JsonMessageConverter jackson2JsonMessageConverter = new Jackson2JsonMessageConverter();
        // 2.配置自动创建消息id，用于识别不同消息，也可以在业务中基于ID判断是否是重复消息
        jackson2JsonMessageConverter.setCreateMessageIds(true);
        return jackson2JsonMessageConverter;
    }
    @Bean
    public MessagePostProcessor messagePostProcessor(){
        return message -> {
            Long userId = UserContext.getUser();
            if(userId != null){
                log.info("mq把user存起来了{}",userId);
                message.getMessageProperties().setHeader("userId", userId);
            }
            return message;
        };
    }
    // 配置 RabbitTemplate
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory, MessageConverter messageConverter) {
        log.debug("配置rabbitTemplate");
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(messageConverter); // 设置消息转换器
        rabbitTemplate.setBeforePublishPostProcessors(messagePostProcessor());
        //接不到用aop
//        rabbitTemplate.setAfterReceivePostProcessors(message -> {
//            Object header = message.getMessageProperties().getHeader("userId");
//            if(header != null){
//                log.info("mq接收到user{}，放到threadlocal", header);
//                UserContext.setUser(Long.valueOf(header.toString()));
//            }
//            return message;
//        });
        return rabbitTemplate;
    }

}