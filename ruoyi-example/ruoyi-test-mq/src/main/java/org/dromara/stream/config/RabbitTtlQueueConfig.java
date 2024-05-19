package org.dromara.stream.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * RabbitTTL队列
 * @author xbhog
 */
@Configuration
public class RabbitTtlQueueConfig {

    //普通交换机名称
    public static final String X_CHANGE = "X";
    //死信交换机名称
    public static final String Y_DEAD_CHANGE = "Y";
    //普通队列
    public static final String QUEUE_A = "QA";
    public static final String QUEUE_B = "QB";
    //没有ttl的普通队列
    public static final String QUEUE_C = "QC";
    //死信队列
    public static final String DEAD_QUEUE_D = "QD";

    //声明普通交换机
    @Bean("xExchange")
    public DirectExchange xExchange() {
        return new DirectExchange(X_CHANGE);
    }

    //声明死信交换机
    @Bean("yExchange")
    public DirectExchange yExchange() {
        return new DirectExchange(Y_DEAD_CHANGE);
    }

    //声明队列
    @Bean("queueA")
    public Queue queueA() {

        return QueueBuilder.durable(QUEUE_A)
            //死信交换机
            .deadLetterExchange(Y_DEAD_CHANGE)
            //死信RoutingKey
            .deadLetterRoutingKey("YD")
            //消息过期时间
            .ttl(10000)
            .build();
    }

    @Bean("queueB")
    public Queue queueB() {
        return QueueBuilder.durable(QUEUE_B)
            //死信交换机
            .deadLetterExchange(Y_DEAD_CHANGE)
            //死信RoutingKey
            .deadLetterRoutingKey("YD")
            //消息过期时间
            .ttl(40000)
            .build();
    }

    @Bean("queueC")
    public Queue queueC() {
        return QueueBuilder.durable(QUEUE_C)
            //死信交换机
            .deadLetterExchange(Y_DEAD_CHANGE)
            //死信RoutingKey
            .deadLetterRoutingKey("YD")
            .build();
    }


    //死信队列
    @Bean("queueD")
    public Queue queueD() {
        return QueueBuilder.durable(DEAD_QUEUE_D).build();
    }


    //绑定  X_CHANGE绑定queueA
    @Bean
    public Binding queueABindingX(@Qualifier("queueA") Queue queueA, @Qualifier("xExchange") DirectExchange xExchange) {
        return BindingBuilder.bind(queueA).to(xExchange).with("XA");
    }

    //绑定  X_CHANGE绑定queueB
    @Bean
    public Binding queueBBindingX(@Qualifier("queueB") Queue queueB, @Qualifier("xExchange") DirectExchange xExchange) {
        return BindingBuilder.bind(queueB).to(xExchange).with("XB");
    }


    //绑定  Y_CHANGE绑定queueD
    @Bean
    public Binding queueDBindingY(@Qualifier("queueD") Queue queueD, @Qualifier("yExchange") DirectExchange yExchange) {
        return BindingBuilder.bind(queueD).to(yExchange).with("YD");
    }

    //绑定  X_CHANGE绑定queueC
    @Bean
    public Binding queueCBindingX(@Qualifier("queueC") Queue queueC, @Qualifier("xExchange") DirectExchange xExchange) {
        return BindingBuilder.bind(queueC).to(xExchange).with("XC");
    }
}

