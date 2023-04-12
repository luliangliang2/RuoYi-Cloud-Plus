package com.example.hmsystem;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.metrics.buffering.BufferingApplicationStartup;

/**
 * @author ：rzy
 * @date ：Created in ${DATE} ${TIME}
 * @description：${description}
 * @modified By：rzy
 */
@EnableDubbo
@SpringBootApplication
public class HmSystemApplication {
    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(HmSystemApplication.class);
        application.setApplicationStartup(new BufferingApplicationStartup(2048));
        application.run(args);
        System.out.println("(♥◠‿◠)ﾉﾞ  基础模块启动成功   ლ(´ڡ`ლ)ﾞ  ");
    }
}
