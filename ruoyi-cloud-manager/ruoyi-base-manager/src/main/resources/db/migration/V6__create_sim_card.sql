-- ----------------------------
-- SIM卡表
-- ----------------------------
CREATE TABLE IF NOT EXISTS biz_sim_card
(
    sim_id              BIGINT         NOT NULL COMMENT 'SIM卡ID',
    tenant_id           VARCHAR(20)    DEFAULT '000000' COMMENT '租户编号',
    tree_id             BIGINT         DEFAULT NULL COMMENT '分类树ID',
    category_node_id    BIGINT         DEFAULT NULL COMMENT '分类节点ID',
    imei                VARCHAR(32)    DEFAULT NULL COMMENT 'IMEI',
    iccid               VARCHAR(32)    NOT NULL COMMENT 'ICCID',
    phone_number        VARCHAR(32)    DEFAULT NULL COMMENT '手机号',
    monthly_data_quota  DECIMAL(10, 2) DEFAULT NULL COMMENT '月套餐流量（GB）',
    activation_time     DATETIME       DEFAULT NULL COMMENT '开卡时间',
    expire_time         DATETIME       DEFAULT NULL COMMENT '到期时间',
    current_data_usage  DECIMAL(10, 2) DEFAULT NULL COMMENT '当前用量（GB）',
    status              CHAR(1)        DEFAULT '0' COMMENT '状态（0正常 1停用）',
    del_flag            CHAR(1)        DEFAULT '0' COMMENT '删除标志（0代表存在 1代表删除）',
    create_dept         BIGINT         DEFAULT NULL COMMENT '创建部门',
    create_by           BIGINT         DEFAULT NULL COMMENT '创建者',
    create_time         DATETIME       DEFAULT NULL COMMENT '创建时间',
    update_by           BIGINT         DEFAULT NULL COMMENT '更新者',
    update_time         DATETIME       DEFAULT NULL COMMENT '更新时间',
    remark              VARCHAR(500)   DEFAULT NULL COMMENT '备注',
    PRIMARY KEY (sim_id),
    KEY idx_biz_sim_card_tenant_iccid (tenant_id, iccid),
    KEY idx_biz_sim_card_tenant_imei (tenant_id, imei),
    KEY idx_biz_sim_card_tenant_phone (tenant_id, phone_number),
    KEY idx_biz_sim_card_category (tenant_id, tree_id, category_node_id),
    KEY idx_biz_sim_card_status (tenant_id, status, del_flag)
) ENGINE = InnoDB COMMENT = 'SIM卡表';

-- ----------------------------
-- 菜单权限
-- ----------------------------
INSERT INTO sys_menu
    (menu_id, menu_name, parent_id, order_num, path, component, query_param,
     is_frame, is_cache, menu_type, visible, status, perms, icon,
     create_dept, create_by, create_time, update_by, update_time, remark)
SELECT 20050, 'SIM卡管理', 20010, 3, 'simCard', 'manager/equipment/simCard/index', '', 1, 0, 'C', '0', '0',
       'manager:simCard:list', 'sim', 103, 1, SYSDATE(), NULL, NULL, 'SIM卡管理菜单'
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE menu_id = 20050);

INSERT INTO sys_menu
    (menu_id, menu_name, parent_id, order_num, path, component, query_param,
     is_frame, is_cache, menu_type, visible, status, perms, icon,
     create_dept, create_by, create_time, update_by, update_time, remark)
SELECT 20051, 'SIM卡查询', 20050, 1, '#', '', '', 1, 0, 'F', '0', '0', 'manager:simCard:query', '#',
       103, 1, SYSDATE(), NULL, NULL, ''
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE menu_id = 20051);
INSERT INTO sys_menu
    (menu_id, menu_name, parent_id, order_num, path, component, query_param,
     is_frame, is_cache, menu_type, visible, status, perms, icon,
     create_dept, create_by, create_time, update_by, update_time, remark)
SELECT 20052, 'SIM卡新增', 20050, 2, '#', '', '', 1, 0, 'F', '0', '0', 'manager:simCard:add', '#',
       103, 1, SYSDATE(), NULL, NULL, ''
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE menu_id = 20052);
INSERT INTO sys_menu
    (menu_id, menu_name, parent_id, order_num, path, component, query_param,
     is_frame, is_cache, menu_type, visible, status, perms, icon,
     create_dept, create_by, create_time, update_by, update_time, remark)
SELECT 20053, 'SIM卡修改', 20050, 3, '#', '', '', 1, 0, 'F', '0', '0', 'manager:simCard:edit', '#',
       103, 1, SYSDATE(), NULL, NULL, ''
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE menu_id = 20053);
INSERT INTO sys_menu
    (menu_id, menu_name, parent_id, order_num, path, component, query_param,
     is_frame, is_cache, menu_type, visible, status, perms, icon,
     create_dept, create_by, create_time, update_by, update_time, remark)
SELECT 20054, 'SIM卡删除', 20050, 4, '#', '', '', 1, 0, 'F', '0', '0', 'manager:simCard:remove', '#',
       103, 1, SYSDATE(), NULL, NULL, ''
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE menu_id = 20054);
INSERT INTO sys_menu
    (menu_id, menu_name, parent_id, order_num, path, component, query_param,
     is_frame, is_cache, menu_type, visible, status, perms, icon,
     create_dept, create_by, create_time, update_by, update_time, remark)
SELECT 20055, 'SIM卡导出', 20050, 5, '#', '', '', 1, 0, 'F', '0', '0', 'manager:simCard:export', '#',
       103, 1, SYSDATE(), NULL, NULL, ''
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE menu_id = 20055);
