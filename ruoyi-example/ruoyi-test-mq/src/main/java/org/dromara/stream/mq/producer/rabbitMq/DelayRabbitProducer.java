package org.dromara.stream.mq.producer.rabbitMq;

import lombok.extern.slf4j.Slf4j;
import org.dromara.stream.config.RabbitTtlQueueConfig;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author xbhog
 * @date 2024/05/25 17:15
 **/
@Slf4j
@Component
public class DelayRabbitProducer {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @GetMapping("/sendDelay")
    public String sendDelayMessage(String message) {
        rabbitTemplate.convertAndSend(RabbitTtlQueueConfig.DELAY_EXCHANGE_NAME, RabbitTtlQueueConfig.DELAY_ROUTING_KEY, message);
        return "Delayed message send: " + message;
    }
}
