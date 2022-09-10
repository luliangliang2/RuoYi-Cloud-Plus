package com.ruoyi.db.constant;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

/**
 * 数据库常量
 *
 * @author lishuyan
 */
public interface DatabaseConstant {

    /**
     * 密钥
     */
    byte[] KEY = "CzuMelNYuQbu6d1VNZQMJBQY".getBytes(StandardCharsets.UTF_8);

    /**
     * 系统数据ids
     */
    List<Long> SYSTEM_DATA_IDS = Arrays.asList(1L, 2L, 3L, 4L);

}
