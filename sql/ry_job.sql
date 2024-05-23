create schema ry_job;
set schema ry_job;
-- ----------------------------
-- Table structure for pj_app_info
-- ----------------------------
DROP TABLE IF EXISTS pj_app_info;
CREATE TABLE pj_app_info  (
    id BIGINT NOT NULL AUTO_INCREMENT,
    app_name VARCHAR2(255) NULL DEFAULT NULL,
    current_server VARCHAR2(255) NULL DEFAULT NULL,
    gmt_create datetime(6) NULL DEFAULT NULL,
    gmt_modified datetime(6) NULL DEFAULT NULL,
    password VARCHAR2(255) NULL DEFAULT NULL,
    PRIMARY KEY (id)
) ;


create UNIQUE INDEX uidx01_app_info on pj_app_info(app_name);
-- ----------------------------
-- Records of pj_app_info
-- ----------------------------
INSERT INTO pj_app_info VALUES (1, 'ruoyi-worker', '127.0.0.1:10010', '2023-06-13 16:32:59.263000', '2023-07-04 17:25:49.798000', '123456');

-- ----------------------------
-- Table structure for pj_container_info
-- ----------------------------
DROP TABLE IF EXISTS pj_container_info;
CREATE TABLE pj_container_info  (
    id BIGINT NOT NULL AUTO_INCREMENT,
    app_id BIGINT NULL DEFAULT NULL,
    container_name VARCHAR2(255) NULL DEFAULT NULL,
    gmt_create datetime(6) NULL DEFAULT NULL,
    gmt_modified datetime(6) NULL DEFAULT NULL,
    last_deploy_time datetime(6) NULL DEFAULT NULL,
    source_info VARCHAR2(255) NULL DEFAULT NULL,
    source_type INT NULL DEFAULT NULL,
    status INT NULL DEFAULT NULL,
    version VARCHAR2(255) NULL DEFAULT NULL,
    PRIMARY KEY (id)
) ;

create     INDEX idx01_container_info on pj_container_info(app_id);
-- ----------------------------
-- Table structure for pj_instance_info
-- ----------------------------
DROP TABLE IF EXISTS pj_instance_info;
CREATE TABLE pj_instance_info  (
    id BIGINT NOT NULL AUTO_INCREMENT,
    actual_trigger_time BIGINT NULL DEFAULT NULL,
    app_id BIGINT NULL DEFAULT NULL,
    expected_trigger_time BIGINT NULL DEFAULT NULL,
    finished_time BIGINT NULL DEFAULT NULL,
    gmt_create datetime(6) NULL DEFAULT NULL,
    gmt_modified datetime(6) NULL DEFAULT NULL,
    instance_id BIGINT NULL DEFAULT NULL,
    instance_params TEXT NULL,
    job_id BIGINT NULL DEFAULT NULL,
    job_params TEXT NULL,
    last_report_time BIGINT NULL DEFAULT NULL,
    "result" TEXT NULL,
    running_times BIGINT NULL DEFAULT NULL,
    status INT NULL DEFAULT NULL,
    task_tracker_address VARCHAR2(255) NULL DEFAULT NULL,
    "type" INT NULL DEFAULT NULL,
    wf_instance_id BIGINT NULL DEFAULT NULL,
    PRIMARY KEY (id)
) ;


create INDEX idx01_instance_info on pj_instance_info(job_id, status);
create INDEX idx02_instance_info on pj_instance_info(app_id, status);
create INDEX idx03_instance_info on pj_instance_info(instance_id, status);
-- ----------------------------
-- Table structure for pj_job_info
-- ----------------------------
DROP TABLE IF EXISTS pj_job_info;
CREATE TABLE pj_job_info  (
    id BIGINT NOT NULL AUTO_INCREMENT,
    alarm_config VARCHAR2(255) NULL DEFAULT NULL,
    app_id BIGINT NULL DEFAULT NULL,
    concurrency INT NULL DEFAULT NULL,
    designated_workers VARCHAR2(255) NULL DEFAULT NULL,
    dispatch_strategy INT NULL DEFAULT NULL,
    execute_type INT NULL DEFAULT NULL,
    extra VARCHAR2(255) NULL DEFAULT NULL,
    gmt_create datetime(6) NULL DEFAULT NULL,
    gmt_modified datetime(6) NULL DEFAULT NULL,
    instance_retry_num INT NULL DEFAULT NULL,
    instance_time_limit BIGINT NULL DEFAULT NULL,
    job_description VARCHAR2(255) NULL DEFAULT NULL,
    job_name VARCHAR2(255) NULL DEFAULT NULL,
    job_params TEXT NULL,
    lifecycle VARCHAR2(255) NULL DEFAULT NULL,
    log_config VARCHAR2(255) NULL DEFAULT NULL,
    max_instance_num INT NULL DEFAULT NULL,
    max_worker_count INT NULL DEFAULT NULL,
    min_cpu_cores double NOT NULL,
    min_disk_space double NOT NULL,
    min_memory_space double NOT NULL,
    next_trigger_time BIGINT NULL DEFAULT NULL,
    notify_user_ids VARCHAR2(255) NULL DEFAULT NULL,
    processor_info VARCHAR2(255) NULL DEFAULT NULL,
    processor_type INT NULL DEFAULT NULL,
    status INT NULL DEFAULT NULL,
    tag VARCHAR2(255) NULL DEFAULT NULL,
    task_retry_num INT NULL DEFAULT NULL,
    time_expression VARCHAR2(255) NULL DEFAULT NULL,
    time_expression_type INT NULL DEFAULT NULL,
    PRIMARY KEY (id)
) ;

create INDEX idx01_job_info on pj_job_info(app_id, status, time_expression_type, next_trigger_time);
-- ----------------------------
-- Records of pj_job_info
-- ----------------------------
INSERT INTO pj_job_info VALUES (1, '{\"alertThreshold\":0,\"silenceWindowLen\":0,\"statisticWindowLen\":0}', 1, 5, '', 2, 1, NULL, '2023-06-02 15:01:27.717000', '2023-07-04 17:22:12.374000', 1, 0, '', '单机处理器执行测试', NULL, '{}', '{\"type\":1}', 0, 0, 0, 0, 0, NULL, NULL, 'org.dromara.job.processors.StandaloneProcessorDemo', 1, 2, NULL, 1, '30000', 3);
INSERT INTO pj_job_info VALUES (2, '{\"alertThreshold\":0,\"silenceWindowLen\":0,\"statisticWindowLen\":0}', 1, 5, '', 1, 2, NULL, '2023-06-02 15:04:45.342000', '2023-07-04 17:22:12.816000', 0, 0, NULL, '广播处理器测试', NULL, '{}', '{\"type\":1}', 0, 0, 0, 0, 0, NULL, NULL, 'org.dromara.job.processors.BroadcastProcessorDemo', 1, 2, NULL, 1, '30000', 3);
INSERT INTO pj_job_info VALUES (3, '{\"alertThreshold\":0,\"silenceWindowLen\":0,\"statisticWindowLen\":0}', 1, 5, '', 1, 4, NULL, '2023-06-02 15:13:23.519000', '2023-06-02 16:03:22.421000', 0, 0, NULL, 'Map处理器测试', NULL, '{}', '{\"type\":1}', 0, 0, 0, 0, 0, NULL, NULL, 'org.dromara.job.processors.MapProcessorDemo', 1, 2, NULL, 1, '1000', 3);
INSERT INTO pj_job_info VALUES (4, '{\"alertThreshold\":0,\"silenceWindowLen\":0,\"statisticWindowLen\":0}', 1, 5, '', 1, 3, NULL, '2023-06-02 15:45:25.896000', '2023-06-02 16:03:23.125000', 0, 0, NULL, 'MapReduce处理器测试', NULL, '{}', '{\"type\":1}', 0, 0, 0, 0, 0, NULL, NULL, 'org.dromara.job.processors.MapReduceProcessorDemo', 1, 2, NULL, 1, '1000', 3);

-- ----------------------------
-- Table structure for pj_oms_lock
-- ----------------------------
DROP TABLE IF EXISTS pj_oms_lock;
CREATE TABLE pj_oms_lock  (
    id BIGINT NOT NULL AUTO_INCREMENT,
    gmt_create datetime(6) NULL DEFAULT NULL,
    gmt_modified datetime(6) NULL DEFAULT NULL,
    lock_name VARCHAR2(255) NULL DEFAULT NULL,
    max_lock_time BIGINT NULL DEFAULT NULL,
    ownerip VARCHAR2(255) NULL DEFAULT NULL,
    PRIMARY KEY (id)
) ;

create     UNIQUE INDEX uidx01_oms_lock on pj_oms_lock(lock_name);
-- ----------------------------
-- Table structure for pj_server_info
-- ----------------------------
DROP TABLE IF EXISTS pj_server_info;
CREATE TABLE pj_server_info  (
    id BIGINT NOT NULL AUTO_INCREMENT,
    gmt_create datetime(6) NULL DEFAULT NULL,
    gmt_modified datetime(6) NULL DEFAULT NULL,
    ip VARCHAR2(255) NULL DEFAULT NULL,
    PRIMARY KEY (id)
) ;

create UNIQUE INDEX uidx01_server_info on pj_server_info(ip);
create INDEX idx01_server_info on pj_server_info(gmt_modified);
-- ----------------------------
-- Table structure for pj_user_info
-- ----------------------------
DROP TABLE IF EXISTS pj_user_info;
CREATE TABLE pj_user_info  (
    id BIGINT NOT NULL AUTO_INCREMENT,
    email VARCHAR2(255) NULL DEFAULT NULL,
    extra VARCHAR2(255) NULL DEFAULT NULL,
    gmt_create datetime(6) NULL DEFAULT NULL,
    gmt_modified datetime(6) NULL DEFAULT NULL,
    password VARCHAR2(255) NULL DEFAULT NULL,
    phone VARCHAR2(255) NULL DEFAULT NULL,
    username VARCHAR2(255) NULL DEFAULT NULL,
    web_hook VARCHAR2(255) NULL DEFAULT NULL,
    PRIMARY KEY (id)
) ;

create INDEX uidx01_user_info on pj_user_info(username);
create INDEX uidx02_user_info on pj_user_info(email) ;
-- ----------------------------
-- Table structure for pj_workflow_info
-- ----------------------------
DROP TABLE IF EXISTS pj_workflow_info;
CREATE TABLE pj_workflow_info  (
    id BIGINT NOT NULL AUTO_INCREMENT,
    app_id BIGINT NULL DEFAULT NULL,
    extra VARCHAR2(255) NULL DEFAULT NULL,
    gmt_create datetime(6) NULL DEFAULT NULL,
    gmt_modified datetime(6) NULL DEFAULT NULL,
    lifecycle VARCHAR2(255) NULL DEFAULT NULL,
    max_wf_instance_num INT NULL DEFAULT NULL,
    next_trigger_time BIGINT NULL DEFAULT NULL,
    notify_user_ids VARCHAR2(255) NULL DEFAULT NULL,
    pedag TEXT NULL,
    status INT NULL DEFAULT NULL,
    time_expression VARCHAR2(255) NULL DEFAULT NULL,
    time_expression_type INT NULL DEFAULT NULL,
    wf_description VARCHAR2(255) NULL DEFAULT NULL,
    wf_name VARCHAR2(255) NULL DEFAULT NULL,
    PRIMARY KEY (id)
) ;

create     INDEX idx01_workflow_info on pj_workflow_info(app_id, status, time_expression_type, next_trigger_time);
-- ----------------------------
-- Table structure for pj_workflow_instance_info
-- ----------------------------
DROP TABLE IF EXISTS pj_workflow_instance_info;
CREATE TABLE pj_workflow_instance_info  (
    id BIGINT NOT NULL AUTO_INCREMENT,
    actual_trigger_time BIGINT NULL DEFAULT NULL,
    app_id BIGINT NULL DEFAULT NULL,
    dag TEXT NULL,
    expected_trigger_time BIGINT NULL DEFAULT NULL,
    finished_time BIGINT NULL DEFAULT NULL,
    gmt_create datetime(6) NULL DEFAULT NULL,
    gmt_modified datetime(6) NULL DEFAULT NULL,
    parent_wf_instance_id BIGINT NULL DEFAULT NULL,
    result TEXT NULL,
    status INT NULL DEFAULT NULL,
    wf_context TEXT NULL,
    wf_init_params TEXT NULL,
    wf_instance_id BIGINT NULL DEFAULT NULL,
    workflow_id BIGINT NULL DEFAULT NULL,
    PRIMARY KEY (id)

) ;

create UNIQUE INDEX uidx01_wf_instance on pj_workflow_instance_info(wf_instance_id);
create INDEX idx01_wf_instance on pj_workflow_instance_info(workflow_id, status, app_id, expected_trigger_time);
-- ----------------------------
-- Table structure for pj_workflow_node_info
-- ----------------------------
DROP TABLE IF EXISTS pj_workflow_node_info;
CREATE TABLE pj_workflow_node_info  (
    id BIGINT NOT NULL AUTO_INCREMENT,
    app_id BIGINT NOT NULL,
    enable bit NOT NULL,
    extra TEXT NULL,
    gmt_create datetime(6) NULL,
    gmt_modified datetime(6) NULL,
    job_id BIGINT NULL DEFAULT NULL,
    node_name VARCHAR2(255) NULL DEFAULT NULL,
    node_params TEXT NULL,
    skip_when_failed bit NOT NULL,
    type INT NULL DEFAULT NULL,
    workflow_id BIGINT NULL DEFAULT NULL,
    PRIMARY KEY (id)
) ;
create INDEX idx01_workflow_node_info on pj_workflow_node_info(workflow_id, gmt_create);