-- ----------------------------
-- 任务模板预览指令按钮权限
-- ----------------------------
INSERT INTO sys_menu
    (menu_id, menu_name, parent_id, order_num, path, component, query_param,
     is_frame, is_cache, menu_type, visible, status, perms, icon,
     create_dept, create_by, create_time, update_by, update_time, remark)
SELECT 20416, '任务模板预览', 20410, 6, '#', '', '', 1, 0, 'F', '0', '0',
       'manager:taskTemplate:preview', '#', 103, 1, SYSDATE(), NULL, NULL, ''
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE menu_id = 20416);
