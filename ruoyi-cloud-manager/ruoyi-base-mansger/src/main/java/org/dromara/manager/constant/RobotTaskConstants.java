package org.dromara.manager.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * 机器人任务常量。
 *
 * @author LionLi
 * @date 2026-06-19
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RobotTaskConstants {

    public static final String TASK_TYPE_TEMPLATE = "template";
    public static final String TASK_TYPE_TEMPORARY = "temporary";

    public static final String ASSIGN_MODE_ASSIGN = "assign";
    public static final String ASSIGN_MODE_DISPATCH = "dispatch";

    public static final String FLAG_YES = "1";
    public static final String FLAG_NO = "0";

    public static final String TASK_STATUS_PENDING = "pending";
    public static final String TASK_STATUS_RUNNING = "running";
    public static final String TASK_STATUS_ABNORMAL = "abnormal";
    public static final String TASK_STATUS_CANCELED = "canceled";
    public static final String TASK_STATUS_COMPLETED = "completed";

    public static final String STEP_STATUS_PENDING = "pending";
    public static final String STEP_STATUS_RUNNING = "running";
    public static final String STEP_STATUS_SUCCESS = "success";
    public static final String STEP_STATUS_FAIL = "fail";
    public static final String STEP_STATUS_SKIPPED = "skipped";

    public static final String STEP_TYPE_POINT = "point";
    public static final String STEP_TYPE_ACTION = "action";

    public static final long RUNTIME_TTL_DAYS = 7L;

}
