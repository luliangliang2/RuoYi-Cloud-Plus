package org.dromara.manager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.metrics.buffering.BufferingApplicationStartup;

/**
 * manager 模块启动类
 */
@SpringBootApplication
public class RuoYiManagerApplication {

    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(RuoYiManagerApplication.class);
        application.setApplicationStartup(new BufferingApplicationStartup(2048));
        application.run(args);
        System.out.println("(♥◠‿◠)ﾉﾞ  manager 模块启动成功   ლ(´ڡ`ლ)ﾞ");
    }
}
