-- ----------------------------
-- 维护树定义表
-- ----------------------------
CREATE TABLE IF NOT EXISTS biz_tree_def
(
    tree_id      BIGINT       NOT NULL COMMENT '树ID',
    tenant_id    VARCHAR(20)  DEFAULT '000000' COMMENT '租户编号',
    tree_code    VARCHAR(64)  NOT NULL COMMENT '树编码',
    tree_name    VARCHAR(100) NOT NULL COMMENT '树名称',
    tree_type    VARCHAR(32)  DEFAULT 'common' COMMENT '树类型',
    module_code  VARCHAR(64)  DEFAULT NULL COMMENT '使用模块编码',
    root_mode    CHAR(1)      DEFAULT '1' COMMENT '根节点模式（1单根 2多根）',
    status       CHAR(1)      DEFAULT '0' COMMENT '状态（0正常 1停用）',
    del_flag     CHAR(1)      DEFAULT '0' COMMENT '删除标志（0代表存在 1代表删除）',
    create_dept  BIGINT       DEFAULT NULL COMMENT '创建部门',
    create_by    BIGINT       DEFAULT NULL COMMENT '创建者',
    create_time  DATETIME     DEFAULT NULL COMMENT '创建时间',
    update_by    BIGINT       DEFAULT NULL COMMENT '更新者',
    update_time  DATETIME     DEFAULT NULL COMMENT '更新时间',
    remark       VARCHAR(500) DEFAULT NULL COMMENT '备注',
    PRIMARY KEY (tree_id),
    KEY idx_biz_tree_def_tenant_code (tenant_id, tree_code),
    KEY idx_biz_tree_def_module (tenant_id, module_code),
    KEY idx_biz_tree_def_status (tenant_id, status, del_flag)
) ENGINE = InnoDB COMMENT = '维护树定义表';

-- ----------------------------
-- 维护树节点表
-- ----------------------------
CREATE TABLE IF NOT EXISTS biz_tree_node
(
    node_id      BIGINT        NOT NULL COMMENT '节点ID',
    tenant_id    VARCHAR(20)   DEFAULT '000000' COMMENT '租户编号',
    tree_id      BIGINT        NOT NULL COMMENT '树ID',
    parent_id    BIGINT        DEFAULT 0 COMMENT '父节点ID',
    ancestors    VARCHAR(500)  DEFAULT '0' COMMENT '祖级列表',
    node_code    VARCHAR(64)   NOT NULL COMMENT '节点编码',
    node_name    VARCHAR(100)  NOT NULL COMMENT '节点名称',
    node_type    VARCHAR(32)   DEFAULT NULL COMMENT '节点类型',
    order_num    INT          DEFAULT 0 COMMENT '显示顺序',
    level_no     INT          DEFAULT 1 COMMENT '层级',
    leaf_flag    CHAR(1)      DEFAULT '1' COMMENT '是否叶子节点（0否 1是）',
    status       CHAR(1)      DEFAULT '0' COMMENT '状态（0正常 1停用）',
    ext_json     VARCHAR(2000) DEFAULT NULL COMMENT '扩展属性JSON',
    del_flag     CHAR(1)      DEFAULT '0' COMMENT '删除标志（0代表存在 1代表删除）',
    create_dept  BIGINT       DEFAULT NULL COMMENT '创建部门',
    create_by    BIGINT       DEFAULT NULL COMMENT '创建者',
    create_time  DATETIME     DEFAULT NULL COMMENT '创建时间',
    update_by    BIGINT       DEFAULT NULL COMMENT '更新者',
    update_time  DATETIME     DEFAULT NULL COMMENT '更新时间',
    remark       VARCHAR(500) DEFAULT NULL COMMENT '备注',
    PRIMARY KEY (node_id),
    KEY idx_biz_tree_node_tree_parent (tenant_id, tree_id, parent_id, order_num),
    KEY idx_biz_tree_node_tree_code (tenant_id, tree_id, node_code),
    KEY idx_biz_tree_node_status (tenant_id, tree_id, status, del_flag)
) ENGINE = InnoDB COMMENT = '维护树节点表';

-- ----------------------------
-- 菜单权限
-- 说明：如 sys_menu 菜单表与本服务共用同一库，可执行以下语句初始化菜单。
-- ----------------------------
INSERT INTO sys_menu
    (menu_id, menu_name, parent_id, order_num, path, component, query_param,
     is_frame, is_cache, menu_type, visible, status, perms, icon,
     create_dept, create_by, create_time, update_by, update_time, remark)
SELECT 20000, '基础数据', 0, 6, 'base', NULL, '', 1, 0, 'M', '0', '0', '', 'database',
       103, 1, SYSDATE(), NULL, NULL, '基础数据目录'
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE menu_id = 20000);

INSERT INTO sys_menu
    (menu_id, menu_name, parent_id, order_num, path, component, query_param,
     is_frame, is_cache, menu_type, visible, status, perms, icon,
     create_dept, create_by, create_time, update_by, update_time, remark)
SELECT 20001, '维护树管理', 20000, 1, 'tree', 'manager/tree/index', '', 1, 0, 'C', '0', '0',
       'manager:tree:list', 'tree', 103, 1, SYSDATE(), NULL, NULL, '维护树管理菜单'
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE menu_id = 20001);

INSERT INTO sys_menu
    (menu_id, menu_name, parent_id, order_num, path, component, query_param,
     is_frame, is_cache, menu_type, visible, status, perms, icon,
     create_dept, create_by, create_time, update_by, update_time, remark)
SELECT 20002, '维护树查询', 20001, 1, '#', '', '', 1, 0, 'F', '0', '0',
       'manager:tree:query', '#', 103, 1, SYSDATE(), NULL, NULL, ''
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE menu_id = 20002);

INSERT INTO sys_menu
    (menu_id, menu_name, parent_id, order_num, path, component, query_param,
     is_frame, is_cache, menu_type, visible, status, perms, icon,
     create_dept, create_by, create_time, update_by, update_time, remark)
SELECT 20003, '维护树新增', 20001, 2, '#', '', '', 1, 0, 'F', '0', '0',
       'manager:tree:add', '#', 103, 1, SYSDATE(), NULL, NULL, ''
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE menu_id = 20003);

INSERT INTO sys_menu
    (menu_id, menu_name, parent_id, order_num, path, component, query_param,
     is_frame, is_cache, menu_type, visible, status, perms, icon,
     create_dept, create_by, create_time, update_by, update_time, remark)
SELECT 20004, '维护树修改', 20001, 3, '#', '', '', 1, 0, 'F', '0', '0',
       'manager:tree:edit', '#', 103, 1, SYSDATE(), NULL, NULL, ''
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE menu_id = 20004);

INSERT INTO sys_menu
    (menu_id, menu_name, parent_id, order_num, path, component, query_param,
     is_frame, is_cache, menu_type, visible, status, perms, icon,
     create_dept, create_by, create_time, update_by, update_time, remark)
SELECT 20005, '维护树删除', 20001, 4, '#', '', '', 1, 0, 'F', '0', '0',
       'manager:tree:remove', '#', 103, 1, SYSDATE(), NULL, NULL, ''
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE menu_id = 20005);

INSERT INTO sys_menu
    (menu_id, menu_name, parent_id, order_num, path, component, query_param,
     is_frame, is_cache, menu_type, visible, status, perms, icon,
     create_dept, create_by, create_time, update_by, update_time, remark)
SELECT 20006, '维护树导出', 20001, 5, '#', '', '', 1, 0, 'F', '0', '0',
       'manager:tree:export', '#', 103, 1, SYSDATE(), NULL, NULL, ''
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE menu_id = 20006);
