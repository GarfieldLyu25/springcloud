package com.hmall.item;

import com.hmall.api.client.ItemClient;
import com.hmall.api.config.DefaultFeignConfig;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

//@EnableFeignClients(defaultConfiguration = DefaultFeignConfig.class)
@MapperScan("com.hmall.item.mapper")
@SpringBootApplication
public class itemApplication {
    public static void main(String[] args) {
        SpringApplication.run(itemApplication.class, args);
    }
}