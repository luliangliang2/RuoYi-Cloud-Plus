create schema ry_seata;
set schema ry_seata;
-- -------------------------------- The script used when storeMode is 'db' --------------------------------
-- the table to store GlobalSession data
CREATE TABLE IF NOT EXISTS global_table
(
    xid                       VARCHAR2(128) NOT NULL,
    transaction_id            BIGINT,
    status                    TINYINT      NOT NULL,
    application_id            VARCHAR2(32),
    transaction_service_group VARCHAR2(32),
    transaction_name          VARCHAR2(128),
    timeout                   INT,
    begin_time                BIGINT,
    application_data          VARCHAR2(2000),
    gmt_create                DATETIME,
    gmt_modified              DATETIME,
    PRIMARY KEY (xid)
) ;

create index idx_status_gmt_modified on global_table(status , gmt_modified);
create index idx_transaction_id on global_table(transaction_id);
-- the table to store BranchSession data
CREATE TABLE IF NOT EXISTS branch_table
(
    branch_id         BIGINT       NOT NULL,
    xid               VARCHAR2(128) NOT NULL,
    transaction_id    BIGINT,
    resource_group_id VARCHAR2(32),
    resource_id       VARCHAR2(256),
    branch_type       VARCHAR2(8),
    status            TINYINT,
    client_id         VARCHAR2(64),
    application_data  VARCHAR2(2000),
    gmt_create        DATETIME(6),
    gmt_modified      DATETIME(6),
    PRIMARY KEY (branch_id)
) ;

create index idx_xid on branch_table(xid);
-- the table to store lock data
CREATE TABLE IF NOT EXISTS lock_table
(
    row_key        VARCHAR2(128) NOT NULL,
    xid            VARCHAR2(128),
    transaction_id BIGINT,
    branch_id      BIGINT       NOT NULL,
    resource_id    VARCHAR2(256),
    table_name     VARCHAR2(32),
    pk             VARCHAR2(36),
    status         TINYINT      NOT NULL DEFAULT '0' COMMENT '0:locked ,1:rollbacking',
    gmt_create     DATETIME,
    gmt_modified   DATETIME,
    PRIMARY KEY (row_key)
) ;

create index idx_status on lock_table(status);
create index idx_branch_id on lock_table(branch_id);
create index idx_xid_and_branch_id on lock_table(xid , branch_id);



CREATE TABLE IF NOT EXISTS distributed_lock
(
    lock_key       CHAR(20) NOT NULL,
    lock_value     VARCHAR2(20) NOT NULL,
    expire         BIGINT,
    primary key (lock_key)
) ;

INSERT INTO distributed_lock (lock_key, lock_value, expire) VALUES ('AsyncCommitting', ' ', 0);
INSERT INTO distributed_lock (lock_key, lock_value, expire) VALUES ('RetryCommitting', ' ', 0);
INSERT INTO distributed_lock (lock_key, lock_value, expire) VALUES ('RetryRollbacking', ' ', 0);
INSERT INTO distributed_lock (lock_key, lock_value, expire) VALUES ('TxTimeoutCheck', ' ', 0);
