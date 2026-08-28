-- ----------------------------
-- 告警管理菜单
-- ----------------------------
INSERT INTO sys_menu
    (menu_id, menu_name, parent_id, order_num, path, component, query_param,
     is_frame, is_cache, menu_type, visible, status, perms, icon,
     create_dept, create_by, create_time, update_by, update_time, remark)
SELECT 20250, '告警管理', 0, 11, 'alarm', NULL, '',
       1, 0, 'M', '0', '0', '', 'bell',
       103, 1, SYSDATE(), NULL, NULL, '告警管理目录'
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE menu_id = 20250);

INSERT INTO sys_menu
    (menu_id, menu_name, parent_id, order_num, path, component, query_param,
     is_frame, is_cache, menu_type, visible, status, perms, icon,
     create_dept, create_by, create_time, update_by, update_time, remark)
SELECT 20251, '告警列表', 20250, 1, 'list', 'manager/alarm/list/index', '',
       1, 0, 'C', '0', '0', 'manager:alarm:list', 'bell-ring',
       103, 1, SYSDATE(), NULL, NULL, '告警列表菜单'
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE menu_id = 20251);

INSERT INTO sys_menu
    (menu_id, menu_name, parent_id, order_num, path, component, query_param,
     is_frame, is_cache, menu_type, visible, status, perms, icon,
     create_dept, create_by, create_time, update_by, update_time, remark)
SELECT 20252, '告警查询', 20251, 1, '#', '', '',
       1, 0, 'F', '0', '0', 'manager:alarm:query', '#',
       103, 1, SYSDATE(), NULL, NULL, ''
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE menu_id = 20252);

INSERT INTO sys_menu
    (menu_id, menu_name, parent_id, order_num, path, component, query_param,
     is_frame, is_cache, menu_type, visible, status, perms, icon,
     create_dept, create_by, create_time, update_by, update_time, remark)
SELECT 20253, '告警导出', 20251, 2, '#', '', '',
       1, 0, 'F', '0', '0', 'manager:alarm:export', '#',
       103, 1, SYSDATE(), NULL, NULL, ''
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE menu_id = 20253);
