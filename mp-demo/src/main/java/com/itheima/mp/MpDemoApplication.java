package com.itheima.mp;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Arrays;


@MapperScan("com.itheima.mp.mapper")
@SpringBootApplication
public class MpDemoApplication {

    public static void main(String[] args) {
        String[] newArgs = Arrays.copyOf(args, args.length + 1);
        newArgs[args.length] = "--mpw.key=e801354ddf538ab8";
        SpringApplication.run(MpDemoApplication.class, newArgs);
    }

}

