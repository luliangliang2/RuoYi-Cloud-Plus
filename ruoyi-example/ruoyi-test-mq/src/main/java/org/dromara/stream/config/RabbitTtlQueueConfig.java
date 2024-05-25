package org.dromara.stream.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * RabbitTTL队列
 * @author xbhog
 */
@Configuration
public class RabbitTtlQueueConfig {

    public static final String DELAY_QUEUE_NAME = "delay-queue";
    public static final String DELAY_EXCHANGE_NAME = "delay-exchange";
    public static final String DELAY_ROUTING_KEY = "delay.routing.key";

    public static final String DEAD_LETTER_EXCHANGE = "dlx-exchange";
    public static final String DEAD_LETTER_QUEUE = "dlx-queue";
    public static final String DEAD_LETTER_ROUTING_KEY = "dlx.routing.key";

    @Value("${rabbitmq.delay.ttl:5000}")
    private long messageTTL;

    @Bean
    public Queue delayQueue() {
        return QueueBuilder.durable(DELAY_QUEUE_NAME)
            .withArgument("x-dead-letter-exchange", DEAD_LETTER_EXCHANGE)
            .withArgument("x-dead-letter-routing-key", DEAD_LETTER_ROUTING_KEY)
            .withArgument("x-message-ttl", messageTTL)
            .build();
    }

    @Bean
    public TopicExchange delayExchange() {
        return new TopicExchange(DELAY_EXCHANGE_NAME);
    }

    @Bean
    public Binding delayBinding(Queue delayQueue, TopicExchange delayExchange) {
        return BindingBuilder.bind(delayQueue).to(delayExchange).with(DELAY_ROUTING_KEY);
    }

    @Bean
    public Queue deadLetterQueue() {
        return new Queue(DEAD_LETTER_QUEUE);
    }

    @Bean
    public TopicExchange deadLetterExchange() {
        return new TopicExchange(DEAD_LETTER_EXCHANGE);
    }

    @Bean
    public Binding deadLetterBinding(Queue deadLetterQueue, TopicExchange deadLetterExchange) {
        return BindingBuilder.bind(deadLetterQueue).to(deadLetterExchange).with(DEAD_LETTER_ROUTING_KEY);
    }
}

