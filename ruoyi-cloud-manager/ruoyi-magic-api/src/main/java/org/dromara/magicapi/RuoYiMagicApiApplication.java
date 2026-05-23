package org.dromara.magicapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.metrics.buffering.BufferingApplicationStartup;

/**
 * Magic API动态接口服务
 *
 * @author ruoyi
 */
@SpringBootApplication
public class RuoYiMagicApiApplication {

    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(RuoYiMagicApiApplication.class);
        application.setApplicationStartup(new BufferingApplicationStartup(2048));
        application.run(args);
        System.out.println("(♥◠‿◠)ﾉﾞ  Magic API动态接口服务启动成功   ლ(´ڡ`ლ)ﾞ  ");
    }
}
