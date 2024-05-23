create schema ry_config;
set schema ry_config;


/******************************************/
/*   表名称 = config_info   */
/******************************************/
CREATE TABLE config_info (
  id BIGINT NOT NULL AUTO_INCREMENT COMMENT 'id',
  data_id VARCHAR2(255) NOT NULL COMMENT 'data_id',
  group_id VARCHAR2(255) DEFAULT NULL,
  "content" text  NOT NULL COMMENT 'content',
  "md5" VARCHAR2(32) DEFAULT NULL COMMENT 'md5',
  gmt_create datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  gmt_modified datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
  src_user text COMMENT 'source user',
  src_ip VARCHAR2(50) DEFAULT NULL COMMENT 'source ip',
  app_name VARCHAR2(128) DEFAULT NULL,
  tenant_id VARCHAR2(128) DEFAULT '' COMMENT '租户字段',
  c_desc VARCHAR2(256) DEFAULT NULL,
  c_use VARCHAR2(64) DEFAULT NULL,
  effect VARCHAR2(64) DEFAULT NULL,
  "type" VARCHAR2(64) DEFAULT NULL,
  c_schema text,
  encrypted_data_key text COMMENT '秘钥',
  PRIMARY KEY (id)
) ;


CREATE UNIQUE INDEX uk_configinfo_datagrouptenant on config_info (data_id,group_id,tenant_id);


insert into config_info(id, data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, type, c_schema, encrypted_data_key) values
(1, 'application-common.yml', 'DEFAULT_GROUP', '# 将项目路径：config/下对应文件中内容复制到此处', '2944a25cb97926efcaa43b3ad7a64cf0', '2022-01-09 15:18:55', '2022-01-09 15:18:55', NULL, '0:0:0:0:0:0:0:1', '', 'dev', '通用配置基础配置', NULL, NULL, 'yaml', NULL, ''),
(2, 'datasource.yml', 'DEFAULT_GROUP', '# 将项目路径：config/下对应文件中内容复制到此处', '2944a25cb97926efcaa43b3ad7a64cf0', '2022-01-09 15:19:07', '2022-01-09 15:19:07', NULL, '0:0:0:0:0:0:0:1', '', 'dev', '数据源配置', NULL, NULL, 'yaml', NULL, ''),
(3, 'ruoyi-gateway.yml', 'DEFAULT_GROUP', '# 将项目路径：config/下对应文件中内容复制到此处', '2944a25cb97926efcaa43b3ad7a64cf0', '2022-01-09 15:19:43', '2022-01-09 15:22:42', NULL, '0:0:0:0:0:0:0:1', '', 'dev', '网关模块', NULL, NULL, 'yaml', NULL, ''),
(4, 'ruoyi-auth.yml', 'DEFAULT_GROUP', '# 将项目路径：config/下对应文件中内容复制到此处', '2944a25cb97926efcaa43b3ad7a64cf0', '2022-01-09 15:19:43', '2022-01-09 15:22:29', NULL, '0:0:0:0:0:0:0:1', '', 'dev', '认证中心', NULL, NULL, 'yaml', NULL, ''),
(5, 'ruoyi-monitor.yml', 'DEFAULT_GROUP', '# 将项目路径：config/下对应文件中内容复制到此处', '2944a25cb97926efcaa43b3ad7a64cf0', '2022-01-09 15:20:18', '2022-01-09 15:22:15', NULL, '0:0:0:0:0:0:0:1', '', 'dev', '监控中心', NULL, NULL, 'yaml', NULL, ''),
(6, 'ruoyi-system.yml', 'DEFAULT_GROUP', '# 将项目路径：config/下对应文件中内容复制到此处', '2944a25cb97926efcaa43b3ad7a64cf0', '2022-01-09 15:20:18', '2022-01-09 15:22:03', NULL, '0:0:0:0:0:0:0:1', '', 'dev', '系统模块', NULL, NULL, 'yaml', NULL, ''),
(7, 'ruoyi-gen.yml', 'DEFAULT_GROUP', '# 将项目路径：config/下对应文件中内容复制到此处', '2944a25cb97926efcaa43b3ad7a64cf0', '2022-01-09 15:20:18', '2022-01-09 15:21:51', NULL, '0:0:0:0:0:0:0:1', '', 'dev', '代码生成', NULL, NULL, 'yaml', NULL, ''),
(8, 'ruoyi-job.yml', 'DEFAULT_GROUP', '# 将项目路径：config/下对应文件中内容复制到此处', '2944a25cb97926efcaa43b3ad7a64cf0', '2022-01-09 15:20:18', '2022-01-09 15:21:36', NULL, '0:0:0:0:0:0:0:1', '', 'dev', '定时任务', NULL, NULL, 'yaml', NULL, ''),
(9, 'ruoyi-resource.yml', 'DEFAULT_GROUP', '# 将项目路径：config/下对应文件中内容复制到此处', '2944a25cb97926efcaa43b3ad7a64cf0', '2022-01-09 15:20:35', '2022-01-09 15:21:21', NULL, '0:0:0:0:0:0:0:1', '', 'dev', '文件服务', NULL, NULL, 'yaml', NULL, ''),
(10, 'sentinel-ruoyi-gateway.json', 'DEFAULT_GROUP', '# 将项目路径：config/下对应文件中内容复制到此处', '2944a25cb97926efcaa43b3ad7a64cf0', '2022-01-09 15:21:02', '2022-01-09 15:21:02', NULL, '0:0:0:0:0:0:0:1', '', 'dev', '限流策略', NULL, NULL, 'json', NULL, ''),
(11, 'ruoyi-powerjob-server.properties', 'DEFAULT_GROUP', '# 将项目路径：config/下对应文件中内容复制到此处', '2944a25cb97926efcaa43b3ad7a64cf0', '2022-01-09 15:21:02', '2022-01-09 15:21:02', NULL, '0:0:0:0:0:0:0:1', '', 'dev', '定时任务控制台', NULL, NULL, 'properties', NULL, ''),
(12, 'seata-server.properties', 'DEFAULT_GROUP', '# 将项目路径：config/下对应文件中内容复制到此处', '2944a25cb97926efcaa43b3ad7a64cf0', '2022-01-09 15:21:02', '2022-01-09 15:21:02', NULL, '0:0:0:0:0:0:0:1', '', 'dev', 'seata配置文件', NULL, NULL, 'properties', NULL, ''),
(13, 'ruoyi-sentinel-dashboard.yml', 'DEFAULT_GROUP', '# 将项目路径：config/下对应文件中内容复制到此处', '2944a25cb97926efcaa43b3ad7a64cf0', '2022-01-09 15:21:02', '2022-01-09 15:21:02', NULL, '0:0:0:0:0:0:0:1', '', 'dev', 'sentinel控制台配置文件', NULL, NULL, 'yaml', NULL, ''),

(101, 'application-common.yml', 'DEFAULT_GROUP', '# 将项目路径：config/下对应文件中内容复制到此处', '2944a25cb97926efcaa43b3ad7a64cf0', '2022-01-09 15:23:00', '2022-01-09 15:23:00', NULL, '0:0:0:0:0:0:0:1', '', 'prod', '通用配置基础配置', NULL, NULL, 'yaml', NULL, ''),
(102, 'datasource.yml', 'DEFAULT_GROUP', '# 将项目路径：config/下对应文件中内容复制到此处', '2944a25cb97926efcaa43b3ad7a64cf0', '2022-01-09 15:23:00', '2022-01-09 15:23:00', NULL, '0:0:0:0:0:0:0:1', '', 'prod', '数据源配置', NULL, NULL, 'yaml', NULL, ''),
(103, 'ruoyi-gateway.yml', 'DEFAULT_GROUP', '# 将项目路径：config/下对应文件中内容复制到此处', '2944a25cb97926efcaa43b3ad7a64cf0', '2022-01-09 15:23:00', '2022-01-09 15:23:00', NULL, '0:0:0:0:0:0:0:1', '', 'prod', '网关模块', NULL, NULL, 'yaml', NULL, ''),
(104, 'ruoyi-auth.yml', 'DEFAULT_GROUP', '# 将项目路径：config/下对应文件中内容复制到此处', '2944a25cb97926efcaa43b3ad7a64cf0', '2022-01-09 15:23:00', '2022-01-09 15:23:00', NULL, '0:0:0:0:0:0:0:1', '', 'prod', '认证中心', NULL, NULL, 'yaml', NULL, ''),
(105, 'ruoyi-monitor.yml', 'DEFAULT_GROUP', '# 将项目路径：config/下对应文件中内容复制到此处', '2944a25cb97926efcaa43b3ad7a64cf0', '2022-01-09 15:23:00', '2022-01-09 15:23:00', NULL, '0:0:0:0:0:0:0:1', '', 'prod', '监控中心', NULL, NULL, 'yaml', NULL, ''),
(106, 'ruoyi-system.yml', 'DEFAULT_GROUP', '# 将项目路径：config/下对应文件中内容复制到此处', '2944a25cb97926efcaa43b3ad7a64cf0', '2022-01-09 15:23:00', '2022-01-09 15:23:00', NULL, '0:0:0:0:0:0:0:1', '', 'prod', '系统模块', NULL, NULL, 'yaml', NULL, ''),
(107, 'ruoyi-gen.yml', 'DEFAULT_GROUP', '# 将项目路径：config/下对应文件中内容复制到此处', '2944a25cb97926efcaa43b3ad7a64cf0', '2022-01-09 15:23:00', '2022-01-09 15:23:00', NULL, '0:0:0:0:0:0:0:1', '', 'prod', '代码生成', NULL, NULL, 'yaml', NULL, ''),
(108, 'ruoyi-job.yml', 'DEFAULT_GROUP', '# 将项目路径：config/下对应文件中内容复制到此处', '2944a25cb97926efcaa43b3ad7a64cf0', '2022-01-09 15:23:00', '2022-01-09 15:23:00', NULL, '0:0:0:0:0:0:0:1', '', 'prod', '定时任务', NULL, NULL, 'yaml', NULL, ''),
(109, 'ruoyi-resource.yml', 'DEFAULT_GROUP', '# 将项目路径：config/下对应文件中内容复制到此处', '2944a25cb97926efcaa43b3ad7a64cf0', '2022-01-09 15:23:00', '2022-01-09 15:23:00', NULL, '0:0:0:0:0:0:0:1', '', 'prod', '文件服务', NULL, NULL, 'yaml', NULL, ''),
(110, 'sentinel-ruoyi-gateway.json', 'DEFAULT_GROUP', '# 将项目路径：config/下对应文件中内容复制到此处', '2944a25cb97926efcaa43b3ad7a64cf0', '2022-01-09 15:23:00', '2022-01-09 15:23:00', NULL, '0:0:0:0:0:0:0:1', '', 'prod', '限流策略', NULL, NULL, 'json', NULL, ''),
(111, 'ruoyi-powerjob-server.properties', 'DEFAULT_GROUP', '# 将项目路径：config/下对应文件中内容复制到此处', '2944a25cb97926efcaa43b3ad7a64cf0', '2022-01-09 15:21:02', '2022-01-09 15:21:02', NULL, '0:0:0:0:0:0:0:1', '', 'prod', '定时任务控制台', NULL, NULL, 'properties', NULL, ''),
(112, 'seata-server.properties', 'DEFAULT_GROUP', '# 将项目路径：config/下对应文件中内容复制到此处', '2944a25cb97926efcaa43b3ad7a64cf0', '2022-01-09 15:21:02', '2022-01-09 15:21:02', NULL, '0:0:0:0:0:0:0:1', '', 'prod', 'seata配置文件', NULL, NULL, 'properties', NULL, ''),
(113, 'ruoyi-sentinel-dashboard.yml', 'DEFAULT_GROUP', '# 将项目路径：config/下对应文件中内容复制到此处', '2944a25cb97926efcaa43b3ad7a64cf0', '2022-01-09 15:21:02', '2022-01-09 15:21:02', NULL, '0:0:0:0:0:0:0:1', '', 'prod', 'sentinel控制台配置文件', NULL, NULL, 'yaml', NULL, '');

/******************************************/
/*   表名称 = config_info_aggr   */
/******************************************/
CREATE TABLE config_info_aggr (
  id BIGINT NOT NULL AUTO_INCREMENT COMMENT 'id',
  data_id VARCHAR2(255) NOT NULL COMMENT 'data_id',
  group_id VARCHAR2(255) NOT NULL COMMENT 'group_id',
  datum_id VARCHAR2(255) NOT NULL COMMENT 'datum_id',
  "content" text  NOT NULL COMMENT '内容',
  gmt_modified datetime NOT NULL COMMENT '修改时间',
  app_name VARCHAR2(128) DEFAULT NULL,
  tenant_id VARCHAR2(128) DEFAULT '' COMMENT '租户字段',
  PRIMARY KEY (id)
) ;

CREATE UNIQUE INDEX uk_configinfoaggr_datagrouptenantdatum on config_info_aggr(data_id,group_id,tenant_id,datum_id);
/******************************************/
/*   表名称 = config_info_beta   */
/******************************************/
CREATE TABLE config_info_beta (
  id BIGINT NOT NULL AUTO_INCREMENT COMMENT 'id',
  data_id VARCHAR2(255) NOT NULL COMMENT 'data_id',
  group_id VARCHAR2(128) NOT NULL COMMENT 'group_id',
  app_name VARCHAR2(128) DEFAULT NULL COMMENT 'app_name',
  "content" text  NOT NULL COMMENT 'content',
  beta_ips VARCHAR2(1024) DEFAULT NULL COMMENT 'betaIps',
  md5 VARCHAR2(32) DEFAULT NULL COMMENT 'md5',
  gmt_create datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  gmt_modified datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
  src_user text COMMENT 'source user',
  src_ip VARCHAR2(50) DEFAULT NULL COMMENT 'source ip',
  tenant_id VARCHAR2(128) DEFAULT '' COMMENT '租户字段',
  encrypted_data_key text COMMENT '秘钥',
  PRIMARY KEY (id)
) ;


CREATE UNIQUE INDEX uk_configinfobeta_datagrouptenant on config_info_beta (data_id,group_id,tenant_id);

/******************************************/
/*   表名称 = config_info_tag   */
/******************************************/
CREATE TABLE config_info_tag (
  id BIGINT NOT NULL AUTO_INCREMENT COMMENT 'id',
  data_id VARCHAR2(255) NOT NULL COMMENT 'data_id',
  group_id VARCHAR2(128) NOT NULL COMMENT 'group_id',
  tenant_id VARCHAR2(128) DEFAULT '' COMMENT 'tenant_id',
  tag_id VARCHAR2(128) NOT NULL COMMENT 'tag_id',
  app_name VARCHAR2(128) DEFAULT NULL COMMENT 'app_name',
  "content" text  NOT NULL COMMENT 'content',
  md5 VARCHAR2(32) DEFAULT NULL COMMENT 'md5',
  gmt_create datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  gmt_modified datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
  src_user text COMMENT 'source user',
  src_ip VARCHAR2(50) DEFAULT NULL COMMENT 'source ip',
  PRIMARY KEY (id)
) ;

CREATE UNIQUE INDEX uk_configinfotag_datagrouptenanttag on config_info_tag(data_id,group_id,tenant_id,tag_id);


/******************************************/
/*   表名称 = config_tags_relation   */
/******************************************/
CREATE TABLE config_tags_relation (
  id BIGINT NOT NULL COMMENT 'id',
  tag_name VARCHAR2(128) NOT NULL COMMENT 'tag_name',
  tag_type VARCHAR2(64) DEFAULT NULL COMMENT 'tag_type',
  data_id VARCHAR2(255) NOT NULL COMMENT 'data_id',
  group_id VARCHAR2(128) NOT NULL COMMENT 'group_id',
  tenant_id VARCHAR2(128) DEFAULT '' COMMENT 'tenant_id',
  nid BIGINT NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (nid)
) ;

CREATE UNIQUE INDEX uk_configtagrelation_configidtag on config_tags_relation(id,tag_name,tag_type);


/******************************************/
/*   表名称 = group_capacity   */
/******************************************/
CREATE TABLE group_capacity (
  id BIGINT  NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  group_id VARCHAR2(128) NOT NULL DEFAULT '' COMMENT 'Group ID，空字符表示整个集群',
  quota INT  NOT NULL DEFAULT '0' COMMENT '配额，0表示使用默认值',
  usage INT  NOT NULL DEFAULT '0' COMMENT '使用量',
  max_size INT  NOT NULL DEFAULT '0' COMMENT '单个配置大小上限，单位为字节，0表示使用默认值',
  max_aggr_count INT  NOT NULL DEFAULT '0' COMMENT '聚合子配置最大个数，，0表示使用默认值',
  max_aggr_size INT  NOT NULL DEFAULT '0' COMMENT '单个聚合数据的子配置大小上限，单位为字节，0表示使用默认值',
  max_history_count INT  NOT NULL DEFAULT '0' COMMENT '最大变更历史数量',
  gmt_create datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  gmt_modified datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (id)
) ;


CREATE UNIQUE INDEX uk_group_id on group_capacity(group_id);
/******************************************/
/*   表名称 = his_config_info   */
/******************************************/
CREATE TABLE his_config_info (
  id BIGINT  NOT NULL,
  nid BIGINT  NOT NULL AUTO_INCREMENT,
  data_id VARCHAR2(255) NOT NULL,
  group_id VARCHAR2(128) NOT NULL,
  app_name VARCHAR2(128) DEFAULT NULL COMMENT 'app_name',
  "content" text  NOT NULL,
  md5 VARCHAR2(32) DEFAULT NULL,
  gmt_create datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  gmt_modified datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  src_user text,
  src_ip VARCHAR2(50) DEFAULT NULL,
  op_type char(10) DEFAULT NULL,
  tenant_id VARCHAR2(128) DEFAULT '' COMMENT '租户字段',
  encrypted_data_key text COMMENT '秘钥',
  PRIMARY KEY (nid)
) ;


CREATE UNIQUE INDEX idx_gmt_create on his_config_info (gmt_create);
CREATE UNIQUE INDEX idx_gmt_modified on his_config_info (gmt_modified);
CREATE UNIQUE INDEX idx_did on his_config_info (data_id);



/******************************************/
/*   数据库全名 = nacos_config   */
/*   表名称 = tenant_capacity   */
/******************************************/
CREATE TABLE tenant_capacity (
  id BIGINT  NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  tenant_id VARCHAR2(128) NOT NULL DEFAULT '' COMMENT 'Tenant ID',
  quota INT  NOT NULL DEFAULT '0' COMMENT '配额，0表示使用默认值',
  usage INT  NOT NULL DEFAULT '0' COMMENT '使用量',
  max_size INT  NOT NULL DEFAULT '0' COMMENT '单个配置大小上限，单位为字节，0表示使用默认值',
  max_aggr_count INT  NOT NULL DEFAULT '0' COMMENT '聚合子配置最大个数',
  max_aggr_size INT  NOT NULL DEFAULT '0' COMMENT '单个聚合数据的子配置大小上限，单位为字节，0表示使用默认值',
  max_history_count INT  NOT NULL DEFAULT '0' COMMENT '最大变更历史数量',
  gmt_create datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  gmt_modified datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (id)
) ;

CREATE UNIQUE INDEX uk_tenant_id on tenant_capacity (tenant_id);


CREATE TABLE tenant_info (
  id BIGINT NOT NULL AUTO_INCREMENT COMMENT 'id',
  kp VARCHAR2(128) NOT NULL COMMENT 'kp',
  tenant_id VARCHAR2(128) default '' COMMENT 'tenant_id',
  tenant_name VARCHAR2(128) default '' COMMENT 'tenant_name',
  tenant_desc VARCHAR2(256) DEFAULT NULL COMMENT 'tenant_desc',
  create_source VARCHAR2(32) DEFAULT NULL COMMENT 'create_source',
  gmt_create BIGINT NOT NULL COMMENT '创建时间',
  gmt_modified BIGINT NOT NULL COMMENT '修改时间',
  PRIMARY KEY (id)
) ;

CREATE UNIQUE INDEX uk_tenant_info_kptenantid on tenant_info(kp,tenant_id);

insert into tenant_info(id, kp, tenant_id, tenant_name, tenant_desc, create_source, gmt_create, gmt_modified) values
(1, '1', 'dev', 'dev', '开发环境', NULL, 1641741261189, 1641741261189),
(2, '1', 'prod', 'prod', '生产环境', NULL, 1641741270448, 1641741287236);

CREATE TABLE users (
	username VARCHAR2(50) NOT NULL PRIMARY KEY,
	password VARCHAR2(500) NOT NULL,
	enabled VARCHAR2(50) NOT NULL
);

CREATE TABLE roles (
	username VARCHAR2(50) NOT NULL,
	role VARCHAR2(50) NOT NULL
);


CREATE UNIQUE INDEX idx_user_role on roles(username ASC, role ASC);


CREATE TABLE permissions (
    role VARCHAR2(50) NOT NULL,
    resource VARCHAR2(255) NOT NULL,
    action VARCHAR2(8) NOT NULL
);

CREATE  UNIQUE INDEX uk_role_permission on permissions(role,resource,action);

INSERT INTO users (username, password, enabled) VALUES ('nacos', '$2a$10$EuWPZHzz32dJN7jexM34MOeYirDdFAZm2kuWj7VEOJhhZkDrxfvUu', TRUE);

INSERT INTO roles (username, role) VALUES ('nacos', 'ROLE_ADMIN');
