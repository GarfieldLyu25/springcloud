package com.itheima.publisher.amqp;

import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.Map;

@SpringBootTest
public class SpringAmqpTest {

    @Autowired
    private RabbitTemplate rabbitTemplate;

//    @Test
//    public void testSimpleQueue() {
//        // 队列名称
//        String queueName = "simple.queue";
//        // 消息
//        String message = "hello, spring amqp!";
//        // 发送消息
//        rabbitTemplate.convertAndSend(queueName, message);
//    }
//
//    /**
//     * workQueue
//     * 向队列中不停发送消息，模拟消息堆积。
//     */
//    @Test
//    public void testWorkQueue() throws InterruptedException {
//        // 队列名称
//        String queueName = "work.queue";
//        // 消息
//        String message = "hello, message_";
//        for (int i = 0; i < 50; i++) {
//            // 发送消息，每20毫秒发送一次，相当于每秒发送50条消息
//            rabbitTemplate.convertAndSend(queueName, message + i);
//            Thread.sleep(20);
//        }
//    }
    @Test
    public void testFanoutExchange() {
        // 交换机名称
        String exchangeName = "hmall.fanout";
        // 消息
        String message = "hello, everyone!";
        rabbitTemplate.convertAndSend(exchangeName, "", message);
    }
    @Test
    public void testSendDirectExchange() {
        // 交换机名称
        String exchangeName = "hmall.direct";
        // 消息
        String message = "红色警报！日本乱排核废水，导致海洋生物变异，惊现哥斯拉！";
        // 发送消息
        rabbitTemplate.convertAndSend(exchangeName, "red", message);
    }
    @Test
    public void testSendDirectExchange1() {
        // 交换机名称
        String exchangeName = "hmall.direct";
        // 消息
        String message = "最新报道，哥斯拉是居民自治巨型气球，虚惊一场！";
        // 发送消息
        rabbitTemplate.convertAndSend(exchangeName, "blue", message);
    }
    /**
     * topicExchange
     */
    @Test
    public void testSendTopicExchange() {
        // 交换机名称
        String exchangeName = "hmall.topic";
        // 消息
        String message = "喜报！孙悟空大战哥斯拉，胜!";
        // 发送消息
        rabbitTemplate.convertAndSend(exchangeName, "china.news", message);
    }

    @Test
    public void testSendMap() throws InterruptedException {
        // 准备消息
        Map<String,Object> msg = new HashMap<>();
        msg.put("name", "柳岩");
        msg.put("age", 21);
        // 发送消息
        rabbitTemplate.convertAndSend("object.queue", msg);
    }
}