package com.ruoyi.common.iotdb.config.properties;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author ：wt
 * @date ：Created in 2023-04-11 9:38
 * @description：
 * @modified By：wt
 */
@Data
@ConfigurationProperties(prefix = "iotdb")
public class IotdbProperties {
    /**
    *     host: 127.0.0.1
    *     port: 6667
    *     username: root
    *     password: root
    *     fetchSize: 5000
    *     maxSize: 10
    */

    private String host;

    private Integer port;

    private String username;

    private String password;

    private String fetchSize;

    private Integer maxSize;

}
