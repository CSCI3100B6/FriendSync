package com.friendsync;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@MapperScan(basePackages = {"com.friendsync.muush.mapper", "com.friendsync.stevenpang.mapper"})
@ComponentScan(basePackages = {"com.friendsync.muush", "com.friendsync.stevenpang"})
public class ServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(ServerApplication.class, args);
    }
}
