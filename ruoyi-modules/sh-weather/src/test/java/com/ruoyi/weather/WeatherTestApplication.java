package com.ruoyi.weather;

import com.ruoyi.weather.WeatherApplication;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.metrics.buffering.BufferingApplicationStartup;

/**
 * @author ：wt
 * @date ：Created in ${DATE} ${TIME}
 * @description：${description}
 * @modified By：wt
 */
@EnableDubbo
@SpringBootApplication
public class WeatherTestApplication {
    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(WeatherApplication.class);
        application.setApplicationStartup(new BufferingApplicationStartup(2048));
        application.run(args);
        System.out.println("(♥◠‿◠)ﾉﾞ  天气模块启动成功   ლ(´ڡ`ლ)ﾞ  ");
    }
}
