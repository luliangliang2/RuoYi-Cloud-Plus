-- ----------------------------
-- 指令配置表
-- ----------------------------
CREATE TABLE IF NOT EXISTS biz_command_config
(
    command_id       BIGINT        NOT NULL COMMENT '指令ID',
    tenant_id        VARCHAR(20)   DEFAULT '000000' COMMENT '租户编号',
    tree_id          BIGINT        DEFAULT NULL COMMENT '分类树ID',
    category_node_id BIGINT        DEFAULT NULL COMMENT '分类节点ID',
    command_code     VARCHAR(64)   NOT NULL COMMENT '指令编码',
    command_name     VARCHAR(100)  NOT NULL COMMENT '指令名称',
    command_type     VARCHAR(32)   NOT NULL COMMENT '指令类型（single单指令 multiple多指令）',
    command_template TEXT          NOT NULL COMMENT '指令JSON模板',
    status           CHAR(1)       DEFAULT '0' COMMENT '状态（0正常 1停用）',
    del_flag         CHAR(1)       DEFAULT '0' COMMENT '删除标志（0代表存在 1代表删除）',
    create_dept      BIGINT        DEFAULT NULL COMMENT '创建部门',
    create_by        BIGINT        DEFAULT NULL COMMENT '创建者',
    create_time      DATETIME      DEFAULT NULL COMMENT '创建时间',
    update_by        BIGINT        DEFAULT NULL COMMENT '更新者',
    update_time      DATETIME      DEFAULT NULL COMMENT '更新时间',
    remark           VARCHAR(500)  DEFAULT NULL COMMENT '备注',
    PRIMARY KEY (command_id),
    KEY idx_biz_command_config_tenant_code (tenant_id, command_code),
    KEY idx_biz_command_config_category (tenant_id, tree_id, category_node_id),
    KEY idx_biz_command_config_type (tenant_id, command_type),
    KEY idx_biz_command_config_status (tenant_id, status, del_flag)
) ENGINE = InnoDB COMMENT = '指令配置表';

-- ----------------------------
-- 指令类型字典
-- ----------------------------
INSERT INTO sys_dict_type
    (dict_id, tenant_id, dict_name, dict_type, create_dept, create_by, create_time, update_by, update_time, remark)
SELECT 20004, '000000', '指令配置类型', 'command_config_type', 103, 1, SYSDATE(), NULL, NULL, '指令配置类型'
WHERE NOT EXISTS (SELECT 1 FROM sys_dict_type WHERE dict_type = 'command_config_type');

INSERT INTO sys_dict_data
    (dict_code, tenant_id, dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default,
     create_dept, create_by, create_time, update_by, update_time, remark)
SELECT 20041, '000000', 1, '单指令', 'single', 'command_config_type', '', 'primary', 'Y', 103, 1, SYSDATE(), NULL, NULL, ''
WHERE NOT EXISTS (SELECT 1 FROM sys_dict_data WHERE dict_code = 20041);
INSERT INTO sys_dict_data
    (dict_code, tenant_id, dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default,
     create_dept, create_by, create_time, update_by, update_time, remark)
SELECT 20042, '000000', 2, '多指令', 'multiple', 'command_config_type', '', 'success', 'N', 103, 1, SYSDATE(), NULL, NULL, ''
WHERE NOT EXISTS (SELECT 1 FROM sys_dict_data WHERE dict_code = 20042);

-- ----------------------------
-- 菜单权限
-- ----------------------------
INSERT INTO sys_menu
    (menu_id, menu_name, parent_id, order_num, path, component, query_param,
     is_frame, is_cache, menu_type, visible, status, perms, icon,
     create_dept, create_by, create_time, update_by, update_time, remark)
SELECT 20070, '指令配置', 20000, 3, 'commandConfig', 'manager/commandConfig/index', '', 1, 0, 'C', '0', '0',
       'manager:commandConfig:list', 'code', 103, 1, SYSDATE(), NULL, NULL, '指令配置菜单'
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE menu_id = 20070);

INSERT INTO sys_menu
    (menu_id, menu_name, parent_id, order_num, path, component, query_param,
     is_frame, is_cache, menu_type, visible, status, perms, icon,
     create_dept, create_by, create_time, update_by, update_time, remark)
SELECT 20071, '指令配置查询', 20070, 1, '#', '', '', 1, 0, 'F', '0', '0',
       'manager:commandConfig:query', '#', 103, 1, SYSDATE(), NULL, NULL, ''
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE menu_id = 20071);
INSERT INTO sys_menu
    (menu_id, menu_name, parent_id, order_num, path, component, query_param,
     is_frame, is_cache, menu_type, visible, status, perms, icon,
     create_dept, create_by, create_time, update_by, update_time, remark)
SELECT 20072, '指令配置新增', 20070, 2, '#', '', '', 1, 0, 'F', '0', '0',
       'manager:commandConfig:add', '#', 103, 1, SYSDATE(), NULL, NULL, ''
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE menu_id = 20072);
INSERT INTO sys_menu
    (menu_id, menu_name, parent_id, order_num, path, component, query_param,
     is_frame, is_cache, menu_type, visible, status, perms, icon,
     create_dept, create_by, create_time, update_by, update_time, remark)
SELECT 20073, '指令配置修改', 20070, 3, '#', '', '', 1, 0, 'F', '0', '0',
       'manager:commandConfig:edit', '#', 103, 1, SYSDATE(), NULL, NULL, ''
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE menu_id = 20073);
INSERT INTO sys_menu
    (menu_id, menu_name, parent_id, order_num, path, component, query_param,
     is_frame, is_cache, menu_type, visible, status, perms, icon,
     create_dept, create_by, create_time, update_by, update_time, remark)
SELECT 20074, '指令配置删除', 20070, 4, '#', '', '', 1, 0, 'F', '0', '0',
       'manager:commandConfig:remove', '#', 103, 1, SYSDATE(), NULL, NULL, ''
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE menu_id = 20074);
INSERT INTO sys_menu
    (menu_id, menu_name, parent_id, order_num, path, component, query_param,
     is_frame, is_cache, menu_type, visible, status, perms, icon,
     create_dept, create_by, create_time, update_by, update_time, remark)
SELECT 20075, '指令配置导出', 20070, 5, '#', '', '', 1, 0, 'F', '0', '0',
       'manager:commandConfig:export', '#', 103, 1, SYSDATE(), NULL, NULL, ''
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE menu_id = 20075);
