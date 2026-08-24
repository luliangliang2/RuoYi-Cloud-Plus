-- ----------------------------
-- 第三方API配置表
-- ----------------------------
CREATE TABLE IF NOT EXISTS biz_third_api_config
(
    config_id          BIGINT        NOT NULL COMMENT '配置ID',
    tenant_id          VARCHAR(20)   DEFAULT '000000' COMMENT '租户编号',
    api_name           VARCHAR(100)  NOT NULL COMMENT 'API名称',
    api_code           VARCHAR(64)   NOT NULL COMMENT 'API编码',
    api_category       VARCHAR(64)   NOT NULL COMMENT 'API分类',
    provider_name      VARCHAR(100)  DEFAULT NULL COMMENT '服务商名称',
    app_id             VARCHAR(128)  DEFAULT NULL COMMENT '应用ID',
    app_key            VARCHAR(128)  DEFAULT NULL COMMENT '应用Key',
    app_secret         VARCHAR(256)  DEFAULT NULL COMMENT '应用密钥',
    app_code           VARCHAR(128)  DEFAULT NULL COMMENT '应用Code',
    endpoint_url       VARCHAR(500)  DEFAULT NULL COMMENT '接口地址',
    auth_type          VARCHAR(32)   DEFAULT NULL COMMENT '认证方式',
    billing_start_time DATETIME      DEFAULT NULL COMMENT '计费开始时间',
    billing_end_time   DATETIME      DEFAULT NULL COMMENT '计费到期时间',
    ext_json           VARCHAR(2000) DEFAULT NULL COMMENT '扩展参数JSON',
    status             CHAR(1)       DEFAULT '0' COMMENT '状态（0正常 1停用）',
    del_flag           CHAR(1)       DEFAULT '0' COMMENT '删除标志（0代表存在 1代表删除）',
    create_dept        BIGINT        DEFAULT NULL COMMENT '创建部门',
    create_by          BIGINT        DEFAULT NULL COMMENT '创建者',
    create_time        DATETIME      DEFAULT NULL COMMENT '创建时间',
    update_by          BIGINT        DEFAULT NULL COMMENT '更新者',
    update_time        DATETIME      DEFAULT NULL COMMENT '更新时间',
    remark             VARCHAR(500)  DEFAULT NULL COMMENT '备注',
    PRIMARY KEY (config_id),
    KEY idx_biz_third_api_tenant_code (tenant_id, api_code),
    KEY idx_biz_third_api_category (tenant_id, api_category),
    KEY idx_biz_third_api_status (tenant_id, status, del_flag)
) ENGINE = InnoDB COMMENT = '第三方API配置表';

-- ----------------------------
-- 第三方API分类字典
-- ----------------------------
INSERT INTO sys_dict_type
    (dict_id, tenant_id, dict_name, dict_type, create_dept, create_by, create_time, update_by, update_time, remark)
SELECT 20002, '000000', '第三方API分类', 'third_api_category', 103, 1, SYSDATE(), NULL, NULL, '第三方API配置分类'
WHERE NOT EXISTS (SELECT 1 FROM sys_dict_type WHERE dict_type = 'third_api_category');

INSERT INTO sys_dict_data
    (dict_code, tenant_id, dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default,
     create_dept, create_by, create_time, update_by, update_time, remark)
SELECT 20021, '000000', 1, '地图服务', 'map', 'third_api_category', '', 'primary', 'N', 103, 1, SYSDATE(), NULL, NULL, ''
WHERE NOT EXISTS (SELECT 1 FROM sys_dict_data WHERE dict_code = 20021);
INSERT INTO sys_dict_data
    (dict_code, tenant_id, dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default,
     create_dept, create_by, create_time, update_by, update_time, remark)
SELECT 20022, '000000', 2, '短信服务', 'sms', 'third_api_category', '', 'success', 'N', 103, 1, SYSDATE(), NULL, NULL, ''
WHERE NOT EXISTS (SELECT 1 FROM sys_dict_data WHERE dict_code = 20022);
INSERT INTO sys_dict_data
    (dict_code, tenant_id, dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default,
     create_dept, create_by, create_time, update_by, update_time, remark)
SELECT 20023, '000000', 3, '支付服务', 'pay', 'third_api_category', '', 'warning', 'N', 103, 1, SYSDATE(), NULL, NULL, ''
WHERE NOT EXISTS (SELECT 1 FROM sys_dict_data WHERE dict_code = 20023);
INSERT INTO sys_dict_data
    (dict_code, tenant_id, dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default,
     create_dept, create_by, create_time, update_by, update_time, remark)
SELECT 20024, '000000', 4, '数据服务', 'data', 'third_api_category', '', 'info', 'N', 103, 1, SYSDATE(), NULL, NULL, ''
WHERE NOT EXISTS (SELECT 1 FROM sys_dict_data WHERE dict_code = 20024);
INSERT INTO sys_dict_data
    (dict_code, tenant_id, dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default,
     create_dept, create_by, create_time, update_by, update_time, remark)
SELECT 20025, '000000', 99, '其他', 'other', 'third_api_category', '', 'default', 'Y', 103, 1, SYSDATE(), NULL, NULL, ''
WHERE NOT EXISTS (SELECT 1 FROM sys_dict_data WHERE dict_code = 20025);

-- ----------------------------
-- 第三方API认证方式字典
-- ----------------------------
INSERT INTO sys_dict_type
    (dict_id, tenant_id, dict_name, dict_type, create_dept, create_by, create_time, update_by, update_time, remark)
SELECT 20003, '000000', '第三方API认证方式', 'third_api_auth_type', 103, 1, SYSDATE(), NULL, NULL, '第三方API认证方式'
WHERE NOT EXISTS (SELECT 1 FROM sys_dict_type WHERE dict_type = 'third_api_auth_type');

INSERT INTO sys_dict_data
    (dict_code, tenant_id, dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default,
     create_dept, create_by, create_time, update_by, update_time, remark)
SELECT 20031, '000000', 1, 'AppKey', 'app_key', 'third_api_auth_type', '', 'primary', 'Y', 103, 1, SYSDATE(), NULL, NULL, ''
WHERE NOT EXISTS (SELECT 1 FROM sys_dict_data WHERE dict_code = 20031);
INSERT INTO sys_dict_data
    (dict_code, tenant_id, dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default,
     create_dept, create_by, create_time, update_by, update_time, remark)
SELECT 20032, '000000', 2, 'AppCode', 'app_code', 'third_api_auth_type', '', 'success', 'N', 103, 1, SYSDATE(), NULL, NULL, ''
WHERE NOT EXISTS (SELECT 1 FROM sys_dict_data WHERE dict_code = 20032);
INSERT INTO sys_dict_data
    (dict_code, tenant_id, dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default,
     create_dept, create_by, create_time, update_by, update_time, remark)
SELECT 20033, '000000', 3, 'OAuth2', 'oauth2', 'third_api_auth_type', '', 'warning', 'N', 103, 1, SYSDATE(), NULL, NULL, ''
WHERE NOT EXISTS (SELECT 1 FROM sys_dict_data WHERE dict_code = 20033);
INSERT INTO sys_dict_data
    (dict_code, tenant_id, dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default,
     create_dept, create_by, create_time, update_by, update_time, remark)
SELECT 20034, '000000', 4, 'Token', 'token', 'third_api_auth_type', '', 'info', 'N', 103, 1, SYSDATE(), NULL, NULL, ''
WHERE NOT EXISTS (SELECT 1 FROM sys_dict_data WHERE dict_code = 20034);

-- ----------------------------
-- 菜单权限
-- ----------------------------
INSERT INTO sys_menu
    (menu_id, menu_name, parent_id, order_num, path, component, query_param,
     is_frame, is_cache, menu_type, visible, status, perms, icon,
     create_dept, create_by, create_time, update_by, update_time, remark)
SELECT 20040, '第三方API配置', 20000, 2, 'thirdApi', 'manager/thirdApi/index', '', 1, 0, 'C', '0', '0',
       'manager:thirdApi:list', 'api', 103, 1, SYSDATE(), NULL, NULL, '第三方API配置菜单'
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE menu_id = 20040);

INSERT INTO sys_menu
    (menu_id, menu_name, parent_id, order_num, path, component, query_param,
     is_frame, is_cache, menu_type, visible, status, perms, icon,
     create_dept, create_by, create_time, update_by, update_time, remark)
SELECT 20041, '第三方API查询', 20040, 1, '#', '', '', 1, 0, 'F', '0', '0',
       'manager:thirdApi:query', '#', 103, 1, SYSDATE(), NULL, NULL, ''
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE menu_id = 20041);
INSERT INTO sys_menu
    (menu_id, menu_name, parent_id, order_num, path, component, query_param,
     is_frame, is_cache, menu_type, visible, status, perms, icon,
     create_dept, create_by, create_time, update_by, update_time, remark)
SELECT 20042, '第三方API新增', 20040, 2, '#', '', '', 1, 0, 'F', '0', '0',
       'manager:thirdApi:add', '#', 103, 1, SYSDATE(), NULL, NULL, ''
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE menu_id = 20042);
INSERT INTO sys_menu
    (menu_id, menu_name, parent_id, order_num, path, component, query_param,
     is_frame, is_cache, menu_type, visible, status, perms, icon,
     create_dept, create_by, create_time, update_by, update_time, remark)
SELECT 20043, '第三方API修改', 20040, 3, '#', '', '', 1, 0, 'F', '0', '0',
       'manager:thirdApi:edit', '#', 103, 1, SYSDATE(), NULL, NULL, ''
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE menu_id = 20043);
INSERT INTO sys_menu
    (menu_id, menu_name, parent_id, order_num, path, component, query_param,
     is_frame, is_cache, menu_type, visible, status, perms, icon,
     create_dept, create_by, create_time, update_by, update_time, remark)
SELECT 20044, '第三方API删除', 20040, 4, '#', '', '', 1, 0, 'F', '0', '0',
       'manager:thirdApi:remove', '#', 103, 1, SYSDATE(), NULL, NULL, ''
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE menu_id = 20044);
INSERT INTO sys_menu
    (menu_id, menu_name, parent_id, order_num, path, component, query_param,
     is_frame, is_cache, menu_type, visible, status, perms, icon,
     create_dept, create_by, create_time, update_by, update_time, remark)
SELECT 20045, '第三方API导出', 20040, 5, '#', '', '', 1, 0, 'F', '0', '0',
       'manager:thirdApi:export', '#', 103, 1, SYSDATE(), NULL, NULL, ''
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE menu_id = 20045);
