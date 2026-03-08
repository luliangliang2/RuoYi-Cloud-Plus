-- 示例：Flyway 迁移脚本
-- 规则：文件名必须为 V<版本号>__<描述>.sql
-- 说明：请把下面 demo 表替换为你真实要维护的表结构变更

CREATE TABLE IF NOT EXISTS manager_demo (
    id            BIGINT PRIMARY KEY,
    name          VARCHAR(64)  NOT NULL,
    remark        VARCHAR(255) NULL,
    create_time   DATETIME     NULL,
    update_time   DATETIME     NULL
);

-- 示例变更（按需保留）：
-- ALTER TABLE manager_demo ADD COLUMN status CHAR(1) DEFAULT '0';
