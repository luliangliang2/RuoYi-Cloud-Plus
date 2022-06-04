package com.project.product;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class ProjectProductApplication {
    public static void main(String[] args) {
        SpringApplication.run(ProjectProductApplication.class, args);
    }
}
