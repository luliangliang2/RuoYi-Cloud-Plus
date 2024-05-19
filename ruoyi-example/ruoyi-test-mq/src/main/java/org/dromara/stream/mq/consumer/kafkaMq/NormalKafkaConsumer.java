package org.dromara.stream.mq.consumer.kafkaMq;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * @author xbhog
 * @date 2024/05/19 18:04
 **/
@Slf4j
@Component
public class NormalKafkaConsumer {

    //默认获取最后一条消息
    @KafkaListener(topics = "test-topic",groupId = "demo")
    public void timiKafka(ConsumerRecord record){
        Object key = record.key();
        Object value = record.value();
        log.info("接收到消息的key {}，value：{}",key,value);
    }

}
