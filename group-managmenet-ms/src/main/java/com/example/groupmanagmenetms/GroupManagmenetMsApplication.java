package com.example.groupmanagmenetms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class GroupManagmenetMsApplication {

    public static void main(String[] args) {
        SpringApplication.run(GroupManagmenetMsApplication.class, args);
    }

}
