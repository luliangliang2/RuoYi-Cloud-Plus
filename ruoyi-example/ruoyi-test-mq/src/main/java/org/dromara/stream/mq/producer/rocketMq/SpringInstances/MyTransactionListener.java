package org.dromara.stream.mq.producer.rocketMq.SpringInstances;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQTransactionListener;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionListener;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionState;
import org.springframework.messaging.Message;

import java.nio.charset.StandardCharsets;

/**
 * @author xbhog
 */
@Slf4j
@RocketMQTransactionListener
public class MyTransactionListener implements RocketMQLocalTransactionListener {

    @Override
    public RocketMQLocalTransactionState executeLocalTransaction(Message msg, Object arg) {
        log.info("【监听执行】start invoke local rocketMQ transaction");
        RocketMQLocalTransactionState resultState = RocketMQLocalTransactionState.COMMIT;

        try
        {
            //处理业务
            String jsonStr = new String((byte[]) msg.getPayload(), StandardCharsets.UTF_8);
            log.info("【监听执行】invoke msg content：{}",jsonStr);
            //模拟异常
            //int c=1/0;
        }
        catch (Exception e)
        {
            log.error("【监听异常】invoke local mq trans error",e);
            resultState = RocketMQLocalTransactionState.UNKNOWN;
        }

        return resultState;
    }

    @Override
    public RocketMQLocalTransactionState checkLocalTransaction(Message msg) {
        log.info("【监听检查】start check Local rocketMQ transaction");

        RocketMQLocalTransactionState resultState = RocketMQLocalTransactionState.COMMIT;

        try
        {
            String jsonStr = new String((byte[]) msg.getPayload(), StandardCharsets.UTF_8);
            log.info("【监听检查】check trans msg content：{}",jsonStr);
        }
        catch (Exception e)
        {
            resultState  = RocketMQLocalTransactionState.ROLLBACK;
        }
        return resultState;
    }
}
