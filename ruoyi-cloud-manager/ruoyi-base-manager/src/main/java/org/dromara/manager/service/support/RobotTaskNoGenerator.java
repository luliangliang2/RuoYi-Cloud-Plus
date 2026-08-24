package org.dromara.manager.service.support;

import org.dromara.common.redis.utils.RedisUtils;
import org.springframework.stereotype.Component;

import java.time.Duration;

/**
 * 机器人任务编号生成器。
 *
 * @author LionLi
 * @date 2026-06-19
 */
@Component
public class RobotTaskNoGenerator {

    private static final String KEY_PREFIX = "robot:task:no:";
    private static final char[] BASE36 = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();

    public String nextNo() {
        long now = System.currentTimeMillis();
        String key = KEY_PREFIX + now;
        var atomicLong = RedisUtils.getClient().getAtomicLong(key);
        long seq = atomicLong.incrementAndGet();
        atomicLong.expire(Duration.ofMinutes(1));
        return "T" + now + toBase36(seq % 1296, 2);
    }

    private String toBase36(long value, int length) {
        char[] chars = new char[length];
        for (int i = length - 1; i >= 0; i--) {
            chars[i] = BASE36[(int) (value % 36)];
            value = value / 36;
        }
        return new String(chars);
    }

}
