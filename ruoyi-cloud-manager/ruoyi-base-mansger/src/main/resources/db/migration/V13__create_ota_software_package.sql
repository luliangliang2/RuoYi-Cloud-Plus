-- ----------------------------
-- OTA软件包表
-- ----------------------------
CREATE TABLE IF NOT EXISTS biz_ota_software_package
(
    package_id    BIGINT        NOT NULL COMMENT '软件包ID',
    tenant_id     VARCHAR(20)   DEFAULT '000000' COMMENT '租户编号',
    package_name  VARCHAR(100)  NOT NULL COMMENT '软件包名称',
    version       VARCHAR(64)   NOT NULL COMMENT '版本号',
    package_desc  VARCHAR(1000) DEFAULT NULL COMMENT '软件包说明',
    file_oss_id   VARCHAR(64)   NOT NULL COMMENT '文件OSS ID',
    status        CHAR(1)       DEFAULT '0' COMMENT '状态（0正常 1停用）',
    del_flag      CHAR(1)       DEFAULT '0' COMMENT '删除标志（0代表存在 1代表删除）',
    create_dept   BIGINT        DEFAULT NULL COMMENT '创建部门',
    create_by     BIGINT        DEFAULT NULL COMMENT '创建者',
    create_time   DATETIME      DEFAULT NULL COMMENT '创建时间',
    update_by     BIGINT        DEFAULT NULL COMMENT '更新者',
    update_time   DATETIME      DEFAULT NULL COMMENT '更新时间',
    remark        VARCHAR(500)  DEFAULT NULL COMMENT '备注',
    PRIMARY KEY (package_id),
    KEY idx_biz_ota_pkg_tenant_name_version (tenant_id, package_name, version),
    KEY idx_biz_ota_pkg_file (tenant_id, file_oss_id),
    KEY idx_biz_ota_pkg_status (tenant_id, status, del_flag)
) ENGINE = InnoDB COMMENT = 'OTA软件包表';

-- ----------------------------
-- 菜单权限
-- ----------------------------
INSERT INTO sys_menu
    (menu_id, menu_name, parent_id, order_num, path, component, query_param,
     is_frame, is_cache, menu_type, visible, status, perms, icon,
     create_dept, create_by, create_time, update_by, update_time, remark)
SELECT 20200, 'OTA管理', 0, 10, 'ota', NULL, '', 1, 0, 'M', '0', '0', '', 'upload-cloud',
       103, 1, SYSDATE(), NULL, NULL, 'OTA管理目录'
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE menu_id = 20200);

INSERT INTO sys_menu
    (menu_id, menu_name, parent_id, order_num, path, component, query_param,
     is_frame, is_cache, menu_type, visible, status, perms, icon,
     create_dept, create_by, create_time, update_by, update_time, remark)
SELECT 20201, '软件管理', 20200, 1, 'software', 'manager/ota/software/index', '', 1, 0, 'C', '0', '0',
       'manager:otaSoftware:list', 'package', 103, 1, SYSDATE(), NULL, NULL, 'OTA软件管理菜单'
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE menu_id = 20201);

INSERT INTO sys_menu
    (menu_id, menu_name, parent_id, order_num, path, component, query_param,
     is_frame, is_cache, menu_type, visible, status, perms, icon,
     create_dept, create_by, create_time, update_by, update_time, remark)
SELECT 20202, '软件包查询', 20201, 1, '#', '', '', 1, 0, 'F', '0', '0',
       'manager:otaSoftware:query', '#', 103, 1, SYSDATE(), NULL, NULL, ''
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE menu_id = 20202);
INSERT INTO sys_menu
    (menu_id, menu_name, parent_id, order_num, path, component, query_param,
     is_frame, is_cache, menu_type, visible, status, perms, icon,
     create_dept, create_by, create_time, update_by, update_time, remark)
SELECT 20203, '软件包新增', 20201, 2, '#', '', '', 1, 0, 'F', '0', '0',
       'manager:otaSoftware:add', '#', 103, 1, SYSDATE(), NULL, NULL, ''
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE menu_id = 20203);
INSERT INTO sys_menu
    (menu_id, menu_name, parent_id, order_num, path, component, query_param,
     is_frame, is_cache, menu_type, visible, status, perms, icon,
     create_dept, create_by, create_time, update_by, update_time, remark)
SELECT 20204, '软件包修改', 20201, 3, '#', '', '', 1, 0, 'F', '0', '0',
       'manager:otaSoftware:edit', '#', 103, 1, SYSDATE(), NULL, NULL, ''
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE menu_id = 20204);
INSERT INTO sys_menu
    (menu_id, menu_name, parent_id, order_num, path, component, query_param,
     is_frame, is_cache, menu_type, visible, status, perms, icon,
     create_dept, create_by, create_time, update_by, update_time, remark)
SELECT 20205, '软件包删除', 20201, 4, '#', '', '', 1, 0, 'F', '0', '0',
       'manager:otaSoftware:remove', '#', 103, 1, SYSDATE(), NULL, NULL, ''
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE menu_id = 20205);
INSERT INTO sys_menu
    (menu_id, menu_name, parent_id, order_num, path, component, query_param,
     is_frame, is_cache, menu_type, visible, status, perms, icon,
     create_dept, create_by, create_time, update_by, update_time, remark)
SELECT 20206, '软件包导出', 20201, 5, '#', '', '', 1, 0, 'F', '0', '0',
       'manager:otaSoftware:export', '#', 103, 1, SYSDATE(), NULL, NULL, ''
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE menu_id = 20206);
