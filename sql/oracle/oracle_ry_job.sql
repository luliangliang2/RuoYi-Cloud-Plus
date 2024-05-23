create schema oracle_ry_job;
set schema oracle_ry_job;

-- ----------------------------
-- Table structure for PJ_APP_INFO
-- ----------------------------
CREATE TABLE "PJ_APP_INFO" (
  "ID" NUMBER(19)  NOT NULL ,
  "APP_NAME" VARCHAR2(255 CHAR)  ,
  "CURRENT_SERVER" VARCHAR2(255 CHAR)  ,
  "GMT_CREATE" TIMESTAMP(6)  ,
  "GMT_MODIFIED" TIMESTAMP(6)  ,
  "PASSWORD" VARCHAR2(255 CHAR) 
)
;

-- ----------------------------
-- Records of "PJ_APP_INFO"
-- ----------------------------
INSERT INTO "PJ_APP_INFO" VALUES ('1', 'ruoyi-worker', '127.0.0.1:10010', NULL, NULL, '123456');

-- ----------------------------
-- Table structure for PJ_CONTAINER_INFO
-- ----------------------------
CREATE TABLE "PJ_CONTAINER_INFO" (
  "ID" NUMBER(19)  NOT NULL ,
  "APP_ID" NUMBER(19)  ,
  "CONTAINER_NAME" VARCHAR2(255 CHAR)  ,
  "GMT_CREATE" TIMESTAMP(6)  ,
  "GMT_MODIFIED" TIMESTAMP(6)  ,
  "LAST_DEPLOY_TIME" TIMESTAMP(6)  ,
  "SOURCE_INFO" VARCHAR2(255 CHAR)  ,
  "SOURCE_TYPE" NUMBER(10)  ,
  "STATUS" NUMBER(10)  ,
  "VERSION" VARCHAR2(255 CHAR) 
)
;

-- ----------------------------
-- Table structure for PJ_INSTANCE_INFO
-- ----------------------------
CREATE TABLE "PJ_INSTANCE_INFO" (
  "ID" NUMBER(19)  NOT NULL ,
  "ACTUAL_TRIGGER_TIME" NUMBER(19)  ,
  "APP_ID" NUMBER(19)  ,
  "EXPECTED_TRIGGER_TIME" NUMBER(19)  ,
  "FINISHED_TIME" NUMBER(19)  ,
  "GMT_CREATE" TIMESTAMP(6)  ,
  "GMT_MODIFIED" TIMESTAMP(6)  ,
  "INSTANCE_ID" NUMBER(19)  ,
  "INSTANCE_PARAMS" CLOB  ,
  "JOB_ID" NUMBER(19)  ,
  "JOB_PARAMS" CLOB  ,
  "LAST_REPORT_TIME" NUMBER(19)  ,
  "RESULT" CLOB  ,
  "RUNNING_TIMES" NUMBER(19)  ,
  "STATUS" NUMBER(10)  ,
  "TASK_TRACKER_ADDRESS" VARCHAR2(255 CHAR)  ,
  "TYPE" NUMBER(10)  ,
  "WF_INSTANCE_ID" NUMBER(19) 
)
;

-- ----------------------------
-- Table structure for PJ_JOB_INFO
-- ----------------------------
CREATE TABLE "PJ_JOB_INFO" (
  "ID" NUMBER(19)  NOT NULL ,
  "ALARM_CONFIG" VARCHAR2(255 CHAR)  ,
  "APP_ID" NUMBER(19)  ,
  "CONCURRENCY" NUMBER(10)  ,
  "DESIGNATED_WORKERS" VARCHAR2(255 CHAR)  ,
  "DISPATCH_STRATEGY" NUMBER(10)  ,
  "EXECUTE_TYPE" NUMBER(10)  ,
  "EXTRA" VARCHAR2(255 CHAR)  ,
  "GMT_CREATE" TIMESTAMP(6)  ,
  "GMT_MODIFIED" TIMESTAMP(6)  ,
  "INSTANCE_RETRY_NUM" NUMBER(10)  ,
  "INSTANCE_TIME_LIMIT" NUMBER(19)  ,
  "JOB_DESCRIPTION" VARCHAR2(255 CHAR)  ,
  "JOB_NAME" VARCHAR2(255 CHAR)  ,
  "JOB_PARAMS" CLOB  ,
  "LIFECYCLE" VARCHAR2(255 CHAR)  ,
  "LOG_CONFIG" VARCHAR2(255 CHAR)  ,
  "MAX_INSTANCE_NUM" NUMBER(10)  ,
  "MAX_WORKER_COUNT" NUMBER(10)  ,
  "MIN_CPU_CORES" FLOAT(126)  NOT NULL ,
  "MIN_DISK_SPACE" FLOAT(126)  NOT NULL ,
  "MIN_MEMORY_SPACE" FLOAT(126)  NOT NULL ,
  "NEXT_TRIGGER_TIME" NUMBER(19)  ,
  "NOTIFY_USER_IDS" VARCHAR2(255 CHAR)  ,
  "PROCESSOR_INFO" VARCHAR2(255 CHAR)  ,
  "PROCESSOR_TYPE" NUMBER(10)  ,
  "STATUS" NUMBER(10)  ,
  "TAG" VARCHAR2(255 CHAR)  ,
  "TASK_RETRY_NUM" NUMBER(10)  ,
  "TIME_EXPRESSION" VARCHAR2(255 CHAR)  ,
  "TIME_EXPRESSION_TYPE" NUMBER(10) 
)
;

-- ----------------------------
-- Records of "PJ_JOB_INFO"
-- ----------------------------
INSERT INTO "PJ_JOB_INFO" VALUES ('1', '{"alertThreshold":0,"silenceWindowLen":0,"statisticWindowLen":0}', '1', '5', NULL, '2', '1', NULL, NULL, NULL, '1', '0', NULL, '单机处理器执行测试', NULL, '{}', '{"type":1}', '0', '0', '0.0000000000000000', '0.0000000000000000', '0.0000000000000000', NULL, NULL, 'org.dromara.job.processors.StandaloneProcessorDemo', '1', '2', NULL, '1', '30000', '3');
INSERT INTO "PJ_JOB_INFO" VALUES ('2', '{"alertThreshold":0,"silenceWindowLen":0,"statisticWindowLen":0}', '1', '5', NULL, '1', '2', NULL, NULL, NULL, '0', '0', NULL, '广播处理器测试', NULL, '{}', '{"type":1}', '0', '0', '0.0000000000000000', '0.0000000000000000', '0.0000000000000000', NULL, NULL, 'org.dromara.job.processors.BroadcastProcessorDemo', '1', '2', NULL, '1', '30000', '3');
INSERT INTO "PJ_JOB_INFO" VALUES ('3', '{"alertThreshold":0,"silenceWindowLen":0,"statisticWindowLen":0}', '1', '5', NULL, '1', '4', NULL, NULL, NULL, '0', '0', NULL, 'Map处理器测试', NULL, '{}', '{"type":1}', '0', '0', '0.0000000000000000', '0.0000000000000000', '0.0000000000000000', NULL, NULL, 'org.dromara.job.processors.MapProcessorDemo', '1', '2', NULL, '1', '1000', '3');
INSERT INTO "PJ_JOB_INFO" VALUES ('4', '{"alertThreshold":0,"silenceWindowLen":0,"statisticWindowLen":0}', '1', '5', NULL, '1', '3', NULL, NULL, NULL, '0', '0', NULL, 'MapReduce处理器测试', NULL, '{}', '{"type":1}', '0', '0', '0.0000000000000000', '0.0000000000000000', '0.0000000000000000', NULL, NULL, 'org.dromara.job.processors.MapReduceProcessorDemo', '1', '2', NULL, '1', '1000', '3');

-- ----------------------------
-- Table structure for PJ_OMS_LOCK
-- ----------------------------
CREATE TABLE "PJ_OMS_LOCK" (
  "ID" NUMBER(19)  NOT NULL ,
  "GMT_CREATE" TIMESTAMP(6)  ,
  "GMT_MODIFIED" TIMESTAMP(6)  ,
  "LOCK_NAME" VARCHAR2(255 CHAR)  ,
  "MAX_LOCK_TIME" NUMBER(19)  ,
  "OWNERIP" VARCHAR2(255 CHAR) 
)
;

-- ----------------------------
-- Table structure for PJ_SERVER_INFO
-- ----------------------------
CREATE TABLE "PJ_SERVER_INFO" (
  "ID" NUMBER(19)  NOT NULL ,
  "GMT_CREATE" TIMESTAMP(6)  ,
  "GMT_MODIFIED" TIMESTAMP(6)  ,
  "IP" VARCHAR2(255 CHAR) 
)
;

-- ----------------------------
-- Table structure for PJ_USER_INFO
-- ----------------------------
CREATE TABLE "PJ_USER_INFO" (
  "ID" NUMBER(19)  NOT NULL ,
  "EMAIL" VARCHAR2(255 CHAR)  ,
  "EXTRA" VARCHAR2(255 CHAR)  ,
  "GMT_CREATE" TIMESTAMP(6)  ,
  "GMT_MODIFIED" TIMESTAMP(6)  ,
  "PASSWORD" VARCHAR2(255 CHAR)  ,
  "PHONE" VARCHAR2(255 CHAR)  ,
  "USERNAME" VARCHAR2(255 CHAR)  ,
  "WEB_HOOK" VARCHAR2(255 CHAR) 
)
;

-- ----------------------------
-- Table structure for PJ_WORKFLOW_INFO
-- ----------------------------
CREATE TABLE "PJ_WORKFLOW_INFO" (
  "ID" NUMBER(19)  NOT NULL ,
  "APP_ID" NUMBER(19)  ,
  "EXTRA" VARCHAR2(255 CHAR)  ,
  "GMT_CREATE" TIMESTAMP(6)  ,
  "GMT_MODIFIED" TIMESTAMP(6)  ,
  "LIFECYCLE" VARCHAR2(255 CHAR)  ,
  "MAX_WF_INSTANCE_NUM" NUMBER(10)  ,
  "NEXT_TRIGGER_TIME" NUMBER(19)  ,
  "NOTIFY_USER_IDS" VARCHAR2(255 CHAR)  ,
  "PEDAG" CLOB  ,
  "STATUS" NUMBER(10)  ,
  "TIME_EXPRESSION" VARCHAR2(255 CHAR)  ,
  "TIME_EXPRESSION_TYPE" NUMBER(10)  ,
  "WF_DESCRIPTION" VARCHAR2(255 CHAR)  ,
  "WF_NAME" VARCHAR2(255 CHAR) 
)
;

-- ----------------------------
-- Table structure for PJ_WORKFLOW_INSTANCE_INFO
-- ----------------------------
CREATE TABLE "PJ_WORKFLOW_INSTANCE_INFO" (
  "ID" NUMBER(19)  NOT NULL ,
  "ACTUAL_TRIGGER_TIME" NUMBER(19)  ,
  "APP_ID" NUMBER(19)  ,
  "DAG" CLOB  ,
  "EXPECTED_TRIGGER_TIME" NUMBER(19)  ,
  "FINISHED_TIME" NUMBER(19)  ,
  "GMT_CREATE" TIMESTAMP(6)  ,
  "GMT_MODIFIED" TIMESTAMP(6)  ,
  "PARENT_WF_INSTANCE_ID" NUMBER(19)  ,
  "RESULT" CLOB  ,
  "STATUS" NUMBER(10)  ,
  "WF_CONTEXT" CLOB  ,
  "WF_INIT_PARAMS" CLOB  ,
  "WF_INSTANCE_ID" NUMBER(19)  ,
  "WORKFLOW_ID" NUMBER(19) 
)
;

-- ----------------------------
-- Table structure for PJ_WORKFLOW_NODE_INFO
-- ----------------------------
CREATE TABLE "PJ_WORKFLOW_NODE_INFO" (
  "ID" NUMBER(19)  NOT NULL ,
  "APP_ID" NUMBER(19)  NOT NULL ,
  "ENABLE" NUMBER(1)  NOT NULL ,
  "EXTRA" CLOB  ,
  "GMT_CREATE" TIMESTAMP(6)  NOT NULL ,
  "GMT_MODIFIED" TIMESTAMP(6)  NOT NULL ,
  "JOB_ID" NUMBER(19)  ,
  "NODE_NAME" VARCHAR2(255 CHAR)  ,
  "NODE_PARAMS" CLOB  ,
  "SKIP_WHEN_FAILED" NUMBER(1)  NOT NULL ,
  "TYPE" NUMBER(10)  ,
  "WORKFLOW_ID" NUMBER(19) 
)
;

-- ----------------------------
-- Primary Key structure for table PJ_APP_INFO
-- ----------------------------
ALTER TABLE "PJ_APP_INFO" ADD CONSTRAINT "SYS_C0012204" PRIMARY KEY ("ID");

-- ----------------------------
-- Uniques structure for table PJ_APP_INFO
-- ----------------------------
ALTER TABLE "PJ_APP_INFO" ADD CONSTRAINT "UIDX01_APP_INFO" UNIQUE ("APP_NAME");

-- ----------------------------
-- Checks structure for table PJ_APP_INFO
-- ----------------------------
ALTER TABLE "PJ_APP_INFO" ADD CONSTRAINT "SYS_C0012203" CHECK ("ID" IS NOT NULL);

-- ----------------------------
-- Primary Key structure for table PJ_CONTAINER_INFO
-- ----------------------------
ALTER TABLE "PJ_CONTAINER_INFO" ADD CONSTRAINT "SYS_C0012206" PRIMARY KEY ("ID");

-- ----------------------------
-- Checks structure for table PJ_CONTAINER_INFO
-- ----------------------------
ALTER TABLE "PJ_CONTAINER_INFO" ADD CONSTRAINT "SYS_C0012205" CHECK ("ID" IS NOT NULL);

-- ----------------------------
-- Indexes structure for table PJ_CONTAINER_INFO
-- ----------------------------
CREATE INDEX "IDX01_CONTAINER_INFO"
  ON "PJ_CONTAINER_INFO" ("APP_ID" ASC);

-- ----------------------------
-- Primary Key structure for table PJ_INSTANCE_INFO
-- ----------------------------
ALTER TABLE "PJ_INSTANCE_INFO" ADD CONSTRAINT "SYS_C0012208" PRIMARY KEY ("ID");

-- ----------------------------
-- Checks structure for table PJ_INSTANCE_INFO
-- ----------------------------
ALTER TABLE "PJ_INSTANCE_INFO" ADD CONSTRAINT "SYS_C0012207" CHECK ("ID" IS NOT NULL);

-- ----------------------------
-- Indexes structure for table PJ_INSTANCE_INFO
-- ----------------------------
CREATE INDEX "IDX01_INSTANCE_INFO"
  ON "PJ_INSTANCE_INFO" ("JOB_ID" ASC, "STATUS" ASC);
CREATE INDEX "IDX02_INSTANCE_INFO"
  ON "PJ_INSTANCE_INFO" ("APP_ID" ASC, "STATUS" ASC);
CREATE INDEX "IDX03_INSTANCE_INFO"
  ON "PJ_INSTANCE_INFO" ("INSTANCE_ID" ASC, "STATUS" ASC);

-- ----------------------------
-- Primary Key structure for table PJ_JOB_INFO
-- ----------------------------
ALTER TABLE "PJ_JOB_INFO" ADD CONSTRAINT "SYS_C0012213" PRIMARY KEY ("ID");

-- ----------------------------
-- Checks structure for table PJ_JOB_INFO
-- ----------------------------
ALTER TABLE "PJ_JOB_INFO" ADD CONSTRAINT "SYS_C0012209" CHECK ("ID" IS NOT NULL);
ALTER TABLE "PJ_JOB_INFO" ADD CONSTRAINT "SYS_C0012210" CHECK ("MIN_CPU_CORES" IS NOT NULL);
ALTER TABLE "PJ_JOB_INFO" ADD CONSTRAINT "SYS_C0012211" CHECK ("MIN_DISK_SPACE" IS NOT NULL);
ALTER TABLE "PJ_JOB_INFO" ADD CONSTRAINT "SYS_C0012212" CHECK ("MIN_MEMORY_SPACE" IS NOT NULL);

-- ----------------------------
-- Indexes structure for table PJ_JOB_INFO
-- ----------------------------
CREATE INDEX "IDX01_JOB_INFO"
  ON "PJ_JOB_INFO" ("APP_ID" ASC, "NEXT_TRIGGER_TIME" ASC, "TIME_EXPRESSION_TYPE" ASC, "STATUS" ASC);

-- ----------------------------
-- Primary Key structure for table PJ_OMS_LOCK
-- ----------------------------
ALTER TABLE "PJ_OMS_LOCK" ADD CONSTRAINT "SYS_C0012215" PRIMARY KEY ("ID");

-- ----------------------------
-- Uniques structure for table PJ_OMS_LOCK
-- ----------------------------
ALTER TABLE "PJ_OMS_LOCK" ADD CONSTRAINT "UIDX01_OMS_LOCK" UNIQUE ("LOCK_NAME");

-- ----------------------------
-- Checks structure for table PJ_OMS_LOCK
-- ----------------------------
ALTER TABLE "PJ_OMS_LOCK" ADD CONSTRAINT "SYS_C0012214" CHECK ("ID" IS NOT NULL);

-- ----------------------------
-- Primary Key structure for table PJ_SERVER_INFO
-- ----------------------------
ALTER TABLE "PJ_SERVER_INFO" ADD CONSTRAINT "SYS_C0012217" PRIMARY KEY ("ID");

-- ----------------------------
-- Uniques structure for table PJ_SERVER_INFO
-- ----------------------------
ALTER TABLE "PJ_SERVER_INFO" ADD CONSTRAINT "UIDX01_SERVER_INFO" UNIQUE ("IP");

-- ----------------------------
-- Checks structure for table PJ_SERVER_INFO
-- ----------------------------
ALTER TABLE "PJ_SERVER_INFO" ADD CONSTRAINT "SYS_C0012216" CHECK ("ID" IS NOT NULL);

-- ----------------------------
-- Indexes structure for table PJ_SERVER_INFO
-- ----------------------------
CREATE INDEX "IDX01_SERVER_INFO"
  ON "PJ_SERVER_INFO" ("GMT_MODIFIED" ASC);

-- ----------------------------
-- Primary Key structure for table PJ_USER_INFO
-- ----------------------------
ALTER TABLE "PJ_USER_INFO" ADD CONSTRAINT "SYS_C0012219" PRIMARY KEY ("ID");

-- ----------------------------
-- Checks structure for table PJ_USER_INFO
-- ----------------------------
ALTER TABLE "PJ_USER_INFO" ADD CONSTRAINT "SYS_C0012218" CHECK ("ID" IS NOT NULL);

-- ----------------------------
-- Indexes structure for table PJ_USER_INFO
-- ----------------------------
CREATE INDEX "UIDX01_USER_INFO"
  ON "PJ_USER_INFO" ("USERNAME" ASC);
CREATE INDEX "UIDX02_USER_INFO"
  ON "PJ_USER_INFO" ("EMAIL" ASC);

-- ----------------------------
-- Primary Key structure for table PJ_WORKFLOW_INFO
-- ----------------------------
ALTER TABLE "PJ_WORKFLOW_INFO" ADD CONSTRAINT "SYS_C0012221" PRIMARY KEY ("ID");

-- ----------------------------
-- Checks structure for table PJ_WORKFLOW_INFO
-- ----------------------------
ALTER TABLE "PJ_WORKFLOW_INFO" ADD CONSTRAINT "SYS_C0012220" CHECK ("ID" IS NOT NULL);

-- ----------------------------
-- Indexes structure for table PJ_WORKFLOW_INFO
-- ----------------------------
CREATE INDEX "IDX01_WORKFLOW_INFO"
  ON "PJ_WORKFLOW_INFO" ("APP_ID" ASC, "NEXT_TRIGGER_TIME" ASC, "TIME_EXPRESSION_TYPE" ASC, "STATUS" ASC);

-- ----------------------------
-- Primary Key structure for table PJ_WORKFLOW_INSTANCE_INFO
-- ----------------------------
ALTER TABLE "PJ_WORKFLOW_INSTANCE_INFO" ADD CONSTRAINT "SYS_C0012223" PRIMARY KEY ("ID");

-- ----------------------------
-- Uniques structure for table PJ_WORKFLOW_INSTANCE_INFO
-- ----------------------------
ALTER TABLE "PJ_WORKFLOW_INSTANCE_INFO" ADD CONSTRAINT "UIDX01_WF_INSTANCE" UNIQUE ("WF_INSTANCE_ID");

-- ----------------------------
-- Checks structure for table PJ_WORKFLOW_INSTANCE_INFO
-- ----------------------------
ALTER TABLE "PJ_WORKFLOW_INSTANCE_INFO" ADD CONSTRAINT "SYS_C0012222" CHECK ("ID" IS NOT NULL);

-- ----------------------------
-- Indexes structure for table PJ_WORKFLOW_INSTANCE_INFO
-- ----------------------------
CREATE INDEX "IDX01_WF_INSTANCE"
  ON "PJ_WORKFLOW_INSTANCE_INFO" ("WORKFLOW_ID" ASC, "EXPECTED_TRIGGER_TIME" ASC, "APP_ID" ASC, "STATUS" ASC);

-- ----------------------------
-- Primary Key structure for table PJ_WORKFLOW_NODE_INFO
-- ----------------------------
ALTER TABLE "PJ_WORKFLOW_NODE_INFO" ADD CONSTRAINT "SYS_C0012230" PRIMARY KEY ("ID");

-- ----------------------------
-- Checks structure for table PJ_WORKFLOW_NODE_INFO
-- ----------------------------
ALTER TABLE "PJ_WORKFLOW_NODE_INFO" ADD CONSTRAINT "SYS_C0012224" CHECK ("ID" IS NOT NULL);
ALTER TABLE "PJ_WORKFLOW_NODE_INFO" ADD CONSTRAINT "SYS_C0012225" CHECK ("APP_ID" IS NOT NULL);
ALTER TABLE "PJ_WORKFLOW_NODE_INFO" ADD CONSTRAINT "SYS_C0012226" CHECK ("ENABLE" IS NOT NULL);
ALTER TABLE "PJ_WORKFLOW_NODE_INFO" ADD CONSTRAINT "SYS_C0012227" CHECK ("GMT_CREATE" IS NOT NULL);
ALTER TABLE "PJ_WORKFLOW_NODE_INFO" ADD CONSTRAINT "SYS_C0012228" CHECK ("GMT_MODIFIED" IS NOT NULL);
ALTER TABLE "PJ_WORKFLOW_NODE_INFO" ADD CONSTRAINT "SYS_C0012229" CHECK ("SKIP_WHEN_FAILED" IS NOT NULL);

-- ----------------------------
-- Indexes structure for table PJ_WORKFLOW_NODE_INFO
-- ----------------------------
CREATE INDEX "IDX01_WORKFLOW_NODE_INFO"
  ON "PJ_WORKFLOW_NODE_INFO" ("GMT_CREATE" ASC, "WORKFLOW_ID" ASC);
