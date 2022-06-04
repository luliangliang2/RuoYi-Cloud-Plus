package com.project.wallet;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class ProjectWalletApplication {
    public static void main(String[] args) {
        SpringApplication.run(ProjectWalletApplication.class, args);
    }
}
