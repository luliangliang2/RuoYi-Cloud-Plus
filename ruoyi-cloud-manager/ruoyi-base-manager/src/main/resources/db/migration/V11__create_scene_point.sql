-- ----------------------------
-- 场景点位表
-- ----------------------------
CREATE TABLE IF NOT EXISTS biz_scene_point
(
    point_id         BIGINT        NOT NULL COMMENT '点位ID',
    tenant_id        VARCHAR(20)   DEFAULT '000000' COMMENT '租户编号',
    tree_id          BIGINT        DEFAULT NULL COMMENT '分类树ID',
    category_node_id BIGINT        DEFAULT NULL COMMENT '分类节点ID',
    route_id         BIGINT        NOT NULL COMMENT '路线ID',
    point_name       VARCHAR(100)  NOT NULL COMMENT '点位名称',
    gcj02_lng        DECIMAL(11,8) NOT NULL COMMENT '高德GCJ02经度',
    gcj02_lat        DECIMAL(10,8) NOT NULL COMMENT '高德GCJ02纬度',
    bd09_lng         DECIMAL(11,8) DEFAULT NULL COMMENT '百度BD09经度',
    bd09_lat         DECIMAL(10,8) DEFAULT NULL COMMENT '百度BD09纬度',
    wgs84_lng        DECIMAL(11,8) DEFAULT NULL COMMENT 'WGS84经度',
    wgs84_lat        DECIMAL(10,8) DEFAULT NULL COMMENT 'WGS84纬度',
    contact_name     VARCHAR(50)   DEFAULT NULL COMMENT '联系人',
    contact_phone    VARCHAR(20)   DEFAULT NULL COMMENT '联系人手机号',
    status           CHAR(1)       DEFAULT '0' COMMENT '状态（0正常 1停用）',
    del_flag         CHAR(1)       DEFAULT '0' COMMENT '删除标志（0代表存在 1代表删除）',
    create_dept      BIGINT        DEFAULT NULL COMMENT '创建部门',
    create_by        BIGINT        DEFAULT NULL COMMENT '创建者',
    create_time      DATETIME      DEFAULT NULL COMMENT '创建时间',
    update_by        BIGINT        DEFAULT NULL COMMENT '更新者',
    update_time      DATETIME      DEFAULT NULL COMMENT '更新时间',
    remark           VARCHAR(500)  DEFAULT NULL COMMENT '备注',
    PRIMARY KEY (point_id),
    KEY idx_biz_scene_point_tenant_name (tenant_id, point_name),
    KEY idx_biz_scene_point_category (tenant_id, tree_id, category_node_id),
    KEY idx_biz_scene_point_route (tenant_id, route_id),
    KEY idx_biz_scene_point_status (tenant_id, status, del_flag)
) ENGINE = InnoDB COMMENT = '场景点位表';

-- ----------------------------
-- 菜单权限
-- ----------------------------
UPDATE sys_menu
SET visible = '0',
    status = '0',
    perms = 'manager:scenePoint:list',
    component = 'manager/scene/point/index',
    update_time = SYSDATE()
WHERE menu_id = 20103;

INSERT INTO sys_menu
    (menu_id, menu_name, parent_id, order_num, path, component, query_param,
     is_frame, is_cache, menu_type, visible, status, perms, icon,
     create_dept, create_by, create_time, update_by, update_time, remark)
SELECT 20114, '点位查询', 20103, 1, '#', '', '', 1, 0, 'F', '0', '0',
       'manager:scenePoint:query', '#', 103, 1, SYSDATE(), NULL, NULL, ''
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE menu_id = 20114);

INSERT INTO sys_menu
    (menu_id, menu_name, parent_id, order_num, path, component, query_param,
     is_frame, is_cache, menu_type, visible, status, perms, icon,
     create_dept, create_by, create_time, update_by, update_time, remark)
SELECT 20115, '点位新增', 20103, 2, '#', '', '', 1, 0, 'F', '0', '0',
       'manager:scenePoint:add', '#', 103, 1, SYSDATE(), NULL, NULL, ''
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE menu_id = 20115);

INSERT INTO sys_menu
    (menu_id, menu_name, parent_id, order_num, path, component, query_param,
     is_frame, is_cache, menu_type, visible, status, perms, icon,
     create_dept, create_by, create_time, update_by, update_time, remark)
SELECT 20116, '点位修改', 20103, 3, '#', '', '', 1, 0, 'F', '0', '0',
       'manager:scenePoint:edit', '#', 103, 1, SYSDATE(), NULL, NULL, ''
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE menu_id = 20116);

INSERT INTO sys_menu
    (menu_id, menu_name, parent_id, order_num, path, component, query_param,
     is_frame, is_cache, menu_type, visible, status, perms, icon,
     create_dept, create_by, create_time, update_by, update_time, remark)
SELECT 20117, '点位删除', 20103, 4, '#', '', '', 1, 0, 'F', '0', '0',
       'manager:scenePoint:remove', '#', 103, 1, SYSDATE(), NULL, NULL, ''
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE menu_id = 20117);

INSERT INTO sys_menu
    (menu_id, menu_name, parent_id, order_num, path, component, query_param,
     is_frame, is_cache, menu_type, visible, status, perms, icon,
     create_dept, create_by, create_time, update_by, update_time, remark)
SELECT 20118, '点位导出', 20103, 5, '#', '', '', 1, 0, 'F', '0', '0',
       'manager:scenePoint:export', '#', 103, 1, SYSDATE(), NULL, NULL, ''
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE menu_id = 20118);
