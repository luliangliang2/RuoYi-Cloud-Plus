-- 缓存监控菜单 SQL
INSERT INTO `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query_param`,
                                 `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`,
                                 `create_time`, `update_by`, `update_time`, `remark`)
VALUES (1562343666828320769, '缓存监控', 2, 2, 'cache', 'monitor/cache/index', NULL, 1, 0, 'C', '0', '0',
        'monitor:cache:list', 'redis', 'admin', '2022-08-24 15:38:52', 'admin', '2022-08-24 15:44:20', '');

-- 缓存列表菜单 SQL
INSERT INTO `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query_param`,
                                 `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`,
                                 `create_time`, `update_by`, `update_time`, `remark`)
VALUES (1562346429444427778, '缓存列表', 2, 2, 'cacheList', 'monitor/cache/list', NULL, 1, 0, 'C', '0', '0',
        'monitor:cache:list', 'redis-list', 'admin', '2022-08-24 15:49:50', 'admin', '2022-08-24 15:49:50', '');
