package com.ruoyi.db;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.metrics.buffering.BufferingApplicationStartup;

/**
 * 数据库服务
 *
 * @author lishuyan wrote on 2022/8/28.
 */
@SpringBootApplication
public class RuoyiDatabaseApplication {

    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(RuoyiDatabaseApplication.class);
        application.setApplicationStartup(new BufferingApplicationStartup(2048));
        application.run(args);
        System.out.println("(♥◠‿◠)ﾉﾞ  数据库服务 启动成功   ლ(´ڡ`ლ)ﾞ  ");
    }
}
