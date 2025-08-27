package com.hmall.search.es;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ESConfig {
    @Bean
    RestHighLevelClient createClient(){
        return new RestHighLevelClient(RestClient.builder(
                HttpHost.create("http://192.168.1.3:9200")
        ));
    }
}
