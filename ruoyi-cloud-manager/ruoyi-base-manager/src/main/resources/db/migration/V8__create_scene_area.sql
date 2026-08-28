-- ----------------------------
-- 场景区域表
-- ----------------------------
CREATE TABLE IF NOT EXISTS biz_scene_area
(
    area_id          BIGINT        NOT NULL COMMENT '区域ID',
    tenant_id        VARCHAR(20)   DEFAULT '000000' COMMENT '租户编号',
    tree_id          BIGINT        DEFAULT NULL COMMENT '分类树ID',
    category_node_id BIGINT        DEFAULT NULL COMMENT '分类节点ID',
    area_name        VARCHAR(100)  NOT NULL COMMENT '区域名称',
    area_type        VARCHAR(32)   NOT NULL COMMENT '区域类型',
    gcj02_path       TEXT          NOT NULL COMMENT '高德GCJ02坐标范围JSON',
    bd09_path        TEXT          DEFAULT NULL COMMENT '百度BD09坐标范围JSON',
    wgs84_path       TEXT          DEFAULT NULL COMMENT 'WGS84坐标范围JSON',
    fill_color       VARCHAR(32)   DEFAULT '#14b8a6' COMMENT '填充颜色',
    stroke_color     VARCHAR(32)   DEFAULT '#0f766e' COMMENT '边界线颜色',
    stroke_style     VARCHAR(16)   DEFAULT 'solid' COMMENT '边界线样式（solid实线 dashed虚线）',
    stroke_weight    INT           DEFAULT 2 COMMENT '边界线宽度',
    status           CHAR(1)       DEFAULT '0' COMMENT '状态（0正常 1停用）',
    del_flag         CHAR(1)       DEFAULT '0' COMMENT '删除标志（0代表存在 1代表删除）',
    create_dept      BIGINT        DEFAULT NULL COMMENT '创建部门',
    create_by        BIGINT        DEFAULT NULL COMMENT '创建者',
    create_time      DATETIME      DEFAULT NULL COMMENT '创建时间',
    update_by        BIGINT        DEFAULT NULL COMMENT '更新者',
    update_time      DATETIME      DEFAULT NULL COMMENT '更新时间',
    remark           VARCHAR(500)  DEFAULT NULL COMMENT '备注',
    PRIMARY KEY (area_id),
    KEY idx_biz_scene_area_tenant_name (tenant_id, area_name),
    KEY idx_biz_scene_area_category (tenant_id, tree_id, category_node_id),
    KEY idx_biz_scene_area_type (tenant_id, area_type),
    KEY idx_biz_scene_area_status (tenant_id, status, del_flag)
) ENGINE = InnoDB COMMENT = '场景区域表';

-- ----------------------------
-- 区域类型字典
-- ----------------------------
INSERT INTO sys_dict_type
    (dict_id, tenant_id, dict_name, dict_type, create_dept, create_by, create_time, update_by, update_time, remark)
SELECT 20005, '000000', '场景区域类型', 'scene_area_type', 103, 1, SYSDATE(), NULL, NULL, '场景区域类型'
WHERE NOT EXISTS (SELECT 1 FROM sys_dict_type WHERE dict_type = 'scene_area_type');

INSERT INTO sys_dict_data
    (dict_code, tenant_id, dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default,
     create_dept, create_by, create_time, update_by, update_time, remark)
SELECT 20051, '000000', 1, '可行区域', 'drivable', 'scene_area_type', '', 'success', 'Y', 103, 1, SYSDATE(), NULL, NULL, ''
WHERE NOT EXISTS (SELECT 1 FROM sys_dict_data WHERE dict_code = 20051);
INSERT INTO sys_dict_data
    (dict_code, tenant_id, dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default,
     create_dept, create_by, create_time, update_by, update_time, remark)
SELECT 20052, '000000', 2, '禁止区域', 'forbidden', 'scene_area_type', '', 'danger', 'N', 103, 1, SYSDATE(), NULL, NULL, ''
WHERE NOT EXISTS (SELECT 1 FROM sys_dict_data WHERE dict_code = 20052);

-- ----------------------------
-- 菜单权限
-- ----------------------------
INSERT INTO sys_menu
    (menu_id, menu_name, parent_id, order_num, path, component, query_param,
     is_frame, is_cache, menu_type, visible, status, perms, icon,
     create_dept, create_by, create_time, update_by, update_time, remark)
SELECT 20080, '场景管理', 0, 8, 'scene', NULL, '', 1, 0, 'M', '0', '0', '', 'map',
       103, 1, SYSDATE(), NULL, NULL, '场景管理目录'
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE menu_id = 20080);

INSERT INTO sys_menu
    (menu_id, menu_name, parent_id, order_num, path, component, query_param,
     is_frame, is_cache, menu_type, visible, status, perms, icon,
     create_dept, create_by, create_time, update_by, update_time, remark)
SELECT 20081, '区域管理', 20080, 1, 'area', 'manager/scene/area/index', '', 1, 0, 'C', '0', '0',
       'manager:sceneArea:list', 'map-pin', 103, 1, SYSDATE(), NULL, NULL, '区域管理菜单'
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE menu_id = 20081);

INSERT INTO sys_menu
    (menu_id, menu_name, parent_id, order_num, path, component, query_param,
     is_frame, is_cache, menu_type, visible, status, perms, icon,
     create_dept, create_by, create_time, update_by, update_time, remark)
SELECT 20082, '路线管理', 20080, 2, 'route', 'manager/scene/route/index', '', 1, 0, 'C', '0', '0',
       'manager:sceneRoute:list', 'route', 103, 1, SYSDATE(), NULL, NULL, '路线管理菜单'
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE menu_id = 20082);

INSERT INTO sys_menu
    (menu_id, menu_name, parent_id, order_num, path, component, query_param,
     is_frame, is_cache, menu_type, visible, status, perms, icon,
     create_dept, create_by, create_time, update_by, update_time, remark)
SELECT 20083, '点位管理', 20080, 3, 'point', 'manager/scene/point/index', '', 1, 0, 'C', '0', '0',
       'manager:scenePoint:list', 'map-pinned', 103, 1, SYSDATE(), NULL, NULL, '点位管理菜单'
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE menu_id = 20083);

INSERT INTO sys_menu
    (menu_id, menu_name, parent_id, order_num, path, component, query_param,
     is_frame, is_cache, menu_type, visible, status, perms, icon,
     create_dept, create_by, create_time, update_by, update_time, remark)
SELECT 20084, '区域查询', 20081, 1, '#', '', '', 1, 0, 'F', '0', '0',
       'manager:sceneArea:query', '#', 103, 1, SYSDATE(), NULL, NULL, ''
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE menu_id = 20084);
INSERT INTO sys_menu
    (menu_id, menu_name, parent_id, order_num, path, component, query_param,
     is_frame, is_cache, menu_type, visible, status, perms, icon,
     create_dept, create_by, create_time, update_by, update_time, remark)
SELECT 20085, '区域新增', 20081, 2, '#', '', '', 1, 0, 'F', '0', '0',
       'manager:sceneArea:add', '#', 103, 1, SYSDATE(), NULL, NULL, ''
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE menu_id = 20085);
INSERT INTO sys_menu
    (menu_id, menu_name, parent_id, order_num, path, component, query_param,
     is_frame, is_cache, menu_type, visible, status, perms, icon,
     create_dept, create_by, create_time, update_by, update_time, remark)
SELECT 20086, '区域修改', 20081, 3, '#', '', '', 1, 0, 'F', '0', '0',
       'manager:sceneArea:edit', '#', 103, 1, SYSDATE(), NULL, NULL, ''
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE menu_id = 20086);
INSERT INTO sys_menu
    (menu_id, menu_name, parent_id, order_num, path, component, query_param,
     is_frame, is_cache, menu_type, visible, status, perms, icon,
     create_dept, create_by, create_time, update_by, update_time, remark)
SELECT 20087, '区域删除', 20081, 4, '#', '', '', 1, 0, 'F', '0', '0',
       'manager:sceneArea:remove', '#', 103, 1, SYSDATE(), NULL, NULL, ''
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE menu_id = 20087);
INSERT INTO sys_menu
    (menu_id, menu_name, parent_id, order_num, path, component, query_param,
     is_frame, is_cache, menu_type, visible, status, perms, icon,
     create_dept, create_by, create_time, update_by, update_time, remark)
SELECT 20088, '区域导出', 20081, 5, '#', '', '', 1, 0, 'F', '0', '0',
       'manager:sceneArea:export', '#', 103, 1, SYSDATE(), NULL, NULL, ''
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE menu_id = 20088);
