-- ----------------------------
-- 维护树节点按钮权限
-- ----------------------------
INSERT INTO sys_menu
    (menu_id, menu_name, parent_id, order_num, path, component, query_param,
     is_frame, is_cache, menu_type, visible, status, perms, icon,
     create_dept, create_by, create_time, update_by, update_time, remark)
SELECT 20007, '树节点查询', 20001, 6, '#', '', '',
       1, 0, 'F', '0', '0', 'manager:treeNode:query', '#',
       103, 1, SYSDATE(), NULL, NULL, ''
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE menu_id = 20007);

INSERT INTO sys_menu
    (menu_id, menu_name, parent_id, order_num, path, component, query_param,
     is_frame, is_cache, menu_type, visible, status, perms, icon,
     create_dept, create_by, create_time, update_by, update_time, remark)
SELECT 20008, '树节点列表', 20001, 7, '#', '', '',
       1, 0, 'F', '0', '0', 'manager:treeNode:list', '#',
       103, 1, SYSDATE(), NULL, NULL, ''
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE menu_id = 20008);

INSERT INTO sys_menu
    (menu_id, menu_name, parent_id, order_num, path, component, query_param,
     is_frame, is_cache, menu_type, visible, status, perms, icon,
     create_dept, create_by, create_time, update_by, update_time, remark)
SELECT 20009, '树节点新增', 20001, 8, '#', '', '',
       1, 0, 'F', '0', '0', 'manager:treeNode:add', '#',
       103, 1, SYSDATE(), NULL, NULL, ''
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE menu_id = 20009);

INSERT INTO sys_menu
    (menu_id, menu_name, parent_id, order_num, path, component, query_param,
     is_frame, is_cache, menu_type, visible, status, perms, icon,
     create_dept, create_by, create_time, update_by, update_time, remark)
SELECT 20010, '树节点修改', 20001, 9, '#', '', '',
       1, 0, 'F', '0', '0', 'manager:treeNode:edit', '#',
       103, 1, SYSDATE(), NULL, NULL, ''
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE menu_id = 20010);

INSERT INTO sys_menu
    (menu_id, menu_name, parent_id, order_num, path, component, query_param,
     is_frame, is_cache, menu_type, visible, status, perms, icon,
     create_dept, create_by, create_time, update_by, update_time, remark)
SELECT 20011, '树节点删除', 20001, 10, '#', '', '',
       1, 0, 'F', '0', '0', 'manager:treeNode:remove', '#',
       103, 1, SYSDATE(), NULL, NULL, ''
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE menu_id = 20011);

INSERT INTO sys_menu
    (menu_id, menu_name, parent_id, order_num, path, component, query_param,
     is_frame, is_cache, menu_type, visible, status, perms, icon,
     create_dept, create_by, create_time, update_by, update_time, remark)
SELECT 20012, '树节点导出', 20001, 11, '#', '', '',
       1, 0, 'F', '0', '0', 'manager:treeNode:export', '#',
       103, 1, SYSDATE(), NULL, NULL, ''
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE menu_id = 20012);
