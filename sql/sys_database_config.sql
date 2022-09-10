-- 数据库配置表
-- ----------------------------
-- Table structure for sys_database_config
-- ----------------------------
DROP TABLE IF EXISTS `sys_database_config`;
CREATE TABLE `sys_database_config`
(
    `db_id`       bigint(20) NOT NULL AUTO_INCREMENT COMMENT '数据库主键',
    `name`        varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '数据库名称',
    `url`         varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '数据库链接地址',
    `username`    varchar(150) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '数据库用户名',
    `password`    varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '数据库密码',
    `status`      char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '1' COMMENT '状态（0=正常,1=停用）',
    `create_by`   varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '创建者',
    `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
    `update_by`   varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '更新者',
    `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
    `remark`      varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '备注',
    PRIMARY KEY (`db_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '数据库配置表' ROW_FORMAT = Dynamic;


-- 数据库文档菜单 SQL
INSERT INTO `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query_param`,
                                 `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`,
                                 `create_time`, `update_by`, `update_time`, `remark`)
VALUES (116, '数据库文档', 3, 4, 'database-doc', 'database/doc/index.vue', '', 1, 0, 'C', '0', '0',
        'database:doc:export', 'documentation', 'admin', '2022-05-23 11:41:05', 'admin', '2022-09-07 09:08:12',
        '系统接口菜单');

-- 数据库配置菜单 SQL
INSERT INTO `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query_param`,
                                 `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`,
                                 `create_time`, `update_by`, `update_time`, `remark`)
VALUES (1564169687974174720, '数据库配置', 3, 3, 'database-config', 'database/config/index', NULL, 1, 0, 'C', '0', '0',
        'database:config:list', 'edit', 'admin', '2022-08-29 19:56:46', 'admin', '2022-09-07 09:08:18',
        '数据库配置菜单');

-- 数据库配置按钮 SQL
INSERT INTO `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query_param`,
                                 `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`,
                                 `create_time`, `update_by`, `update_time`, `remark`)
VALUES (1564169687974174721, '数据库配置查询', 1564169687974174720, 1, '#', '', NULL, 1, 0, 'F', '0', '0',
        'database:config:query', '#', 'admin', '2022-08-29 17:54:10', '', NULL, '');
INSERT INTO `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query_param`,
                                 `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`,
                                 `create_time`, `update_by`, `update_time`, `remark`)
VALUES (1564169687974174722, '数据库配置新增', 1564169687974174720, 2, '#', '', NULL, 1, 0, 'F', '0', '0',
        'database:config:add', '#', 'admin', '2022-08-29 17:54:10', '', NULL, '');
INSERT INTO `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query_param`,
                                 `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`,
                                 `create_time`, `update_by`, `update_time`, `remark`)
VALUES (1564169687974174723, '数据库配置修改', 1564169687974174720, 3, '#', '', NULL, 1, 0, 'F', '0', '0',
        'database:config:edit', '#', 'admin', '2022-08-29 17:54:10', '', NULL, '');
INSERT INTO `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query_param`,
                                 `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`,
                                 `create_time`, `update_by`, `update_time`, `remark`)
VALUES (1564169687974174724, '数据库配置删除', 1564169687974174720, 4, '#', '', NULL, 1, 0, 'F', '0', '0',
        'database:config:remove', '#', 'admin', '2022-08-29 17:54:10', '', NULL, '');
INSERT INTO `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query_param`,
                                 `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`,
                                 `create_time`, `update_by`, `update_time`, `remark`)
VALUES (1564169687974174725, '数据库配置导出', 1564169687974174720, 5, '#', '', NULL, 1, 0, 'F', '0', '0',
        'database:config:export', '#', 'admin', '2022-08-29 17:54:10', '', NULL, '');

