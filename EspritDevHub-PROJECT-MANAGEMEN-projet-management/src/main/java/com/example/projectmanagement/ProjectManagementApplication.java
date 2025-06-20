package com.example.projectmanagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ProjectManagementApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProjectManagementApplication.class, args);
    }

}
