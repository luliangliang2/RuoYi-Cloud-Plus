package org.dromara.stream.mq.producer.rabbitMq;

import jakarta.annotation.Resource;
import org.dromara.stream.config.RabbitConfig;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

/**
 * @author xbhog
 */
@Component
public class NormalRabbitProducer {

    @Resource
    RabbitTemplate rabbitTemplate;


    public String sendMq(String mq) {
        rabbitTemplate.convertAndSend(RabbitConfig.EXCHANGE_NAME, "rabbit.demo", mq);
        return "发送成功，消息是：" + mq;
    }
}
