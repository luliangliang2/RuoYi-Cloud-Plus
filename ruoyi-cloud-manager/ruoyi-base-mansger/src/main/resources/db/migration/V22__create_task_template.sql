-- ----------------------------
-- 任务模板表
-- ----------------------------
CREATE TABLE IF NOT EXISTS biz_task_template
(
    template_id   BIGINT        NOT NULL COMMENT '模板ID',
    tenant_id     VARCHAR(20)   DEFAULT '000000' COMMENT '租户编号',
    template_code VARCHAR(64)   NOT NULL COMMENT '模板编码',
    template_name VARCHAR(100)  NOT NULL COMMENT '模板名称',
    route_id      BIGINT        NOT NULL COMMENT '路线ID',
    template_desc VARCHAR(1000) DEFAULT NULL COMMENT '任务说明',
    status        CHAR(1)       DEFAULT '0' COMMENT '状态（0正常 1停用）',
    del_flag      CHAR(1)       DEFAULT '0' COMMENT '删除标志（0代表存在 1代表删除）',
    create_dept   BIGINT        DEFAULT NULL COMMENT '创建部门',
    create_by     BIGINT        DEFAULT NULL COMMENT '创建者',
    create_time   DATETIME      DEFAULT NULL COMMENT '创建时间',
    update_by     BIGINT        DEFAULT NULL COMMENT '更新者',
    update_time   DATETIME      DEFAULT NULL COMMENT '更新时间',
    remark        VARCHAR(500)  DEFAULT NULL COMMENT '备注',
    PRIMARY KEY (template_id),
    KEY idx_biz_task_template_tenant_code (tenant_id, template_code),
    KEY idx_biz_task_template_route (tenant_id, route_id),
    KEY idx_biz_task_template_status (tenant_id, status, del_flag)
) ENGINE = InnoDB COMMENT = '任务模板表';

-- ----------------------------
-- 任务模板点位编排表
-- ----------------------------
CREATE TABLE IF NOT EXISTS biz_task_template_point
(
    template_point_id BIGINT       NOT NULL COMMENT '模板点位ID',
    tenant_id         VARCHAR(20)  DEFAULT '000000' COMMENT '租户编号',
    template_id       BIGINT       NOT NULL COMMENT '模板ID',
    route_id          BIGINT       NOT NULL COMMENT '路线ID',
    point_id          BIGINT       NOT NULL COMMENT '点位ID',
    point_name        VARCHAR(100) NOT NULL COMMENT '点位名称快照',
    sequence          INT          DEFAULT 1 COMMENT '到达顺序',
    required_flag     CHAR(1)      DEFAULT '1' COMMENT '是否必须到达（0否 1是）',
    del_flag          CHAR(1)      DEFAULT '0' COMMENT '删除标志（0代表存在 1代表删除）',
    create_dept       BIGINT       DEFAULT NULL COMMENT '创建部门',
    create_by         BIGINT       DEFAULT NULL COMMENT '创建者',
    create_time       DATETIME     DEFAULT NULL COMMENT '创建时间',
    update_by         BIGINT       DEFAULT NULL COMMENT '更新者',
    update_time       DATETIME     DEFAULT NULL COMMENT '更新时间',
    remark            VARCHAR(500) DEFAULT NULL COMMENT '备注',
    PRIMARY KEY (template_point_id),
    KEY idx_biz_task_tpl_point_template (tenant_id, template_id, sequence),
    KEY idx_biz_task_tpl_point_route (tenant_id, route_id, point_id)
) ENGINE = InnoDB COMMENT = '任务模板点位编排表';

-- ----------------------------
-- 任务模板点位动作表
-- ----------------------------
CREATE TABLE IF NOT EXISTS biz_task_template_point_action
(
    template_action_id BIGINT       NOT NULL COMMENT '模板动作ID',
    tenant_id          VARCHAR(20)  DEFAULT '000000' COMMENT '租户编号',
    template_id        BIGINT       NOT NULL COMMENT '模板ID',
    template_point_id  BIGINT       NOT NULL COMMENT '模板点位ID',
    point_id           BIGINT       NOT NULL COMMENT '点位ID',
    action_id          BIGINT       NOT NULL COMMENT '动作ID',
    action_code        VARCHAR(64)  NOT NULL COMMENT '动作编码快照',
    action_name        VARCHAR(100) NOT NULL COMMENT '动作名称快照',
    action_type        VARCHAR(32)  NOT NULL COMMENT '动作类型快照',
    sequence           INT          DEFAULT 1 COMMENT '动作顺序',
    action_params      TEXT         DEFAULT NULL COMMENT '当前点位定制动作参数JSON',
    del_flag           CHAR(1)      DEFAULT '0' COMMENT '删除标志（0代表存在 1代表删除）',
    create_dept        BIGINT       DEFAULT NULL COMMENT '创建部门',
    create_by          BIGINT       DEFAULT NULL COMMENT '创建者',
    create_time        DATETIME     DEFAULT NULL COMMENT '创建时间',
    update_by          BIGINT       DEFAULT NULL COMMENT '更新者',
    update_time        DATETIME     DEFAULT NULL COMMENT '更新时间',
    remark             VARCHAR(500) DEFAULT NULL COMMENT '备注',
    PRIMARY KEY (template_action_id),
    KEY idx_biz_task_tpl_action_template (tenant_id, template_id, template_point_id, sequence),
    KEY idx_biz_task_tpl_action_action (tenant_id, action_id)
) ENGINE = InnoDB COMMENT = '任务模板点位动作表';

-- ----------------------------
-- 菜单权限
-- ----------------------------
INSERT INTO sys_menu
    (menu_id, menu_name, parent_id, order_num, path, component, query_param,
     is_frame, is_cache, menu_type, visible, status, perms, icon,
     create_dept, create_by, create_time, update_by, update_time, remark)
SELECT 20410, '任务模板', 20400, 2, 'template', 'manager/task/template/index', '', 1, 0, 'C', '0', '0',
       'manager:taskTemplate:list', 'clipboard-list', 103, 1, SYSDATE(), NULL, NULL, '任务模板菜单'
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE menu_id = 20410);

INSERT INTO sys_menu
    (menu_id, menu_name, parent_id, order_num, path, component, query_param,
     is_frame, is_cache, menu_type, visible, status, perms, icon,
     create_dept, create_by, create_time, update_by, update_time, remark)
SELECT 20411, '任务模板查询', 20410, 1, '#', '', '', 1, 0, 'F', '0', '0',
       'manager:taskTemplate:query', '#', 103, 1, SYSDATE(), NULL, NULL, ''
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE menu_id = 20411);
INSERT INTO sys_menu
    (menu_id, menu_name, parent_id, order_num, path, component, query_param,
     is_frame, is_cache, menu_type, visible, status, perms, icon,
     create_dept, create_by, create_time, update_by, update_time, remark)
SELECT 20412, '任务模板新增', 20410, 2, '#', '', '', 1, 0, 'F', '0', '0',
       'manager:taskTemplate:add', '#', 103, 1, SYSDATE(), NULL, NULL, ''
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE menu_id = 20412);
INSERT INTO sys_menu
    (menu_id, menu_name, parent_id, order_num, path, component, query_param,
     is_frame, is_cache, menu_type, visible, status, perms, icon,
     create_dept, create_by, create_time, update_by, update_time, remark)
SELECT 20413, '任务模板修改', 20410, 3, '#', '', '', 1, 0, 'F', '0', '0',
       'manager:taskTemplate:edit', '#', 103, 1, SYSDATE(), NULL, NULL, ''
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE menu_id = 20413);
INSERT INTO sys_menu
    (menu_id, menu_name, parent_id, order_num, path, component, query_param,
     is_frame, is_cache, menu_type, visible, status, perms, icon,
     create_dept, create_by, create_time, update_by, update_time, remark)
SELECT 20414, '任务模板删除', 20410, 4, '#', '', '', 1, 0, 'F', '0', '0',
       'manager:taskTemplate:remove', '#', 103, 1, SYSDATE(), NULL, NULL, ''
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE menu_id = 20414);
INSERT INTO sys_menu
    (menu_id, menu_name, parent_id, order_num, path, component, query_param,
     is_frame, is_cache, menu_type, visible, status, perms, icon,
     create_dept, create_by, create_time, update_by, update_time, remark)
SELECT 20415, '任务模板导出', 20410, 5, '#', '', '', 1, 0, 'F', '0', '0',
       'manager:taskTemplate:export', '#', 103, 1, SYSDATE(), NULL, NULL, ''
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE menu_id = 20415);
INSERT INTO sys_menu
    (menu_id, menu_name, parent_id, order_num, path, component, query_param,
     is_frame, is_cache, menu_type, visible, status, perms, icon,
     create_dept, create_by, create_time, update_by, update_time, remark)
SELECT 20416, '任务模板预览指令', 20410, 6, '#', '', '', 1, 0, 'F', '0', '0',
       'manager:taskTemplate:preview', '#', 103, 1, SYSDATE(), NULL, NULL, ''
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE menu_id = 20416);
