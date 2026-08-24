-- ----------------------------
-- 场景路线表
-- ----------------------------
CREATE TABLE IF NOT EXISTS biz_scene_route
(
    route_id         BIGINT        NOT NULL COMMENT '路线ID',
    tenant_id        VARCHAR(20)   DEFAULT '000000' COMMENT '租户编号',
    tree_id          BIGINT        DEFAULT NULL COMMENT '分类树ID',
    category_node_id BIGINT        DEFAULT NULL COMMENT '分类节点ID',
    route_name       VARCHAR(100)  NOT NULL COMMENT '路线名称',
    gcj02_path       TEXT          NOT NULL COMMENT '高德GCJ02坐标路线JSON',
    bd09_path        TEXT          DEFAULT NULL COMMENT '百度BD09坐标路线JSON',
    wgs84_path       TEXT          DEFAULT NULL COMMENT 'WGS84坐标路线JSON',
    stroke_color     VARCHAR(32)   DEFAULT '#0f766e' COMMENT '线颜色',
    stroke_style     VARCHAR(16)   DEFAULT 'solid' COMMENT '线样式（solid实线 dashed虚线）',
    stroke_weight    INT           DEFAULT 4 COMMENT '线宽度',
    status           CHAR(1)       DEFAULT '0' COMMENT '状态（0正常 1停用）',
    del_flag         CHAR(1)       DEFAULT '0' COMMENT '删除标志（0代表存在 1代表删除）',
    create_dept      BIGINT        DEFAULT NULL COMMENT '创建部门',
    create_by        BIGINT        DEFAULT NULL COMMENT '创建者',
    create_time      DATETIME      DEFAULT NULL COMMENT '创建时间',
    update_by        BIGINT        DEFAULT NULL COMMENT '更新者',
    update_time      DATETIME      DEFAULT NULL COMMENT '更新时间',
    remark           VARCHAR(500)  DEFAULT NULL COMMENT '备注',
    PRIMARY KEY (route_id),
    KEY idx_biz_scene_route_tenant_name (tenant_id, route_name),
    KEY idx_biz_scene_route_category (tenant_id, tree_id, category_node_id),
    KEY idx_biz_scene_route_status (tenant_id, status, del_flag)
) ENGINE = InnoDB COMMENT = '场景路线表';

-- ----------------------------
-- 菜单权限
-- ----------------------------
UPDATE sys_menu
SET visible = '0',
    status = '0',
    perms = 'manager:sceneRoute:list',
    component = 'manager/scene/route/index',
    update_time = SYSDATE()
WHERE menu_id = 20102;

INSERT INTO sys_menu
    (menu_id, menu_name, parent_id, order_num, path, component, query_param,
     is_frame, is_cache, menu_type, visible, status, perms, icon,
     create_dept, create_by, create_time, update_by, update_time, remark)
SELECT 20109, '路线查询', 20102, 1, '#', '', '', 1, 0, 'F', '0', '0',
       'manager:sceneRoute:query', '#', 103, 1, SYSDATE(), NULL, NULL, ''
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE menu_id = 20109);

INSERT INTO sys_menu
    (menu_id, menu_name, parent_id, order_num, path, component, query_param,
     is_frame, is_cache, menu_type, visible, status, perms, icon,
     create_dept, create_by, create_time, update_by, update_time, remark)
SELECT 20110, '路线新增', 20102, 2, '#', '', '', 1, 0, 'F', '0', '0',
       'manager:sceneRoute:add', '#', 103, 1, SYSDATE(), NULL, NULL, ''
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE menu_id = 20110);

INSERT INTO sys_menu
    (menu_id, menu_name, parent_id, order_num, path, component, query_param,
     is_frame, is_cache, menu_type, visible, status, perms, icon,
     create_dept, create_by, create_time, update_by, update_time, remark)
SELECT 20111, '路线修改', 20102, 3, '#', '', '', 1, 0, 'F', '0', '0',
       'manager:sceneRoute:edit', '#', 103, 1, SYSDATE(), NULL, NULL, ''
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE menu_id = 20111);

INSERT INTO sys_menu
    (menu_id, menu_name, parent_id, order_num, path, component, query_param,
     is_frame, is_cache, menu_type, visible, status, perms, icon,
     create_dept, create_by, create_time, update_by, update_time, remark)
SELECT 20112, '路线删除', 20102, 4, '#', '', '', 1, 0, 'F', '0', '0',
       'manager:sceneRoute:remove', '#', 103, 1, SYSDATE(), NULL, NULL, ''
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE menu_id = 20112);

INSERT INTO sys_menu
    (menu_id, menu_name, parent_id, order_num, path, component, query_param,
     is_frame, is_cache, menu_type, visible, status, perms, icon,
     create_dept, create_by, create_time, update_by, update_time, remark)
SELECT 20113, '路线导出', 20102, 5, '#', '', '', 1, 0, 'F', '0', '0',
       'manager:sceneRoute:export', '#', 103, 1, SYSDATE(), NULL, NULL, ''
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE menu_id = 20113);
