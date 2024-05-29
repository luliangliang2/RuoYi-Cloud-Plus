package org.dromara.stream.mq.producer.rocketMq.nativeInstances;


import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.apis.ClientConfiguration;
import org.apache.rocketmq.client.apis.ClientException;
import org.apache.rocketmq.client.apis.ClientServiceProvider;
import org.apache.rocketmq.client.apis.message.Message;
import org.apache.rocketmq.client.apis.producer.Producer;
import org.apache.rocketmq.client.apis.producer.SendReceipt;
import org.apache.rocketmq.client.apis.producer.Transaction;
import org.apache.rocketmq.client.apis.producer.TransactionResolution;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;


/**
 * @author xbhog
 * @date 2024/05/19 11:18
 **/
@Slf4j
@Component
public class RocketTransactionProducer {

    private RocketTransactionProducer() {
    }

    public void sendTransactionMessage(String orderIdParam) throws ClientException, InterruptedException, IOException {
        String endpoint = "192.168.1.13:8081";
        ClientConfiguration configuration = ClientConfiguration.newBuilder()
            .setEndpoints(endpoint).build();

        String topic = "transaction_topic";
        // 构造事务生产者：事务消息需要生产者构建一个事务检查器，用于检查确认异常事务的中间状态
        ClientServiceProvider provider = ClientServiceProvider.loadService();
        Producer producer = provider.newProducerBuilder()
            .setClientConfiguration(configuration)
            // 设置事务检查器
            .setTransactionChecker(messageView -> {
                // 事务检查器一般是根据业务的ID去检查本地事务是否正确提交还是回滚，此处以订单ID属性为例
                // 在订单表找到了这个订单，说明本地事务插入订单的操作已经正确提交；如果订单表没有订单，说明本地事务已经回滚
                String orderId = messageView.getProperties().get("orderId");
                log.info("查看是否有这个数据：{}",orderId);
                if ("1111".equals(orderId)) {
                    // 没有业务ID直接回滚
                    log.info("没有数据；直接回滚");
                    return TransactionResolution.ROLLBACK;
                }
                // 检查本地事务是否提交
                boolean orderExist = checkOrderById(orderId);
                log.info("【生产者】check transaction start order={} [orderId={}]", orderExist, orderId);
                 return orderExist ? TransactionResolution.COMMIT : TransactionResolution.ROLLBACK;
            }).build();


        // 构建事务消息
        Message message = provider.newMessageBuilder()
            .setTopic(topic)
            //.setKeys("transaction_key")
            .setTag("transaction_tag")
            // 一般事务消息都会设置一个本地事务关联的唯一ID，用来做本地事务回查的校验
            .addProperty("orderId", orderIdParam)
            .setBody("hello rocketMQ this is a transaction message".getBytes(StandardCharsets.UTF_8))
            .build();
        // 开启事务分支
        Transaction transaction;
        try {
            transaction = producer.beginTransaction();
        } catch (ClientException e) {
            e.printStackTrace();
            // 事务分支开启失败则直接退出
            return;
        }
        // 发送事务消息
        SendReceipt sendReceipt;
        try {
            sendReceipt = producer.send(message, transaction);
            log.info("【生产者】send message successfully, messageId={}", sendReceipt.getMessageId());
        } catch (ClientException e) {
            e.printStackTrace();
            // 事务消息发送失败，事务可以直接退出并回滚
            return;
        }
        /**
         * 执行本地事务，并确定本地事务结果
         * 1. 如果本地事务提交成功，则提交消息事务
         * 2. 如果本地事务提交失败，则回滚消息事务
         * 3. 如果本地事务未知异常，则不处理，等待事务消息回查
         */
        boolean localTransactionOk = doLocalTransaction();
        if (localTransactionOk) {
            transaction.commit();
            log.info("发送完成");
        } else {
            try {
                log.info("本地事务失败：开始回滚");
                transaction.rollback();
            } catch (ClientException e) {
                // 建议记录异常信息，回滚异常时可以无需重试，依赖事务消息回查机制进行事务状态的提交
                e.printStackTrace();
            }
        }
    }
    // 模拟订单查询服务用来确认订单事务是否提交成功
    private boolean checkOrderById(String orderId) {
        log.info("检查事务是否已经提交成功");
        return true;
    }

    // 模拟本地事务执行结果
    private static boolean doLocalTransaction() throws InterruptedException {
        log.info("查看本地事务的执行结果");
        //模拟业务执行
        Thread.sleep(20000);
        return true;
    }
}
