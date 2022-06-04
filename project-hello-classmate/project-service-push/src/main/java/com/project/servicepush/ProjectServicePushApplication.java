package com.project.servicepush;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class ProjectServicePushApplication {
    public static void main(String[] args) {
        SpringApplication.run(ProjectServicePushApplication.class, args);
    }
}
