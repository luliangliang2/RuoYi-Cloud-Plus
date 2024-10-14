package org.dromara.resource;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.metrics.buffering.BufferingApplicationStartup;
import org.springframework.boot.system.ApplicationHome;

import java.io.File;

/**
 * 资源服务
 *
 * @author Lion Li
 */
@EnableDubbo
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class RuoYiResourceApplication {
    public static void main(String[] args) {
        // 设置dubbo缓存目录为jar包所在目录
        ApplicationHome home = new ApplicationHome(RuoYiResourceApplication.class);
        String dirPath = home.getSource().getParentFile().toString();
        String filePath = dirPath + File.separator + ".dubbo";
        System.out.println("dubbo 缓存目录："+filePath);
        System.setProperty("dubbo.meta.cache.filePath", filePath);
        System.setProperty("dubbo.mapping.cache.filePath",filePath);

        SpringApplication application = new SpringApplication(RuoYiResourceApplication.class);
        application.setApplicationStartup(new BufferingApplicationStartup(2048));
        application.run(args);
        System.out.println("(♥◠‿◠)ﾉﾞ  资源服务模块启动成功   ლ(´ڡ`ლ)ﾞ  ");
    }
}
