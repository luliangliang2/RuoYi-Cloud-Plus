package org.dromara.stream;

import org.apache.rocketmq.spring.autoconfigure.RocketMQAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.metrics.buffering.BufferingApplicationStartup;

/**
 * SpringCloud-Stream-MQ 案例项目
 *手动导入 RocketMQ 的自动配置类
 * @author Lion Li
 */
@SpringBootApplication
@ImportAutoConfiguration({RocketMQAutoConfiguration.class})
public class RuoYiStreamMqApplication {

    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(RuoYiStreamMqApplication.class);
        application.setApplicationStartup(new BufferingApplicationStartup(2048));
        application.run(args);
        System.out.println("(♥◠‿◠)ﾉﾞ  MQ案例模块启动成功   ლ(´ڡ`ლ)ﾞ  ");
    }

}
