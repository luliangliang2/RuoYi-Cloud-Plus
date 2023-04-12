package com.ruoyi.common.iotdb.config;

import com.ruoyi.common.iotdb.config.properties.IotdbProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.iotdb.session.pool.SessionPool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;

/**
 * @author ：wt
 * @date ：Created in 2023-04-11 9:36
 * @description：iotdb时序数据库
 * @modified By：wt
 */
@Slf4j
@AutoConfiguration
@EnableCaching
@EnableConfigurationProperties(IotdbProperties.class)
public class IotdbAutoConfiguration {
    @Autowired
    private IotdbProperties iotdbProperties;

    @Bean
    public SessionPool iotdbSessionPool(){
        log.info("iotdb init ...");
        log.info("iotdb will connect server at {}:{}",iotdbProperties.getHost(),iotdbProperties.getPort());
        return new SessionPool(
            iotdbProperties.getHost(),
            iotdbProperties.getPort(),
            iotdbProperties.getUsername(),
            iotdbProperties.getPassword(),
            iotdbProperties.getMaxSize()
        );
    }
}
