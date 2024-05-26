package org.dromara.stream.mq.producer.rocketMq.SpringInstances;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.stereotype.Component;

/**
 * @author xbhog
 * @date 2024/05/25 17:55
 **/
@Slf4j
@Component
public class SpringRocketNormalProducer {

    @Resource
    private RocketMQTemplate rocketMQTemplate;

    public void springNormalMessage(){
        rocketMQTemplate.convertAndSend("test", "hello Spring Normal RocketMQ");
    }
}
