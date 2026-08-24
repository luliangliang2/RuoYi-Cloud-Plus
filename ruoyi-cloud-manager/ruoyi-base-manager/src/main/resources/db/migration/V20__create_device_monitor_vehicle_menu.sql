-- ----------------------------
-- 设备监控 / 车辆监控菜单
-- ----------------------------
INSERT INTO sys_menu
    (menu_id, menu_name, parent_id, order_num, path, component, query_param,
     is_frame, is_cache, menu_type, visible, status, perms, icon,
     create_dept, create_by, create_time, update_by, update_time, remark)
SELECT 20350, '设备监控', 0, 13, 'device-monitor', NULL, '',
       1, 0, 'M', '0', '0', '', 'monitor',
       103, 1, SYSDATE(), NULL, NULL, '设备监控目录'
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE menu_id = 20350);

INSERT INTO sys_menu
    (menu_id, menu_name, parent_id, order_num, path, component, query_param,
     is_frame, is_cache, menu_type, visible, status, perms, icon,
     create_dept, create_by, create_time, update_by, update_time, remark)
SELECT 20351, '车辆监控', 20350, 1, 'vehicle', 'manager/vehicleMonitor/index', '',
       1, 0, 'C', '0', '0', 'manager:vehicleMonitor:list', 'car',
       103, 1, SYSDATE(), NULL, NULL, '车辆监控菜单'
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE menu_id = 20351);
