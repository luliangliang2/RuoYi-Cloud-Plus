-- ----------------------------
-- 机器人任务执行表
-- ----------------------------
CREATE TABLE IF NOT EXISTS biz_robot_task
(
    task_id            BIGINT        NOT NULL COMMENT '任务ID',
    tenant_id          VARCHAR(20)   DEFAULT '000000' COMMENT '租户编号',
    task_no            VARCHAR(16)   NOT NULL COMMENT '任务编号',
    task_name          VARCHAR(100)  NOT NULL COMMENT '任务名称',
    task_type          VARCHAR(32)   NOT NULL COMMENT '任务类型（template模板任务 temporary临时任务）',
    template_id        BIGINT        DEFAULT NULL COMMENT '模板ID',
    route_id           BIGINT        DEFAULT NULL COMMENT '路线ID',
    assign_mode        VARCHAR(32)   NOT NULL COMMENT '车辆方式（assign指派 dispatch调派）',
    vehicle_id         BIGINT        DEFAULT NULL COMMENT '车辆ID快照',
    vin                VARCHAR(64)   DEFAULT NULL COMMENT 'VIN快照',
    plate_no           VARCHAR(64)   DEFAULT NULL COMMENT '车牌号快照',
    loop_flag          CHAR(1)       DEFAULT '0' COMMENT '是否循环（0否 1是）',
    loop_count         INT           DEFAULT 1 COMMENT '循环次数',
    schedule_flag      CHAR(1)       DEFAULT '0' COMMENT '是否定时（0否 1是）',
    start_time         DATETIME      DEFAULT NULL COMMENT '计划开始时间',
    actual_start_time  DATETIME      DEFAULT NULL COMMENT '实际开始时间',
    finish_time        DATETIME      DEFAULT NULL COMMENT '完成时间',
    task_status        VARCHAR(32)   DEFAULT 'pending' COMMENT '任务状态',
    current_loop_no    INT           DEFAULT 0 COMMENT '当前循环轮次',
    current_point_seq  INT           DEFAULT 0 COMMENT '当前点位顺序',
    current_action_seq INT           DEFAULT 0 COMMENT '当前动作顺序',
    command_json       LONGTEXT      DEFAULT NULL COMMENT '下发指令JSON快照',
    error_message      VARCHAR(1000) DEFAULT NULL COMMENT '异常信息',
    del_flag           CHAR(1)       DEFAULT '0' COMMENT '删除标志（0代表存在 1代表删除）',
    create_dept        BIGINT        DEFAULT NULL COMMENT '创建部门',
    create_by          BIGINT        DEFAULT NULL COMMENT '创建者',
    create_time        DATETIME      DEFAULT NULL COMMENT '创建时间',
    update_by          BIGINT        DEFAULT NULL COMMENT '更新者',
    update_time        DATETIME      DEFAULT NULL COMMENT '更新时间',
    remark             VARCHAR(500)  DEFAULT NULL COMMENT '备注',
    PRIMARY KEY (task_id),
    UNIQUE KEY uk_biz_robot_task_no (tenant_id, task_no),
    KEY idx_biz_robot_task_status (tenant_id, task_status, del_flag),
    KEY idx_biz_robot_task_vehicle (tenant_id, vehicle_id, del_flag),
    KEY idx_biz_robot_task_vin (tenant_id, vin, del_flag),
    KEY idx_biz_robot_task_template (tenant_id, template_id, del_flag),
    KEY idx_biz_robot_task_start_time (tenant_id, start_time, del_flag)
) ENGINE = InnoDB COMMENT = '机器人任务执行表';

-- ----------------------------
-- 机器人任务点位执行实例表
-- ----------------------------
CREATE TABLE IF NOT EXISTS biz_robot_task_point
(
    task_point_id BIGINT       NOT NULL COMMENT '任务点位ID',
    tenant_id     VARCHAR(20)  DEFAULT '000000' COMMENT '租户编号',
    task_id       BIGINT       NOT NULL COMMENT '任务ID',
    task_no       VARCHAR(16)  NOT NULL COMMENT '任务编号快照',
    loop_no       INT          DEFAULT 1 COMMENT '循环轮次',
    route_id      BIGINT       DEFAULT NULL COMMENT '路线ID',
    point_id      BIGINT       NOT NULL COMMENT '点位ID',
    point_name    VARCHAR(100) NOT NULL COMMENT '点位名称快照',
    point_seq     INT          DEFAULT 1 COMMENT '点位顺序',
    required_flag CHAR(1)      DEFAULT '1' COMMENT '是否必须到达（0否 1是）',
    gcj02_lng     DECIMAL(18, 8) DEFAULT NULL COMMENT '高德GCJ02经度',
    gcj02_lat     DECIMAL(18, 8) DEFAULT NULL COMMENT '高德GCJ02纬度',
    bd09_lng      DECIMAL(18, 8) DEFAULT NULL COMMENT '百度BD09经度',
    bd09_lat      DECIMAL(18, 8) DEFAULT NULL COMMENT '百度BD09纬度',
    wgs84_lng     DECIMAL(18, 8) DEFAULT NULL COMMENT 'WGS84经度',
    wgs84_lat     DECIMAL(18, 8) DEFAULT NULL COMMENT 'WGS84纬度',
    point_status  VARCHAR(32)  DEFAULT 'pending' COMMENT '点位状态',
    arrive_time   DATETIME     DEFAULT NULL COMMENT '到达时间',
    finish_time   DATETIME     DEFAULT NULL COMMENT '完成时间',
    report_payload LONGTEXT    DEFAULT NULL COMMENT '上报内容快照',
    del_flag      CHAR(1)      DEFAULT '0' COMMENT '删除标志（0代表存在 1代表删除）',
    create_dept   BIGINT       DEFAULT NULL COMMENT '创建部门',
    create_by     BIGINT       DEFAULT NULL COMMENT '创建者',
    create_time   DATETIME     DEFAULT NULL COMMENT '创建时间',
    update_by     BIGINT       DEFAULT NULL COMMENT '更新者',
    update_time   DATETIME     DEFAULT NULL COMMENT '更新时间',
    remark        VARCHAR(500) DEFAULT NULL COMMENT '备注',
    PRIMARY KEY (task_point_id),
    KEY idx_biz_robot_task_point_task (tenant_id, task_id, loop_no, point_seq),
    KEY idx_biz_robot_task_point_status (tenant_id, task_id, point_status, del_flag),
    KEY idx_biz_robot_task_point_no (tenant_id, task_no, loop_no)
) ENGINE = InnoDB COMMENT = '机器人任务点位执行实例表';

-- ----------------------------
-- 机器人任务动作执行实例表
-- ----------------------------
CREATE TABLE IF NOT EXISTS biz_robot_task_action
(
    task_action_id BIGINT       NOT NULL COMMENT '任务动作ID',
    tenant_id      VARCHAR(20)  DEFAULT '000000' COMMENT '租户编号',
    task_id        BIGINT       NOT NULL COMMENT '任务ID',
    task_no        VARCHAR(16)  NOT NULL COMMENT '任务编号快照',
    task_point_id  BIGINT       NOT NULL COMMENT '任务点位ID',
    loop_no        INT          DEFAULT 1 COMMENT '循环轮次',
    point_id       BIGINT       NOT NULL COMMENT '点位ID',
    point_seq      INT          DEFAULT 1 COMMENT '点位顺序',
    action_id      BIGINT       NOT NULL COMMENT '动作ID',
    action_code    VARCHAR(64)  NOT NULL COMMENT '动作编码快照',
    action_name    VARCHAR(100) NOT NULL COMMENT '动作名称快照',
    action_type    VARCHAR(32)  NOT NULL COMMENT '动作类型快照',
    action_seq     INT          DEFAULT 1 COMMENT '动作顺序',
    action_params  TEXT         DEFAULT NULL COMMENT '动作参数JSON',
    action_status  VARCHAR(32)  DEFAULT 'pending' COMMENT '动作状态',
    start_time     DATETIME     DEFAULT NULL COMMENT '开始时间',
    finish_time    DATETIME     DEFAULT NULL COMMENT '完成时间',
    report_payload LONGTEXT     DEFAULT NULL COMMENT '上报内容快照',
    error_message  VARCHAR(1000) DEFAULT NULL COMMENT '异常信息',
    del_flag       CHAR(1)      DEFAULT '0' COMMENT '删除标志（0代表存在 1代表删除）',
    create_dept    BIGINT       DEFAULT NULL COMMENT '创建部门',
    create_by      BIGINT       DEFAULT NULL COMMENT '创建者',
    create_time    DATETIME     DEFAULT NULL COMMENT '创建时间',
    update_by      BIGINT       DEFAULT NULL COMMENT '更新者',
    update_time    DATETIME     DEFAULT NULL COMMENT '更新时间',
    remark         VARCHAR(500) DEFAULT NULL COMMENT '备注',
    PRIMARY KEY (task_action_id),
    KEY idx_biz_robot_task_action_task (tenant_id, task_id, loop_no, point_seq, action_seq),
    KEY idx_biz_robot_task_action_status (tenant_id, task_id, action_status, del_flag),
    KEY idx_biz_robot_task_action_no (tenant_id, task_no, loop_no),
    KEY idx_biz_robot_task_action_action (tenant_id, action_id)
) ENGINE = InnoDB COMMENT = '机器人任务动作执行实例表';

-- ----------------------------
-- 数据字典：任务类型/状态/指派方式/步骤状态
-- ----------------------------
INSERT INTO sys_dict_type
    (dict_id, tenant_id, dict_name, dict_type, create_dept, create_by, create_time, update_by, update_time, remark)
SELECT 20500, '000000', '机器人任务类型', 'robot_task_type', 103, 1, SYSDATE(), NULL, NULL, '机器人任务类型'
WHERE NOT EXISTS (SELECT 1 FROM sys_dict_type WHERE dict_type = 'robot_task_type');

INSERT INTO sys_dict_data
    (dict_code, tenant_id, dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default,
     create_dept, create_by, create_time, update_by, update_time, remark)
SELECT 20501, '000000', 1, '模板任务', 'template', 'robot_task_type', '', 'primary', 'Y', 103, 1, SYSDATE(), NULL, NULL, ''
WHERE NOT EXISTS (SELECT 1 FROM sys_dict_data WHERE dict_type = 'robot_task_type' AND dict_value = 'template');
INSERT INTO sys_dict_data
    (dict_code, tenant_id, dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default,
     create_dept, create_by, create_time, update_by, update_time, remark)
SELECT 20502, '000000', 2, '临时任务', 'temporary', 'robot_task_type', '', 'success', 'N', 103, 1, SYSDATE(), NULL, NULL, ''
WHERE NOT EXISTS (SELECT 1 FROM sys_dict_data WHERE dict_type = 'robot_task_type' AND dict_value = 'temporary');

INSERT INTO sys_dict_type
    (dict_id, tenant_id, dict_name, dict_type, create_dept, create_by, create_time, update_by, update_time, remark)
SELECT 20510, '000000', '机器人任务状态', 'robot_task_status', 103, 1, SYSDATE(), NULL, NULL, '机器人任务状态'
WHERE NOT EXISTS (SELECT 1 FROM sys_dict_type WHERE dict_type = 'robot_task_status');

INSERT INTO sys_dict_data
    (dict_code, tenant_id, dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default,
     create_dept, create_by, create_time, update_by, update_time, remark)
SELECT 20511, '000000', 1, '未开始', 'pending', 'robot_task_status', '', 'default', 'Y', 103, 1, SYSDATE(), NULL, NULL, ''
WHERE NOT EXISTS (SELECT 1 FROM sys_dict_data WHERE dict_type = 'robot_task_status' AND dict_value = 'pending');
INSERT INTO sys_dict_data
    (dict_code, tenant_id, dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default,
     create_dept, create_by, create_time, update_by, update_time, remark)
SELECT 20512, '000000', 2, '进行中', 'running', 'robot_task_status', '', 'primary', 'N', 103, 1, SYSDATE(), NULL, NULL, ''
WHERE NOT EXISTS (SELECT 1 FROM sys_dict_data WHERE dict_type = 'robot_task_status' AND dict_value = 'running');
INSERT INTO sys_dict_data
    (dict_code, tenant_id, dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default,
     create_dept, create_by, create_time, update_by, update_time, remark)
SELECT 20513, '000000', 3, '执行异常', 'abnormal', 'robot_task_status', '', 'danger', 'N', 103, 1, SYSDATE(), NULL, NULL, ''
WHERE NOT EXISTS (SELECT 1 FROM sys_dict_data WHERE dict_type = 'robot_task_status' AND dict_value = 'abnormal');
INSERT INTO sys_dict_data
    (dict_code, tenant_id, dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default,
     create_dept, create_by, create_time, update_by, update_time, remark)
SELECT 20514, '000000', 4, '取消执行', 'canceled', 'robot_task_status', '', 'warning', 'N', 103, 1, SYSDATE(), NULL, NULL, ''
WHERE NOT EXISTS (SELECT 1 FROM sys_dict_data WHERE dict_type = 'robot_task_status' AND dict_value = 'canceled');
INSERT INTO sys_dict_data
    (dict_code, tenant_id, dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default,
     create_dept, create_by, create_time, update_by, update_time, remark)
SELECT 20515, '000000', 5, '执行完成', 'completed', 'robot_task_status', '', 'success', 'N', 103, 1, SYSDATE(), NULL, NULL, ''
WHERE NOT EXISTS (SELECT 1 FROM sys_dict_data WHERE dict_type = 'robot_task_status' AND dict_value = 'completed');

INSERT INTO sys_dict_type
    (dict_id, tenant_id, dict_name, dict_type, create_dept, create_by, create_time, update_by, update_time, remark)
SELECT 20520, '000000', '机器人任务车辆方式', 'robot_task_assign_mode', 103, 1, SYSDATE(), NULL, NULL, '机器人任务车辆方式'
WHERE NOT EXISTS (SELECT 1 FROM sys_dict_type WHERE dict_type = 'robot_task_assign_mode');

INSERT INTO sys_dict_data
    (dict_code, tenant_id, dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default,
     create_dept, create_by, create_time, update_by, update_time, remark)
SELECT 20521, '000000', 1, '指派', 'assign', 'robot_task_assign_mode', '', 'primary', 'Y', 103, 1, SYSDATE(), NULL, NULL, ''
WHERE NOT EXISTS (SELECT 1 FROM sys_dict_data WHERE dict_type = 'robot_task_assign_mode' AND dict_value = 'assign');
INSERT INTO sys_dict_data
    (dict_code, tenant_id, dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default,
     create_dept, create_by, create_time, update_by, update_time, remark)
SELECT 20522, '000000', 2, '调派', 'dispatch', 'robot_task_assign_mode', '', 'success', 'N', 103, 1, SYSDATE(), NULL, NULL, ''
WHERE NOT EXISTS (SELECT 1 FROM sys_dict_data WHERE dict_type = 'robot_task_assign_mode' AND dict_value = 'dispatch');

INSERT INTO sys_dict_type
    (dict_id, tenant_id, dict_name, dict_type, create_dept, create_by, create_time, update_by, update_time, remark)
SELECT 20530, '000000', '机器人任务步骤状态', 'robot_task_step_status', 103, 1, SYSDATE(), NULL, NULL, '机器人任务步骤状态'
WHERE NOT EXISTS (SELECT 1 FROM sys_dict_type WHERE dict_type = 'robot_task_step_status');

INSERT INTO sys_dict_data
    (dict_code, tenant_id, dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default,
     create_dept, create_by, create_time, update_by, update_time, remark)
SELECT 20531, '000000', 1, '未开始', 'pending', 'robot_task_step_status', '', 'default', 'Y', 103, 1, SYSDATE(), NULL, NULL, ''
WHERE NOT EXISTS (SELECT 1 FROM sys_dict_data WHERE dict_type = 'robot_task_step_status' AND dict_value = 'pending');
INSERT INTO sys_dict_data
    (dict_code, tenant_id, dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default,
     create_dept, create_by, create_time, update_by, update_time, remark)
SELECT 20532, '000000', 2, '执行中', 'running', 'robot_task_step_status', '', 'primary', 'N', 103, 1, SYSDATE(), NULL, NULL, ''
WHERE NOT EXISTS (SELECT 1 FROM sys_dict_data WHERE dict_type = 'robot_task_step_status' AND dict_value = 'running');
INSERT INTO sys_dict_data
    (dict_code, tenant_id, dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default,
     create_dept, create_by, create_time, update_by, update_time, remark)
SELECT 20533, '000000', 3, '成功', 'success', 'robot_task_step_status', '', 'success', 'N', 103, 1, SYSDATE(), NULL, NULL, ''
WHERE NOT EXISTS (SELECT 1 FROM sys_dict_data WHERE dict_type = 'robot_task_step_status' AND dict_value = 'success');
INSERT INTO sys_dict_data
    (dict_code, tenant_id, dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default,
     create_dept, create_by, create_time, update_by, update_time, remark)
SELECT 20534, '000000', 4, '失败', 'fail', 'robot_task_step_status', '', 'danger', 'N', 103, 1, SYSDATE(), NULL, NULL, ''
WHERE NOT EXISTS (SELECT 1 FROM sys_dict_data WHERE dict_type = 'robot_task_step_status' AND dict_value = 'fail');
INSERT INTO sys_dict_data
    (dict_code, tenant_id, dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default,
     create_dept, create_by, create_time, update_by, update_time, remark)
SELECT 20535, '000000', 5, '跳过', 'skipped', 'robot_task_step_status', '', 'warning', 'N', 103, 1, SYSDATE(), NULL, NULL, ''
WHERE NOT EXISTS (SELECT 1 FROM sys_dict_data WHERE dict_type = 'robot_task_step_status' AND dict_value = 'skipped');

-- ----------------------------
-- 菜单权限：任务列表
-- ----------------------------
INSERT INTO sys_menu
    (menu_id, menu_name, parent_id, order_num, path, component, query_param,
     is_frame, is_cache, menu_type, visible, status, perms, icon,
     create_dept, create_by, create_time, update_by, update_time, remark)
SELECT 20420, '任务列表', 20400, 3, 'robot-task', 'manager/task/robot-task/index', '', 1, 0, 'C', '0', '0',
       'manager:robotTask:list', 'list-checks', 103, 1, SYSDATE(), NULL, NULL, '机器人任务列表菜单'
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE menu_id = 20420);

INSERT INTO sys_menu
    (menu_id, menu_name, parent_id, order_num, path, component, query_param,
     is_frame, is_cache, menu_type, visible, status, perms, icon,
     create_dept, create_by, create_time, update_by, update_time, remark)
SELECT 20421, '任务查询', 20420, 1, '#', '', '', 1, 0, 'F', '0', '0',
       'manager:robotTask:query', '#', 103, 1, SYSDATE(), NULL, NULL, ''
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE menu_id = 20421);
INSERT INTO sys_menu
    (menu_id, menu_name, parent_id, order_num, path, component, query_param,
     is_frame, is_cache, menu_type, visible, status, perms, icon,
     create_dept, create_by, create_time, update_by, update_time, remark)
SELECT 20422, '任务新增', 20420, 2, '#', '', '', 1, 0, 'F', '0', '0',
       'manager:robotTask:add', '#', 103, 1, SYSDATE(), NULL, NULL, ''
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE menu_id = 20422);
INSERT INTO sys_menu
    (menu_id, menu_name, parent_id, order_num, path, component, query_param,
     is_frame, is_cache, menu_type, visible, status, perms, icon,
     create_dept, create_by, create_time, update_by, update_time, remark)
SELECT 20423, '任务修改', 20420, 3, '#', '', '', 1, 0, 'F', '0', '0',
       'manager:robotTask:edit', '#', 103, 1, SYSDATE(), NULL, NULL, ''
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE menu_id = 20423);
INSERT INTO sys_menu
    (menu_id, menu_name, parent_id, order_num, path, component, query_param,
     is_frame, is_cache, menu_type, visible, status, perms, icon,
     create_dept, create_by, create_time, update_by, update_time, remark)
SELECT 20424, '任务删除', 20420, 4, '#', '', '', 1, 0, 'F', '0', '0',
       'manager:robotTask:remove', '#', 103, 1, SYSDATE(), NULL, NULL, ''
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE menu_id = 20424);
INSERT INTO sys_menu
    (menu_id, menu_name, parent_id, order_num, path, component, query_param,
     is_frame, is_cache, menu_type, visible, status, perms, icon,
     create_dept, create_by, create_time, update_by, update_time, remark)
SELECT 20425, '任务开始', 20420, 5, '#', '', '', 1, 0, 'F', '0', '0',
       'manager:robotTask:start', '#', 103, 1, SYSDATE(), NULL, NULL, ''
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE menu_id = 20425);
INSERT INTO sys_menu
    (menu_id, menu_name, parent_id, order_num, path, component, query_param,
     is_frame, is_cache, menu_type, visible, status, perms, icon,
     create_dept, create_by, create_time, update_by, update_time, remark)
SELECT 20426, '任务取消', 20420, 6, '#', '', '', 1, 0, 'F', '0', '0',
       'manager:robotTask:cancel', '#', 103, 1, SYSDATE(), NULL, NULL, ''
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE menu_id = 20426);
INSERT INTO sys_menu
    (menu_id, menu_name, parent_id, order_num, path, component, query_param,
     is_frame, is_cache, menu_type, visible, status, perms, icon,
     create_dept, create_by, create_time, update_by, update_time, remark)
SELECT 20427, '任务导出', 20420, 7, '#', '', '', 1, 0, 'F', '0', '0',
       'manager:robotTask:export', '#', 103, 1, SYSDATE(), NULL, NULL, ''
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE menu_id = 20427);
