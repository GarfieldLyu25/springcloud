package com.hmall.cart.listener;

import com.hmall.cart.service.ICartService;
import com.hmall.common.utils.UserContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@RequiredArgsConstructor
@Slf4j
public class OrderCreateListener {
    private final ICartService cartService;
    private final RabbitTemplate rabbitTemplate;
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "cart.clear.queue",durable = "true"),
            exchange = @Exchange(name = "trade.topic"),
            key = "order.create"
    ))
    public void listenOrderCreate(Set<Long> itemIds, Message message)
                                  //,@Header("userId") Long userId)
                                                                    {
        //try {
            //log.debug("{}",userId);
            //UserContext.setUser(userId);
            cartService.removeByItemIds(itemIds);
//        }finally {
//            log.debug("删除user");
//            UserContext.removeUser();
//        }

    }
}
