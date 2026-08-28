-- ----------------------------
-- 上装相机表
-- ----------------------------
CREATE TABLE IF NOT EXISTS biz_camera
(
    camera_id        BIGINT        NOT NULL COMMENT '相机ID',
    tenant_id        VARCHAR(20)   DEFAULT '000000' COMMENT '租户编号',
    tree_id          BIGINT        DEFAULT NULL COMMENT '分类树ID',
    category_node_id BIGINT        DEFAULT NULL COMMENT '分类节点ID',
    camera_code      VARCHAR(64)   NOT NULL COMMENT '相机编码',
    camera_name      VARCHAR(100)  NOT NULL COMMENT '相机名称',
    sn               VARCHAR(100)  DEFAULT NULL COMMENT '设备SN号',
    view_angle       DECIMAL(6, 2) DEFAULT NULL COMMENT '光角度数',
    manufacturer     VARCHAR(100)  DEFAULT NULL COMMENT '厂商',
    model_name       VARCHAR(100)  DEFAULT NULL COMMENT '型号',
    status           CHAR(1)       DEFAULT '0' COMMENT '状态（0正常 1停用）',
    del_flag         CHAR(1)       DEFAULT '0' COMMENT '删除标志（0代表存在 1代表删除）',
    create_dept      BIGINT        DEFAULT NULL COMMENT '创建部门',
    create_by        BIGINT        DEFAULT NULL COMMENT '创建者',
    create_time      DATETIME      DEFAULT NULL COMMENT '创建时间',
    update_by        BIGINT        DEFAULT NULL COMMENT '更新者',
    update_time      DATETIME      DEFAULT NULL COMMENT '更新时间',
    remark           VARCHAR(500)  DEFAULT NULL COMMENT '备注',
    PRIMARY KEY (camera_id),
    KEY idx_biz_camera_tenant_code (tenant_id, camera_code),
    KEY idx_biz_camera_tenant_sn (tenant_id, sn),
    KEY idx_biz_camera_category (tenant_id, tree_id, category_node_id),
    KEY idx_biz_camera_status (tenant_id, status, del_flag)
) ENGINE = InnoDB COMMENT = '上装相机表';

-- ----------------------------
-- 上装雷达表
-- ----------------------------
CREATE TABLE IF NOT EXISTS biz_radar
(
    radar_id         BIGINT         NOT NULL COMMENT '雷达ID',
    tenant_id        VARCHAR(20)    DEFAULT '000000' COMMENT '租户编号',
    tree_id          BIGINT         DEFAULT NULL COMMENT '分类树ID',
    category_node_id BIGINT         DEFAULT NULL COMMENT '分类节点ID',
    radar_code       VARCHAR(64)    NOT NULL COMMENT '雷达编码',
    radar_name       VARCHAR(100)   NOT NULL COMMENT '雷达名称',
    sn               VARCHAR(100)   DEFAULT NULL COMMENT '设备SN号',
    line_count       INT            DEFAULT NULL COMMENT '雷达线数',
    detection_range  DECIMAL(10, 2) DEFAULT NULL COMMENT '探测范围（米）',
    manufacturer     VARCHAR(100)   DEFAULT NULL COMMENT '厂商',
    model_name       VARCHAR(100)   DEFAULT NULL COMMENT '型号',
    status           CHAR(1)        DEFAULT '0' COMMENT '状态（0正常 1停用）',
    del_flag         CHAR(1)        DEFAULT '0' COMMENT '删除标志（0代表存在 1代表删除）',
    create_dept      BIGINT         DEFAULT NULL COMMENT '创建部门',
    create_by        BIGINT         DEFAULT NULL COMMENT '创建者',
    create_time      DATETIME       DEFAULT NULL COMMENT '创建时间',
    update_by        BIGINT         DEFAULT NULL COMMENT '更新者',
    update_time      DATETIME       DEFAULT NULL COMMENT '更新时间',
    remark           VARCHAR(500)   DEFAULT NULL COMMENT '备注',
    PRIMARY KEY (radar_id),
    KEY idx_biz_radar_tenant_code (tenant_id, radar_code),
    KEY idx_biz_radar_tenant_sn (tenant_id, sn),
    KEY idx_biz_radar_category (tenant_id, tree_id, category_node_id),
    KEY idx_biz_radar_status (tenant_id, status, del_flag)
) ENGINE = InnoDB COMMENT = '上装雷达表';

-- ----------------------------
-- 车辆上装绑定表
-- ----------------------------
CREATE TABLE IF NOT EXISTS biz_vehicle_equipment_bind
(
    bind_id          BIGINT       NOT NULL COMMENT '绑定ID',
    tenant_id        VARCHAR(20)  DEFAULT '000000' COMMENT '租户编号',
    vehicle_id       BIGINT       NOT NULL COMMENT '车辆ID',
    equipment_type   CHAR(1)      NOT NULL COMMENT '设备类型（1相机 2雷达）',
    equipment_id     BIGINT       NOT NULL COMMENT '设备ID',
    install_position VARCHAR(64)  DEFAULT NULL COMMENT '安装位置',
    install_time     DATETIME     DEFAULT NULL COMMENT '安装时间',
    status           CHAR(1)      DEFAULT '0' COMMENT '状态（0正常 1停用）',
    del_flag         CHAR(1)      DEFAULT '0' COMMENT '删除标志（0代表存在 1代表删除）',
    create_dept      BIGINT       DEFAULT NULL COMMENT '创建部门',
    create_by        BIGINT       DEFAULT NULL COMMENT '创建者',
    create_time      DATETIME     DEFAULT NULL COMMENT '创建时间',
    update_by        BIGINT       DEFAULT NULL COMMENT '更新者',
    update_time      DATETIME     DEFAULT NULL COMMENT '更新时间',
    remark           VARCHAR(500) DEFAULT NULL COMMENT '备注',
    PRIMARY KEY (bind_id),
    KEY idx_biz_vehicle_equipment_vehicle (tenant_id, vehicle_id, equipment_type, del_flag),
    KEY idx_biz_vehicle_equipment_device (tenant_id, equipment_type, equipment_id, del_flag)
) ENGINE = InnoDB COMMENT = '车辆上装绑定表';

-- ----------------------------
-- 安装位置字典
-- ----------------------------
INSERT INTO sys_dict_type
    (dict_id, tenant_id, dict_name, dict_type, create_dept, create_by, create_time, update_by, update_time, remark)
SELECT 20001, '000000', '车辆设备安装位置', 'vehicle_install_position', 103, 1, SYSDATE(), NULL, NULL, '车辆上装安装位置'
WHERE NOT EXISTS (SELECT 1 FROM sys_dict_type WHERE dict_type = 'vehicle_install_position');

INSERT INTO sys_dict_data
    (dict_code, tenant_id, dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default,
     create_dept, create_by, create_time, update_by, update_time, remark)
SELECT 20001, '000000', 1, '前方', 'front', 'vehicle_install_position', '', 'primary', 'N', 103, 1, SYSDATE(), NULL, NULL, ''
WHERE NOT EXISTS (SELECT 1 FROM sys_dict_data WHERE dict_code = 20001);
INSERT INTO sys_dict_data
    (dict_code, tenant_id, dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default,
     create_dept, create_by, create_time, update_by, update_time, remark)
SELECT 20002, '000000', 2, '后方', 'rear', 'vehicle_install_position', '', 'success', 'N', 103, 1, SYSDATE(), NULL, NULL, ''
WHERE NOT EXISTS (SELECT 1 FROM sys_dict_data WHERE dict_code = 20002);
INSERT INTO sys_dict_data
    (dict_code, tenant_id, dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default,
     create_dept, create_by, create_time, update_by, update_time, remark)
SELECT 20003, '000000', 3, '左侧', 'left', 'vehicle_install_position', '', 'info', 'N', 103, 1, SYSDATE(), NULL, NULL, ''
WHERE NOT EXISTS (SELECT 1 FROM sys_dict_data WHERE dict_code = 20003);
INSERT INTO sys_dict_data
    (dict_code, tenant_id, dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default,
     create_dept, create_by, create_time, update_by, update_time, remark)
SELECT 20004, '000000', 4, '右侧', 'right', 'vehicle_install_position', '', 'info', 'N', 103, 1, SYSDATE(), NULL, NULL, ''
WHERE NOT EXISTS (SELECT 1 FROM sys_dict_data WHERE dict_code = 20004);
INSERT INTO sys_dict_data
    (dict_code, tenant_id, dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default,
     create_dept, create_by, create_time, update_by, update_time, remark)
SELECT 20005, '000000', 5, '车内', 'inside', 'vehicle_install_position', '', 'warning', 'N', 103, 1, SYSDATE(), NULL, NULL, ''
WHERE NOT EXISTS (SELECT 1 FROM sys_dict_data WHERE dict_code = 20005);
INSERT INTO sys_dict_data
    (dict_code, tenant_id, dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default,
     create_dept, create_by, create_time, update_by, update_time, remark)
SELECT 20006, '000000', 6, '车顶', 'top', 'vehicle_install_position', '', 'warning', 'N', 103, 1, SYSDATE(), NULL, NULL, ''
WHERE NOT EXISTS (SELECT 1 FROM sys_dict_data WHERE dict_code = 20006);
INSERT INTO sys_dict_data
    (dict_code, tenant_id, dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default,
     create_dept, create_by, create_time, update_by, update_time, remark)
SELECT 20007, '000000', 99, '其他', 'other', 'vehicle_install_position', '', 'default', 'Y', 103, 1, SYSDATE(), NULL, NULL, ''
WHERE NOT EXISTS (SELECT 1 FROM sys_dict_data WHERE dict_code = 20007);

-- ----------------------------
-- 菜单权限
-- ----------------------------
INSERT INTO sys_menu
    (menu_id, menu_name, parent_id, order_num, path, component, query_param,
     is_frame, is_cache, menu_type, visible, status, perms, icon,
     create_dept, create_by, create_time, update_by, update_time, remark)
SELECT 20010, '上装数据', 0, 7, 'equipment', NULL, '', 1, 0, 'M', '0', '0', '', 'component',
       103, 1, SYSDATE(), NULL, NULL, '上装数据目录'
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE menu_id = 20010);

INSERT INTO sys_menu
    (menu_id, menu_name, parent_id, order_num, path, component, query_param,
     is_frame, is_cache, menu_type, visible, status, perms, icon,
     create_dept, create_by, create_time, update_by, update_time, remark)
SELECT 20011, '相机管理', 20010, 1, 'camera', 'manager/equipment/camera/index', '', 1, 0, 'C', '0', '0',
       'manager:camera:list', 'camera', 103, 1, SYSDATE(), NULL, NULL, '相机管理菜单'
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE menu_id = 20011);

INSERT INTO sys_menu
    (menu_id, menu_name, parent_id, order_num, path, component, query_param,
     is_frame, is_cache, menu_type, visible, status, perms, icon,
     create_dept, create_by, create_time, update_by, update_time, remark)
SELECT 20012, '相机查询', 20011, 1, '#', '', '', 1, 0, 'F', '0', '0', 'manager:camera:query', '#',
       103, 1, SYSDATE(), NULL, NULL, ''
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE menu_id = 20012);
INSERT INTO sys_menu
    (menu_id, menu_name, parent_id, order_num, path, component, query_param,
     is_frame, is_cache, menu_type, visible, status, perms, icon,
     create_dept, create_by, create_time, update_by, update_time, remark)
SELECT 20013, '相机新增', 20011, 2, '#', '', '', 1, 0, 'F', '0', '0', 'manager:camera:add', '#',
       103, 1, SYSDATE(), NULL, NULL, ''
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE menu_id = 20013);
INSERT INTO sys_menu
    (menu_id, menu_name, parent_id, order_num, path, component, query_param,
     is_frame, is_cache, menu_type, visible, status, perms, icon,
     create_dept, create_by, create_time, update_by, update_time, remark)
SELECT 20014, '相机修改', 20011, 3, '#', '', '', 1, 0, 'F', '0', '0', 'manager:camera:edit', '#',
       103, 1, SYSDATE(), NULL, NULL, ''
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE menu_id = 20014);
INSERT INTO sys_menu
    (menu_id, menu_name, parent_id, order_num, path, component, query_param,
     is_frame, is_cache, menu_type, visible, status, perms, icon,
     create_dept, create_by, create_time, update_by, update_time, remark)
SELECT 20015, '相机删除', 20011, 4, '#', '', '', 1, 0, 'F', '0', '0', 'manager:camera:remove', '#',
       103, 1, SYSDATE(), NULL, NULL, ''
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE menu_id = 20015);
INSERT INTO sys_menu
    (menu_id, menu_name, parent_id, order_num, path, component, query_param,
     is_frame, is_cache, menu_type, visible, status, perms, icon,
     create_dept, create_by, create_time, update_by, update_time, remark)
SELECT 20016, '相机导出', 20011, 5, '#', '', '', 1, 0, 'F', '0', '0', 'manager:camera:export', '#',
       103, 1, SYSDATE(), NULL, NULL, ''
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE menu_id = 20016);

INSERT INTO sys_menu
    (menu_id, menu_name, parent_id, order_num, path, component, query_param,
     is_frame, is_cache, menu_type, visible, status, perms, icon,
     create_dept, create_by, create_time, update_by, update_time, remark)
SELECT 20020, '雷达管理', 20010, 2, 'radar', 'manager/equipment/radar/index', '', 1, 0, 'C', '0', '0',
       'manager:radar:list', 'radar', 103, 1, SYSDATE(), NULL, NULL, '雷达管理菜单'
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE menu_id = 20020);

INSERT INTO sys_menu
    (menu_id, menu_name, parent_id, order_num, path, component, query_param,
     is_frame, is_cache, menu_type, visible, status, perms, icon,
     create_dept, create_by, create_time, update_by, update_time, remark)
SELECT 20021, '雷达查询', 20020, 1, '#', '', '', 1, 0, 'F', '0', '0', 'manager:radar:query', '#',
       103, 1, SYSDATE(), NULL, NULL, ''
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE menu_id = 20021);
INSERT INTO sys_menu
    (menu_id, menu_name, parent_id, order_num, path, component, query_param,
     is_frame, is_cache, menu_type, visible, status, perms, icon,
     create_dept, create_by, create_time, update_by, update_time, remark)
SELECT 20022, '雷达新增', 20020, 2, '#', '', '', 1, 0, 'F', '0', '0', 'manager:radar:add', '#',
       103, 1, SYSDATE(), NULL, NULL, ''
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE menu_id = 20022);
INSERT INTO sys_menu
    (menu_id, menu_name, parent_id, order_num, path, component, query_param,
     is_frame, is_cache, menu_type, visible, status, perms, icon,
     create_dept, create_by, create_time, update_by, update_time, remark)
SELECT 20023, '雷达修改', 20020, 3, '#', '', '', 1, 0, 'F', '0', '0', 'manager:radar:edit', '#',
       103, 1, SYSDATE(), NULL, NULL, ''
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE menu_id = 20023);
INSERT INTO sys_menu
    (menu_id, menu_name, parent_id, order_num, path, component, query_param,
     is_frame, is_cache, menu_type, visible, status, perms, icon,
     create_dept, create_by, create_time, update_by, update_time, remark)
SELECT 20024, '雷达删除', 20020, 4, '#', '', '', 1, 0, 'F', '0', '0', 'manager:radar:remove', '#',
       103, 1, SYSDATE(), NULL, NULL, ''
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE menu_id = 20024);
INSERT INTO sys_menu
    (menu_id, menu_name, parent_id, order_num, path, component, query_param,
     is_frame, is_cache, menu_type, visible, status, perms, icon,
     create_dept, create_by, create_time, update_by, update_time, remark)
SELECT 20025, '雷达导出', 20020, 5, '#', '', '', 1, 0, 'F', '0', '0', 'manager:radar:export', '#',
       103, 1, SYSDATE(), NULL, NULL, ''
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE menu_id = 20025);

INSERT INTO sys_menu
    (menu_id, menu_name, parent_id, order_num, path, component, query_param,
     is_frame, is_cache, menu_type, visible, status, perms, icon,
     create_dept, create_by, create_time, update_by, update_time, remark)
SELECT 20030, '车辆上装查询', 20010, 50, '#', '', '', 1, 0, 'F', '0', '0', 'manager:vehicleEquipment:list', '#',
       103, 1, SYSDATE(), NULL, NULL, ''
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE menu_id = 20030);
INSERT INTO sys_menu
    (menu_id, menu_name, parent_id, order_num, path, component, query_param,
     is_frame, is_cache, menu_type, visible, status, perms, icon,
     create_dept, create_by, create_time, update_by, update_time, remark)
SELECT 20031, '车辆上装绑定', 20010, 51, '#', '', '', 1, 0, 'F', '0', '0', 'manager:vehicleEquipment:add', '#',
       103, 1, SYSDATE(), NULL, NULL, ''
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE menu_id = 20031);
INSERT INTO sys_menu
    (menu_id, menu_name, parent_id, order_num, path, component, query_param,
     is_frame, is_cache, menu_type, visible, status, perms, icon,
     create_dept, create_by, create_time, update_by, update_time, remark)
SELECT 20032, '车辆上装修改', 20010, 52, '#', '', '', 1, 0, 'F', '0', '0', 'manager:vehicleEquipment:edit', '#',
       103, 1, SYSDATE(), NULL, NULL, ''
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE menu_id = 20032);
INSERT INTO sys_menu
    (menu_id, menu_name, parent_id, order_num, path, component, query_param,
     is_frame, is_cache, menu_type, visible, status, perms, icon,
     create_dept, create_by, create_time, update_by, update_time, remark)
SELECT 20033, '车辆上装解绑', 20010, 53, '#', '', '', 1, 0, 'F', '0', '0', 'manager:vehicleEquipment:remove', '#',
       103, 1, SYSDATE(), NULL, NULL, ''
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE menu_id = 20033);
