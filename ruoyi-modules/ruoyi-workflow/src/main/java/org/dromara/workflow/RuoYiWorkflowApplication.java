package org.dromara.workflow;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.metrics.buffering.BufferingApplicationStartup;
import org.springframework.boot.system.ApplicationHome;

import java.io.File;

/**
 * 系统模块
 *
 * @author ruoyi
 */
@EnableDubbo
@SpringBootApplication
public class RuoYiWorkflowApplication {
    public static void main(String[] args) {
        // 设置dubbo缓存目录为jar包所在目录
        ApplicationHome home = new ApplicationHome(RuoYiWorkflowApplication.class);
        String dirPath = home.getSource().getParentFile().toString();
        String filePath = dirPath + File.separator + ".dubbo";
        System.out.println("dubbo 缓存目录："+filePath);
        System.setProperty("dubbo.meta.cache.filePath", filePath);
        System.setProperty("dubbo.mapping.cache.filePath",filePath);

        SpringApplication application = new SpringApplication(RuoYiWorkflowApplication.class);
        application.setApplicationStartup(new BufferingApplicationStartup(2048));
        application.run(args);
        System.out.println("(♥◠‿◠)ﾉﾞ  工作流模块启动成功   ლ(´ڡ`ლ)ﾞ  ");
    }
}
