package com.hmall.cart.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RemoteCallConfig {
    //没有用
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}