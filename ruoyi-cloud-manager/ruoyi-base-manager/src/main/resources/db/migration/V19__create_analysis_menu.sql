-- ----------------------------
-- 数据分析一级菜单
-- ----------------------------
INSERT INTO sys_menu
    (menu_id, menu_name, parent_id, order_num, path, component, query_param,
     is_frame, is_cache, menu_type, visible, status, perms, icon,
     create_dept, create_by, create_time, update_by, update_time, remark)
SELECT 20300, '数据分析', 0, 12, 'analysis', 'dashboard/analytics-clean/index', '',
       1, 0, 'C', '0', '0', 'analysis:dashboard:view', 'chart-line',
       103, 1, SYSDATE(), NULL, NULL, '数据分析一级菜单'
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE menu_id = 20300);
