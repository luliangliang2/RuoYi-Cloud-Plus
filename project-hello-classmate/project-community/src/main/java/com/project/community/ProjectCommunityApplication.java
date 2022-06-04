package com.project.community;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class ProjectCommunityApplication {
    public static void main(String[] args) {
        SpringApplication.run(ProjectCommunityApplication.class, args);
    }
}
