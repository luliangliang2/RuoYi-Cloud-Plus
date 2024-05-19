package org.dromara.stream.mq.consumer.rabbit;

import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author xbhog
 * @date 2024年5月18日
 */
@Slf4j
@Component
public class ConsumerListener {

    /**
     * 设置监听哪一个队列 这个队列是RabbitConfig里面设置好的队列名字
     * 普通消息
     **/
    @RabbitListener(queues = "rabbit_queue")
    public void listenQueue(Message message) {
        log.info("开始消费数据：{}",new String(message.getBody()));
    }

    /**
     * 处理延迟队列的操作
     * 该部分处理的延迟操作在消费上的时间可能与设置的TTl不同；
     * 一般会超长；原因是消息可能并不会按时死亡；可通过延迟队列插件处理
     */
    @RabbitListener(queues = "QD")
    public void receiveMessage(Message message, Channel channel){
        String msg = new String(message.getBody());
        log.info("当前时间{}，收到死信队列的消息：{}",new Date().toString(),msg);
    }
}
