package org.dromara.cognition;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.metrics.buffering.BufferingApplicationStartup;

/**
 * 认知模块
 *
 * @author zhang
 */
@EnableDubbo
@SpringBootApplication
public class RuoYiCognitionApplication {
    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(RuoYiCognitionApplication.class);
        application.setApplicationStartup(new BufferingApplicationStartup(2048));
        application.run(args);
        System.out.println("(♥◠‿◠)ﾉﾞ  认知模块启动成功   ლ(´ڡ`ლ)ﾞ  ");
    }
}
