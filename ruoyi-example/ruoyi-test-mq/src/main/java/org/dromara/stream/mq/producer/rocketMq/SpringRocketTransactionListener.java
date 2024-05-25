package org.dromara.stream.mq.producer.rocketMq;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQTransactionListener;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionListener;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionState;
import org.apache.rocketmq.spring.support.RocketMQHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * @author xbhog
 */
@Slf4j
@Component
@RocketMQTransactionListener
public class SpringRocketTransactionListener implements RocketMQLocalTransactionListener {

    @Override
    public RocketMQLocalTransactionState executeLocalTransaction(Message message, Object arg) {
        MessageHeaders headers = message.getHeaders();
        // 本地事务id
        String transactionId = (String) headers.get(RocketMQHeaders.PREFIX + RocketMQHeaders.TRANSACTION_ID);
        String orderId = (String) headers.get("orderId");
        log.info("local transaction start transactionId = {} [orderId = {}]", transactionId, orderId);
        if (!StringUtils.hasLength(orderId)) {
            // 直接回滚
            return RocketMQLocalTransactionState.ROLLBACK;
        }
        int id = Integer.parseInt(orderId);
        // ===== 本地事务开始 =====
        // 执行保存 orderService.saveOrder(order) 执行本地事务
        // ===== 本地事务结束 =====

        // 模拟本地事务执行成功(偶数)和失败(奇数)
        if (id % 2 == 0) {
            return RocketMQLocalTransactionState.COMMIT;
        } else {
            // 假设这里的失败是本地事务还在执行(还不确定提交还是回滚)
            return RocketMQLocalTransactionState.UNKNOWN;
        }
    }

    /**
     * 检查事务状态（监听回查请求）
     * 1.当成产者执行本地事务发生故障或者是返回 UNKNOWN 状态,要保证这条消息最终被消费,RocketMQ会像服务端发送回查请求,确认本地事务的执行状态
     * 2.不会无休止的的信息事务状态回查，默认回查15次，如果15次回查还是无法得知事务状态，RocketMQ默认回滚该消息
     */
    @Override
    public RocketMQLocalTransactionState checkLocalTransaction(Message message) {
        MessageHeaders headers = message.getHeaders();
        String transactionId = (String) headers.get(RocketMQHeaders.PREFIX + RocketMQHeaders.TRANSACTION_ID);
        String orderId = (String) headers.get("orderId");
        log.info("check transaction start transactionId = {} [orderId = {}]", transactionId, orderId);
        if (!StringUtils.hasLength(orderId)) {
            // 直接回滚
            return RocketMQLocalTransactionState.ROLLBACK;
        }

        // 查询 orderService.getOrderById(id)
        // 如果能查到则说明本地事务执行成功 返回rocketMQLocalTransactionState.COMMIT
        // 反之则说明本地事务还在执行或者是出现故障

        // 这里统一模拟为出现故障，不让消费者消费消息
        return RocketMQLocalTransactionState.ROLLBACK;
    }
}
