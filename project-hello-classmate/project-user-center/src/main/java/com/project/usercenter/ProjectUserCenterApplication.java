package com.project.usercenter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class ProjectUserCenterApplication {
    public static void main(String[] args) {
        SpringApplication.run(ProjectUserCenterApplication.class, args);
    }
}
