-- ----------------------------
-- 机器人动作定义表
-- ----------------------------
CREATE TABLE IF NOT EXISTS biz_robot_action_def
(
    action_id       BIGINT        NOT NULL COMMENT '动作ID',
    tenant_id       VARCHAR(20)   DEFAULT '000000' COMMENT '租户编号',
    action_code     VARCHAR(64)   NOT NULL COMMENT '动作唯一编码',
    action_name     VARCHAR(100)  NOT NULL COMMENT '动作名称',
    action_type     VARCHAR(32)   NOT NULL COMMENT '动作类型（trigger触发一次 continuous持续动作）',
    params_template TEXT          DEFAULT NULL COMMENT '动作参数模板JSON',
    description     VARCHAR(500)  DEFAULT NULL COMMENT '动作描述',
    sort_order      INT           DEFAULT 0 COMMENT '显示顺序',
    status          CHAR(1)       DEFAULT '0' COMMENT '状态（0正常 1停用）',
    del_flag        CHAR(1)       DEFAULT '0' COMMENT '删除标志（0代表存在 1代表删除）',
    create_dept     BIGINT        DEFAULT NULL COMMENT '创建部门',
    create_by       BIGINT        DEFAULT NULL COMMENT '创建者',
    create_time     DATETIME      DEFAULT NULL COMMENT '创建时间',
    update_by       BIGINT        DEFAULT NULL COMMENT '更新者',
    update_time     DATETIME      DEFAULT NULL COMMENT '更新时间',
    remark          VARCHAR(500)  DEFAULT NULL COMMENT '备注',
    PRIMARY KEY (action_id),
    KEY idx_biz_robot_action_tenant_code (tenant_id, action_code),
    KEY idx_biz_robot_action_type (tenant_id, action_type),
    KEY idx_biz_robot_action_status (tenant_id, status, del_flag)
) ENGINE = InnoDB COMMENT = '机器人动作定义表';

-- ----------------------------
-- 动作类型字典
-- ----------------------------
INSERT INTO sys_dict_type
    (dict_id, tenant_id, dict_name, dict_type, create_dept, create_by, create_time, update_by, update_time, remark)
SELECT 20410, '000000', '机器人动作类型', 'robot_action_type', 103, 1, SYSDATE(), NULL, NULL, '机器人动作类型'
WHERE NOT EXISTS (SELECT 1 FROM sys_dict_type WHERE dict_type = 'robot_action_type');

INSERT INTO sys_dict_data
    (dict_code, tenant_id, dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default,
     create_dept, create_by, create_time, update_by, update_time, remark)
SELECT 20411, '000000', 1, '触发动作', 'trigger', 'robot_action_type', '', 'primary', 'Y', 103, 1, SYSDATE(), NULL, NULL, '触发一次的瞬时动作'
WHERE NOT EXISTS (SELECT 1 FROM sys_dict_data WHERE dict_code = 20411);

INSERT INTO sys_dict_data
    (dict_code, tenant_id, dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default,
     create_dept, create_by, create_time, update_by, update_time, remark)
SELECT 20412, '000000', 2, '持续动作', 'continuous', 'robot_action_type', '', 'success', 'N', 103, 1, SYSDATE(), NULL, NULL, '需要持续执行或成对启停的动作'
WHERE NOT EXISTS (SELECT 1 FROM sys_dict_data WHERE dict_code = 20412);

-- ----------------------------
-- 菜单权限
-- ----------------------------
INSERT INTO sys_menu
    (menu_id, menu_name, parent_id, order_num, path, component, query_param,
     is_frame, is_cache, menu_type, visible, status, perms, icon,
     create_dept, create_by, create_time, update_by, update_time, remark)
SELECT 20400, '任务管理', 0, 11, 'task', NULL, '', 1, 0, 'M', '0', '0', '', 'workflow',
       103, 1, SYSDATE(), NULL, NULL, '机器人任务管理目录'
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE menu_id = 20400);

INSERT INTO sys_menu
    (menu_id, menu_name, parent_id, order_num, path, component, query_param,
     is_frame, is_cache, menu_type, visible, status, perms, icon,
     create_dept, create_by, create_time, update_by, update_time, remark)
SELECT 20401, '动作维护', 20400, 1, 'action', 'manager/task/action/index', '', 1, 0, 'C', '0', '0',
       'manager:robotAction:list', 'settings-2', 103, 1, SYSDATE(), NULL, NULL, '机器人动作维护菜单'
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE menu_id = 20401);

INSERT INTO sys_menu
    (menu_id, menu_name, parent_id, order_num, path, component, query_param,
     is_frame, is_cache, menu_type, visible, status, perms, icon,
     create_dept, create_by, create_time, update_by, update_time, remark)
SELECT 20402, '动作维护查询', 20401, 1, '#', '', '', 1, 0, 'F', '0', '0',
       'manager:robotAction:query', '#', 103, 1, SYSDATE(), NULL, NULL, ''
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE menu_id = 20402);

INSERT INTO sys_menu
    (menu_id, menu_name, parent_id, order_num, path, component, query_param,
     is_frame, is_cache, menu_type, visible, status, perms, icon,
     create_dept, create_by, create_time, update_by, update_time, remark)
SELECT 20403, '动作维护新增', 20401, 2, '#', '', '', 1, 0, 'F', '0', '0',
       'manager:robotAction:add', '#', 103, 1, SYSDATE(), NULL, NULL, ''
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE menu_id = 20403);

INSERT INTO sys_menu
    (menu_id, menu_name, parent_id, order_num, path, component, query_param,
     is_frame, is_cache, menu_type, visible, status, perms, icon,
     create_dept, create_by, create_time, update_by, update_time, remark)
SELECT 20404, '动作维护修改', 20401, 3, '#', '', '', 1, 0, 'F', '0', '0',
       'manager:robotAction:edit', '#', 103, 1, SYSDATE(), NULL, NULL, ''
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE menu_id = 20404);

INSERT INTO sys_menu
    (menu_id, menu_name, parent_id, order_num, path, component, query_param,
     is_frame, is_cache, menu_type, visible, status, perms, icon,
     create_dept, create_by, create_time, update_by, update_time, remark)
SELECT 20405, '动作维护删除', 20401, 4, '#', '', '', 1, 0, 'F', '0', '0',
       'manager:robotAction:remove', '#', 103, 1, SYSDATE(), NULL, NULL, ''
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE menu_id = 20405);

INSERT INTO sys_menu
    (menu_id, menu_name, parent_id, order_num, path, component, query_param,
     is_frame, is_cache, menu_type, visible, status, perms, icon,
     create_dept, create_by, create_time, update_by, update_time, remark)
SELECT 20406, '动作维护导出', 20401, 5, '#', '', '', 1, 0, 'F', '0', '0',
       'manager:robotAction:export', '#', 103, 1, SYSDATE(), NULL, NULL, ''
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE menu_id = 20406);
