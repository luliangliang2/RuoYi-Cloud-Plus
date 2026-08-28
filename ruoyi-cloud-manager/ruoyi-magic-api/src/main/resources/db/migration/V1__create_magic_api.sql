CREATE TABLE IF NOT EXISTS magic_api_file (
    file_path    VARCHAR(512) NOT NULL COMMENT '文件路径',
    file_content MEDIUMTEXT   NOT NULL COMMENT '文件内容',
    PRIMARY KEY (file_path)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='Magic API接口定义';

CREATE TABLE IF NOT EXISTS magic_api_backup (
    id          VARCHAR(32)   NOT NULL COMMENT '资源ID',
    create_date BIGINT        NOT NULL COMMENT '备份时间',
    tag         VARCHAR(32)   DEFAULT NULL COMMENT '标签',
    type        VARCHAR(32)   DEFAULT NULL COMMENT '类型',
    name        VARCHAR(255)  DEFAULT NULL COMMENT '名称',
    create_by   VARCHAR(64)   DEFAULT NULL COMMENT '创建者',
    content     LONGBLOB      DEFAULT NULL COMMENT '备份内容',
    PRIMARY KEY (id, create_date),
    KEY idx_magic_api_backup_tag (tag),
    KEY idx_magic_api_backup_create_date (create_date)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='Magic API备份';

INSERT INTO sys_menu
    (menu_id, menu_name, parent_id, order_num, path, component, query_param,
     is_frame, is_cache, menu_type, visible, status, perms, icon,
     create_dept, create_by, create_time, update_by, update_time, remark)
SELECT 20080, 'Magic API', 3, 3, 'magic-api', 'tool/magic-api/index', '',
       1, 0, 'C', '0', '0', 'tool:magic:list', 'code',
       103, 1, NOW(), NULL, NULL, 'Magic API动态接口'
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE menu_id = 20080);

INSERT INTO sys_menu
    (menu_id, menu_name, parent_id, order_num, path, component, query_param,
     is_frame, is_cache, menu_type, visible, status, perms, icon,
     create_dept, create_by, create_time, update_by, update_time, remark)
SELECT 20081, 'Magic API查看', 20080, 1, '#', '', '',
       1, 0, 'F', '0', '0', 'tool:magic:view', '#',
       103, 1, NOW(), NULL, NULL, ''
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE menu_id = 20081);

INSERT INTO sys_menu
    (menu_id, menu_name, parent_id, order_num, path, component, query_param,
     is_frame, is_cache, menu_type, visible, status, perms, icon,
     create_dept, create_by, create_time, update_by, update_time, remark)
SELECT 20082, 'Magic API保存', 20080, 2, '#', '', '',
       1, 0, 'F', '0', '0', 'tool:magic:save', '#',
       103, 1, NOW(), NULL, NULL, ''
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE menu_id = 20082);

INSERT INTO sys_menu
    (menu_id, menu_name, parent_id, order_num, path, component, query_param,
     is_frame, is_cache, menu_type, visible, status, perms, icon,
     create_dept, create_by, create_time, update_by, update_time, remark)
SELECT 20083, 'Magic API删除', 20080, 3, '#', '', '',
       1, 0, 'F', '0', '0', 'tool:magic:remove', '#',
       103, 1, NOW(), NULL, NULL, ''
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE menu_id = 20083);

INSERT INTO sys_menu
    (menu_id, menu_name, parent_id, order_num, path, component, query_param,
     is_frame, is_cache, menu_type, visible, status, perms, icon,
     create_dept, create_by, create_time, update_by, update_time, remark)
SELECT 20084, 'Magic API上传', 20080, 4, '#', '', '',
       1, 0, 'F', '0', '0', 'tool:magic:upload', '#',
       103, 1, NOW(), NULL, NULL, ''
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE menu_id = 20084);

INSERT INTO sys_menu
    (menu_id, menu_name, parent_id, order_num, path, component, query_param,
     is_frame, is_cache, menu_type, visible, status, perms, icon,
     create_dept, create_by, create_time, update_by, update_time, remark)
SELECT 20085, 'Magic API下载', 20080, 5, '#', '', '',
       1, 0, 'F', '0', '0', 'tool:magic:download', '#',
       103, 1, NOW(), NULL, NULL, ''
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE menu_id = 20085);
