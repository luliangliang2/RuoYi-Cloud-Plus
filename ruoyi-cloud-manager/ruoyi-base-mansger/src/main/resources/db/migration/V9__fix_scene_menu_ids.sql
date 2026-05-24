-- ----------------------------
-- 修正场景管理菜单ID，避开 Magic API 使用的 20080~20085
-- ----------------------------
DELETE FROM sys_menu
WHERE menu_id IN (20080, 20081, 20082, 20083, 20084, 20085, 20086, 20087, 20088)
  AND (
      menu_name IN ('场景管理', '区域管理', '路线管理', '点位管理', '区域查询', '区域新增', '区域修改', '区域删除', '区域导出')
      OR perms LIKE 'manager:scene%'
  );

INSERT INTO sys_menu
    (menu_id, menu_name, parent_id, order_num, path, component, query_param,
     is_frame, is_cache, menu_type, visible, status, perms, icon,
     create_dept, create_by, create_time, update_by, update_time, remark)
SELECT 20100, '场景管理', 0, 8, 'scene', NULL, '', 1, 0, 'M', '0', '0', '', 'map',
       103, 1, SYSDATE(), NULL, NULL, '场景管理目录'
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE menu_id = 20100);

INSERT INTO sys_menu
    (menu_id, menu_name, parent_id, order_num, path, component, query_param,
     is_frame, is_cache, menu_type, visible, status, perms, icon,
     create_dept, create_by, create_time, update_by, update_time, remark)
SELECT 20101, '区域管理', 20100, 1, 'area', 'manager/scene/area/index', '', 1, 0, 'C', '0', '0',
       'manager:sceneArea:list', 'map-pin', 103, 1, SYSDATE(), NULL, NULL, '区域管理菜单'
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE menu_id = 20101);

INSERT INTO sys_menu
    (menu_id, menu_name, parent_id, order_num, path, component, query_param,
     is_frame, is_cache, menu_type, visible, status, perms, icon,
     create_dept, create_by, create_time, update_by, update_time, remark)
SELECT 20102, '路线管理', 20100, 2, 'route', 'manager/scene/route/index', '', 1, 0, 'C', '0', '0',
       'manager:sceneRoute:list', 'route', 103, 1, SYSDATE(), NULL, NULL, '路线管理菜单'
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE menu_id = 20102);

INSERT INTO sys_menu
    (menu_id, menu_name, parent_id, order_num, path, component, query_param,
     is_frame, is_cache, menu_type, visible, status, perms, icon,
     create_dept, create_by, create_time, update_by, update_time, remark)
SELECT 20103, '点位管理', 20100, 3, 'point', 'manager/scene/point/index', '', 1, 0, 'C', '0', '0',
       'manager:scenePoint:list', 'map-pinned', 103, 1, SYSDATE(), NULL, NULL, '点位管理菜单'
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE menu_id = 20103);

INSERT INTO sys_menu
    (menu_id, menu_name, parent_id, order_num, path, component, query_param,
     is_frame, is_cache, menu_type, visible, status, perms, icon,
     create_dept, create_by, create_time, update_by, update_time, remark)
SELECT 20104, '区域查询', 20101, 1, '#', '', '', 1, 0, 'F', '0', '0',
       'manager:sceneArea:query', '#', 103, 1, SYSDATE(), NULL, NULL, ''
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE menu_id = 20104);

INSERT INTO sys_menu
    (menu_id, menu_name, parent_id, order_num, path, component, query_param,
     is_frame, is_cache, menu_type, visible, status, perms, icon,
     create_dept, create_by, create_time, update_by, update_time, remark)
SELECT 20105, '区域新增', 20101, 2, '#', '', '', 1, 0, 'F', '0', '0',
       'manager:sceneArea:add', '#', 103, 1, SYSDATE(), NULL, NULL, ''
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE menu_id = 20105);

INSERT INTO sys_menu
    (menu_id, menu_name, parent_id, order_num, path, component, query_param,
     is_frame, is_cache, menu_type, visible, status, perms, icon,
     create_dept, create_by, create_time, update_by, update_time, remark)
SELECT 20106, '区域修改', 20101, 3, '#', '', '', 1, 0, 'F', '0', '0',
       'manager:sceneArea:edit', '#', 103, 1, SYSDATE(), NULL, NULL, ''
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE menu_id = 20106);

INSERT INTO sys_menu
    (menu_id, menu_name, parent_id, order_num, path, component, query_param,
     is_frame, is_cache, menu_type, visible, status, perms, icon,
     create_dept, create_by, create_time, update_by, update_time, remark)
SELECT 20107, '区域删除', 20101, 4, '#', '', '', 1, 0, 'F', '0', '0',
       'manager:sceneArea:remove', '#', 103, 1, SYSDATE(), NULL, NULL, ''
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE menu_id = 20107);

INSERT INTO sys_menu
    (menu_id, menu_name, parent_id, order_num, path, component, query_param,
     is_frame, is_cache, menu_type, visible, status, perms, icon,
     create_dept, create_by, create_time, update_by, update_time, remark)
SELECT 20108, '区域导出', 20101, 5, '#', '', '', 1, 0, 'F', '0', '0',
       'manager:sceneArea:export', '#', 103, 1, SYSDATE(), NULL, NULL, ''
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE menu_id = 20108);
