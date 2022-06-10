/*
 Navicat Premium Data Transfer

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 50737
 Source Host           : localhost:3306
 Source Schema         : ry-cloud

 Target Server Type    : MySQL
 Target Server Version : 50737
 File Encoding         : 65001

 Date: 10/06/2022 21:46:28
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for gen_table
-- ----------------------------
DROP TABLE IF EXISTS `gen_table`;
CREATE TABLE `gen_table`  (
  `table_id` bigint(20) NOT NULL COMMENT '编号',
  `table_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '表名称',
  `table_comment` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '表描述',
  `sub_table_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '关联子表的表名',
  `sub_table_fk_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '子表关联的外键名',
  `class_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '实体类名称',
  `tpl_category` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT 'crud' COMMENT '使用的模板（crud单表操作 tree树表操作）',
  `package_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '生成包路径',
  `module_name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '生成模块名',
  `business_name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '生成业务名',
  `function_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '生成功能名',
  `function_author` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '生成功能作者',
  `gen_type` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '生成代码方式（0zip压缩包 1自定义路径）',
  `gen_path` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '/' COMMENT '生成路径（不填默认项目路径）',
  `options` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '其它生成选项',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`table_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '代码生成业务表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of gen_table
-- ----------------------------
INSERT INTO `gen_table` VALUES (1535024117240549377, 'user_occupation_label', '人脉职业标签', NULL, NULL, 'UserOccupationLabel', 'crud', 'com.project.system', 'system', 'label', '人脉职业标签', 'project', '0', '/', '{\"treeCode\":null,\"treeName\":null,\"treeParentCode\":null,\"parentMenuId\":1}', 'admin', '2022-06-09 21:48:26', 'admin', '2022-06-10 07:33:57', NULL);
INSERT INTO `gen_table` VALUES (1535209716031397890, 'user_education', '学历', NULL, NULL, 'UserEducation', 'crud', 'com.project.admin', 'admin', 'education', '学历管理', 'project', '0', '/', '{\"treeCode\":null,\"treeName\":null,\"treeParentCode\":null,\"parentMenuId\":\"1534894022110633986\"}', 'admin', '2022-06-09 21:48:25', 'admin', '2022-06-10 18:58:56', NULL);
INSERT INTO `gen_table` VALUES (1535219091211001857, 'user_interested_to_me', '对我感兴趣（收藏人脉卡片）', NULL, NULL, 'UserInterestedToMe', 'crud', 'com.project.contact', 'contact', 'interested', '对我感兴趣', 'huan.li', '0', '/', '{\"treeCode\":null,\"treeName\":null,\"treeParentCode\":null,\"parentMenuId\":\"1534894022110633986\"}', 'admin', '2022-06-09 21:48:25', 'admin', '2022-06-10 21:06:08', NULL);

-- ----------------------------
-- Table structure for gen_table_column
-- ----------------------------
DROP TABLE IF EXISTS `gen_table_column`;
CREATE TABLE `gen_table_column`  (
  `column_id` bigint(20) NOT NULL COMMENT '编号',
  `table_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '归属表编号',
  `column_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '列名称',
  `column_comment` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '列描述',
  `column_type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '列类型',
  `java_type` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'JAVA类型',
  `java_field` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'JAVA字段名',
  `is_pk` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '是否主键（1是）',
  `is_increment` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '是否自增（1是）',
  `is_required` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '是否必填（1是）',
  `is_insert` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '是否为插入字段（1是）',
  `is_edit` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '是否编辑字段（1是）',
  `is_list` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '是否列表字段（1是）',
  `is_query` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '是否查询字段（1是）',
  `query_type` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT 'EQ' COMMENT '查询方式（等于、不等于、大于、小于、范围）',
  `html_type` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '显示类型（文本框、文本域、下拉框、复选框、单选框、日期控件）',
  `dict_type` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '字典类型',
  `sort` int(11) NULL DEFAULT NULL COMMENT '排序',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`column_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '代码生成业务表字段' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of gen_table_column
-- ----------------------------
INSERT INTO `gen_table_column` VALUES (1535024117991329793, '1535024117240549377', 'id', 'ID', 'bigint(20)', 'Long', 'id', '1', '1', '1', NULL, '1', '1', NULL, 'EQ', 'input', '', 1, 'admin', '2022-06-10 06:20:44', 'admin', '2022-06-10 07:33:57');
INSERT INTO `gen_table_column` VALUES (1535024118008107009, '1535024117240549377', 'user_info_id', '代表当前用户;用户信息ID', 'bigint(20)', 'Long', 'userInfoId', '0', '0', '1', '1', '1', '1', '1', 'EQ', 'input', '', 2, 'admin', '2022-06-10 06:20:44', 'admin', '2022-06-10 07:33:57');
INSERT INTO `gen_table_column` VALUES (1535024118016495617, '1535024117240549377', 'occupation_label', '职业标签', 'varchar(64)', 'String', 'occupationLabel', '0', '0', '1', '1', '1', '1', '1', 'EQ', 'input', '', 3, 'admin', '2022-06-10 06:20:44', 'admin', '2022-06-10 07:33:57');
INSERT INTO `gen_table_column` VALUES (1535024118024884225, '1535024117240549377', 'search_value', '搜索值', 'varchar(32)', 'String', 'searchValue', '0', '0', '1', '1', '1', '1', '1', 'EQ', 'input', '', 4, 'admin', '2022-06-10 06:20:44', 'admin', '2022-06-10 07:33:57');
INSERT INTO `gen_table_column` VALUES (1535024118024884226, '1535024117240549377', 'deleted', '逻辑删除;0->未删除，1->删除', 'tinyint(1)', 'Integer', 'deleted', '0', '0', '1', '1', '1', '1', '1', 'EQ', 'input', '', 5, 'admin', '2022-06-10 06:20:44', 'admin', '2022-06-10 07:33:57');
INSERT INTO `gen_table_column` VALUES (1535024118024884227, '1535024117240549377', 'create_time', '创建时间', 'timestamp', 'Date', 'createTime', '0', '0', '1', NULL, NULL, NULL, NULL, 'EQ', 'datetime', '', 6, 'admin', '2022-06-10 06:20:44', 'admin', '2022-06-10 07:33:57');
INSERT INTO `gen_table_column` VALUES (1535024118033272833, '1535024117240549377', 'create_by', '创建者', 'varchar(32)', 'String', 'createBy', '0', '0', '1', NULL, NULL, NULL, NULL, 'EQ', 'input', '', 7, 'admin', '2022-06-10 06:20:44', 'admin', '2022-06-10 07:33:57');
INSERT INTO `gen_table_column` VALUES (1535024118033272834, '1535024117240549377', 'update_time', '更新时间', 'timestamp', 'Date', 'updateTime', '0', '0', '1', NULL, NULL, NULL, NULL, 'EQ', 'datetime', '', 8, 'admin', '2022-06-10 06:20:44', 'admin', '2022-06-10 07:33:57');
INSERT INTO `gen_table_column` VALUES (1535024118033272835, '1535024117240549377', 'update_by', '更新者', 'varchar(32)', 'String', 'updateBy', '0', '0', '1', NULL, NULL, NULL, NULL, 'EQ', 'input', '', 9, 'admin', '2022-06-10 06:20:44', 'admin', '2022-06-10 07:33:57');
INSERT INTO `gen_table_column` VALUES (1535209716475994114, '1535209716031397890', 'id', '学历ID', 'bigint(20)', 'Long', 'id', '1', '1', '1', NULL, '1', '1', NULL, 'EQ', 'input', '', 1, 'admin', '2022-06-10 18:38:14', 'admin', '2022-06-10 18:58:56');
INSERT INTO `gen_table_column` VALUES (1535209716484382721, '1535209716031397890', 'user_info_id', '用户ID', 'bigint(20)', 'Long', 'userInfoId', '0', '0', '1', '1', '1', '1', NULL, 'EQ', 'input', '', 2, 'admin', '2022-06-10 18:38:14', 'admin', '2022-06-10 18:58:56');
INSERT INTO `gen_table_column` VALUES (1535209716492771329, '1535209716031397890', 'name', '学校名称', 'varchar(128)', 'String', 'name', '0', '0', '1', '1', '1', '1', '1', 'LIKE', 'input', '', 3, 'admin', '2022-06-10 18:38:14', 'admin', '2022-06-10 18:58:56');
INSERT INTO `gen_table_column` VALUES (1535209716492771330, '1535209716031397890', 'admissionTime', '入学时间', 'timestamp', 'Date', 'admissionTime', '0', '0', '1', '1', '1', '1', '1', 'EQ', 'datetime', '', 4, 'admin', '2022-06-10 18:38:14', 'admin', '2022-06-10 18:58:56');
INSERT INTO `gen_table_column` VALUES (1535209716501159938, '1535209716031397890', 'graduationTime', '毕业时间', 'timestamp', 'Date', 'graduationTime', '0', '0', '1', '1', '1', '1', '1', 'EQ', 'datetime', '', 5, 'admin', '2022-06-10 18:38:14', 'admin', '2022-06-10 18:58:56');
INSERT INTO `gen_table_column` VALUES (1535209716501159939, '1535209716031397890', 'major', '专业', 'tinyint(1)', 'Integer', 'major', '0', '0', '1', '1', '1', '1', '1', 'EQ', 'input', '', 6, 'admin', '2022-06-10 18:38:14', 'admin', '2022-06-10 18:58:56');
INSERT INTO `gen_table_column` VALUES (1535209716501159940, '1535209716031397890', 'education', '学历', 'tinyint(1)', 'Integer', 'education', '0', '0', '1', '1', '1', '1', '1', 'EQ', 'input', '', 7, 'admin', '2022-06-10 18:38:14', 'admin', '2022-06-10 18:58:56');
INSERT INTO `gen_table_column` VALUES (1535209716509548545, '1535209716031397890', 'introduction', '说明介绍', 'varchar(256)', 'String', 'introduction', '0', '0', '1', '1', '1', '1', '1', 'EQ', 'input', '', 8, 'admin', '2022-06-10 18:38:14', 'admin', '2022-06-10 18:58:56');
INSERT INTO `gen_table_column` VALUES (1535209716509548546, '1535209716031397890', 'search_value', '搜索值', 'varchar(32)', 'String', 'searchValue', '0', '0', '1', '1', '1', '1', '1', 'EQ', 'input', '', 9, 'admin', '2022-06-10 18:38:14', 'admin', '2022-06-10 18:58:56');
INSERT INTO `gen_table_column` VALUES (1535209716517937153, '1535209716031397890', 'deleted', '逻辑删除', 'tinyint(1)', 'Integer', 'deleted', '0', '0', '1', NULL, '1', '1', NULL, 'EQ', 'select', '', 10, 'admin', '2022-06-10 18:38:14', 'admin', '2022-06-10 18:58:56');
INSERT INTO `gen_table_column` VALUES (1535209716517937154, '1535209716031397890', 'create_time', '创建时间', 'timestamp', 'Date', 'createTime', '0', '0', '1', NULL, NULL, NULL, NULL, 'EQ', 'datetime', '', 11, 'admin', '2022-06-10 18:38:14', 'admin', '2022-06-10 18:58:56');
INSERT INTO `gen_table_column` VALUES (1535209716522131458, '1535209716031397890', 'create_by', '创建者', 'varchar(32)', 'String', 'createBy', '0', '0', '1', NULL, NULL, NULL, NULL, 'EQ', 'input', '', 12, 'admin', '2022-06-10 18:38:14', 'admin', '2022-06-10 18:58:56');
INSERT INTO `gen_table_column` VALUES (1535209716522131459, '1535209716031397890', 'update_time', '更新时间', 'timestamp', 'Date', 'updateTime', '0', '0', '1', NULL, NULL, NULL, NULL, 'EQ', 'datetime', '', 13, 'admin', '2022-06-10 18:38:14', 'admin', '2022-06-10 18:58:56');
INSERT INTO `gen_table_column` VALUES (1535209716530520066, '1535209716031397890', 'update_by', '更新者', 'varchar(32)', 'String', 'updateBy', '0', '0', '1', NULL, NULL, NULL, NULL, 'EQ', 'input', '', 14, 'admin', '2022-06-10 18:38:14', 'admin', '2022-06-10 18:58:56');
INSERT INTO `gen_table_column` VALUES (1535219091559129090, '1535219091211001857', 'id', 'ID', 'bigint(20)', 'Long', 'id', '1', '1', '1', NULL, '1', '1', NULL, 'EQ', 'input', '', 1, 'admin', '2022-06-10 19:15:29', 'admin', '2022-06-10 21:06:08');
INSERT INTO `gen_table_column` VALUES (1535219091559129091, '1535219091211001857', 'user_info_id', '当前用户', 'bigint(20)', 'Long', 'userInfoId', '0', '0', '1', '1', '1', '1', '1', 'EQ', 'input', '', 2, 'admin', '2022-06-10 19:15:29', 'admin', '2022-06-10 21:06:08');
INSERT INTO `gen_table_column` VALUES (1535219091559129092, '1535219091211001857', 'contact_info_id', '人脉用户', 'bigint(20)', 'Long', 'contactInfoId', '0', '0', '1', '1', '1', '1', '1', 'EQ', 'input', '', 3, 'admin', '2022-06-10 19:15:29', 'admin', '2022-06-10 21:06:08');
INSERT INTO `gen_table_column` VALUES (1535219091559129093, '1535219091211001857', 'search_value', '搜索值', 'varchar(32)', 'String', 'searchValue', '0', '0', '1', '1', '1', '1', '1', 'EQ', 'input', '', 4, 'admin', '2022-06-10 19:15:29', 'admin', '2022-06-10 21:06:08');
INSERT INTO `gen_table_column` VALUES (1535219091567517698, '1535219091211001857', 'deleted', '逻辑删除;0->未删除，1->删除', 'tinyint(1)', 'Integer', 'deleted', '0', '0', '1', NULL, '1', '1', NULL, 'EQ', 'input', '', 5, 'admin', '2022-06-10 19:15:29', 'admin', '2022-06-10 21:06:08');
INSERT INTO `gen_table_column` VALUES (1535219091567517699, '1535219091211001857', 'create_time', '创建时间', 'timestamp', 'Date', 'createTime', '0', '0', '1', NULL, NULL, NULL, NULL, 'EQ', 'datetime', '', 6, 'admin', '2022-06-10 19:15:29', 'admin', '2022-06-10 21:06:08');
INSERT INTO `gen_table_column` VALUES (1535219091567517700, '1535219091211001857', 'create_by', '创建者', 'varchar(32)', 'String', 'createBy', '0', '0', '1', NULL, NULL, NULL, NULL, 'EQ', 'input', '', 7, 'admin', '2022-06-10 19:15:29', 'admin', '2022-06-10 21:06:08');
INSERT INTO `gen_table_column` VALUES (1535219091567517701, '1535219091211001857', 'update_time', '更新时间', 'timestamp', 'Date', 'updateTime', '0', '0', '1', NULL, NULL, NULL, NULL, 'EQ', 'datetime', '', 8, 'admin', '2022-06-10 19:15:29', 'admin', '2022-06-10 21:06:08');
INSERT INTO `gen_table_column` VALUES (1535219091571712002, '1535219091211001857', 'update_by', '更新者', 'varchar(32)', 'String', 'updateBy', '0', '0', '1', NULL, NULL, NULL, NULL, 'EQ', 'input', '', 9, 'admin', '2022-06-10 19:15:29', 'admin', '2022-06-10 21:06:08');

-- ----------------------------
-- Table structure for sys_config
-- ----------------------------
DROP TABLE IF EXISTS `sys_config`;
CREATE TABLE `sys_config`  (
  `config_id` bigint(20) NOT NULL COMMENT '参数主键',
  `config_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '参数名称',
  `config_key` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '参数键名',
  `config_value` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '参数键值',
  `config_type` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT 'N' COMMENT '系统内置（Y是 N否）',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`config_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '参数配置表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_config
-- ----------------------------
INSERT INTO `sys_config` VALUES (1, '主框架页-默认皮肤样式名称', 'sys.index.skinName', 'skin-blue', 'Y', 'admin', '2022-05-31 06:31:34', '', NULL, '蓝色 skin-blue、绿色 skin-green、紫色 skin-purple、红色 skin-red、黄色 skin-yellow');
INSERT INTO `sys_config` VALUES (2, '用户管理-账号初始密码', 'sys.user.initPassword', '123456', 'Y', 'admin', '2022-05-31 06:31:34', '', NULL, '初始化密码 123456');
INSERT INTO `sys_config` VALUES (3, '主框架页-侧边栏主题', 'sys.index.sideTheme', 'theme-dark', 'Y', 'admin', '2022-05-31 06:31:34', '', NULL, '深色主题theme-dark，浅色主题theme-light');
INSERT INTO `sys_config` VALUES (4, '账号自助-是否开启用户注册功能', 'sys.account.registerUser', 'false', 'Y', 'admin', '2022-05-31 06:31:34', '', NULL, '是否开启注册用户功能（true开启，false关闭）');
INSERT INTO `sys_config` VALUES (11, 'OSS预览列表资源开关', 'sys.oss.previewListResource', 'true', 'Y', 'admin', '2022-05-31 06:31:34', '', NULL, 'true:开启, false:关闭');

-- ----------------------------
-- Table structure for sys_dept
-- ----------------------------
DROP TABLE IF EXISTS `sys_dept`;
CREATE TABLE `sys_dept`  (
  `dept_id` bigint(20) NOT NULL COMMENT '部门id',
  `parent_id` bigint(20) NULL DEFAULT 0 COMMENT '父部门id',
  `ancestors` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '祖级列表',
  `dept_name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '部门名称',
  `order_num` int(4) NULL DEFAULT 0 COMMENT '显示顺序',
  `leader` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '负责人',
  `phone` varchar(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '联系电话',
  `email` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '邮箱',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '部门状态（0正常 1停用）',
  `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '删除标志（0代表存在 2代表删除）',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`dept_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '部门表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_dept
-- ----------------------------
INSERT INTO `sys_dept` VALUES (100, 0, '0', '若依科技', 0, '若依', '15888888888', 'ry@qq.com', '0', '0', 'admin', '2022-05-31 06:31:31', '', NULL);
INSERT INTO `sys_dept` VALUES (101, 100, '0,100', '深圳总公司', 1, '若依', '15888888888', 'ry@qq.com', '0', '0', 'admin', '2022-05-31 06:31:31', '', NULL);
INSERT INTO `sys_dept` VALUES (102, 100, '0,100', '长沙分公司', 2, '若依', '15888888888', 'ry@qq.com', '0', '0', 'admin', '2022-05-31 06:31:31', '', NULL);
INSERT INTO `sys_dept` VALUES (103, 101, '0,100,101', '研发部门', 1, '若依', '15888888888', 'ry@qq.com', '0', '0', 'admin', '2022-05-31 06:31:31', '', NULL);
INSERT INTO `sys_dept` VALUES (104, 101, '0,100,101', '市场部门', 2, '若依', '15888888888', 'ry@qq.com', '0', '0', 'admin', '2022-05-31 06:31:31', '', NULL);
INSERT INTO `sys_dept` VALUES (105, 101, '0,100,101', '测试部门', 3, '若依', '15888888888', 'ry@qq.com', '0', '0', 'admin', '2022-05-31 06:31:31', '', NULL);
INSERT INTO `sys_dept` VALUES (106, 101, '0,100,101', '财务部门', 4, '若依', '15888888888', 'ry@qq.com', '0', '0', 'admin', '2022-05-31 06:31:31', '', NULL);
INSERT INTO `sys_dept` VALUES (107, 101, '0,100,101', '运维部门', 5, '若依', '15888888888', 'ry@qq.com', '0', '0', 'admin', '2022-05-31 06:31:31', '', NULL);
INSERT INTO `sys_dept` VALUES (108, 102, '0,100,102', '市场部门', 1, '若依', '15888888888', 'ry@qq.com', '0', '0', 'admin', '2022-05-31 06:31:31', '', NULL);
INSERT INTO `sys_dept` VALUES (109, 102, '0,100,102', '财务部门', 2, '若依', '15888888888', 'ry@qq.com', '0', '0', 'admin', '2022-05-31 06:31:31', '', NULL);

-- ----------------------------
-- Table structure for sys_dict_data
-- ----------------------------
DROP TABLE IF EXISTS `sys_dict_data`;
CREATE TABLE `sys_dict_data`  (
  `dict_code` bigint(20) NOT NULL COMMENT '字典编码',
  `dict_sort` int(4) NULL DEFAULT 0 COMMENT '字典排序',
  `dict_label` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '字典标签',
  `dict_value` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '字典键值',
  `dict_type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '字典类型',
  `css_class` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '样式属性（其他样式扩展）',
  `list_class` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '表格回显样式',
  `is_default` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT 'N' COMMENT '是否默认（Y是 N否）',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '状态（0正常 1停用）',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`dict_code`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '字典数据表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_dict_data
-- ----------------------------
INSERT INTO `sys_dict_data` VALUES (1, 1, '男', '0', 'sys_user_sex', '', '', 'Y', '0', 'admin', '2022-05-31 06:31:34', '', NULL, '性别男');
INSERT INTO `sys_dict_data` VALUES (2, 2, '女', '1', 'sys_user_sex', '', '', 'N', '0', 'admin', '2022-05-31 06:31:34', '', NULL, '性别女');
INSERT INTO `sys_dict_data` VALUES (3, 3, '未知', '2', 'sys_user_sex', '', '', 'N', '0', 'admin', '2022-05-31 06:31:34', '', NULL, '性别未知');
INSERT INTO `sys_dict_data` VALUES (4, 1, '显示', '0', 'sys_show_hide', '', 'primary', 'Y', '0', 'admin', '2022-05-31 06:31:34', '', NULL, '显示菜单');
INSERT INTO `sys_dict_data` VALUES (5, 2, '隐藏', '1', 'sys_show_hide', '', 'danger', 'N', '0', 'admin', '2022-05-31 06:31:34', '', NULL, '隐藏菜单');
INSERT INTO `sys_dict_data` VALUES (6, 1, '正常', '0', 'sys_normal_disable', '', 'primary', 'Y', '0', 'admin', '2022-05-31 06:31:34', '', NULL, '正常状态');
INSERT INTO `sys_dict_data` VALUES (7, 2, '停用', '1', 'sys_normal_disable', '', 'danger', 'N', '0', 'admin', '2022-05-31 06:31:34', '', NULL, '停用状态');
INSERT INTO `sys_dict_data` VALUES (8, 1, '正常', '0', 'sys_job_status', '', 'primary', 'Y', '0', 'admin', '2022-05-31 06:31:34', '', NULL, '正常状态');
INSERT INTO `sys_dict_data` VALUES (9, 2, '暂停', '1', 'sys_job_status', '', 'danger', 'N', '0', 'admin', '2022-05-31 06:31:34', '', NULL, '停用状态');
INSERT INTO `sys_dict_data` VALUES (10, 1, '默认', 'DEFAULT', 'sys_job_group', '', '', 'Y', '0', 'admin', '2022-05-31 06:31:34', '', NULL, '默认分组');
INSERT INTO `sys_dict_data` VALUES (11, 2, '系统', 'SYSTEM', 'sys_job_group', '', '', 'N', '0', 'admin', '2022-05-31 06:31:34', '', NULL, '系统分组');
INSERT INTO `sys_dict_data` VALUES (12, 1, '是', 'Y', 'sys_yes_no', '', 'primary', 'Y', '0', 'admin', '2022-05-31 06:31:34', '', NULL, '系统默认是');
INSERT INTO `sys_dict_data` VALUES (13, 2, '否', 'N', 'sys_yes_no', '', 'danger', 'N', '0', 'admin', '2022-05-31 06:31:34', '', NULL, '系统默认否');
INSERT INTO `sys_dict_data` VALUES (14, 1, '通知', '1', 'sys_notice_type', '', 'warning', 'Y', '0', 'admin', '2022-05-31 06:31:34', '', NULL, '通知');
INSERT INTO `sys_dict_data` VALUES (15, 2, '公告', '2', 'sys_notice_type', '', 'success', 'N', '0', 'admin', '2022-05-31 06:31:34', '', NULL, '公告');
INSERT INTO `sys_dict_data` VALUES (16, 1, '正常', '0', 'sys_notice_status', '', 'primary', 'Y', '0', 'admin', '2022-05-31 06:31:34', '', NULL, '正常状态');
INSERT INTO `sys_dict_data` VALUES (17, 2, '关闭', '1', 'sys_notice_status', '', 'danger', 'N', '0', 'admin', '2022-05-31 06:31:34', '', NULL, '关闭状态');
INSERT INTO `sys_dict_data` VALUES (18, 1, '新增', '1', 'sys_oper_type', '', 'info', 'N', '0', 'admin', '2022-05-31 06:31:34', '', NULL, '新增操作');
INSERT INTO `sys_dict_data` VALUES (19, 2, '修改', '2', 'sys_oper_type', '', 'info', 'N', '0', 'admin', '2022-05-31 06:31:34', '', NULL, '修改操作');
INSERT INTO `sys_dict_data` VALUES (20, 3, '删除', '3', 'sys_oper_type', '', 'danger', 'N', '0', 'admin', '2022-05-31 06:31:34', '', NULL, '删除操作');
INSERT INTO `sys_dict_data` VALUES (21, 4, '授权', '4', 'sys_oper_type', '', 'primary', 'N', '0', 'admin', '2022-05-31 06:31:34', '', NULL, '授权操作');
INSERT INTO `sys_dict_data` VALUES (22, 5, '导出', '5', 'sys_oper_type', '', 'warning', 'N', '0', 'admin', '2022-05-31 06:31:34', '', NULL, '导出操作');
INSERT INTO `sys_dict_data` VALUES (23, 6, '导入', '6', 'sys_oper_type', '', 'warning', 'N', '0', 'admin', '2022-05-31 06:31:34', '', NULL, '导入操作');
INSERT INTO `sys_dict_data` VALUES (24, 7, '强退', '7', 'sys_oper_type', '', 'danger', 'N', '0', 'admin', '2022-05-31 06:31:34', '', NULL, '强退操作');
INSERT INTO `sys_dict_data` VALUES (25, 8, '生成代码', '8', 'sys_oper_type', '', 'warning', 'N', '0', 'admin', '2022-05-31 06:31:34', '', NULL, '生成操作');
INSERT INTO `sys_dict_data` VALUES (26, 9, '清空数据', '9', 'sys_oper_type', '', 'danger', 'N', '0', 'admin', '2022-05-31 06:31:34', '', NULL, '清空操作');
INSERT INTO `sys_dict_data` VALUES (27, 1, '成功', '0', 'sys_common_status', '', 'primary', 'N', '0', 'admin', '2022-05-31 06:31:34', '', NULL, '正常状态');
INSERT INTO `sys_dict_data` VALUES (28, 2, '失败', '1', 'sys_common_status', '', 'danger', 'N', '0', 'admin', '2022-05-31 06:31:34', '', NULL, '停用状态');

-- ----------------------------
-- Table structure for sys_dict_type
-- ----------------------------
DROP TABLE IF EXISTS `sys_dict_type`;
CREATE TABLE `sys_dict_type`  (
  `dict_id` bigint(20) NOT NULL COMMENT '字典主键',
  `dict_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '字典名称',
  `dict_type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '字典类型',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '状态（0正常 1停用）',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`dict_id`) USING BTREE,
  UNIQUE INDEX `dict_type`(`dict_type`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '字典类型表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_dict_type
-- ----------------------------
INSERT INTO `sys_dict_type` VALUES (1, '用户性别', 'sys_user_sex', '0', 'admin', '2022-05-31 06:31:33', '', NULL, '用户性别列表');
INSERT INTO `sys_dict_type` VALUES (2, '菜单状态', 'sys_show_hide', '0', 'admin', '2022-05-31 06:31:33', '', NULL, '菜单状态列表');
INSERT INTO `sys_dict_type` VALUES (3, '系统开关', 'sys_normal_disable', '0', 'admin', '2022-05-31 06:31:33', '', NULL, '系统开关列表');
INSERT INTO `sys_dict_type` VALUES (4, '任务状态', 'sys_job_status', '0', 'admin', '2022-05-31 06:31:33', '', NULL, '任务状态列表');
INSERT INTO `sys_dict_type` VALUES (5, '任务分组', 'sys_job_group', '0', 'admin', '2022-05-31 06:31:33', '', NULL, '任务分组列表');
INSERT INTO `sys_dict_type` VALUES (6, '系统是否', 'sys_yes_no', '0', 'admin', '2022-05-31 06:31:33', '', NULL, '系统是否列表');
INSERT INTO `sys_dict_type` VALUES (7, '通知类型', 'sys_notice_type', '0', 'admin', '2022-05-31 06:31:33', '', NULL, '通知类型列表');
INSERT INTO `sys_dict_type` VALUES (8, '通知状态', 'sys_notice_status', '0', 'admin', '2022-05-31 06:31:33', '', NULL, '通知状态列表');
INSERT INTO `sys_dict_type` VALUES (9, '操作类型', 'sys_oper_type', '0', 'admin', '2022-05-31 06:31:33', '', NULL, '操作类型列表');
INSERT INTO `sys_dict_type` VALUES (10, '系统状态', 'sys_common_status', '0', 'admin', '2022-05-31 06:31:33', '', NULL, '登录状态列表');

-- ----------------------------
-- Table structure for sys_logininfor
-- ----------------------------
DROP TABLE IF EXISTS `sys_logininfor`;
CREATE TABLE `sys_logininfor`  (
  `info_id` bigint(20) NOT NULL COMMENT '访问ID',
  `user_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '用户账号',
  `ipaddr` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '登录IP地址',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '登录状态（0成功 1失败）',
  `msg` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '提示信息',
  `access_time` datetime(0) NULL DEFAULT NULL COMMENT '访问时间',
  PRIMARY KEY (`info_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '系统访问记录' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_logininfor
-- ----------------------------
INSERT INTO `sys_logininfor` VALUES (1532020932976447490, 'admin', '127.0.0.1', '0', '登录成功', '2022-06-01 23:27:09');
INSERT INTO `sys_logininfor` VALUES (1534536539672289281, 'admin', '127.0.0.1', '0', '登录成功', '2022-06-08 22:03:16');
INSERT INTO `sys_logininfor` VALUES (1534893362212397057, 'admin', '127.0.0.1', '0', '登录成功', '2022-06-09 21:41:09');
INSERT INTO `sys_logininfor` VALUES (1534921166526345218, 'admin', '127.0.0.1', '0', '登录成功', '2022-06-09 23:31:38');
INSERT INTO `sys_logininfor` VALUES (1534921249816834049, 'admin', '127.0.0.1', '0', '登录成功', '2022-06-09 23:31:58');
INSERT INTO `sys_logininfor` VALUES (1534922779701800961, 'admin', '127.0.0.1', '0', '登录成功', '2022-06-09 23:38:03');
INSERT INTO `sys_logininfor` VALUES (1535023739342032898, 'admin', '127.0.0.1', '0', '登录成功', '2022-06-10 06:19:13');
INSERT INTO `sys_logininfor` VALUES (1535042202555101185, 'admin', '127.0.0.1', '0', '登录成功', '2022-06-10 07:32:35');
INSERT INTO `sys_logininfor` VALUES (1535044804596350978, 'admin', '127.0.0.1', '0', '登录成功', '2022-06-10 07:42:56');
INSERT INTO `sys_logininfor` VALUES (1535045765989625857, 'admin', '127.0.0.1', '0', '登录成功', '2022-06-10 07:46:45');
INSERT INTO `sys_logininfor` VALUES (1535207151944208386, 'admin', '127.0.0.1', '0', '登录成功', '2022-06-10 18:28:02');
INSERT INTO `sys_logininfor` VALUES (1535231241715437569, 'admin', '127.0.0.1', '0', '登录成功', '2022-06-10 20:03:46');
INSERT INTO `sys_logininfor` VALUES (1535237191033679874, 'admin', '127.0.0.1', '0', '登录成功', '2022-06-10 20:27:24');
INSERT INTO `sys_logininfor` VALUES (1535248606922530817, 'admin', '127.0.0.1', '0', '登录成功', '2022-06-10 21:12:46');

-- ----------------------------
-- Table structure for sys_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu`  (
  `menu_id` bigint(20) NOT NULL COMMENT '菜单ID',
  `menu_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '菜单名称',
  `parent_id` bigint(20) NULL DEFAULT 0 COMMENT '父菜单ID',
  `order_num` int(4) NULL DEFAULT 0 COMMENT '显示顺序',
  `path` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '路由地址',
  `component` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '组件路径',
  `query_param` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '路由参数',
  `is_frame` int(1) NULL DEFAULT 1 COMMENT '是否为外链（0是 1否）',
  `is_cache` int(1) NULL DEFAULT 0 COMMENT '是否缓存（0缓存 1不缓存）',
  `menu_type` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '菜单类型（M目录 C菜单 F按钮）',
  `visible` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '菜单状态（0显示 1隐藏）',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '菜单状态（0正常 1停用）',
  `perms` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '权限标识',
  `icon` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '#' COMMENT '菜单图标',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '备注',
  PRIMARY KEY (`menu_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '菜单权限表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_menu
-- ----------------------------
INSERT INTO `sys_menu` VALUES (1, '系统管理', 0, 1, 'system', NULL, '', 1, 0, 'M', '0', '0', '', 'system', 'admin', '2022-05-31 06:31:32', 'admin', '2022-06-10 18:37:49', '系统管理目录');
INSERT INTO `sys_menu` VALUES (2, '系统监控', 0, 2, 'monitor', NULL, '', 1, 0, 'M', '0', '0', '', 'monitor', 'admin', '2022-05-31 06:31:32', '', NULL, '系统监控目录');
INSERT INTO `sys_menu` VALUES (3, '系统工具', 0, 3, 'tool', NULL, '', 1, 0, 'M', '0', '0', '', 'tool', 'admin', '2022-05-31 06:31:32', '', NULL, '系统工具目录');
INSERT INTO `sys_menu` VALUES (4, 'PLUS官网', 0, 4, 'https://gitee.com/JavaLionLi/RuoYi-Cloud-Plus', NULL, '', 0, 0, 'M', '0', '0', '', 'guide', 'admin', '2022-05-31 06:31:32', '', NULL, '若依官网地址');
INSERT INTO `sys_menu` VALUES (100, '用户管理', 1, 1, 'user', 'system/user/index', '', 1, 0, 'C', '0', '0', 'system:user:list', 'user', 'admin', '2022-05-31 06:31:32', 'admin', '2022-06-10 19:02:19', '用户管理菜单');
INSERT INTO `sys_menu` VALUES (101, '角色管理', 1, 2, 'role', 'system/role/index', '', 1, 0, 'C', '0', '0', 'system:role:list', 'peoples', 'admin', '2022-05-31 06:31:32', '', NULL, '角色管理菜单');
INSERT INTO `sys_menu` VALUES (102, '菜单管理', 1, 3, 'menu', 'system/menu/index', '', 1, 0, 'C', '0', '0', 'system:menu:list', 'tree-table', 'admin', '2022-05-31 06:31:32', '', NULL, '菜单管理菜单');
INSERT INTO `sys_menu` VALUES (103, '部门管理', 1, 4, 'dept', 'system/dept/index', '', 1, 0, 'C', '0', '0', 'system:dept:list', 'tree', 'admin', '2022-05-31 06:31:32', '', NULL, '部门管理菜单');
INSERT INTO `sys_menu` VALUES (104, '岗位管理', 1, 5, 'post', 'system/post/index', '', 1, 0, 'C', '0', '0', 'system:post:list', 'post', 'admin', '2022-05-31 06:31:32', '', NULL, '岗位管理菜单');
INSERT INTO `sys_menu` VALUES (105, '字典管理', 1, 6, 'dict', 'system/dict/index', '', 1, 0, 'C', '0', '0', 'system:dict:list', 'dict', 'admin', '2022-05-31 06:31:32', '', NULL, '字典管理菜单');
INSERT INTO `sys_menu` VALUES (106, '参数设置', 1, 7, 'config', 'system/config/index', '', 1, 0, 'C', '0', '0', 'system:config:list', 'edit', 'admin', '2022-05-31 06:31:32', '', NULL, '参数设置菜单');
INSERT INTO `sys_menu` VALUES (107, '通知公告', 1, 8, 'notice', 'system/notice/index', '', 1, 0, 'C', '0', '0', 'system:notice:list', 'message', 'admin', '2022-05-31 06:31:32', '', NULL, '通知公告菜单');
INSERT INTO `sys_menu` VALUES (108, '日志管理', 1, 9, 'log', '', '', 1, 0, 'M', '0', '0', '', 'log', 'admin', '2022-05-31 06:31:32', '', NULL, '日志管理菜单');
INSERT INTO `sys_menu` VALUES (109, '在线用户', 2, 1, 'online', 'monitor/online/index', '', 1, 0, 'C', '0', '0', 'monitor:online:list', 'online', 'admin', '2022-05-31 06:31:32', '', NULL, '在线用户菜单');
INSERT INTO `sys_menu` VALUES (110, 'XxlJob控制台', 2, 2, 'http://localhost:9900', '', '', 0, 0, 'C', '0', '0', 'monitor:job:list', 'job', 'admin', '2022-05-31 06:31:32', '', NULL, '定时任务菜单');
INSERT INTO `sys_menu` VALUES (111, 'Sentinel控制台', 2, 3, 'http://localhost:8718', '', '', 0, 0, 'C', '0', '0', 'monitor:sentinel:list', 'sentinel', 'admin', '2022-05-31 06:31:32', '', NULL, '流量控制菜单');
INSERT INTO `sys_menu` VALUES (112, 'Nacos控制台', 2, 4, 'http://localhost:8848/nacos', '', '', 0, 0, 'C', '0', '0', 'monitor:nacos:list', 'nacos', 'admin', '2022-05-31 06:31:32', '', NULL, '服务治理菜单');
INSERT INTO `sys_menu` VALUES (113, 'Admin控制台', 2, 5, 'http://localhost:9100/login', '', '', 0, 0, 'C', '0', '0', 'monitor:server:list', 'server', 'admin', '2022-05-31 06:31:32', '', NULL, '服务监控菜单');
INSERT INTO `sys_menu` VALUES (114, '表单构建', 3, 1, 'build', 'tool/build/index', '', 1, 0, 'C', '0', '0', 'tool:build:list', 'build', 'admin', '2022-05-31 06:31:32', '', NULL, '表单构建菜单');
INSERT INTO `sys_menu` VALUES (115, '代码生成', 3, 2, 'gen', 'tool/gen/index', '', 1, 0, 'C', '0', '0', 'tool:gen:list', 'code', 'admin', '2022-05-31 06:31:32', '', NULL, '代码生成菜单');
INSERT INTO `sys_menu` VALUES (116, '系统接口', 3, 3, 'http://localhost:18000/doc.html', '', '', 0, 0, 'C', '0', '0', 'tool:swagger:list', 'swagger', 'admin', '2022-05-31 06:31:32', '', NULL, '系统接口菜单');
INSERT INTO `sys_menu` VALUES (118, '文件管理', 1, 10, 'oss', 'system/oss/index', '', 1, 0, 'C', '0', '0', 'system:oss:list', 'upload', 'admin', '2022-05-31 06:31:32', '', NULL, '文件管理菜单');
INSERT INTO `sys_menu` VALUES (500, '操作日志', 108, 1, 'operlog', 'system/operlog/index', '', 1, 0, 'C', '0', '0', 'system:operlog:list', 'form', 'admin', '2022-05-31 06:31:32', '', NULL, '操作日志菜单');
INSERT INTO `sys_menu` VALUES (501, '登录日志', 108, 2, 'logininfor', 'system/logininfor/index', '', 1, 0, 'C', '0', '0', 'system:logininfor:list', 'logininfor', 'admin', '2022-05-31 06:31:32', '', NULL, '登录日志菜单');
INSERT INTO `sys_menu` VALUES (1001, '用户查询', 100, 1, '', '', '', 1, 0, 'F', '0', '0', 'system:user:query', '#', 'admin', '2022-05-31 06:31:32', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1002, '用户新增', 100, 2, '', '', '', 1, 0, 'F', '0', '0', 'system:user:add', '#', 'admin', '2022-05-31 06:31:32', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1003, '用户修改', 100, 3, '', '', '', 1, 0, 'F', '0', '0', 'system:user:edit', '#', 'admin', '2022-05-31 06:31:32', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1004, '用户删除', 100, 4, '', '', '', 1, 0, 'F', '0', '0', 'system:user:remove', '#', 'admin', '2022-05-31 06:31:32', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1005, '用户导出', 100, 5, '', '', '', 1, 0, 'F', '0', '0', 'system:user:export', '#', 'admin', '2022-05-31 06:31:32', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1006, '用户导入', 100, 6, '', '', '', 1, 0, 'F', '0', '0', 'system:user:import', '#', 'admin', '2022-05-31 06:31:32', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1007, '重置密码', 100, 7, '', '', '', 1, 0, 'F', '0', '0', 'system:user:resetPwd', '#', 'admin', '2022-05-31 06:31:32', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1008, '角色查询', 101, 1, '', '', '', 1, 0, 'F', '0', '0', 'system:role:query', '#', 'admin', '2022-05-31 06:31:32', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1009, '角色新增', 101, 2, '', '', '', 1, 0, 'F', '0', '0', 'system:role:add', '#', 'admin', '2022-05-31 06:31:32', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1010, '角色修改', 101, 3, '', '', '', 1, 0, 'F', '0', '0', 'system:role:edit', '#', 'admin', '2022-05-31 06:31:32', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1011, '角色删除', 101, 4, '', '', '', 1, 0, 'F', '0', '0', 'system:role:remove', '#', 'admin', '2022-05-31 06:31:32', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1012, '角色导出', 101, 5, '', '', '', 1, 0, 'F', '0', '0', 'system:role:export', '#', 'admin', '2022-05-31 06:31:32', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1013, '菜单查询', 102, 1, '', '', '', 1, 0, 'F', '0', '0', 'system:menu:query', '#', 'admin', '2022-05-31 06:31:32', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1014, '菜单新增', 102, 2, '', '', '', 1, 0, 'F', '0', '0', 'system:menu:add', '#', 'admin', '2022-05-31 06:31:32', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1015, '菜单修改', 102, 3, '', '', '', 1, 0, 'F', '0', '0', 'system:menu:edit', '#', 'admin', '2022-05-31 06:31:32', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1016, '菜单删除', 102, 4, '', '', '', 1, 0, 'F', '0', '0', 'system:menu:remove', '#', 'admin', '2022-05-31 06:31:32', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1017, '部门查询', 103, 1, '', '', '', 1, 0, 'F', '0', '0', 'system:dept:query', '#', 'admin', '2022-05-31 06:31:32', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1018, '部门新增', 103, 2, '', '', '', 1, 0, 'F', '0', '0', 'system:dept:add', '#', 'admin', '2022-05-31 06:31:32', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1019, '部门修改', 103, 3, '', '', '', 1, 0, 'F', '0', '0', 'system:dept:edit', '#', 'admin', '2022-05-31 06:31:32', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1020, '部门删除', 103, 4, '', '', '', 1, 0, 'F', '0', '0', 'system:dept:remove', '#', 'admin', '2022-05-31 06:31:32', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1021, '岗位查询', 104, 1, '', '', '', 1, 0, 'F', '0', '0', 'system:post:query', '#', 'admin', '2022-05-31 06:31:32', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1022, '岗位新增', 104, 2, '', '', '', 1, 0, 'F', '0', '0', 'system:post:add', '#', 'admin', '2022-05-31 06:31:32', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1023, '岗位修改', 104, 3, '', '', '', 1, 0, 'F', '0', '0', 'system:post:edit', '#', 'admin', '2022-05-31 06:31:32', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1024, '岗位删除', 104, 4, '', '', '', 1, 0, 'F', '0', '0', 'system:post:remove', '#', 'admin', '2022-05-31 06:31:32', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1025, '岗位导出', 104, 5, '', '', '', 1, 0, 'F', '0', '0', 'system:post:export', '#', 'admin', '2022-05-31 06:31:32', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1026, '字典查询', 105, 1, '#', '', '', 1, 0, 'F', '0', '0', 'system:dict:query', '#', 'admin', '2022-05-31 06:31:32', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1027, '字典新增', 105, 2, '#', '', '', 1, 0, 'F', '0', '0', 'system:dict:add', '#', 'admin', '2022-05-31 06:31:32', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1028, '字典修改', 105, 3, '#', '', '', 1, 0, 'F', '0', '0', 'system:dict:edit', '#', 'admin', '2022-05-31 06:31:32', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1029, '字典删除', 105, 4, '#', '', '', 1, 0, 'F', '0', '0', 'system:dict:remove', '#', 'admin', '2022-05-31 06:31:32', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1030, '字典导出', 105, 5, '#', '', '', 1, 0, 'F', '0', '0', 'system:dict:export', '#', 'admin', '2022-05-31 06:31:32', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1031, '参数查询', 106, 1, '#', '', '', 1, 0, 'F', '0', '0', 'system:config:query', '#', 'admin', '2022-05-31 06:31:32', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1032, '参数新增', 106, 2, '#', '', '', 1, 0, 'F', '0', '0', 'system:config:add', '#', 'admin', '2022-05-31 06:31:32', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1033, '参数修改', 106, 3, '#', '', '', 1, 0, 'F', '0', '0', 'system:config:edit', '#', 'admin', '2022-05-31 06:31:32', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1034, '参数删除', 106, 4, '#', '', '', 1, 0, 'F', '0', '0', 'system:config:remove', '#', 'admin', '2022-05-31 06:31:32', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1035, '参数导出', 106, 5, '#', '', '', 1, 0, 'F', '0', '0', 'system:config:export', '#', 'admin', '2022-05-31 06:31:32', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1036, '公告查询', 107, 1, '#', '', '', 1, 0, 'F', '0', '0', 'system:notice:query', '#', 'admin', '2022-05-31 06:31:32', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1037, '公告新增', 107, 2, '#', '', '', 1, 0, 'F', '0', '0', 'system:notice:add', '#', 'admin', '2022-05-31 06:31:32', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1038, '公告修改', 107, 3, '#', '', '', 1, 0, 'F', '0', '0', 'system:notice:edit', '#', 'admin', '2022-05-31 06:31:32', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1039, '公告删除', 107, 4, '#', '', '', 1, 0, 'F', '0', '0', 'system:notice:remove', '#', 'admin', '2022-05-31 06:31:32', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1040, '操作查询', 500, 1, '#', '', '', 1, 0, 'F', '0', '0', 'system:operlog:query', '#', 'admin', '2022-05-31 06:31:32', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1041, '操作删除', 500, 2, '#', '', '', 1, 0, 'F', '0', '0', 'system:operlog:remove', '#', 'admin', '2022-05-31 06:31:32', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1042, '日志导出', 500, 4, '#', '', '', 1, 0, 'F', '0', '0', 'system:operlog:export', '#', 'admin', '2022-05-31 06:31:32', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1043, '登录查询', 501, 1, '#', '', '', 1, 0, 'F', '0', '0', 'system:logininfor:query', '#', 'admin', '2022-05-31 06:31:32', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1044, '登录删除', 501, 2, '#', '', '', 1, 0, 'F', '0', '0', 'system:logininfor:remove', '#', 'admin', '2022-05-31 06:31:32', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1045, '日志导出', 501, 3, '#', '', '', 1, 0, 'F', '0', '0', 'system:logininfor:export', '#', 'admin', '2022-05-31 06:31:32', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1046, '在线查询', 109, 1, '#', '', '', 1, 0, 'F', '0', '0', 'monitor:online:query', '#', 'admin', '2022-05-31 06:31:32', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1047, '批量强退', 109, 2, '#', '', '', 1, 0, 'F', '0', '0', 'monitor:online:batchLogout', '#', 'admin', '2022-05-31 06:31:32', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1048, '单条强退', 109, 3, '#', '', '', 1, 0, 'F', '0', '0', 'monitor:online:forceLogout', '#', 'admin', '2022-05-31 06:31:32', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1055, '生成查询', 115, 1, '#', '', '', 1, 0, 'F', '0', '0', 'tool:gen:query', '#', 'admin', '2022-05-31 06:31:32', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1056, '生成修改', 115, 2, '#', '', '', 1, 0, 'F', '0', '0', 'tool:gen:edit', '#', 'admin', '2022-05-31 06:31:32', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1057, '生成删除', 115, 3, '#', '', '', 1, 0, 'F', '0', '0', 'tool:gen:remove', '#', 'admin', '2022-05-31 06:31:32', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1058, '导入代码', 115, 2, '#', '', '', 1, 0, 'F', '0', '0', 'tool:gen:import', '#', 'admin', '2022-05-31 06:31:32', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1059, '预览代码', 115, 4, '#', '', '', 1, 0, 'F', '0', '0', 'tool:gen:preview', '#', 'admin', '2022-05-31 06:31:32', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1060, '生成代码', 115, 5, '#', '', '', 1, 0, 'F', '0', '0', 'tool:gen:code', '#', 'admin', '2022-05-31 06:31:32', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1600, '文件查询', 118, 1, '#', '', '', 1, 0, 'F', '0', '0', 'system:oss:query', '#', 'admin', '2022-05-31 06:31:32', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1601, '文件上传', 118, 2, '#', '', '', 1, 0, 'F', '0', '0', 'system:oss:upload', '#', 'admin', '2022-05-31 06:31:32', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1602, '文件下载', 118, 3, '#', '', '', 1, 0, 'F', '0', '0', 'system:oss:download', '#', 'admin', '2022-05-31 06:31:32', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1603, '文件删除', 118, 4, '#', '', '', 1, 0, 'F', '0', '0', 'system:oss:remove', '#', 'admin', '2022-05-31 06:31:32', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1604, '配置添加', 118, 5, '#', '', '', 1, 0, 'F', '0', '0', 'system:oss:add', '#', 'admin', '2022-05-31 06:31:32', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1605, '配置编辑', 118, 6, '#', '', '', 1, 0, 'F', '0', '0', 'system:oss:edit', '#', 'admin', '2022-05-31 06:31:32', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1534894022110633986, '人脉管理', 0, 0, 'contact', NULL, NULL, 1, 0, 'M', '0', '0', NULL, 'build', 'admin', '2022-06-09 21:43:46', 'admin', '2022-06-10 21:04:09', '');
INSERT INTO `sys_menu` VALUES (1535042592084262912, '人脉职业标签', 1, 1, 'label', 'system/label/index', NULL, 1, 0, 'C', '0', '0', 'system:label:list', 'chart', 'admin', '2022-06-10 07:34:36', 'admin', '2022-06-10 19:00:55', '人脉职业标签菜单');
INSERT INTO `sys_menu` VALUES (1535042592084262913, '人脉职业标签查询', 1535042592084262912, 1, '#', '', NULL, 1, 0, 'F', '0', '0', 'system:label:query', '#', 'admin', '2022-06-10 07:34:37', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1535042592084262914, '人脉职业标签新增', 1535042592084262912, 2, '#', '', NULL, 1, 0, 'F', '0', '0', 'system:label:add', '#', 'admin', '2022-06-10 07:34:37', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1535042592084262915, '人脉职业标签修改', 1535042592084262912, 3, '#', '', NULL, 1, 0, 'F', '0', '0', 'system:label:edit', '#', 'admin', '2022-06-10 07:34:37', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1535042592084262916, '人脉职业标签删除', 1535042592084262912, 4, '#', '', NULL, 1, 0, 'F', '0', '0', 'system:label:remove', '#', 'admin', '2022-06-10 07:34:37', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1535042592084262917, '人脉职业标签导出', 1535042592084262912, 5, '#', '', NULL, 1, 0, 'F', '0', '0', 'system:label:export', '#', 'admin', '2022-06-10 07:34:37', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1535246948960182272, '对我感兴趣', 1534894022110633986, 1, 'interested', 'contact/interested/index', NULL, 1, 0, 'C', '0', '0', 'contact:interested:list', 'cascader', 'admin', '2022-06-10 21:09:53', 'admin', '2022-06-10 21:11:22', '对我感兴趣菜单');
INSERT INTO `sys_menu` VALUES (1535246948960182273, '对我感兴趣查询', 1535246948960182272, 1, '#', '', NULL, 1, 0, 'F', '0', '0', 'contact:interested:query', '#', 'admin', '2022-06-10 21:09:53', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1535246948960182274, '对我感兴趣新增', 1535246948960182272, 2, '#', '', NULL, 1, 0, 'F', '0', '0', 'contact:interested:add', '#', 'admin', '2022-06-10 21:09:53', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1535246948960182275, '对我感兴趣修改', 1535246948960182272, 3, '#', '', NULL, 1, 0, 'F', '0', '0', 'contact:interested:edit', '#', 'admin', '2022-06-10 21:09:53', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1535246948960182276, '对我感兴趣删除', 1535246948960182272, 4, '#', '', NULL, 1, 0, 'F', '0', '0', 'contact:interested:remove', '#', 'admin', '2022-06-10 21:09:53', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1535246948960182277, '对我感兴趣导出', 1535246948960182272, 5, '#', '', NULL, 1, 0, 'F', '0', '0', 'contact:interested:export', '#', 'admin', '2022-06-10 21:09:53', '', NULL, '');

-- ----------------------------
-- Table structure for sys_notice
-- ----------------------------
DROP TABLE IF EXISTS `sys_notice`;
CREATE TABLE `sys_notice`  (
  `notice_id` bigint(20) NOT NULL COMMENT '公告ID',
  `notice_title` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '公告标题',
  `notice_type` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '公告类型（1通知 2公告）',
  `notice_content` longblob NULL COMMENT '公告内容',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '公告状态（0正常 1关闭）',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`notice_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '通知公告表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_notice
-- ----------------------------
INSERT INTO `sys_notice` VALUES (1, '温馨提醒：2018-07-01 若依新版本发布啦', '2', 0xE696B0E78988E69CACE58685E5AEB9, '0', 'admin', '2022-05-31 06:31:34', '', NULL, '管理员');
INSERT INTO `sys_notice` VALUES (2, '维护通知：2018-07-01 若依系统凌晨维护', '1', 0xE7BBB4E68AA4E58685E5AEB9, '0', 'admin', '2022-05-31 06:31:34', '', NULL, '管理员');

-- ----------------------------
-- Table structure for sys_oper_log
-- ----------------------------
DROP TABLE IF EXISTS `sys_oper_log`;
CREATE TABLE `sys_oper_log`  (
  `oper_id` bigint(20) NOT NULL COMMENT '日志主键',
  `title` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '模块标题',
  `business_type` int(2) NULL DEFAULT 0 COMMENT '业务类型（0其它 1新增 2修改 3删除）',
  `method` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '方法名称',
  `request_method` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '请求方式',
  `operator_type` int(1) NULL DEFAULT 0 COMMENT '操作类别（0其它 1后台用户 2手机端用户）',
  `oper_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '操作人员',
  `dept_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '部门名称',
  `oper_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '请求URL',
  `oper_ip` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '主机地址',
  `oper_location` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '操作地点',
  `oper_param` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '请求参数',
  `json_result` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '返回参数',
  `status` int(1) NULL DEFAULT 0 COMMENT '操作状态（0正常 1异常）',
  `error_msg` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '错误消息',
  `oper_time` datetime(0) NULL DEFAULT NULL COMMENT '操作时间',
  PRIMARY KEY (`oper_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '操作日志记录' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_oper_log
-- ----------------------------
INSERT INTO `sys_oper_log` VALUES (1534536673093099521, '代码生成', 6, 'com.project.gen.controller.GenController.importTableSave()', 'POST', 1, 'admin', '', '/gen/importTable', '127.0.0.1', '', '\"user_info,user_education,user_contact_relation_status,user_contact_relation,user_communication_message,user_interested_to_me,user_occupation_label,user_work_experience\"', '{\"code\":200,\"msg\":\"操作成功\",\"data\":null}', 0, '', '2022-06-08 22:03:48');
INSERT INTO `sys_oper_log` VALUES (1534537075637231618, '代码生成', 2, 'com.project.gen.controller.GenController.editSave()', 'PUT', 1, 'admin', '', '/gen', '127.0.0.1', '', '{\"searchValue\":null,\"createBy\":null,\"createTime\":null,\"updateBy\":\"admin\",\"updateTime\":\"2022-06-08 22:05:23\",\"params\":{\"treeCode\":null,\"treeName\":null,\"treeParentCode\":null,\"parentMenuId\":3},\"tableId\":\"1534536666604609538\",\"tableName\":\"user_communication_message\",\"tableComment\":\"沟通消息\",\"subTableName\":null,\"subTableFkName\":null,\"className\":\"UserCommunicationMessage\",\"tplCategory\":\"crud\",\"packageName\":\"com.project.admin.contact\",\"moduleName\":\"contact\",\"businessName\":\"message\",\"functionName\":\"沟通消息\",\"functionAuthor\":\"project\",\"genType\":\"0\",\"genPath\":\"/\",\"pkColumn\":null,\"subTable\":null,\"columns\":[{\"searchValue\":null,\"createBy\":\"admin\",\"createTime\":\"2022-06-08 22:03:46\",\"updateBy\":\"admin\",\"updateTime\":\"2022-06-08 22:05:23\",\"params\":{},\"columnId\":\"1534536667053400065\",\"tableId\":\"1534536666604609538\",\"columnName\":\"id\",\"columnComment\":\"ID\",\"columnType\":\"bigint(20)\",\"javaType\":\"Long\",\"javaField\":\"id\",\"isPk\":\"1\",\"isIncrement\":\"1\",\"isRequired\":\"1\",\"isInsert\":null,\"isEdit\":\"1\",\"isList\":\"1\",\"isQuery\":null,\"queryType\":\"EQ\",\"htmlType\":\"input\",\"dictType\":\"\",\"sort\":1,\"list\":true,\"edit\":true,\"superColumn\":false,\"insert\":false,\"pk\":true,\"usableColumn\":false,\"increment\":true,\"query\":false,\"capJavaField\":\"id\",\"required\":true},{\"searchValue\":null,\"createBy\":\"admin\",\"createTime\":\"2022-06-08 22:03:46\",\"updateBy\":\"admin\",\"updateTime\":\"2022-06-08 22:05:23\",\"params\":{},\"columnId\":\"1534536667057594369\",\"tableId\":\"1534536666604609538\",\"columnName\":\"user_info_id\",\"columnComment\":\"代表当前用户;用户信息ID\",\"columnType\":\"bigint(20)\",\"javaType\":\"Long\",\"javaField\":\"userInfoId\",\"isPk\":\"0\",\"isIncrement\":\"0\",\"isRequired\":\"1\",\"isInsert\":\"1\",\"isEdit\":\"1\",\"isList\":\"1\",\"isQuery\":\"1\",\"queryType\":\"EQ\",\"htmlType\":\"input\",\"dictType\":\"\",\"sort\":2,\"list\":true,\"edit\":true,\"superColumn\":false,\"insert\":true,\"pk\":false,\"usableColumn\":false,\"increment\":false,\"query\":true,\"capJavaField\":\"userInfoId\",\"required\":true},{\"searchValue\":null,\"createBy\":\"admin\",\"createTime\":\"2022-06-08 22:03:46\",\"updateBy\":\"admin\",\"updateTime\":\"2022-06-08 ', '{\"code\":200,\"msg\":\"操作成功\",\"data\":null}', 0, '', '2022-06-08 22:05:24');
INSERT INTO `sys_oper_log` VALUES (1534537258596966402, '代码生成', 2, 'com.project.gen.controller.GenController.editSave()', 'PUT', 1, 'admin', '', '/gen', '127.0.0.1', '', '{\"searchValue\":null,\"createBy\":null,\"createTime\":null,\"updateBy\":\"admin\",\"updateTime\":\"2022-06-08 22:06:07\",\"params\":{\"treeCode\":null,\"treeName\":null,\"treeParentCode\":null,\"parentMenuId\":3},\"tableId\":\"1534536667862900737\",\"tableName\":\"user_contact_relation\",\"tableComment\":\"好友关系\",\"subTableName\":null,\"subTableFkName\":null,\"className\":\"UserContactRelation\",\"tplCategory\":\"crud\",\"packageName\":\"com.project.admin.contact\",\"moduleName\":\"contact\",\"businessName\":\"relation\",\"functionName\":\"好友关系\",\"functionAuthor\":\"project\",\"genType\":\"0\",\"genPath\":\"/\",\"pkColumn\":null,\"subTable\":null,\"columns\":[{\"searchValue\":null,\"createBy\":\"admin\",\"createTime\":\"2022-06-08 22:03:47\",\"updateBy\":\"admin\",\"updateTime\":\"2022-06-08 22:06:07\",\"params\":{},\"columnId\":\"1534536667980341249\",\"tableId\":\"1534536667862900737\",\"columnName\":\"id\",\"columnComment\":\"好友关系ID\",\"columnType\":\"bigint(20)\",\"javaType\":\"Long\",\"javaField\":\"id\",\"isPk\":\"1\",\"isIncrement\":\"1\",\"isRequired\":\"1\",\"isInsert\":null,\"isEdit\":\"1\",\"isList\":\"1\",\"isQuery\":null,\"queryType\":\"EQ\",\"htmlType\":\"input\",\"dictType\":\"\",\"sort\":1,\"list\":true,\"edit\":true,\"superColumn\":false,\"insert\":false,\"pk\":true,\"usableColumn\":false,\"increment\":true,\"query\":false,\"capJavaField\":\"id\",\"required\":true},{\"searchValue\":null,\"createBy\":\"admin\",\"createTime\":\"2022-06-08 22:03:47\",\"updateBy\":\"admin\",\"updateTime\":\"2022-06-08 22:06:07\",\"params\":{},\"columnId\":\"1534536667992924162\",\"tableId\":\"1534536667862900737\",\"columnName\":\"user_info_id\",\"columnComment\":\"代表当前用户;用户信息ID\",\"columnType\":\"bigint(20)\",\"javaType\":\"Long\",\"javaField\":\"userInfoId\",\"isPk\":\"0\",\"isIncrement\":\"0\",\"isRequired\":\"1\",\"isInsert\":\"1\",\"isEdit\":\"1\",\"isList\":\"1\",\"isQuery\":\"1\",\"queryType\":\"EQ\",\"htmlType\":\"input\",\"dictType\":\"\",\"sort\":2,\"list\":true,\"edit\":true,\"superColumn\":false,\"insert\":true,\"pk\":false,\"usableColumn\":false,\"increment\":false,\"query\":true,\"capJavaField\":\"userInfoId\",\"required\":true},{\"searchValue\":null,\"createBy\":\"admin\",\"createTime\":\"2022-06-08 22:03:47\",\"updateBy\":\"admin\",\"updateTime\":\"2022-06-08 22:06', '{\"code\":200,\"msg\":\"操作成功\",\"data\":null}', 0, '', '2022-06-08 22:06:07');
INSERT INTO `sys_oper_log` VALUES (1534537380210810882, '代码生成', 2, 'com.project.gen.controller.GenController.editSave()', 'PUT', 1, 'admin', '', '/gen', '127.0.0.1', '', '{\"searchValue\":null,\"createBy\":null,\"createTime\":null,\"updateBy\":\"admin\",\"updateTime\":\"2022-06-08 22:06:36\",\"params\":{\"treeCode\":null,\"treeName\":null,\"treeParentCode\":null,\"parentMenuId\":3},\"tableId\":\"1534536668471074818\",\"tableName\":\"user_contact_relation_status\",\"tableComment\":\"好友关系状态\",\"subTableName\":null,\"subTableFkName\":null,\"className\":\"UserContactRelationStatus\",\"tplCategory\":\"crud\",\"packageName\":\"com.project.admin.contact\",\"moduleName\":\"contact\",\"businessName\":\"status\",\"functionName\":\"好友关系状态\",\"functionAuthor\":\"project\",\"genType\":\"0\",\"genPath\":\"/\",\"pkColumn\":null,\"subTable\":null,\"columns\":[{\"searchValue\":null,\"createBy\":\"admin\",\"createTime\":\"2022-06-08 22:03:47\",\"updateBy\":\"admin\",\"updateTime\":\"2022-06-08 22:06:36\",\"params\":{},\"columnId\":\"1534536668672401409\",\"tableId\":\"1534536668471074818\",\"columnName\":\"id\",\"columnComment\":\"好友状态ID\",\"columnType\":\"bigint(20)\",\"javaType\":\"Long\",\"javaField\":\"id\",\"isPk\":\"1\",\"isIncrement\":\"1\",\"isRequired\":\"1\",\"isInsert\":null,\"isEdit\":\"1\",\"isList\":\"1\",\"isQuery\":null,\"queryType\":\"EQ\",\"htmlType\":\"input\",\"dictType\":\"\",\"sort\":1,\"list\":true,\"edit\":true,\"superColumn\":false,\"insert\":false,\"pk\":true,\"usableColumn\":false,\"increment\":true,\"query\":false,\"capJavaField\":\"id\",\"required\":true},{\"searchValue\":null,\"createBy\":\"admin\",\"createTime\":\"2022-06-08 22:03:47\",\"updateBy\":\"admin\",\"updateTime\":\"2022-06-08 22:06:36\",\"params\":{},\"columnId\":\"1534536668672401410\",\"tableId\":\"1534536668471074818\",\"columnName\":\"user_info_id\",\"columnComment\":\"代表当前用户;用户信息ID\",\"columnType\":\"bigint(20)\",\"javaType\":\"Long\",\"javaField\":\"userInfoId\",\"isPk\":\"0\",\"isIncrement\":\"0\",\"isRequired\":\"1\",\"isInsert\":\"1\",\"isEdit\":\"1\",\"isList\":\"1\",\"isQuery\":\"1\",\"queryType\":\"EQ\",\"htmlType\":\"input\",\"dictType\":\"\",\"sort\":2,\"list\":true,\"edit\":true,\"superColumn\":false,\"insert\":true,\"pk\":false,\"usableColumn\":false,\"increment\":false,\"query\":true,\"capJavaField\":\"userInfoId\",\"required\":true},{\"searchValue\":null,\"createBy\":\"admin\",\"createTime\":\"2022-06-08 22:03:47\",\"updateBy\":\"admin\",\"updateTime\":\"2', '{\"code\":200,\"msg\":\"操作成功\",\"data\":null}', 0, '', '2022-06-08 22:06:36');
INSERT INTO `sys_oper_log` VALUES (1534537458669461506, '代码生成', 2, 'com.project.gen.controller.GenController.editSave()', 'PUT', 1, 'admin', '', '/gen', '127.0.0.1', '', '{\"searchValue\":null,\"createBy\":null,\"createTime\":null,\"updateBy\":\"admin\",\"updateTime\":\"2022-06-08 22:06:54\",\"params\":{\"treeCode\":null,\"treeName\":null,\"treeParentCode\":null,\"parentMenuId\":3},\"tableId\":\"1534536669217660929\",\"tableName\":\"user_education\",\"tableComment\":\"学历\",\"subTableName\":null,\"subTableFkName\":null,\"className\":\"UserEducation\",\"tplCategory\":\"crud\",\"packageName\":\"com.project.admin.contact\",\"moduleName\":\"contact\",\"businessName\":\"education\",\"functionName\":\"学历\",\"functionAuthor\":\"project\",\"genType\":\"0\",\"genPath\":\"/\",\"pkColumn\":null,\"subTable\":null,\"columns\":[{\"searchValue\":null,\"createBy\":\"admin\",\"createTime\":\"2022-06-08 22:03:47\",\"updateBy\":\"admin\",\"updateTime\":\"2022-06-08 22:06:54\",\"params\":{},\"columnId\":\"1534536669339295746\",\"tableId\":\"1534536669217660929\",\"columnName\":\"id\",\"columnComment\":\"学历ID\",\"columnType\":\"bigint(20)\",\"javaType\":\"Long\",\"javaField\":\"id\",\"isPk\":\"1\",\"isIncrement\":\"1\",\"isRequired\":\"1\",\"isInsert\":null,\"isEdit\":\"1\",\"isList\":\"1\",\"isQuery\":null,\"queryType\":\"EQ\",\"htmlType\":\"input\",\"dictType\":\"\",\"sort\":1,\"list\":true,\"edit\":true,\"superColumn\":false,\"insert\":false,\"pk\":true,\"usableColumn\":false,\"increment\":true,\"query\":false,\"capJavaField\":\"id\",\"required\":true},{\"searchValue\":null,\"createBy\":\"admin\",\"createTime\":\"2022-06-08 22:03:47\",\"updateBy\":\"admin\",\"updateTime\":\"2022-06-08 22:06:54\",\"params\":{},\"columnId\":\"1534536669339295747\",\"tableId\":\"1534536669217660929\",\"columnName\":\"user_info_id\",\"columnComment\":\"代表当前用户;用户信息ID\",\"columnType\":\"bigint(20)\",\"javaType\":\"Long\",\"javaField\":\"userInfoId\",\"isPk\":\"0\",\"isIncrement\":\"0\",\"isRequired\":\"1\",\"isInsert\":\"1\",\"isEdit\":\"1\",\"isList\":\"1\",\"isQuery\":\"1\",\"queryType\":\"EQ\",\"htmlType\":\"input\",\"dictType\":\"\",\"sort\":2,\"list\":true,\"edit\":true,\"superColumn\":false,\"insert\":true,\"pk\":false,\"usableColumn\":false,\"increment\":false,\"query\":true,\"capJavaField\":\"userInfoId\",\"required\":true},{\"searchValue\":null,\"createBy\":\"admin\",\"createTime\":\"2022-06-08 22:03:47\",\"updateBy\":\"admin\",\"updateTime\":\"2022-06-08 22:06:54\",\"params\":{},\"', '{\"code\":200,\"msg\":\"操作成功\",\"data\":null}', 0, '', '2022-06-08 22:06:55');
INSERT INTO `sys_oper_log` VALUES (1534537620397629441, '代码生成', 2, 'com.project.gen.controller.GenController.editSave()', 'PUT', 1, 'admin', '', '/gen', '127.0.0.1', '', '{\"searchValue\":null,\"createBy\":null,\"createTime\":null,\"updateBy\":\"admin\",\"updateTime\":\"2022-06-08 22:07:33\",\"params\":{\"treeCode\":null,\"treeName\":null,\"treeParentCode\":null,\"parentMenuId\":3},\"tableId\":\"1534536669871972354\",\"tableName\":\"user_info\",\"tableComment\":\"用户信息\",\"subTableName\":null,\"subTableFkName\":null,\"className\":\"UserInfo\",\"tplCategory\":\"crud\",\"packageName\":\"com.project.admin.contact\",\"moduleName\":\"contact\",\"businessName\":\"info\",\"functionName\":\"用户信息\",\"functionAuthor\":\"project\",\"genType\":\"0\",\"genPath\":\"/\",\"pkColumn\":null,\"subTable\":null,\"columns\":[{\"searchValue\":null,\"createBy\":\"admin\",\"createTime\":\"2022-06-08 22:03:47\",\"updateBy\":\"admin\",\"updateTime\":\"2022-06-08 22:07:33\",\"params\":{},\"columnId\":\"1534536670085881858\",\"tableId\":\"1534536669871972354\",\"columnName\":\"id\",\"columnComment\":\"用户信息ID\",\"columnType\":\"bigint(20)\",\"javaType\":\"Long\",\"javaField\":\"id\",\"isPk\":\"1\",\"isIncrement\":\"1\",\"isRequired\":\"1\",\"isInsert\":null,\"isEdit\":\"1\",\"isList\":\"1\",\"isQuery\":null,\"queryType\":\"EQ\",\"htmlType\":\"input\",\"dictType\":\"\",\"sort\":1,\"list\":true,\"edit\":true,\"superColumn\":false,\"insert\":false,\"pk\":true,\"usableColumn\":false,\"increment\":true,\"query\":false,\"capJavaField\":\"id\",\"required\":true},{\"searchValue\":null,\"createBy\":\"admin\",\"createTime\":\"2022-06-08 22:03:47\",\"updateBy\":\"admin\",\"updateTime\":\"2022-06-08 22:07:33\",\"params\":{},\"columnId\":\"1534536670094270466\",\"tableId\":\"1534536669871972354\",\"columnName\":\"name\",\"columnComment\":\"姓名\",\"columnType\":\"varchar(64)\",\"javaType\":\"String\",\"javaField\":\"name\",\"isPk\":\"0\",\"isIncrement\":\"0\",\"isRequired\":\"1\",\"isInsert\":\"1\",\"isEdit\":\"1\",\"isList\":\"1\",\"isQuery\":\"1\",\"queryType\":\"LIKE\",\"htmlType\":\"input\",\"dictType\":\"\",\"sort\":2,\"list\":true,\"edit\":true,\"superColumn\":false,\"insert\":true,\"pk\":false,\"usableColumn\":false,\"increment\":false,\"query\":true,\"capJavaField\":\"name\",\"required\":true},{\"searchValue\":null,\"createBy\":\"admin\",\"createTime\":\"2022-06-08 22:03:47\",\"updateBy\":\"admin\",\"updateTime\":\"2022-06-08 22:07:33\",\"params\":{},\"columnId\":\"1534536670094270467\",\"ta', '{\"code\":200,\"msg\":\"操作成功\",\"data\":null}', 0, '', '2022-06-08 22:07:34');
INSERT INTO `sys_oper_log` VALUES (1534537729709580290, '代码生成', 2, 'com.project.gen.controller.GenController.editSave()', 'PUT', 1, 'admin', '', '/gen', '127.0.0.1', '', '{\"searchValue\":null,\"createBy\":null,\"createTime\":null,\"updateBy\":\"admin\",\"updateTime\":\"2022-06-08 22:07:59\",\"params\":{\"treeCode\":null,\"treeName\":null,\"treeParentCode\":null,\"parentMenuId\":3},\"tableId\":\"1534536670907965441\",\"tableName\":\"user_interested_to_me\",\"tableComment\":\"对我感兴趣（收藏人脉卡片）\",\"subTableName\":null,\"subTableFkName\":null,\"className\":\"UserInterestedToMe\",\"tplCategory\":\"crud\",\"packageName\":\"com.project.admin.contact\",\"moduleName\":\"contact\",\"businessName\":\"me\",\"functionName\":\"对我感兴趣（收藏人脉卡片）\",\"functionAuthor\":\"project\",\"genType\":\"0\",\"genPath\":\"/\",\"pkColumn\":null,\"subTable\":null,\"columns\":[{\"searchValue\":null,\"createBy\":\"admin\",\"createTime\":\"2022-06-08 22:03:47\",\"updateBy\":\"admin\",\"updateTime\":\"2022-06-08 22:07:59\",\"params\":{},\"columnId\":\"1534536671025405953\",\"tableId\":\"1534536670907965441\",\"columnName\":\"id\",\"columnComment\":\"对我感兴趣ID\",\"columnType\":\"bigint(20)\",\"javaType\":\"Long\",\"javaField\":\"id\",\"isPk\":\"1\",\"isIncrement\":\"1\",\"isRequired\":\"1\",\"isInsert\":null,\"isEdit\":\"1\",\"isList\":\"1\",\"isQuery\":null,\"queryType\":\"EQ\",\"htmlType\":\"input\",\"dictType\":\"\",\"sort\":1,\"list\":true,\"edit\":true,\"superColumn\":false,\"insert\":false,\"pk\":true,\"usableColumn\":false,\"increment\":true,\"query\":false,\"capJavaField\":\"id\",\"required\":true},{\"searchValue\":null,\"createBy\":\"admin\",\"createTime\":\"2022-06-08 22:03:47\",\"updateBy\":\"admin\",\"updateTime\":\"2022-06-08 22:07:59\",\"params\":{},\"columnId\":\"1534536671029600257\",\"tableId\":\"1534536670907965441\",\"columnName\":\"user_info_id\",\"columnComment\":\"代表当前用户;用户信息ID\",\"columnType\":\"bigint(20)\",\"javaType\":\"Long\",\"javaField\":\"userInfoId\",\"isPk\":\"0\",\"isIncrement\":\"0\",\"isRequired\":\"1\",\"isInsert\":\"1\",\"isEdit\":\"1\",\"isList\":\"1\",\"isQuery\":\"1\",\"queryType\":\"EQ\",\"htmlType\":\"input\",\"dictType\":\"\",\"sort\":2,\"list\":true,\"edit\":true,\"superColumn\":false,\"insert\":true,\"pk\":false,\"usableColumn\":false,\"increment\":false,\"query\":true,\"capJavaField\":\"userInfoId\",\"required\":true},{\"searchValue\":null,\"createBy\":\"admin\",\"createTime\":\"2022-06-08 22:03:47\",\"updateBy\":\"admin\",\"updateTime\":\"2022', '{\"code\":200,\"msg\":\"操作成功\",\"data\":null}', 0, '', '2022-06-08 22:08:00');
INSERT INTO `sys_oper_log` VALUES (1534537828741292033, '代码生成', 2, 'com.project.gen.controller.GenController.editSave()', 'PUT', 1, 'admin', '', '/gen', '127.0.0.1', '', '{\"searchValue\":null,\"createBy\":null,\"createTime\":null,\"updateBy\":\"admin\",\"updateTime\":\"2022-06-08 22:08:23\",\"params\":{\"treeCode\":null,\"treeName\":null,\"treeParentCode\":null,\"parentMenuId\":3},\"tableId\":\"1534536671461613569\",\"tableName\":\"user_occupation_label\",\"tableComment\":\"人脉职业标签\",\"subTableName\":null,\"subTableFkName\":null,\"className\":\"UserOccupationLabel\",\"tplCategory\":\"crud\",\"packageName\":\"com.project.admin.contact\",\"moduleName\":\"contact\",\"businessName\":\"label\",\"functionName\":\"人脉职业标签\",\"functionAuthor\":\"project\",\"genType\":\"0\",\"genPath\":\"/\",\"pkColumn\":null,\"subTable\":null,\"columns\":[{\"searchValue\":null,\"createBy\":\"admin\",\"createTime\":\"2022-06-08 22:03:47\",\"updateBy\":\"admin\",\"updateTime\":\"2022-06-08 22:08:23\",\"params\":{},\"columnId\":\"1534536671679717377\",\"tableId\":\"1534536671461613569\",\"columnName\":\"id\",\"columnComment\":\"ID\",\"columnType\":\"bigint(20)\",\"javaType\":\"Long\",\"javaField\":\"id\",\"isPk\":\"1\",\"isIncrement\":\"1\",\"isRequired\":\"1\",\"isInsert\":null,\"isEdit\":\"1\",\"isList\":\"1\",\"isQuery\":null,\"queryType\":\"EQ\",\"htmlType\":\"input\",\"dictType\":\"\",\"sort\":1,\"list\":true,\"edit\":true,\"superColumn\":false,\"insert\":false,\"pk\":true,\"usableColumn\":false,\"increment\":true,\"query\":false,\"capJavaField\":\"id\",\"required\":true},{\"searchValue\":null,\"createBy\":\"admin\",\"createTime\":\"2022-06-08 22:03:47\",\"updateBy\":\"admin\",\"updateTime\":\"2022-06-08 22:08:23\",\"params\":{},\"columnId\":\"1534536671679717378\",\"tableId\":\"1534536671461613569\",\"columnName\":\"user_info_id\",\"columnComment\":\"代表当前用户;用户信息ID\",\"columnType\":\"bigint(20)\",\"javaType\":\"Long\",\"javaField\":\"userInfoId\",\"isPk\":\"0\",\"isIncrement\":\"0\",\"isRequired\":\"1\",\"isInsert\":\"1\",\"isEdit\":\"1\",\"isList\":\"1\",\"isQuery\":\"1\",\"queryType\":\"EQ\",\"htmlType\":\"input\",\"dictType\":\"\",\"sort\":2,\"list\":true,\"edit\":true,\"superColumn\":false,\"insert\":true,\"pk\":false,\"usableColumn\":false,\"increment\":false,\"query\":true,\"capJavaField\":\"userInfoId\",\"required\":true},{\"searchValue\":null,\"createBy\":\"admin\",\"createTime\":\"2022-06-08 22:03:48\",\"updateBy\":\"admin\",\"updateTime\":\"2022-06-08 22:08:23', '{\"code\":200,\"msg\":\"操作成功\",\"data\":null}', 0, '', '2022-06-08 22:08:23');
INSERT INTO `sys_oper_log` VALUES (1534537882264805378, '代码生成', 2, 'com.project.gen.controller.GenController.editSave()', 'PUT', 1, 'admin', '', '/gen', '127.0.0.1', '', '{\"searchValue\":null,\"createBy\":null,\"createTime\":null,\"updateBy\":\"admin\",\"updateTime\":\"2022-06-08 22:08:35\",\"params\":{\"treeCode\":null,\"treeName\":null,\"treeParentCode\":null,\"parentMenuId\":3},\"tableId\":\"1534536672061399041\",\"tableName\":\"user_work_experience\",\"tableComment\":\"工作经历\",\"subTableName\":null,\"subTableFkName\":null,\"className\":\"UserWorkExperience\",\"tplCategory\":\"crud\",\"packageName\":\"com.project.admin.contact\",\"moduleName\":\"contact\",\"businessName\":\"experience\",\"functionName\":\"工作经历\",\"functionAuthor\":\"project\",\"genType\":\"0\",\"genPath\":\"/\",\"pkColumn\":null,\"subTable\":null,\"columns\":[{\"searchValue\":null,\"createBy\":\"admin\",\"createTime\":\"2022-06-08 22:03:48\",\"updateBy\":\"admin\",\"updateTime\":\"2022-06-08 22:08:35\",\"params\":{},\"columnId\":\"1534536672166256642\",\"tableId\":\"1534536672061399041\",\"columnName\":\"id\",\"columnComment\":\"工作经历ID\",\"columnType\":\"bigint(20)\",\"javaType\":\"Long\",\"javaField\":\"id\",\"isPk\":\"1\",\"isIncrement\":\"1\",\"isRequired\":\"1\",\"isInsert\":null,\"isEdit\":\"1\",\"isList\":\"1\",\"isQuery\":null,\"queryType\":\"EQ\",\"htmlType\":\"input\",\"dictType\":\"\",\"sort\":1,\"list\":true,\"edit\":true,\"superColumn\":false,\"insert\":false,\"pk\":true,\"usableColumn\":false,\"increment\":true,\"query\":false,\"capJavaField\":\"id\",\"required\":true},{\"searchValue\":null,\"createBy\":\"admin\",\"createTime\":\"2022-06-08 22:03:48\",\"updateBy\":\"admin\",\"updateTime\":\"2022-06-08 22:08:35\",\"params\":{},\"columnId\":\"1534536672174645249\",\"tableId\":\"1534536672061399041\",\"columnName\":\"user_info_id\",\"columnComment\":\"代表当前用户;用户信息ID\",\"columnType\":\"bigint(20)\",\"javaType\":\"Long\",\"javaField\":\"userInfoId\",\"isPk\":\"0\",\"isIncrement\":\"0\",\"isRequired\":\"1\",\"isInsert\":\"1\",\"isEdit\":\"1\",\"isList\":\"1\",\"isQuery\":\"1\",\"queryType\":\"EQ\",\"htmlType\":\"input\",\"dictType\":\"\",\"sort\":2,\"list\":true,\"edit\":true,\"superColumn\":false,\"insert\":true,\"pk\":false,\"usableColumn\":false,\"increment\":false,\"query\":true,\"capJavaField\":\"userInfoId\",\"required\":true},{\"searchValue\":null,\"createBy\":\"admin\",\"createTime\":\"2022-06-08 22:03:48\",\"updateBy\":\"admin\",\"updateTime\":\"2022-06-08 22:08', '{\"code\":200,\"msg\":\"操作成功\",\"data\":null}', 0, '', '2022-06-08 22:08:36');
INSERT INTO `sys_oper_log` VALUES (1534537963130986498, '代码生成', 8, 'com.project.gen.controller.GenController.batchGenCode()', 'GET', 1, 'admin', '', '/gen/batchGenCode', '127.0.0.1', '', '', '', 0, '', '2022-06-08 22:08:55');
INSERT INTO `sys_oper_log` VALUES (1534894022479732737, '菜单管理', 1, 'com.project.system.controller.SysMenuController.add()', 'POST', 1, 'admin', '', '/menu', '127.0.0.1', '', '{\"searchValue\":null,\"createBy\":\"admin\",\"createTime\":\"2022-06-09 21:43:46\",\"updateBy\":\"admin\",\"updateTime\":\"2022-06-09 21:43:46\",\"params\":{},\"parentName\":null,\"parentId\":0,\"children\":[],\"menuId\":\"1534894022110633986\",\"menuName\":\"你好同学\",\"orderNum\":2,\"path\":\"classmate\",\"component\":null,\"queryParam\":null,\"isFrame\":\"1\",\"isCache\":\"0\",\"menuType\":\"M\",\"visible\":\"0\",\"status\":\"0\",\"icon\":\"build\",\"remark\":null}', '{\"code\":200,\"msg\":\"操作成功\",\"data\":null}', 0, '', '2022-06-09 21:43:47');
INSERT INTO `sys_oper_log` VALUES (1534894383688998913, '菜单管理', 2, 'com.project.system.controller.SysMenuController.edit()', 'PUT', 1, 'admin', '', '/menu', '127.0.0.1', '', '{\"searchValue\":null,\"createBy\":\"admin\",\"createTime\":\"2022-06-09 21:43:46\",\"updateBy\":\"admin\",\"updateTime\":\"2022-06-09 21:45:12\",\"params\":{},\"parentName\":null,\"parentId\":0,\"children\":[],\"menuId\":\"1534894022110633986\",\"menuName\":\"你好同学\",\"orderNum\":1,\"path\":\"classmate\",\"component\":null,\"queryParam\":null,\"isFrame\":\"1\",\"isCache\":\"0\",\"menuType\":\"M\",\"visible\":\"0\",\"status\":\"0\",\"icon\":\"build\",\"remark\":\"\"}', '{\"code\":200,\"msg\":\"操作成功\",\"data\":null}', 0, '', '2022-06-09 21:45:13');
INSERT INTO `sys_oper_log` VALUES (1534894502798843906, '菜单管理', 2, 'com.project.system.controller.SysMenuController.edit()', 'PUT', 1, 'admin', '', '/menu', '127.0.0.1', '', '{\"searchValue\":null,\"createBy\":\"admin\",\"createTime\":\"2022-06-09 21:43:46\",\"updateBy\":\"admin\",\"updateTime\":\"2022-06-09 21:45:41\",\"params\":{},\"parentName\":null,\"parentId\":0,\"children\":[],\"menuId\":\"1534894022110633986\",\"menuName\":\"你好同学\",\"orderNum\":0,\"path\":\"classmate\",\"component\":null,\"queryParam\":null,\"isFrame\":\"1\",\"isCache\":\"0\",\"menuType\":\"M\",\"visible\":\"0\",\"status\":\"0\",\"icon\":\"build\",\"remark\":\"\"}', '{\"code\":200,\"msg\":\"操作成功\",\"data\":null}', 0, '', '2022-06-09 21:45:41');
INSERT INTO `sys_oper_log` VALUES (1534894907171692546, '菜单管理', 1, 'com.project.system.controller.SysMenuController.add()', 'POST', 1, 'admin', '', '/menu', '127.0.0.1', '', '{\"searchValue\":null,\"createBy\":\"admin\",\"createTime\":\"2022-06-09 21:47:17\",\"updateBy\":\"admin\",\"updateTime\":\"2022-06-09 21:47:17\",\"params\":{},\"parentName\":null,\"parentId\":\"1534894022110633986\",\"children\":[],\"menuId\":\"1534894906991337474\",\"menuName\":\"人脉\",\"orderNum\":1,\"path\":\"contact\",\"component\":null,\"queryParam\":null,\"isFrame\":\"1\",\"isCache\":\"0\",\"menuType\":\"C\",\"visible\":\"0\",\"status\":\"0\",\"icon\":\"button\",\"remark\":null}', '{\"code\":200,\"msg\":\"操作成功\",\"data\":null}', 0, '', '2022-06-09 21:47:17');
INSERT INTO `sys_oper_log` VALUES (1534895107307102210, '代码生成', 3, 'com.project.gen.controller.GenController.remove()', 'DELETE', 1, 'admin', '', '/gen/1534536666604609538,1534536667862900737,1534536668471074818,1534536669217660929,1534536669871972354,1534536670907965441,1534536671461613569,1534536672061399041', '127.0.0.1', '', '', '{\"code\":200,\"msg\":\"操作成功\",\"data\":null}', 0, '', '2022-06-09 21:48:05');
INSERT INTO `sys_oper_log` VALUES (1534895382717685762, '代码生成', 6, 'com.project.gen.controller.GenController.importTableSave()', 'POST', 1, 'admin', '', '/gen/importTable', '127.0.0.1', '', '\"user_work_experience,user_occupation_label,user_interested_to_me,user_info,user_contact_relation_status,user_education,user_contact_relation,user_communication_message\"', '{\"code\":200,\"msg\":\"操作成功\",\"data\":null}', 0, '', '2022-06-09 21:49:11');
INSERT INTO `sys_oper_log` VALUES (1534895703217037314, '代码生成', 2, 'com.project.gen.controller.GenController.editSave()', 'PUT', 1, 'admin', '', '/gen', '127.0.0.1', '', '{\"searchValue\":null,\"createBy\":null,\"createTime\":null,\"updateBy\":\"admin\",\"updateTime\":\"2022-06-09 21:50:26\",\"params\":{\"treeCode\":null,\"treeName\":null,\"treeParentCode\":null,\"parentMenuId\":\"1534894906991337474\"},\"tableId\":\"1534895378175340545\",\"tableName\":\"user_communication_message\",\"tableComment\":\"沟通消息\",\"subTableName\":null,\"subTableFkName\":null,\"className\":\"UserCommunicationMessage\",\"tplCategory\":\"crud\",\"packageName\":\"com.project.admin.contact\",\"moduleName\":\"admin\",\"businessName\":\"message\",\"functionName\":\"沟通消息\",\"functionAuthor\":\"huan.li\",\"genType\":\"0\",\"genPath\":\"/\",\"pkColumn\":null,\"subTable\":null,\"columns\":[{\"searchValue\":null,\"createBy\":\"admin\",\"createTime\":\"2022-06-09 21:49:10\",\"updateBy\":\"admin\",\"updateTime\":\"2022-06-09 21:50:27\",\"params\":{},\"columnId\":\"1534895378397638658\",\"tableId\":\"1534895378175340545\",\"columnName\":\"id\",\"columnComment\":\"ID\",\"columnType\":\"bigint(20)\",\"javaType\":\"Long\",\"javaField\":\"id\",\"isPk\":\"1\",\"isIncrement\":\"1\",\"isRequired\":\"1\",\"isInsert\":null,\"isEdit\":\"1\",\"isList\":\"1\",\"isQuery\":null,\"queryType\":\"EQ\",\"htmlType\":\"input\",\"dictType\":\"\",\"sort\":1,\"required\":true,\"list\":true,\"edit\":true,\"usableColumn\":false,\"superColumn\":false,\"insert\":false,\"pk\":true,\"increment\":true,\"capJavaField\":\"id\",\"query\":false},{\"searchValue\":null,\"createBy\":\"admin\",\"createTime\":\"2022-06-09 21:49:10\",\"updateBy\":\"admin\",\"updateTime\":\"2022-06-09 21:50:27\",\"params\":{},\"columnId\":\"1534895378406027266\",\"tableId\":\"1534895378175340545\",\"columnName\":\"send_message_id\",\"columnComment\":\"发送消息用户;用户信息ID\",\"columnType\":\"bigint(20)\",\"javaType\":\"Long\",\"javaField\":\"sendMessageId\",\"isPk\":\"0\",\"isIncrement\":\"0\",\"isRequired\":\"1\",\"isInsert\":\"1\",\"isEdit\":\"1\",\"isList\":\"1\",\"isQuery\":\"1\",\"queryType\":\"EQ\",\"htmlType\":\"input\",\"dictType\":\"\",\"sort\":2,\"required\":true,\"list\":true,\"edit\":true,\"usableColumn\":false,\"superColumn\":false,\"insert\":true,\"pk\":false,\"increment\":false,\"capJavaField\":\"sendMessageId\",\"query\":true},{\"searchValue\":null,\"createBy\":\"admin\",\"createTime\":\"2022-06-09 21:49:10\",\"updateBy\":\"admin', '{\"code\":200,\"msg\":\"操作成功\",\"data\":null}', 0, '', '2022-06-09 21:50:27');
INSERT INTO `sys_oper_log` VALUES (1534895853821911041, '代码生成', 2, 'com.project.gen.controller.GenController.editSave()', 'PUT', 1, 'admin', '', '/gen', '127.0.0.1', '', '{\"searchValue\":null,\"createBy\":null,\"createTime\":null,\"updateBy\":\"admin\",\"updateTime\":\"2022-06-09 21:51:02\",\"params\":{\"treeCode\":null,\"treeName\":null,\"treeParentCode\":null,\"parentMenuId\":\"1534894906991337474\"},\"tableId\":\"1534895379093893121\",\"tableName\":\"user_contact_relation\",\"tableComment\":\"好友关系\",\"subTableName\":null,\"subTableFkName\":null,\"className\":\"UserContactRelation\",\"tplCategory\":\"crud\",\"packageName\":\"com.project.admin.contact\",\"moduleName\":\"admin\",\"businessName\":\"relation\",\"functionName\":\"好友关系\",\"functionAuthor\":\"huan.li\",\"genType\":\"0\",\"genPath\":\"/\",\"pkColumn\":null,\"subTable\":null,\"columns\":[{\"searchValue\":null,\"createBy\":\"admin\",\"createTime\":\"2022-06-09 21:49:10\",\"updateBy\":\"admin\",\"updateTime\":\"2022-06-09 21:51:02\",\"params\":{},\"columnId\":\"1534895379366522882\",\"tableId\":\"1534895379093893121\",\"columnName\":\"id\",\"columnComment\":\"好友关系ID\",\"columnType\":\"bigint(20)\",\"javaType\":\"Long\",\"javaField\":\"id\",\"isPk\":\"1\",\"isIncrement\":\"1\",\"isRequired\":\"1\",\"isInsert\":null,\"isEdit\":\"1\",\"isList\":\"1\",\"isQuery\":null,\"queryType\":\"EQ\",\"htmlType\":\"input\",\"dictType\":\"\",\"sort\":1,\"required\":true,\"list\":true,\"edit\":true,\"usableColumn\":false,\"superColumn\":false,\"insert\":false,\"pk\":true,\"increment\":true,\"capJavaField\":\"id\",\"query\":false},{\"searchValue\":null,\"createBy\":\"admin\",\"createTime\":\"2022-06-09 21:49:10\",\"updateBy\":\"admin\",\"updateTime\":\"2022-06-09 21:51:02\",\"params\":{},\"columnId\":\"1534895379366522883\",\"tableId\":\"1534895379093893121\",\"columnName\":\"user_info_id\",\"columnComment\":\"代表当前用户;用户信息ID\",\"columnType\":\"bigint(20)\",\"javaType\":\"Long\",\"javaField\":\"userInfoId\",\"isPk\":\"0\",\"isIncrement\":\"0\",\"isRequired\":\"1\",\"isInsert\":\"1\",\"isEdit\":\"1\",\"isList\":\"1\",\"isQuery\":\"1\",\"queryType\":\"EQ\",\"htmlType\":\"input\",\"dictType\":\"\",\"sort\":2,\"required\":true,\"list\":true,\"edit\":true,\"usableColumn\":false,\"superColumn\":false,\"insert\":true,\"pk\":false,\"increment\":false,\"capJavaField\":\"userInfoId\",\"query\":true},{\"searchValue\":null,\"createBy\":\"admin\",\"createTime\":\"2022-06-09 21:49:10\",\"updateBy\":\"admin\",\"updateTime\"', '{\"code\":200,\"msg\":\"操作成功\",\"data\":null}', 0, '', '2022-06-09 21:51:03');
INSERT INTO `sys_oper_log` VALUES (1534896013134159874, '代码生成', 2, 'com.project.gen.controller.GenController.editSave()', 'PUT', 1, 'admin', '', '/gen', '127.0.0.1', '', '{\"searchValue\":null,\"createBy\":null,\"createTime\":null,\"updateBy\":\"admin\",\"updateTime\":\"2022-06-09 21:51:40\",\"params\":{\"treeCode\":null,\"treeName\":null,\"treeParentCode\":null,\"parentMenuId\":\"1534894906991337474\"},\"tableId\":\"1534895379655929857\",\"tableName\":\"user_contact_relation_status\",\"tableComment\":\"好友关系状态\",\"subTableName\":null,\"subTableFkName\":null,\"className\":\"UserContactRelationStatus\",\"tplCategory\":\"crud\",\"packageName\":\"com.project.admin.contact\",\"moduleName\":\"admin\",\"businessName\":\"status\",\"functionName\":\"好友关系状态\",\"functionAuthor\":\"project\",\"genType\":\"0\",\"genPath\":\"/\",\"pkColumn\":null,\"subTable\":null,\"columns\":[{\"searchValue\":null,\"createBy\":\"admin\",\"createTime\":\"2022-06-09 21:49:10\",\"updateBy\":\"admin\",\"updateTime\":\"2022-06-09 21:51:40\",\"params\":{},\"columnId\":\"1534895379853062145\",\"tableId\":\"1534895379655929857\",\"columnName\":\"id\",\"columnComment\":\"好友状态ID\",\"columnType\":\"bigint(20)\",\"javaType\":\"Long\",\"javaField\":\"id\",\"isPk\":\"1\",\"isIncrement\":\"1\",\"isRequired\":\"1\",\"isInsert\":null,\"isEdit\":\"1\",\"isList\":\"1\",\"isQuery\":null,\"queryType\":\"EQ\",\"htmlType\":\"input\",\"dictType\":\"\",\"sort\":1,\"required\":true,\"list\":true,\"edit\":true,\"usableColumn\":false,\"superColumn\":false,\"insert\":false,\"pk\":true,\"increment\":true,\"capJavaField\":\"id\",\"query\":false},{\"searchValue\":null,\"createBy\":\"admin\",\"createTime\":\"2022-06-09 21:49:10\",\"updateBy\":\"admin\",\"updateTime\":\"2022-06-09 21:51:40\",\"params\":{},\"columnId\":\"1534895379853062146\",\"tableId\":\"1534895379655929857\",\"columnName\":\"user_info_id\",\"columnComment\":\"代表当前用户;用户信息ID\",\"columnType\":\"bigint(20)\",\"javaType\":\"Long\",\"javaField\":\"userInfoId\",\"isPk\":\"0\",\"isIncrement\":\"0\",\"isRequired\":\"1\",\"isInsert\":\"1\",\"isEdit\":\"1\",\"isList\":\"1\",\"isQuery\":\"1\",\"queryType\":\"EQ\",\"htmlType\":\"input\",\"dictType\":\"\",\"sort\":2,\"required\":true,\"list\":true,\"edit\":true,\"usableColumn\":false,\"superColumn\":false,\"insert\":true,\"pk\":false,\"increment\":false,\"capJavaField\":\"userInfoId\",\"query\":true},{\"searchValue\":null,\"createBy\":\"admin\",\"createTime\":\"2022-06-09 21:49:10\",\"updateBy\":\"admi', '{\"code\":200,\"msg\":\"操作成功\",\"data\":null}', 0, '', '2022-06-09 21:51:41');
INSERT INTO `sys_oper_log` VALUES (1534896119686258689, '代码生成', 3, 'com.project.gen.controller.GenController.remove()', 'DELETE', 1, 'admin', '', '/gen/1534895379093893121,1534895379655929857,1534895380306046978,1534895380821946369,1534895381337845761,1534895381715333121,1534895382273175554', '127.0.0.1', '', '', '{\"code\":200,\"msg\":\"操作成功\",\"data\":null}', 0, '', '2022-06-09 21:52:07');
INSERT INTO `sys_oper_log` VALUES (1534896231498014721, '代码生成', 2, 'com.project.gen.controller.GenController.editSave()', 'PUT', 1, 'admin', '', '/gen', '127.0.0.1', '', '{\"searchValue\":null,\"createBy\":null,\"createTime\":null,\"updateBy\":\"admin\",\"updateTime\":\"2022-06-09 21:52:32\",\"params\":{\"treeCode\":null,\"treeName\":null,\"treeParentCode\":null,\"parentMenuId\":\"1534894906991337474\"},\"tableId\":\"1534895378175340545\",\"tableName\":\"user_communication_message\",\"tableComment\":\"沟通消息\",\"subTableName\":null,\"subTableFkName\":null,\"className\":\"UserCommunicationMessage\",\"tplCategory\":\"crud\",\"packageName\":\"com.project.admin.contact\",\"moduleName\":\"admin\",\"businessName\":\"message\",\"functionName\":\"沟通消息\",\"functionAuthor\":\"huan.li\",\"genType\":\"0\",\"genPath\":\"/\",\"pkColumn\":null,\"subTable\":null,\"columns\":[{\"searchValue\":null,\"createBy\":\"admin\",\"createTime\":\"2022-06-09 21:49:10\",\"updateBy\":\"admin\",\"updateTime\":\"2022-06-09 21:52:32\",\"params\":{},\"columnId\":\"1534895378397638658\",\"tableId\":\"1534895378175340545\",\"columnName\":\"id\",\"columnComment\":\"ID\",\"columnType\":\"bigint(20)\",\"javaType\":\"Long\",\"javaField\":\"id\",\"isPk\":\"1\",\"isIncrement\":\"1\",\"isRequired\":\"1\",\"isInsert\":null,\"isEdit\":\"1\",\"isList\":\"1\",\"isQuery\":null,\"queryType\":\"EQ\",\"htmlType\":\"input\",\"dictType\":\"\",\"sort\":1,\"required\":true,\"list\":true,\"edit\":true,\"usableColumn\":false,\"superColumn\":false,\"insert\":false,\"pk\":true,\"increment\":true,\"capJavaField\":\"id\",\"query\":false},{\"searchValue\":null,\"createBy\":\"admin\",\"createTime\":\"2022-06-09 21:49:10\",\"updateBy\":\"admin\",\"updateTime\":\"2022-06-09 21:52:32\",\"params\":{},\"columnId\":\"1534895378406027266\",\"tableId\":\"1534895378175340545\",\"columnName\":\"send_message_id\",\"columnComment\":\"发送消息用户;用户信息ID\",\"columnType\":\"bigint(20)\",\"javaType\":\"Long\",\"javaField\":\"sendMessageId\",\"isPk\":\"0\",\"isIncrement\":\"0\",\"isRequired\":\"1\",\"isInsert\":\"1\",\"isEdit\":\"1\",\"isList\":\"1\",\"isQuery\":\"1\",\"queryType\":\"EQ\",\"htmlType\":\"input\",\"dictType\":\"\",\"sort\":2,\"required\":true,\"list\":true,\"edit\":true,\"usableColumn\":false,\"superColumn\":false,\"insert\":true,\"pk\":false,\"increment\":false,\"capJavaField\":\"sendMessageId\",\"query\":true},{\"searchValue\":null,\"createBy\":\"admin\",\"createTime\":\"2022-06-09 21:49:10\",\"updateBy\":\"admin', '{\"code\":200,\"msg\":\"操作成功\",\"data\":null}', 0, '', '2022-06-09 21:52:33');
INSERT INTO `sys_oper_log` VALUES (1534896256097607682, '代码生成', 8, 'com.project.gen.controller.GenController.batchGenCode()', 'GET', 1, 'admin', '', '/gen/batchGenCode', '127.0.0.1', '', '', '', 0, '', '2022-06-09 21:52:39');
INSERT INTO `sys_oper_log` VALUES (1534904554498711553, '代码生成', 3, 'com.project.gen.controller.GenController.remove()', 'DELETE', 1, 'admin', '', '/gen/1534895378175340545', '127.0.0.1', '', '', '{\"code\":200,\"msg\":\"操作成功\",\"data\":null}', 0, '', '2022-06-09 22:25:38');
INSERT INTO `sys_oper_log` VALUES (1534904581665218561, '代码生成', 6, 'com.project.gen.controller.GenController.importTableSave()', 'POST', 1, 'admin', '', '/gen/importTable', '127.0.0.1', '', '\"user_work_experience\"', '{\"code\":200,\"msg\":\"操作成功\",\"data\":null}', 0, '', '2022-06-09 22:25:44');
INSERT INTO `sys_oper_log` VALUES (1534904591106596865, '代码生成', 8, 'com.project.gen.controller.GenController.batchGenCode()', 'GET', 1, 'admin', '', '/gen/batchGenCode', '127.0.0.1', '', '', '', 0, '', '2022-06-09 22:25:46');
INSERT INTO `sys_oper_log` VALUES (1534906101752291330, '代码生成', 2, 'com.project.gen.controller.GenController.editSave()', 'PUT', 1, 'admin', '', '/gen', '127.0.0.1', '', '{\"searchValue\":null,\"createBy\":null,\"createTime\":null,\"updateBy\":\"admin\",\"updateTime\":\"2022-06-09 22:31:46\",\"params\":{\"treeCode\":null,\"treeName\":null,\"treeParentCode\":null,\"parentMenuId\":null},\"tableId\":\"1534904581061324801\",\"tableName\":\"user_work_experience\",\"tableComment\":\"工作经历\",\"subTableName\":null,\"subTableFkName\":null,\"className\":\"UserWorkExperience\",\"tplCategory\":\"crud\",\"packageName\":\"com.project.admin\",\"moduleName\":\"admin\",\"businessName\":\"experience\",\"functionName\":\"工作经历\",\"functionAuthor\":\"project\",\"genType\":\"0\",\"genPath\":\"/\",\"pkColumn\":null,\"subTable\":null,\"columns\":[{\"searchValue\":null,\"createBy\":\"admin\",\"createTime\":\"2022-06-09 22:25:44\",\"updateBy\":\"admin\",\"updateTime\":\"2022-06-09 22:31:46\",\"params\":{},\"columnId\":\"1534904581182959617\",\"tableId\":\"1534904581061324801\",\"columnName\":\"id\",\"columnComment\":\"工作经历ID\",\"columnType\":\"bigint(20)\",\"javaType\":\"Long\",\"javaField\":\"id\",\"isPk\":\"1\",\"isIncrement\":\"1\",\"isRequired\":\"1\",\"isInsert\":null,\"isEdit\":\"1\",\"isList\":\"1\",\"isQuery\":null,\"queryType\":\"EQ\",\"htmlType\":\"input\",\"dictType\":\"\",\"sort\":1,\"required\":true,\"list\":true,\"edit\":true,\"usableColumn\":false,\"superColumn\":false,\"insert\":false,\"pk\":true,\"increment\":true,\"capJavaField\":\"id\",\"query\":false},{\"searchValue\":null,\"createBy\":\"admin\",\"createTime\":\"2022-06-09 22:25:44\",\"updateBy\":\"admin\",\"updateTime\":\"2022-06-09 22:31:46\",\"params\":{},\"columnId\":\"1534904581182959618\",\"tableId\":\"1534904581061324801\",\"columnName\":\"user_info_id\",\"columnComment\":\"代表当前用户;用户信息ID\",\"columnType\":\"bigint(20)\",\"javaType\":\"Long\",\"javaField\":\"userInfoId\",\"isPk\":\"0\",\"isIncrement\":\"0\",\"isRequired\":\"1\",\"isInsert\":\"1\",\"isEdit\":\"1\",\"isList\":\"1\",\"isQuery\":\"1\",\"queryType\":\"EQ\",\"htmlType\":\"input\",\"dictType\":\"\",\"sort\":2,\"required\":true,\"list\":true,\"edit\":true,\"usableColumn\":false,\"superColumn\":false,\"insert\":true,\"pk\":false,\"increment\":false,\"capJavaField\":\"userInfoId\",\"query\":true},{\"searchValue\":null,\"createBy\":\"admin\",\"createTime\":\"2022-06-09 22:25:44\",\"updateBy\":\"admin\",\"updateTime\":\"2022-06-09 22:31:46\",\"p', '{\"code\":200,\"msg\":\"操作成功\",\"data\":null}', 0, '', '2022-06-09 22:31:46');
INSERT INTO `sys_oper_log` VALUES (1534906183755128834, '代码生成', 2, 'com.project.gen.controller.GenController.editSave()', 'PUT', 1, 'admin', '', '/gen', '127.0.0.1', '', '{\"searchValue\":null,\"createBy\":null,\"createTime\":null,\"updateBy\":\"admin\",\"updateTime\":\"2022-06-09 22:32:05\",\"params\":{\"treeCode\":null,\"treeName\":null,\"treeParentCode\":null,\"parentMenuId\":3},\"tableId\":\"1534904581061324801\",\"tableName\":\"user_work_experience\",\"tableComment\":\"工作经历\",\"subTableName\":null,\"subTableFkName\":null,\"className\":\"UserWorkExperience\",\"tplCategory\":\"crud\",\"packageName\":\"com.project.admin\",\"moduleName\":\"admin\",\"businessName\":\"experience\",\"functionName\":\"工作经历\",\"functionAuthor\":\"project\",\"genType\":\"0\",\"genPath\":\"/\",\"pkColumn\":null,\"subTable\":null,\"columns\":[{\"searchValue\":null,\"createBy\":\"admin\",\"createTime\":\"2022-06-09 22:25:44\",\"updateBy\":\"admin\",\"updateTime\":\"2022-06-09 22:32:05\",\"params\":{},\"columnId\":\"1534904581182959617\",\"tableId\":\"1534904581061324801\",\"columnName\":\"id\",\"columnComment\":\"工作经历ID\",\"columnType\":\"bigint(20)\",\"javaType\":\"Long\",\"javaField\":\"id\",\"isPk\":\"1\",\"isIncrement\":\"1\",\"isRequired\":\"1\",\"isInsert\":null,\"isEdit\":\"1\",\"isList\":\"1\",\"isQuery\":null,\"queryType\":\"EQ\",\"htmlType\":\"input\",\"dictType\":\"\",\"sort\":1,\"required\":true,\"list\":true,\"edit\":true,\"usableColumn\":false,\"superColumn\":false,\"insert\":false,\"pk\":true,\"increment\":true,\"capJavaField\":\"id\",\"query\":false},{\"searchValue\":null,\"createBy\":\"admin\",\"createTime\":\"2022-06-09 22:25:44\",\"updateBy\":\"admin\",\"updateTime\":\"2022-06-09 22:32:05\",\"params\":{},\"columnId\":\"1534904581182959618\",\"tableId\":\"1534904581061324801\",\"columnName\":\"user_info_id\",\"columnComment\":\"代表当前用户;用户信息ID\",\"columnType\":\"bigint(20)\",\"javaType\":\"Long\",\"javaField\":\"userInfoId\",\"isPk\":\"0\",\"isIncrement\":\"0\",\"isRequired\":\"1\",\"isInsert\":\"1\",\"isEdit\":\"1\",\"isList\":\"1\",\"isQuery\":\"1\",\"queryType\":\"EQ\",\"htmlType\":\"input\",\"dictType\":\"\",\"sort\":2,\"required\":true,\"list\":true,\"edit\":true,\"usableColumn\":false,\"superColumn\":false,\"insert\":true,\"pk\":false,\"increment\":false,\"capJavaField\":\"userInfoId\",\"query\":true},{\"searchValue\":null,\"createBy\":\"admin\",\"createTime\":\"2022-06-09 22:25:44\",\"updateBy\":\"admin\",\"updateTime\":\"2022-06-09 22:32:05\",\"para', '{\"code\":200,\"msg\":\"操作成功\",\"data\":null}', 0, '', '2022-06-09 22:32:06');
INSERT INTO `sys_oper_log` VALUES (1534906551931133954, '代码生成', 8, 'com.project.gen.controller.GenController.batchGenCode()', 'GET', 1, 'admin', '', '/gen/batchGenCode', '127.0.0.1', '', '', '', 0, '', '2022-06-09 22:33:34');
INSERT INTO `sys_oper_log` VALUES (1534906596080377857, '代码生成', 8, 'com.project.gen.controller.GenController.batchGenCode()', 'GET', 1, 'admin', '', '/gen/batchGenCode', '127.0.0.1', '', '', '', 0, '', '2022-06-09 22:33:44');
INSERT INTO `sys_oper_log` VALUES (1534909328531021826, '菜单管理', 2, 'com.project.system.controller.SysMenuController.edit()', 'PUT', 1, 'admin', '', '/menu', '127.0.0.1', '', '{\"searchValue\":null,\"createBy\":\"admin\",\"createTime\":\"2022-06-09 21:43:46\",\"updateBy\":\"admin\",\"updateTime\":\"2022-06-09 22:44:35\",\"params\":{},\"parentName\":null,\"parentId\":0,\"children\":[],\"menuId\":\"1534894022110633986\",\"menuName\":\"你好同学\",\"orderNum\":0,\"path\":\"classmate\",\"component\":null,\"queryParam\":null,\"isFrame\":\"1\",\"isCache\":\"0\",\"menuType\":\"M\",\"visible\":\"0\",\"status\":\"0\",\"icon\":\"build\",\"remark\":\"\"}', '{\"code\":200,\"msg\":\"操作成功\",\"data\":null}', 0, '', '2022-06-09 22:44:36');
INSERT INTO `sys_oper_log` VALUES (1534909371623301122, '菜单管理', 2, 'com.project.system.controller.SysMenuController.edit()', 'PUT', 1, 'admin', '', '/menu', '127.0.0.1', '', '{\"searchValue\":null,\"createBy\":\"admin\",\"createTime\":\"2022-06-09 21:43:46\",\"updateBy\":\"admin\",\"updateTime\":\"2022-06-09 22:44:46\",\"params\":{},\"parentName\":null,\"parentId\":0,\"children\":[],\"menuId\":\"1534894022110633986\",\"menuName\":\"你好同学\",\"orderNum\":0,\"path\":\"admin\",\"component\":null,\"queryParam\":null,\"isFrame\":\"1\",\"isCache\":\"0\",\"menuType\":\"M\",\"visible\":\"0\",\"status\":\"0\",\"icon\":\"build\",\"remark\":\"\"}', '{\"code\":200,\"msg\":\"操作成功\",\"data\":null}', 0, '', '2022-06-09 22:44:46');
INSERT INTO `sys_oper_log` VALUES (1534909390178902018, '菜单管理', 2, 'com.project.system.controller.SysMenuController.edit()', 'PUT', 1, 'admin', '', '/menu', '127.0.0.1', '', '{\"searchValue\":null,\"createBy\":\"admin\",\"createTime\":\"2022-06-09 21:43:46\",\"updateBy\":\"admin\",\"updateTime\":\"2022-06-09 22:44:50\",\"params\":{},\"parentName\":null,\"parentId\":0,\"children\":[],\"menuId\":\"1534894022110633986\",\"menuName\":\"你好同学\",\"orderNum\":0,\"path\":\"admin\",\"component\":null,\"queryParam\":null,\"isFrame\":\"1\",\"isCache\":\"0\",\"menuType\":\"M\",\"visible\":\"0\",\"status\":\"0\",\"icon\":\"build\",\"remark\":\"\"}', '{\"code\":200,\"msg\":\"操作成功\",\"data\":null}', 0, '', '2022-06-09 22:44:51');
INSERT INTO `sys_oper_log` VALUES (1534909453798105089, '菜单管理', 2, 'com.project.system.controller.SysMenuController.edit()', 'PUT', 1, 'admin', '', '/menu', '127.0.0.1', '', '{\"searchValue\":null,\"createBy\":\"admin\",\"createTime\":\"2022-06-09 21:47:17\",\"updateBy\":\"admin\",\"updateTime\":\"2022-06-09 22:45:05\",\"params\":{},\"parentName\":null,\"parentId\":\"1534894022110633986\",\"children\":[],\"menuId\":\"1534894906991337474\",\"menuName\":\"人脉\",\"orderNum\":1,\"path\":\"contact\",\"component\":null,\"queryParam\":null,\"isFrame\":\"1\",\"isCache\":\"0\",\"menuType\":\"C\",\"visible\":\"0\",\"status\":\"0\",\"icon\":\"button\",\"remark\":\"\"}', '{\"code\":200,\"msg\":\"操作成功\",\"data\":null}', 0, '', '2022-06-09 22:45:06');
INSERT INTO `sys_oper_log` VALUES (1534910214154117121, '菜单管理', 3, 'com.project.system.controller.SysMenuController.remove()', 'DELETE', 1, 'admin', '', '/menu/1534906596013355008', '127.0.0.1', '', '', '{\"code\":500,\"msg\":\"存在子菜单,不允许删除\",\"data\":null}', 0, '', '2022-06-09 22:48:07');
INSERT INTO `sys_oper_log` VALUES (1534910263487520769, '菜单管理', 3, 'com.project.system.controller.SysMenuController.remove()', 'DELETE', 1, 'admin', '', '/menu/1534906596013355013', '127.0.0.1', '', '', '{\"code\":200,\"msg\":\"操作成功\",\"data\":null}', 0, '', '2022-06-09 22:48:19');
INSERT INTO `sys_oper_log` VALUES (1534910283892809730, '菜单管理', 3, 'com.project.system.controller.SysMenuController.remove()', 'DELETE', 1, 'admin', '', '/menu/1534906596013355012', '127.0.0.1', '', '', '{\"code\":200,\"msg\":\"操作成功\",\"data\":null}', 0, '', '2022-06-09 22:48:24');
INSERT INTO `sys_oper_log` VALUES (1534910305690607617, '菜单管理', 3, 'com.project.system.controller.SysMenuController.remove()', 'DELETE', 1, 'admin', '', '/menu/1534906596013355010', '127.0.0.1', '', '', '{\"code\":200,\"msg\":\"操作成功\",\"data\":null}', 0, '', '2022-06-09 22:48:29');
INSERT INTO `sys_oper_log` VALUES (1534910317333995521, '菜单管理', 3, 'com.project.system.controller.SysMenuController.remove()', 'DELETE', 1, 'admin', '', '/menu/1534906596013355009', '127.0.0.1', '', '', '{\"code\":200,\"msg\":\"操作成功\",\"data\":null}', 0, '', '2022-06-09 22:48:32');
INSERT INTO `sys_oper_log` VALUES (1534910327366770690, '菜单管理', 3, 'com.project.system.controller.SysMenuController.remove()', 'DELETE', 1, 'admin', '', '/menu/1534906596013355011', '127.0.0.1', '', '', '{\"code\":200,\"msg\":\"操作成功\",\"data\":null}', 0, '', '2022-06-09 22:48:34');
INSERT INTO `sys_oper_log` VALUES (1534910339924516865, '菜单管理', 3, 'com.project.system.controller.SysMenuController.remove()', 'DELETE', 1, 'admin', '', '/menu/1534906596013355008', '127.0.0.1', '', '', '{\"code\":200,\"msg\":\"操作成功\",\"data\":null}', 0, '', '2022-06-09 22:48:37');
INSERT INTO `sys_oper_log` VALUES (1534910410950860801, '菜单管理', 3, 'com.project.system.controller.SysMenuController.remove()', 'DELETE', 1, 'admin', '', '/menu/1534896255204306944', '127.0.0.1', '', '', '{\"code\":500,\"msg\":\"存在子菜单,不允许删除\",\"data\":null}', 0, '', '2022-06-09 22:48:54');
INSERT INTO `sys_oper_log` VALUES (1534910430240464898, '菜单管理', 3, 'com.project.system.controller.SysMenuController.remove()', 'DELETE', 1, 'admin', '', '/menu/1534896255204306945', '127.0.0.1', '', '', '{\"code\":200,\"msg\":\"操作成功\",\"data\":null}', 0, '', '2022-06-09 22:48:58');
INSERT INTO `sys_oper_log` VALUES (1534910438012510209, '菜单管理', 3, 'com.project.system.controller.SysMenuController.remove()', 'DELETE', 1, 'admin', '', '/menu/1534896255204306946', '127.0.0.1', '', '', '{\"code\":200,\"msg\":\"操作成功\",\"data\":null}', 0, '', '2022-06-09 22:49:00');
INSERT INTO `sys_oper_log` VALUES (1534910445616783361, '菜单管理', 3, 'com.project.system.controller.SysMenuController.remove()', 'DELETE', 1, 'admin', '', '/menu/1534896255204306947', '127.0.0.1', '', '', '{\"code\":200,\"msg\":\"操作成功\",\"data\":null}', 0, '', '2022-06-09 22:49:02');
INSERT INTO `sys_oper_log` VALUES (1534910455972519938, '菜单管理', 3, 'com.project.system.controller.SysMenuController.remove()', 'DELETE', 1, 'admin', '', '/menu/1534896255204306948', '127.0.0.1', '', '', '{\"code\":200,\"msg\":\"操作成功\",\"data\":null}', 0, '', '2022-06-09 22:49:05');
INSERT INTO `sys_oper_log` VALUES (1534910465036410881, '菜单管理', 3, 'com.project.system.controller.SysMenuController.remove()', 'DELETE', 1, 'admin', '', '/menu/1534896255204306949', '127.0.0.1', '', '', '{\"code\":200,\"msg\":\"操作成功\",\"data\":null}', 0, '', '2022-06-09 22:49:07');
INSERT INTO `sys_oper_log` VALUES (1534910476142927873, '菜单管理', 3, 'com.project.system.controller.SysMenuController.remove()', 'DELETE', 1, 'admin', '', '/menu/1534896255204306944', '127.0.0.1', '', '', '{\"code\":200,\"msg\":\"操作成功\",\"data\":null}', 0, '', '2022-06-09 22:49:09');
INSERT INTO `sys_oper_log` VALUES (1534910488776171522, '菜单管理', 3, 'com.project.system.controller.SysMenuController.remove()', 'DELETE', 1, 'admin', '', '/menu/1534894906991337474', '127.0.0.1', '', '', '{\"code\":200,\"msg\":\"操作成功\",\"data\":null}', 0, '', '2022-06-09 22:49:12');
INSERT INTO `sys_oper_log` VALUES (1534910821912961026, '菜单管理', 1, 'com.project.system.controller.SysMenuController.add()', 'POST', 1, 'admin', '', '/menu', '127.0.0.1', '', '{\"searchValue\":null,\"createBy\":\"admin\",\"createTime\":\"2022-06-09 22:50:31\",\"updateBy\":\"admin\",\"updateTime\":\"2022-06-09 22:50:31\",\"params\":{},\"parentName\":null,\"parentId\":\"1534894022110633986\",\"children\":[],\"menuId\":\"1534910821728411650\",\"menuName\":\"学历\",\"orderNum\":2,\"path\":\"education\",\"component\":null,\"queryParam\":null,\"isFrame\":\"1\",\"isCache\":\"0\",\"menuType\":\"C\",\"visible\":\"0\",\"status\":\"0\",\"icon\":\"build\",\"remark\":null}', '{\"code\":200,\"msg\":\"操作成功\",\"data\":null}', 0, '', '2022-06-09 22:50:32');
INSERT INTO `sys_oper_log` VALUES (1534910855547084801, '菜单管理', 2, 'com.project.system.controller.SysMenuController.edit()', 'PUT', 1, 'admin', '', '/menu', '127.0.0.1', '', '{\"searchValue\":null,\"createBy\":\"admin\",\"createTime\":\"2022-06-09 21:43:46\",\"updateBy\":\"admin\",\"updateTime\":\"2022-06-09 22:50:39\",\"params\":{},\"parentName\":null,\"parentId\":0,\"children\":[],\"menuId\":\"1534894022110633986\",\"menuName\":\"你好同学\",\"orderNum\":0,\"path\":\"admin\",\"component\":null,\"queryParam\":null,\"isFrame\":\"1\",\"isCache\":\"0\",\"menuType\":\"M\",\"visible\":\"0\",\"status\":\"0\",\"icon\":\"build\",\"remark\":\"\"}', '{\"code\":200,\"msg\":\"操作成功\",\"data\":null}', 0, '', '2022-06-09 22:50:40');
INSERT INTO `sys_oper_log` VALUES (1534910871820984321, '菜单管理', 2, 'com.project.system.controller.SysMenuController.edit()', 'PUT', 1, 'admin', '', '/menu', '127.0.0.1', '', '{\"searchValue\":null,\"createBy\":\"admin\",\"createTime\":\"2022-06-09 22:50:32\",\"updateBy\":\"admin\",\"updateTime\":\"2022-06-09 22:50:43\",\"params\":{},\"parentName\":null,\"parentId\":\"1534894022110633986\",\"children\":[],\"menuId\":\"1534910821728411650\",\"menuName\":\"学历\",\"orderNum\":2,\"path\":\"education\",\"component\":null,\"queryParam\":null,\"isFrame\":\"1\",\"isCache\":\"0\",\"menuType\":\"C\",\"visible\":\"0\",\"status\":\"0\",\"icon\":\"build\",\"remark\":\"\"}', '{\"code\":200,\"msg\":\"操作成功\",\"data\":null}', 0, '', '2022-06-09 22:50:44');
INSERT INTO `sys_oper_log` VALUES (1534911115245805569, '代码生成', 3, 'com.project.gen.controller.GenController.remove()', 'DELETE', 1, 'admin', '', '/gen/1534904581061324801', '127.0.0.1', '', '', '{\"code\":200,\"msg\":\"操作成功\",\"data\":null}', 0, '', '2022-06-09 22:51:42');
INSERT INTO `sys_oper_log` VALUES (1534911138851348482, '代码生成', 6, 'com.project.gen.controller.GenController.importTableSave()', 'POST', 1, 'admin', '', '/gen/importTable', '127.0.0.1', '', '\"user_education\"', '{\"code\":200,\"msg\":\"操作成功\",\"data\":null}', 0, '', '2022-06-09 22:51:47');
INSERT INTO `sys_oper_log` VALUES (1534911328656187393, '代码生成', 2, 'com.project.gen.controller.GenController.editSave()', 'PUT', 1, 'admin', '', '/gen', '127.0.0.1', '', '{\"searchValue\":null,\"createBy\":null,\"createTime\":null,\"updateBy\":\"admin\",\"updateTime\":\"2022-06-09 22:52:32\",\"params\":{\"treeCode\":null,\"treeName\":null,\"treeParentCode\":null,\"parentMenuId\":\"1534910821728411650\"},\"tableId\":\"1534911138004185089\",\"tableName\":\"user_education\",\"tableComment\":\"学历\",\"subTableName\":null,\"subTableFkName\":null,\"className\":\"UserEducation\",\"tplCategory\":\"crud\",\"packageName\":\"com.project.admin\",\"moduleName\":\"admin\",\"businessName\":\"education\",\"functionName\":\"学历\",\"functionAuthor\":\"huan.li\",\"genType\":\"0\",\"genPath\":\"/\",\"pkColumn\":null,\"subTable\":null,\"columns\":[{\"searchValue\":null,\"createBy\":\"admin\",\"createTime\":\"2022-06-09 22:51:47\",\"updateBy\":\"admin\",\"updateTime\":\"2022-06-09 22:52:32\",\"params\":{},\"columnId\":\"1534911138398449665\",\"tableId\":\"1534911138004185089\",\"columnName\":\"id\",\"columnComment\":\"学历ID\",\"columnType\":\"bigint(20)\",\"javaType\":\"Long\",\"javaField\":\"id\",\"isPk\":\"1\",\"isIncrement\":\"1\",\"isRequired\":\"1\",\"isInsert\":null,\"isEdit\":\"1\",\"isList\":\"1\",\"isQuery\":null,\"queryType\":\"EQ\",\"htmlType\":\"input\",\"dictType\":\"\",\"sort\":1,\"required\":true,\"list\":true,\"edit\":true,\"usableColumn\":false,\"superColumn\":false,\"insert\":false,\"pk\":true,\"increment\":true,\"capJavaField\":\"id\",\"query\":false},{\"searchValue\":null,\"createBy\":\"admin\",\"createTime\":\"2022-06-09 22:51:47\",\"updateBy\":\"admin\",\"updateTime\":\"2022-06-09 22:52:32\",\"params\":{},\"columnId\":\"1534911138402643969\",\"tableId\":\"1534911138004185089\",\"columnName\":\"user_info_id\",\"columnComment\":\"代表当前用户;用户信息ID\",\"columnType\":\"bigint(20)\",\"javaType\":\"Long\",\"javaField\":\"userInfoId\",\"isPk\":\"0\",\"isIncrement\":\"0\",\"isRequired\":\"1\",\"isInsert\":\"1\",\"isEdit\":\"1\",\"isList\":\"1\",\"isQuery\":\"1\",\"queryType\":\"EQ\",\"htmlType\":\"input\",\"dictType\":\"\",\"sort\":2,\"required\":true,\"list\":true,\"edit\":true,\"usableColumn\":false,\"superColumn\":false,\"insert\":true,\"pk\":false,\"increment\":false,\"capJavaField\":\"userInfoId\",\"query\":true},{\"searchValue\":null,\"createBy\":\"admin\",\"createTime\":\"2022-06-09 22:51:47\",\"updateBy\":\"admin\",\"updateTime\":\"2022-06-09 22:52:32\",\"pa', '{\"code\":200,\"msg\":\"操作成功\",\"data\":null}', 0, '', '2022-06-09 22:52:33');
INSERT INTO `sys_oper_log` VALUES (1534911339284553730, '代码生成', 8, 'com.project.gen.controller.GenController.batchGenCode()', 'GET', 1, 'admin', '', '/gen/batchGenCode', '127.0.0.1', '', '', '', 0, '', '2022-06-09 22:52:35');
INSERT INTO `sys_oper_log` VALUES (1534916143373234178, '代码生成', 8, 'com.project.gen.controller.GenController.batchGenCode()', 'GET', 1, 'admin', '', '/gen/batchGenCode', '127.0.0.1', '', '', '', 0, '', '2022-06-09 23:11:41');
INSERT INTO `sys_oper_log` VALUES (1534916232447668225, '代码生成', 8, 'com.project.gen.controller.GenController.batchGenCode()', 'GET', 1, 'admin', '', '/gen/batchGenCode', '127.0.0.1', '', '', '', 0, '', '2022-06-09 23:12:02');
INSERT INTO `sys_oper_log` VALUES (1534918750359662593, '菜单管理', 3, 'com.project.system.controller.SysMenuController.remove()', 'DELETE', 1, 'admin', '', '/menu/1534911339217530880', '127.0.0.1', '', '', '{\"code\":500,\"msg\":\"存在子菜单,不允许删除\",\"data\":null}', 0, '', '2022-06-09 23:22:02');
INSERT INTO `sys_oper_log` VALUES (1534918770072891394, '菜单管理', 3, 'com.project.system.controller.SysMenuController.remove()', 'DELETE', 1, 'admin', '', '/menu/1534911339217530885', '127.0.0.1', '', '', '{\"code\":200,\"msg\":\"操作成功\",\"data\":null}', 0, '', '2022-06-09 23:22:07');
INSERT INTO `sys_oper_log` VALUES (1534918781527535617, '菜单管理', 3, 'com.project.system.controller.SysMenuController.remove()', 'DELETE', 1, 'admin', '', '/menu/1534911339217530883', '127.0.0.1', '', '', '{\"code\":200,\"msg\":\"操作成功\",\"data\":null}', 0, '', '2022-06-09 23:22:10');
INSERT INTO `sys_oper_log` VALUES (1534918790046167042, '菜单管理', 3, 'com.project.system.controller.SysMenuController.remove()', 'DELETE', 1, 'admin', '', '/menu/1534911339217530884', '127.0.0.1', '', '', '{\"code\":200,\"msg\":\"操作成功\",\"data\":null}', 0, '', '2022-06-09 23:22:12');
INSERT INTO `sys_oper_log` VALUES (1534918798954868738, '菜单管理', 3, 'com.project.system.controller.SysMenuController.remove()', 'DELETE', 1, 'admin', '', '/menu/1534911339217530881', '127.0.0.1', '', '', '{\"code\":200,\"msg\":\"操作成功\",\"data\":null}', 0, '', '2022-06-09 23:22:14');
INSERT INTO `sys_oper_log` VALUES (1534918809050558466, '菜单管理', 3, 'com.project.system.controller.SysMenuController.remove()', 'DELETE', 1, 'admin', '', '/menu/1534911339217530882', '127.0.0.1', '', '', '{\"code\":200,\"msg\":\"操作成功\",\"data\":null}', 0, '', '2022-06-09 23:22:16');
INSERT INTO `sys_oper_log` VALUES (1534918824582066177, '菜单管理', 3, 'com.project.system.controller.SysMenuController.remove()', 'DELETE', 1, 'admin', '', '/menu/1534911339217530880', '127.0.0.1', '', '', '{\"code\":200,\"msg\":\"操作成功\",\"data\":null}', 0, '', '2022-06-09 23:22:20');
INSERT INTO `sys_oper_log` VALUES (1534918833947947009, '菜单管理', 3, 'com.project.system.controller.SysMenuController.remove()', 'DELETE', 1, 'admin', '', '/menu/1534916232355483648', '127.0.0.1', '', '', '{\"code\":500,\"msg\":\"存在子菜单,不允许删除\",\"data\":null}', 0, '', '2022-06-09 23:22:22');
INSERT INTO `sys_oper_log` VALUES (1534918850226040834, '菜单管理', 3, 'com.project.system.controller.SysMenuController.remove()', 'DELETE', 1, 'admin', '', '/menu/1534916232355483648', '127.0.0.1', '', '', '{\"code\":500,\"msg\":\"存在子菜单,不允许删除\",\"data\":null}', 0, '', '2022-06-09 23:22:26');
INSERT INTO `sys_oper_log` VALUES (1534918858191024130, '菜单管理', 3, 'com.project.system.controller.SysMenuController.remove()', 'DELETE', 1, 'admin', '', '/menu/1534916232355483648', '127.0.0.1', '', '', '{\"code\":500,\"msg\":\"存在子菜单,不允许删除\",\"data\":null}', 0, '', '2022-06-09 23:22:28');
INSERT INTO `sys_oper_log` VALUES (1534918869205266434, '菜单管理', 3, 'com.project.system.controller.SysMenuController.remove()', 'DELETE', 1, 'admin', '', '/menu/1534916232355483650', '127.0.0.1', '', '', '{\"code\":200,\"msg\":\"操作成功\",\"data\":null}', 0, '', '2022-06-09 23:22:30');
INSERT INTO `sys_oper_log` VALUES (1534918875840655362, '菜单管理', 3, 'com.project.system.controller.SysMenuController.remove()', 'DELETE', 1, 'admin', '', '/menu/1534916232355483651', '127.0.0.1', '', '', '{\"code\":200,\"msg\":\"操作成功\",\"data\":null}', 0, '', '2022-06-09 23:22:32');
INSERT INTO `sys_oper_log` VALUES (1534918885873430530, '菜单管理', 3, 'com.project.system.controller.SysMenuController.remove()', 'DELETE', 1, 'admin', '', '/menu/1534916232355483652', '127.0.0.1', '', '', '{\"code\":200,\"msg\":\"操作成功\",\"data\":null}', 0, '', '2022-06-09 23:22:34');
INSERT INTO `sys_oper_log` VALUES (1534918895025401857, '菜单管理', 3, 'com.project.system.controller.SysMenuController.remove()', 'DELETE', 1, 'admin', '', '/menu/1534916232355483649', '127.0.0.1', '', '', '{\"code\":200,\"msg\":\"操作成功\",\"data\":null}', 0, '', '2022-06-09 23:22:37');
INSERT INTO `sys_oper_log` VALUES (1534918904932347905, '菜单管理', 3, 'com.project.system.controller.SysMenuController.remove()', 'DELETE', 1, 'admin', '', '/menu/1534916232355483653', '127.0.0.1', '', '', '{\"code\":200,\"msg\":\"操作成功\",\"data\":null}', 0, '', '2022-06-09 23:22:39');
INSERT INTO `sys_oper_log` VALUES (1534918983604908033, '菜单管理', 3, 'com.project.system.controller.SysMenuController.remove()', 'DELETE', 1, 'admin', '', '/menu/1534916232355483648', '127.0.0.1', '', '', '{\"code\":200,\"msg\":\"操作成功\",\"data\":null}', 0, '', '2022-06-09 23:22:58');
INSERT INTO `sys_oper_log` VALUES (1534919107072634882, '代码生成', 2, 'com.project.gen.controller.GenController.editSave()', 'PUT', 1, 'admin', '', '/gen', '127.0.0.1', '', '{\"searchValue\":null,\"createBy\":null,\"createTime\":null,\"updateBy\":\"admin\",\"updateTime\":\"2022-06-09 23:23:26\",\"params\":{\"treeCode\":null,\"treeName\":null,\"treeParentCode\":null,\"parentMenuId\":\"1534910821728411650\"},\"tableId\":\"1534911138004185089\",\"tableName\":\"user_education\",\"tableComment\":\"学历\",\"subTableName\":null,\"subTableFkName\":null,\"className\":\"UserEducation\",\"tplCategory\":\"crud\",\"packageName\":\"com.project.admin\",\"moduleName\":\"admin\",\"businessName\":\"education\",\"functionName\":\"学历\",\"functionAuthor\":\"huan.li\",\"genType\":\"0\",\"genPath\":\"/\",\"pkColumn\":null,\"subTable\":null,\"columns\":[{\"searchValue\":null,\"createBy\":\"admin\",\"createTime\":\"2022-06-09 22:51:47\",\"updateBy\":\"admin\",\"updateTime\":\"2022-06-09 23:23:26\",\"params\":{},\"columnId\":\"1534911138398449665\",\"tableId\":\"1534911138004185089\",\"columnName\":\"id\",\"columnComment\":\"学历ID\",\"columnType\":\"bigint(20)\",\"javaType\":\"Long\",\"javaField\":\"id\",\"isPk\":\"1\",\"isIncrement\":\"1\",\"isRequired\":\"1\",\"isInsert\":null,\"isEdit\":\"1\",\"isList\":\"1\",\"isQuery\":null,\"queryType\":\"EQ\",\"htmlType\":\"input\",\"dictType\":\"\",\"sort\":1,\"required\":true,\"list\":true,\"superColumn\":false,\"insert\":false,\"pk\":true,\"edit\":true,\"usableColumn\":false,\"query\":false,\"capJavaField\":\"id\",\"increment\":true},{\"searchValue\":null,\"createBy\":\"admin\",\"createTime\":\"2022-06-09 22:51:47\",\"updateBy\":\"admin\",\"updateTime\":\"2022-06-09 23:23:26\",\"params\":{},\"columnId\":\"1534911138402643969\",\"tableId\":\"1534911138004185089\",\"columnName\":\"user_info_id\",\"columnComment\":\"代表当前用户;用户信息ID\",\"columnType\":\"bigint(20)\",\"javaType\":\"Long\",\"javaField\":\"userInfoId\",\"isPk\":\"0\",\"isIncrement\":\"0\",\"isRequired\":\"1\",\"isInsert\":\"1\",\"isEdit\":\"1\",\"isList\":\"1\",\"isQuery\":\"1\",\"queryType\":\"EQ\",\"htmlType\":\"input\",\"dictType\":\"\",\"sort\":2,\"required\":true,\"list\":true,\"superColumn\":false,\"insert\":true,\"pk\":false,\"edit\":true,\"usableColumn\":false,\"query\":true,\"capJavaField\":\"userInfoId\",\"increment\":false},{\"searchValue\":null,\"createBy\":\"admin\",\"createTime\":\"2022-06-09 22:51:47\",\"updateBy\":\"admin\",\"updateTime\":\"2022-06-09 23:23:26\",\"pa', '{\"code\":200,\"msg\":\"操作成功\",\"data\":null}', 0, '', '2022-06-09 23:23:27');
INSERT INTO `sys_oper_log` VALUES (1534919118380478465, '代码生成', 8, 'com.project.gen.controller.GenController.batchGenCode()', 'GET', 1, 'admin', '', '/gen/batchGenCode', '127.0.0.1', '', '', '', 0, '', '2022-06-09 23:23:30');
INSERT INTO `sys_oper_log` VALUES (1534921439822999553, '菜单管理', 3, 'com.project.system.controller.SysMenuController.remove()', 'DELETE', 1, 'admin', '', '/menu/1534919118305071109', '127.0.0.1', '', '', '{\"code\":200,\"msg\":\"操作成功\",\"data\":null}', 0, '', '2022-06-09 23:32:43');
INSERT INTO `sys_oper_log` VALUES (1534921447238529026, '菜单管理', 3, 'com.project.system.controller.SysMenuController.remove()', 'DELETE', 1, 'admin', '', '/menu/1534919118305071108', '127.0.0.1', '', '', '{\"code\":200,\"msg\":\"操作成功\",\"data\":null}', 0, '', '2022-06-09 23:32:45');
INSERT INTO `sys_oper_log` VALUES (1534921457271304194, '菜单管理', 3, 'com.project.system.controller.SysMenuController.remove()', 'DELETE', 1, 'admin', '', '/menu/1534919118305071107', '127.0.0.1', '', '', '{\"code\":200,\"msg\":\"操作成功\",\"data\":null}', 0, '', '2022-06-09 23:32:48');
INSERT INTO `sys_oper_log` VALUES (1534921465890598913, '菜单管理', 3, 'com.project.system.controller.SysMenuController.remove()', 'DELETE', 1, 'admin', '', '/menu/1534919118305071106', '127.0.0.1', '', '', '{\"code\":200,\"msg\":\"操作成功\",\"data\":null}', 0, '', '2022-06-09 23:32:50');
INSERT INTO `sys_oper_log` VALUES (1534921473301934082, '菜单管理', 3, 'com.project.system.controller.SysMenuController.remove()', 'DELETE', 1, 'admin', '', '/menu/1534919118305071105', '127.0.0.1', '', '', '{\"code\":200,\"msg\":\"操作成功\",\"data\":null}', 0, '', '2022-06-09 23:32:51');
INSERT INTO `sys_oper_log` VALUES (1534921482491654145, '菜单管理', 3, 'com.project.system.controller.SysMenuController.remove()', 'DELETE', 1, 'admin', '', '/menu/1534919118305071104', '127.0.0.1', '', '', '{\"code\":200,\"msg\":\"操作成功\",\"data\":null}', 0, '', '2022-06-09 23:32:54');
INSERT INTO `sys_oper_log` VALUES (1534921495355584514, '菜单管理', 3, 'com.project.system.controller.SysMenuController.remove()', 'DELETE', 1, 'admin', '', '/menu/1534910821728411650', '127.0.0.1', '', '', '{\"code\":200,\"msg\":\"操作成功\",\"data\":null}', 0, '', '2022-06-09 23:32:57');
INSERT INTO `sys_oper_log` VALUES (1534921554910507010, '菜单管理', 2, 'com.project.system.controller.SysMenuController.edit()', 'PUT', 1, 'admin', '', '/menu', '127.0.0.1', '', '{\"searchValue\":null,\"createBy\":\"admin\",\"createTime\":\"2022-06-09 21:43:46\",\"updateBy\":\"admin\",\"updateTime\":\"2022-06-09 23:33:10\",\"params\":{},\"parentName\":null,\"parentId\":0,\"children\":[],\"menuId\":\"1534894022110633986\",\"menuName\":\"你好同学\",\"orderNum\":0,\"path\":\"admin\",\"component\":null,\"queryParam\":null,\"isFrame\":\"1\",\"isCache\":\"0\",\"menuType\":\"M\",\"visible\":\"0\",\"status\":\"0\",\"icon\":\"build\",\"remark\":\"\"}', '{\"code\":200,\"msg\":\"操作成功\",\"data\":null}', 0, '', '2022-06-09 23:33:11');
INSERT INTO `sys_oper_log` VALUES (1534921649206849538, '代码生成', 2, 'com.project.gen.controller.GenController.editSave()', 'PUT', 1, 'admin', '', '/gen', '127.0.0.1', '', '{\"searchValue\":null,\"createBy\":null,\"createTime\":null,\"updateBy\":\"admin\",\"updateTime\":\"2022-06-09 23:33:32\",\"params\":{\"treeCode\":null,\"treeName\":null,\"treeParentCode\":null,\"parentMenuId\":\"1534894022110633986\"},\"tableId\":\"1534911138004185089\",\"tableName\":\"user_education\",\"tableComment\":\"学历\",\"subTableName\":null,\"subTableFkName\":null,\"className\":\"UserEducation\",\"tplCategory\":\"crud\",\"packageName\":\"com.project.admin\",\"moduleName\":\"admin\",\"businessName\":\"education\",\"functionName\":\"学历\",\"functionAuthor\":\"huan.li\",\"genType\":\"0\",\"genPath\":\"/\",\"pkColumn\":null,\"subTable\":null,\"columns\":[{\"searchValue\":null,\"createBy\":\"admin\",\"createTime\":\"2022-06-09 22:51:47\",\"updateBy\":\"admin\",\"updateTime\":\"2022-06-09 23:33:32\",\"params\":{},\"columnId\":\"1534911138398449665\",\"tableId\":\"1534911138004185089\",\"columnName\":\"id\",\"columnComment\":\"学历ID\",\"columnType\":\"bigint(20)\",\"javaType\":\"Long\",\"javaField\":\"id\",\"isPk\":\"1\",\"isIncrement\":\"1\",\"isRequired\":\"1\",\"isInsert\":null,\"isEdit\":\"1\",\"isList\":\"1\",\"isQuery\":null,\"queryType\":\"EQ\",\"htmlType\":\"input\",\"dictType\":\"\",\"sort\":1,\"required\":true,\"list\":true,\"usableColumn\":false,\"superColumn\":false,\"insert\":false,\"edit\":true,\"pk\":true,\"increment\":true,\"query\":false,\"capJavaField\":\"id\"},{\"searchValue\":null,\"createBy\":\"admin\",\"createTime\":\"2022-06-09 22:51:47\",\"updateBy\":\"admin\",\"updateTime\":\"2022-06-09 23:33:32\",\"params\":{},\"columnId\":\"1534911138402643969\",\"tableId\":\"1534911138004185089\",\"columnName\":\"user_info_id\",\"columnComment\":\"代表当前用户;用户信息ID\",\"columnType\":\"bigint(20)\",\"javaType\":\"Long\",\"javaField\":\"userInfoId\",\"isPk\":\"0\",\"isIncrement\":\"0\",\"isRequired\":\"1\",\"isInsert\":\"1\",\"isEdit\":\"1\",\"isList\":\"1\",\"isQuery\":\"1\",\"queryType\":\"EQ\",\"htmlType\":\"input\",\"dictType\":\"\",\"sort\":2,\"required\":true,\"list\":true,\"usableColumn\":false,\"superColumn\":false,\"insert\":true,\"edit\":true,\"pk\":false,\"increment\":false,\"query\":true,\"capJavaField\":\"userInfoId\"},{\"searchValue\":null,\"createBy\":\"admin\",\"createTime\":\"2022-06-09 22:51:47\",\"updateBy\":\"admin\",\"updateTime\":\"2022-06-09 23:33:32\",\"pa', '{\"code\":200,\"msg\":\"操作成功\",\"data\":null}', 0, '', '2022-06-09 23:33:33');
INSERT INTO `sys_oper_log` VALUES (1534921671138865154, '代码生成', 8, 'com.project.gen.controller.GenController.batchGenCode()', 'GET', 1, 'admin', '', '/gen/batchGenCode', '127.0.0.1', '', '', '', 0, '', '2022-06-09 23:33:39');
INSERT INTO `sys_oper_log` VALUES (1534921719637602306, '代码生成', 8, 'com.project.gen.controller.GenController.batchGenCode()', 'GET', 1, 'admin', '', '/gen/batchGenCode', '127.0.0.1', '', '', '', 0, '', '2022-06-09 23:33:50');
INSERT INTO `sys_oper_log` VALUES (1535024064706777089, '代码生成', 3, 'com.project.gen.controller.GenController.remove()', 'DELETE', 1, 'admin', '', '/gen/1534911138004185089', '127.0.0.1', '', '', '{\"code\":200,\"msg\":\"操作成功\",\"data\":null}', 0, '', '2022-06-10 06:20:31');
INSERT INTO `sys_oper_log` VALUES (1535024118431617025, '代码生成', 6, 'com.project.gen.controller.GenController.importTableSave()', 'POST', 1, 'admin', '', '/gen/importTable', '127.0.0.1', '', '\"user_occupation_label\"', '{\"code\":200,\"msg\":\"操作成功\",\"data\":null}', 0, '', '2022-06-10 06:20:44');
INSERT INTO `sys_oper_log` VALUES (1535034248883826689, '代码生成', 2, 'com.project.gen.controller.GenController.editSave()', 'PUT', 1, 'admin', '', '/gen', '127.0.0.1', '', '{\"searchValue\":null,\"createBy\":null,\"createTime\":null,\"updateBy\":\"admin\",\"updateTime\":\"2022-06-10 07:00:58\",\"params\":{\"treeCode\":null,\"treeName\":null,\"treeParentCode\":null,\"parentMenuId\":1},\"tableId\":\"1535024117240549377\",\"tableName\":\"user_occupation_label\",\"tableComment\":\"人脉职业标签\",\"subTableName\":null,\"subTableFkName\":null,\"className\":\"UserOccupationLabel\",\"tplCategory\":\"crud\",\"packageName\":\"com.project.system\",\"moduleName\":\"system\",\"businessName\":\"label\",\"functionName\":\"人脉职业标签\",\"functionAuthor\":\"project\",\"genType\":\"0\",\"genPath\":\"/\",\"pkColumn\":null,\"subTable\":null,\"columns\":[{\"searchValue\":null,\"createBy\":\"admin\",\"createTime\":\"2022-06-10 06:20:44\",\"updateBy\":\"admin\",\"updateTime\":\"2022-06-10 07:00:58\",\"params\":{},\"columnId\":\"1535024117991329793\",\"tableId\":\"1535024117240549377\",\"columnName\":\"id\",\"columnComment\":\"ID\",\"columnType\":\"bigint(20)\",\"javaType\":\"Long\",\"javaField\":\"id\",\"isPk\":\"1\",\"isIncrement\":\"1\",\"isRequired\":\"1\",\"isInsert\":null,\"isEdit\":\"1\",\"isList\":\"1\",\"isQuery\":null,\"queryType\":\"EQ\",\"htmlType\":\"input\",\"dictType\":\"\",\"sort\":1,\"required\":true,\"list\":true,\"insert\":false,\"superColumn\":false,\"usableColumn\":false,\"edit\":true,\"pk\":true,\"query\":false,\"increment\":true,\"capJavaField\":\"id\"},{\"searchValue\":null,\"createBy\":\"admin\",\"createTime\":\"2022-06-10 06:20:44\",\"updateBy\":\"admin\",\"updateTime\":\"2022-06-10 07:00:58\",\"params\":{},\"columnId\":\"1535024118008107009\",\"tableId\":\"1535024117240549377\",\"columnName\":\"user_info_id\",\"columnComment\":\"代表当前用户;用户信息ID\",\"columnType\":\"bigint(20)\",\"javaType\":\"Long\",\"javaField\":\"userInfoId\",\"isPk\":\"0\",\"isIncrement\":\"0\",\"isRequired\":\"1\",\"isInsert\":\"1\",\"isEdit\":\"1\",\"isList\":\"1\",\"isQuery\":\"1\",\"queryType\":\"EQ\",\"htmlType\":\"input\",\"dictType\":\"\",\"sort\":2,\"required\":true,\"list\":true,\"insert\":true,\"superColumn\":false,\"usableColumn\":false,\"edit\":true,\"pk\":false,\"query\":true,\"increment\":false,\"capJavaField\":\"userInfoId\"},{\"searchValue\":null,\"createBy\":\"admin\",\"createTime\":\"2022-06-10 06:20:44\",\"updateBy\":\"admin\",\"updateTime\":\"2022-06-10 07:00:58\",\"param', '{\"code\":200,\"msg\":\"操作成功\",\"data\":null}', 0, '', '2022-06-10 07:00:59');
INSERT INTO `sys_oper_log` VALUES (1535034507852738561, '菜单管理', 1, 'com.project.system.controller.SysMenuController.add()', 'POST', 1, 'admin', '', '/menu', '127.0.0.1', '', '{\"searchValue\":null,\"createBy\":\"admin\",\"createTime\":\"2022-06-10 07:02:00\",\"updateBy\":\"admin\",\"updateTime\":\"2022-06-10 07:02:00\",\"params\":{},\"parentName\":null,\"parentId\":1,\"children\":[],\"menuId\":\"1535034507529777153\",\"menuName\":\"标签管理\",\"orderNum\":2,\"path\":\"lable\",\"component\":null,\"queryParam\":null,\"isFrame\":\"1\",\"isCache\":\"0\",\"menuType\":\"C\",\"visible\":\"0\",\"status\":\"0\",\"icon\":\"button\",\"remark\":null}', '{\"code\":200,\"msg\":\"操作成功\",\"data\":null}', 0, '', '2022-06-10 07:02:01');
INSERT INTO `sys_oper_log` VALUES (1535034680620314626, '代码生成', 2, 'com.project.gen.controller.GenController.editSave()', 'PUT', 1, 'admin', '', '/gen', '127.0.0.1', '', '{\"searchValue\":null,\"createBy\":null,\"createTime\":null,\"updateBy\":\"admin\",\"updateTime\":\"2022-06-10 07:02:41\",\"params\":{\"treeCode\":null,\"treeName\":null,\"treeParentCode\":null,\"parentMenuId\":\"1535034507529777153\"},\"tableId\":\"1535024117240549377\",\"tableName\":\"user_occupation_label\",\"tableComment\":\"人脉职业标签\",\"subTableName\":null,\"subTableFkName\":null,\"className\":\"UserOccupationLabel\",\"tplCategory\":\"crud\",\"packageName\":\"com.project.system\",\"moduleName\":\"system\",\"businessName\":\"label\",\"functionName\":\"人脉职业标签\",\"functionAuthor\":\"project\",\"genType\":\"0\",\"genPath\":\"/\",\"pkColumn\":null,\"subTable\":null,\"columns\":[{\"searchValue\":null,\"createBy\":\"admin\",\"createTime\":\"2022-06-10 06:20:44\",\"updateBy\":\"admin\",\"updateTime\":\"2022-06-10 07:02:41\",\"params\":{},\"columnId\":\"1535024117991329793\",\"tableId\":\"1535024117240549377\",\"columnName\":\"id\",\"columnComment\":\"ID\",\"columnType\":\"bigint(20)\",\"javaType\":\"Long\",\"javaField\":\"id\",\"isPk\":\"1\",\"isIncrement\":\"1\",\"isRequired\":\"1\",\"isInsert\":null,\"isEdit\":\"1\",\"isList\":\"1\",\"isQuery\":null,\"queryType\":\"EQ\",\"htmlType\":\"input\",\"dictType\":\"\",\"sort\":1,\"required\":true,\"list\":true,\"insert\":false,\"superColumn\":false,\"usableColumn\":false,\"edit\":true,\"pk\":true,\"query\":false,\"increment\":true,\"capJavaField\":\"id\"},{\"searchValue\":null,\"createBy\":\"admin\",\"createTime\":\"2022-06-10 06:20:44\",\"updateBy\":\"admin\",\"updateTime\":\"2022-06-10 07:02:41\",\"params\":{},\"columnId\":\"1535024118008107009\",\"tableId\":\"1535024117240549377\",\"columnName\":\"user_info_id\",\"columnComment\":\"代表当前用户;用户信息ID\",\"columnType\":\"bigint(20)\",\"javaType\":\"Long\",\"javaField\":\"userInfoId\",\"isPk\":\"0\",\"isIncrement\":\"0\",\"isRequired\":\"1\",\"isInsert\":\"1\",\"isEdit\":\"1\",\"isList\":\"1\",\"isQuery\":\"1\",\"queryType\":\"EQ\",\"htmlType\":\"input\",\"dictType\":\"\",\"sort\":2,\"required\":true,\"list\":true,\"insert\":true,\"superColumn\":false,\"usableColumn\":false,\"edit\":true,\"pk\":false,\"query\":true,\"increment\":false,\"capJavaField\":\"userInfoId\"},{\"searchValue\":null,\"createBy\":\"admin\",\"createTime\":\"2022-06-10 06:20:44\",\"updateBy\":\"admin\",\"updateTime\":\"2022-06', '{\"code\":200,\"msg\":\"操作成功\",\"data\":null}', 0, '', '2022-06-10 07:02:42');
INSERT INTO `sys_oper_log` VALUES (1535034776846036993, '代码生成', 8, 'com.project.gen.controller.GenController.batchGenCode()', 'GET', 1, 'admin', '', '/gen/batchGenCode', '127.0.0.1', '', '', '', 0, '', '2022-06-10 07:03:05');
INSERT INTO `sys_oper_log` VALUES (1535034808949239809, '代码生成', 8, 'com.project.gen.controller.GenController.batchGenCode()', 'GET', 1, 'admin', '', '/gen/batchGenCode', '127.0.0.1', '', '', '', 0, '', '2022-06-10 07:03:13');
INSERT INTO `sys_oper_log` VALUES (1535034829434220546, '代码生成', 8, 'com.project.gen.controller.GenController.batchGenCode()', 'GET', 1, 'admin', '', '/gen/batchGenCode', '127.0.0.1', '', '', '', 0, '', '2022-06-10 07:03:18');
INSERT INTO `sys_oper_log` VALUES (1535042298373976066, '菜单管理', 3, 'com.project.system.controller.SysMenuController.remove()', 'DELETE', 1, 'admin', '', '/menu/1534921719545319429', '127.0.0.1', '', '', '{\"code\":200,\"msg\":\"操作成功\",\"data\":null}', 0, '', '2022-06-10 07:32:58');
INSERT INTO `sys_oper_log` VALUES (1535042306913579009, '菜单管理', 3, 'com.project.system.controller.SysMenuController.remove()', 'DELETE', 1, 'admin', '', '/menu/1534921719545319427', '127.0.0.1', '', '', '{\"code\":200,\"msg\":\"操作成功\",\"data\":null}', 0, '', '2022-06-10 07:33:00');
INSERT INTO `sys_oper_log` VALUES (1535042313918066690, '菜单管理', 3, 'com.project.system.controller.SysMenuController.remove()', 'DELETE', 1, 'admin', '', '/menu/1534921719545319426', '127.0.0.1', '', '', '{\"code\":200,\"msg\":\"操作成功\",\"data\":null}', 0, '', '2022-06-10 07:33:02');
INSERT INTO `sys_oper_log` VALUES (1535042322612858882, '菜单管理', 3, 'com.project.system.controller.SysMenuController.remove()', 'DELETE', 1, 'admin', '', '/menu/1534921719545319425', '127.0.0.1', '', '', '{\"code\":200,\"msg\":\"操作成功\",\"data\":null}', 0, '', '2022-06-10 07:33:04');
INSERT INTO `sys_oper_log` VALUES (1535042332532387841, '菜单管理', 3, 'com.project.system.controller.SysMenuController.remove()', 'DELETE', 1, 'admin', '', '/menu/1534921719545319424', '127.0.0.1', '', '', '{\"code\":500,\"msg\":\"存在子菜单,不允许删除\",\"data\":null}', 0, '', '2022-06-10 07:33:06');
INSERT INTO `sys_oper_log` VALUES (1535042352186896386, '菜单管理', 3, 'com.project.system.controller.SysMenuController.remove()', 'DELETE', 1, 'admin', '', '/menu/1534921719545319428', '127.0.0.1', '', '', '{\"code\":200,\"msg\":\"操作成功\",\"data\":null}', 0, '', '2022-06-10 07:33:11');
INSERT INTO `sys_oper_log` VALUES (1535042360571310081, '菜单管理', 3, 'com.project.system.controller.SysMenuController.remove()', 'DELETE', 1, 'admin', '', '/menu/1534921719545319424', '127.0.0.1', '', '', '{\"code\":200,\"msg\":\"操作成功\",\"data\":null}', 0, '', '2022-06-10 07:33:13');
INSERT INTO `sys_oper_log` VALUES (1535042409342676994, '菜单管理', 3, 'com.project.system.controller.SysMenuController.remove()', 'DELETE', 1, 'admin', '', '/menu/1535034808785694725', '127.0.0.1', '', '', '{\"code\":200,\"msg\":\"操作成功\",\"data\":null}', 0, '', '2022-06-10 07:33:25');
INSERT INTO `sys_oper_log` VALUES (1535042417471238146, '菜单管理', 3, 'com.project.system.controller.SysMenuController.remove()', 'DELETE', 1, 'admin', '', '/menu/1535034808785694722', '127.0.0.1', '', '', '{\"code\":200,\"msg\":\"操作成功\",\"data\":null}', 0, '', '2022-06-10 07:33:27');
INSERT INTO `sys_oper_log` VALUES (1535042425960509441, '菜单管理', 3, 'com.project.system.controller.SysMenuController.remove()', 'DELETE', 1, 'admin', '', '/menu/1535034808785694723', '127.0.0.1', '', '', '{\"code\":200,\"msg\":\"操作成功\",\"data\":null}', 0, '', '2022-06-10 07:33:29');
INSERT INTO `sys_oper_log` VALUES (1535042434307174402, '菜单管理', 3, 'com.project.system.controller.SysMenuController.remove()', 'DELETE', 1, 'admin', '', '/menu/1535034808785694724', '127.0.0.1', '', '', '{\"code\":200,\"msg\":\"操作成功\",\"data\":null}', 0, '', '2022-06-10 07:33:31');
INSERT INTO `sys_oper_log` VALUES (1535042445547909121, '菜单管理', 3, 'com.project.system.controller.SysMenuController.remove()', 'DELETE', 1, 'admin', '', '/menu/1535034808785694720', '127.0.0.1', '', '', '{\"code\":200,\"msg\":\"操作成功\",\"data\":null}', 0, '', '2022-06-10 07:33:33');
INSERT INTO `sys_oper_log` VALUES (1535042457258405890, '菜单管理', 3, 'com.project.system.controller.SysMenuController.remove()', 'DELETE', 1, 'admin', '', '/menu/1535034507529777153', '127.0.0.1', '', '', '{\"code\":200,\"msg\":\"操作成功\",\"data\":null}', 0, '', '2022-06-10 07:33:36');
INSERT INTO `sys_oper_log` VALUES (1535042545129074689, '代码生成', 2, 'com.project.gen.controller.GenController.editSave()', 'PUT', 1, 'admin', '', '/gen', '127.0.0.1', '', '{\"searchValue\":null,\"createBy\":null,\"createTime\":null,\"updateBy\":\"admin\",\"updateTime\":\"2022-06-10 07:33:56\",\"params\":{\"treeCode\":null,\"treeName\":null,\"treeParentCode\":null,\"parentMenuId\":1},\"tableId\":\"1535024117240549377\",\"tableName\":\"user_occupation_label\",\"tableComment\":\"人脉职业标签\",\"subTableName\":null,\"subTableFkName\":null,\"className\":\"UserOccupationLabel\",\"tplCategory\":\"crud\",\"packageName\":\"com.project.system\",\"moduleName\":\"system\",\"businessName\":\"label\",\"functionName\":\"人脉职业标签\",\"functionAuthor\":\"project\",\"genType\":\"0\",\"genPath\":\"/\",\"pkColumn\":null,\"subTable\":null,\"columns\":[{\"searchValue\":null,\"createBy\":\"admin\",\"createTime\":\"2022-06-10 06:20:44\",\"updateBy\":\"admin\",\"updateTime\":\"2022-06-10 07:33:56\",\"params\":{},\"columnId\":\"1535024117991329793\",\"tableId\":\"1535024117240549377\",\"columnName\":\"id\",\"columnComment\":\"ID\",\"columnType\":\"bigint(20)\",\"javaType\":\"Long\",\"javaField\":\"id\",\"isPk\":\"1\",\"isIncrement\":\"1\",\"isRequired\":\"1\",\"isInsert\":null,\"isEdit\":\"1\",\"isList\":\"1\",\"isQuery\":null,\"queryType\":\"EQ\",\"htmlType\":\"input\",\"dictType\":\"\",\"sort\":1,\"required\":true,\"list\":true,\"pk\":true,\"edit\":true,\"usableColumn\":false,\"superColumn\":false,\"insert\":false,\"capJavaField\":\"id\",\"query\":false,\"increment\":true},{\"searchValue\":null,\"createBy\":\"admin\",\"createTime\":\"2022-06-10 06:20:44\",\"updateBy\":\"admin\",\"updateTime\":\"2022-06-10 07:33:56\",\"params\":{},\"columnId\":\"1535024118008107009\",\"tableId\":\"1535024117240549377\",\"columnName\":\"user_info_id\",\"columnComment\":\"代表当前用户;用户信息ID\",\"columnType\":\"bigint(20)\",\"javaType\":\"Long\",\"javaField\":\"userInfoId\",\"isPk\":\"0\",\"isIncrement\":\"0\",\"isRequired\":\"1\",\"isInsert\":\"1\",\"isEdit\":\"1\",\"isList\":\"1\",\"isQuery\":\"1\",\"queryType\":\"EQ\",\"htmlType\":\"input\",\"dictType\":\"\",\"sort\":2,\"required\":true,\"list\":true,\"pk\":false,\"edit\":true,\"usableColumn\":false,\"superColumn\":false,\"insert\":true,\"capJavaField\":\"userInfoId\",\"query\":true,\"increment\":false},{\"searchValue\":null,\"createBy\":\"admin\",\"createTime\":\"2022-06-10 06:20:44\",\"updateBy\":\"admin\",\"updateTime\":\"2022-06-10 07:33:56\",\"param', '{\"code\":200,\"msg\":\"操作成功\",\"data\":null}', 0, '', '2022-06-10 07:33:57');
INSERT INTO `sys_oper_log` VALUES (1535042558190137346, '代码生成', 8, 'com.project.gen.controller.GenController.batchGenCode()', 'GET', 1, 'admin', '', '/gen/batchGenCode', '127.0.0.1', '', '', '', 0, '', '2022-06-10 07:34:00');
INSERT INTO `sys_oper_log` VALUES (1535042592176582657, '代码生成', 8, 'com.project.gen.controller.GenController.batchGenCode()', 'GET', 1, 'admin', '', '/gen/batchGenCode', '127.0.0.1', '', '', '', 0, '', '2022-06-10 07:34:08');
INSERT INTO `sys_oper_log` VALUES (1535207284303859713, '人脉职业标签', 1, 'com.project.system.controller.UserOccupationLabelController.add()', 'POST', 1, 'admin', '', '/label', '127.0.0.1', '', '{\"searchValue\":\"2\",\"createBy\":null,\"createTime\":null,\"updateBy\":null,\"updateTime\":null,\"params\":{},\"id\":\"1535207283829903362\",\"userInfoId\":12,\"occupationLabel\":\"很棒\",\"deleted\":0}', '{\"code\":200,\"msg\":\"操作成功\",\"data\":null}', 0, '', '2022-06-10 18:28:34');
INSERT INTO `sys_oper_log` VALUES (1535209575408865282, '菜单管理', 2, 'com.project.system.controller.SysMenuController.edit()', 'PUT', 1, 'admin', '', '/menu', '127.0.0.1', '', '{\"searchValue\":null,\"createBy\":\"admin\",\"createTime\":\"2022-06-09 21:43:46\",\"updateBy\":\"admin\",\"updateTime\":\"2022-06-10 18:37:40\",\"params\":{},\"parentName\":null,\"parentId\":0,\"children\":[],\"menuId\":\"1534894022110633986\",\"menuName\":\"你好同学\",\"orderNum\":0,\"path\":\"admin\",\"component\":null,\"queryParam\":null,\"isFrame\":\"1\",\"isCache\":\"0\",\"menuType\":\"M\",\"visible\":\"0\",\"status\":\"0\",\"icon\":\"build\",\"remark\":\"\"}', '{\"code\":200,\"msg\":\"操作成功\",\"data\":null}', 0, '', '2022-06-10 18:37:40');
INSERT INTO `sys_oper_log` VALUES (1535209611215638530, '菜单管理', 2, 'com.project.system.controller.SysMenuController.edit()', 'PUT', 1, 'admin', '', '/menu', '127.0.0.1', '', '{\"searchValue\":null,\"createBy\":\"admin\",\"createTime\":\"2022-05-31 06:31:32\",\"updateBy\":\"admin\",\"updateTime\":\"2022-06-10 18:37:48\",\"params\":{},\"parentName\":null,\"parentId\":0,\"children\":[],\"menuId\":1,\"menuName\":\"系统管理\",\"orderNum\":1,\"path\":\"system\",\"component\":null,\"queryParam\":\"\",\"isFrame\":\"1\",\"isCache\":\"0\",\"menuType\":\"M\",\"visible\":\"0\",\"status\":\"0\",\"perms\":\"\",\"icon\":\"system\",\"remark\":\"系统管理目录\"}', '{\"code\":200,\"msg\":\"操作成功\",\"data\":null}', 0, '', '2022-06-10 18:37:49');
INSERT INTO `sys_oper_log` VALUES (1535209631079862273, '菜单管理', 2, 'com.project.system.controller.SysMenuController.edit()', 'PUT', 1, 'admin', '', '/menu', '127.0.0.1', '', '{\"searchValue\":null,\"createBy\":\"admin\",\"createTime\":\"2022-06-09 21:43:46\",\"updateBy\":\"admin\",\"updateTime\":\"2022-06-10 18:37:53\",\"params\":{},\"parentName\":null,\"parentId\":0,\"children\":[],\"menuId\":\"1534894022110633986\",\"menuName\":\"你好同学\",\"orderNum\":0,\"path\":\"admin\",\"component\":null,\"queryParam\":null,\"isFrame\":\"1\",\"isCache\":\"0\",\"menuType\":\"M\",\"visible\":\"0\",\"status\":\"0\",\"icon\":\"build\",\"remark\":\"\"}', '{\"code\":200,\"msg\":\"操作成功\",\"data\":null}', 0, '', '2022-06-10 18:37:54');
INSERT INTO `sys_oper_log` VALUES (1535209717755154433, '代码生成', 6, 'com.project.gen.controller.GenController.importTableSave()', 'POST', 1, 'admin', '', '/gen/importTable', '127.0.0.1', '', '\"user_education\"', '{\"code\":200,\"msg\":\"操作成功\",\"data\":null}', 0, '', '2022-06-10 18:38:14');
INSERT INTO `sys_oper_log` VALUES (1535210647229698050, '代码生成', 2, 'com.project.gen.controller.GenController.editSave()', 'PUT', 1, 'admin', '', '/gen', '127.0.0.1', '', '{\"searchValue\":null,\"createBy\":null,\"createTime\":null,\"updateBy\":\"admin\",\"updateTime\":\"2022-06-10 18:41:55\",\"params\":{\"treeCode\":null,\"treeName\":null,\"treeParentCode\":null,\"parentMenuId\":\"1534894022110633986\"},\"tableId\":\"1535209716031397890\",\"tableName\":\"user_education\",\"tableComment\":\"学历\",\"subTableName\":null,\"subTableFkName\":null,\"className\":\"UserEducation\",\"tplCategory\":\"crud\",\"packageName\":\"com.project.admin\",\"moduleName\":\"admin\",\"businessName\":\"education\",\"functionName\":\"学历管理\",\"functionAuthor\":\"project\",\"genType\":\"0\",\"genPath\":\"/\",\"pkColumn\":null,\"subTable\":null,\"columns\":[{\"searchValue\":null,\"createBy\":\"admin\",\"createTime\":\"2022-06-10 18:38:14\",\"updateBy\":\"admin\",\"updateTime\":\"2022-06-10 18:41:55\",\"params\":{},\"columnId\":\"1535209716475994114\",\"tableId\":\"1535209716031397890\",\"columnName\":\"id\",\"columnComment\":\"学历ID\",\"columnType\":\"bigint(20)\",\"javaType\":\"Long\",\"javaField\":\"id\",\"isPk\":\"1\",\"isIncrement\":\"1\",\"isRequired\":\"1\",\"isInsert\":null,\"isEdit\":\"1\",\"isList\":\"1\",\"isQuery\":null,\"queryType\":\"EQ\",\"htmlType\":\"input\",\"dictType\":\"\",\"sort\":1,\"required\":true,\"list\":true,\"usableColumn\":false,\"edit\":true,\"insert\":false,\"pk\":true,\"superColumn\":false,\"capJavaField\":\"id\",\"increment\":true,\"query\":false},{\"searchValue\":null,\"createBy\":\"admin\",\"createTime\":\"2022-06-10 18:38:14\",\"updateBy\":\"admin\",\"updateTime\":\"2022-06-10 18:41:55\",\"params\":{},\"columnId\":\"1535209716484382721\",\"tableId\":\"1535209716031397890\",\"columnName\":\"user_info_id\",\"columnComment\":\"代表当前用户;用户信息ID\",\"columnType\":\"bigint(20)\",\"javaType\":\"Long\",\"javaField\":\"userInfoId\",\"isPk\":\"0\",\"isIncrement\":\"0\",\"isRequired\":\"1\",\"isInsert\":\"1\",\"isEdit\":\"1\",\"isList\":\"1\",\"isQuery\":null,\"queryType\":\"EQ\",\"htmlType\":\"input\",\"dictType\":\"\",\"sort\":2,\"required\":true,\"list\":true,\"usableColumn\":false,\"edit\":true,\"insert\":true,\"pk\":false,\"superColumn\":false,\"capJavaField\":\"userInfoId\",\"increment\":false,\"query\":false},{\"searchValue\":null,\"createBy\":\"admin\",\"createTime\":\"2022-06-10 18:38:14\",\"updateBy\":\"admin\",\"updateTime\":\"2022-06-10 18:41:55\"', '{\"code\":200,\"msg\":\"操作成功\",\"data\":null}', 0, '', '2022-06-10 18:41:56');
INSERT INTO `sys_oper_log` VALUES (1535210697569734658, '代码生成', 8, 'com.project.gen.controller.GenController.batchGenCode()', 'GET', 1, 'admin', '', '/gen/batchGenCode', '127.0.0.1', '', '', '', 0, '', '2022-06-10 18:42:08');
INSERT INTO `sys_oper_log` VALUES (1535212929639911425, '人脉职业标签', 1, 'com.project.system.controller.UserOccupationLabelController.add()', 'POST', 1, 'admin', '', '/label', '127.0.0.1', '', '{\"searchValue\":\"2332\",\"createBy\":null,\"createTime\":null,\"updateBy\":null,\"updateTime\":null,\"params\":{},\"id\":\"1535212929455362050\",\"userInfoId\":3322,\"occupationLabel\":\"游戏人生\",\"deleted\":0}', '{\"code\":200,\"msg\":\"操作成功\",\"data\":null}', 0, '', '2022-06-10 18:51:00');
INSERT INTO `sys_oper_log` VALUES (1535213489243951105, '学历管理', 1, 'com.project.admin.controller.UserEducationController.add()', 'POST', 1, 'admin', '', '/education', '127.0.0.1', '', '{\"searchValue\":\"1\",\"createBy\":null,\"createTime\":null,\"updateBy\":null,\"updateTime\":null,\"params\":{},\"id\":null,\"userInfoId\":11,\"name\":\"华中科技大学\",\"admissionTime\":\"2013-09-01 18:51:48\",\"graduationTime\":\"2017-01-01 00:00:00\",\"major\":1,\"education\":1,\"introduction\":\"网络教育\",\"deleted\":null}', '', 1, '\r\n### Error updating database.  Cause: java.sql.SQLSyntaxErrorException: Unknown column \'admission_time\' in \'field list\'\r\n### The error may exist in com/project/admin/mapper/UserEducationMapper.java (best guess)\r\n### The error may involve com.project.admin.mapper.UserEducationMapper.insert-Inline\r\n### The error occurred while setting parameters\r\n### SQL: INSERT INTO user_education  ( id, user_info_id, name, admission_time, graduation_time, major, education, introduction, search_value,  create_by, create_time, update_by, update_time )  VALUES  ( ?, ?, ?, ?, ?, ?, ?, ?, ?,  ?, ?, ?, ? )\r\n### Cause: java.sql.SQLSyntaxErrorException: Unknown column \'admission_time\' in \'field list\'\n; bad SQL grammar []; nested exception is java.sql.SQLSyntaxErrorException: Unknown column \'admission_time\' in \'field list\'', '2022-06-10 18:53:13');
INSERT INTO `sys_oper_log` VALUES (1535213998528925697, '学历管理', 1, 'com.project.admin.controller.UserEducationController.add()', 'POST', 1, 'admin', '', '/education', '127.0.0.1', '', '{\"searchValue\":\"1\",\"createBy\":null,\"createTime\":null,\"updateBy\":null,\"updateTime\":null,\"params\":{},\"id\":null,\"userInfoId\":11,\"name\":\"华中科技大学\",\"admissionTime\":\"2013-09-01 18:51:48\",\"graduationTime\":\"2017-01-01 00:00:00\",\"major\":1,\"education\":1,\"introduction\":\"网络教育\",\"deleted\":null}', '', 1, '\r\n### Error updating database.  Cause: java.sql.SQLSyntaxErrorException: Unknown column \'admission_time\' in \'field list\'\r\n### The error may exist in com/project/admin/mapper/UserEducationMapper.java (best guess)\r\n### The error may involve com.project.admin.mapper.UserEducationMapper.insert-Inline\r\n### The error occurred while setting parameters\r\n### SQL: INSERT INTO user_education  ( id, user_info_id, name, admission_time, graduation_time, major, education, introduction, search_value,  create_by, create_time, update_by, update_time )  VALUES  ( ?, ?, ?, ?, ?, ?, ?, ?, ?,  ?, ?, ?, ? )\r\n### Cause: java.sql.SQLSyntaxErrorException: Unknown column \'admission_time\' in \'field list\'\n; bad SQL grammar []; nested exception is java.sql.SQLSyntaxErrorException: Unknown column \'admission_time\' in \'field list\'', '2022-06-10 18:55:15');
INSERT INTO `sys_oper_log` VALUES (1535214261230768130, '学历管理', 1, 'com.project.admin.controller.UserEducationController.add()', 'POST', 1, 'admin', '', '/education', '127.0.0.1', '', '{\"searchValue\":\"1\",\"createBy\":null,\"createTime\":null,\"updateBy\":null,\"updateTime\":null,\"params\":{},\"id\":null,\"userInfoId\":11,\"name\":\"华中科技大学\",\"admissionTime\":\"2013-09-01 18:51:48\",\"graduationTime\":\"2017-01-01 00:00:00\",\"major\":1,\"education\":1,\"introduction\":\"网络教育\",\"deleted\":null}', '', 1, '\r\n### Error updating database.  Cause: java.sql.SQLSyntaxErrorException: Unknown column \'admission_time\' in \'field list\'\r\n### The error may exist in com/project/admin/mapper/UserEducationMapper.java (best guess)\r\n### The error may involve com.project.admin.mapper.UserEducationMapper.insert-Inline\r\n### The error occurred while setting parameters\r\n### SQL: INSERT INTO user_education  ( id, user_info_id, name, admission_time, graduation_time, major, education, introduction, search_value,  create_by, create_time, update_by, update_time )  VALUES  ( ?, ?, ?, ?, ?, ?, ?, ?, ?,  ?, ?, ?, ? )\r\n### Cause: java.sql.SQLSyntaxErrorException: Unknown column \'admission_time\' in \'field list\'\n; bad SQL grammar []; nested exception is java.sql.SQLSyntaxErrorException: Unknown column \'admission_time\' in \'field list\'', '2022-06-10 18:56:17');
INSERT INTO `sys_oper_log` VALUES (1535214927751811074, '代码生成', 2, 'com.project.gen.controller.GenController.editSave()', 'PUT', 1, 'admin', '', '/gen', '127.0.0.1', '', '{\"searchValue\":null,\"createBy\":null,\"createTime\":null,\"updateBy\":\"admin\",\"updateTime\":\"2022-06-10 18:58:55\",\"params\":{\"treeCode\":null,\"treeName\":null,\"treeParentCode\":null,\"parentMenuId\":\"1534894022110633986\"},\"tableId\":\"1535209716031397890\",\"tableName\":\"user_education\",\"tableComment\":\"学历\",\"subTableName\":null,\"subTableFkName\":null,\"className\":\"UserEducation\",\"tplCategory\":\"crud\",\"packageName\":\"com.project.admin\",\"moduleName\":\"admin\",\"businessName\":\"education\",\"functionName\":\"学历管理\",\"functionAuthor\":\"project\",\"genType\":\"0\",\"genPath\":\"/\",\"pkColumn\":null,\"subTable\":null,\"columns\":[{\"searchValue\":null,\"createBy\":\"admin\",\"createTime\":\"2022-06-10 18:38:14\",\"updateBy\":\"admin\",\"updateTime\":\"2022-06-10 18:58:55\",\"params\":{},\"columnId\":\"1535209716475994114\",\"tableId\":\"1535209716031397890\",\"columnName\":\"id\",\"columnComment\":\"学历ID\",\"columnType\":\"bigint(20)\",\"javaType\":\"Long\",\"javaField\":\"id\",\"isPk\":\"1\",\"isIncrement\":\"1\",\"isRequired\":\"1\",\"isInsert\":null,\"isEdit\":\"1\",\"isList\":\"1\",\"isQuery\":null,\"queryType\":\"EQ\",\"htmlType\":\"input\",\"dictType\":\"\",\"sort\":1,\"required\":true,\"list\":true,\"usableColumn\":false,\"edit\":true,\"insert\":false,\"pk\":true,\"superColumn\":false,\"capJavaField\":\"id\",\"increment\":true,\"query\":false},{\"searchValue\":null,\"createBy\":\"admin\",\"createTime\":\"2022-06-10 18:38:14\",\"updateBy\":\"admin\",\"updateTime\":\"2022-06-10 18:58:55\",\"params\":{},\"columnId\":\"1535209716484382721\",\"tableId\":\"1535209716031397890\",\"columnName\":\"user_info_id\",\"columnComment\":\"用户ID\",\"columnType\":\"bigint(20)\",\"javaType\":\"Long\",\"javaField\":\"userInfoId\",\"isPk\":\"0\",\"isIncrement\":\"0\",\"isRequired\":\"1\",\"isInsert\":\"1\",\"isEdit\":\"1\",\"isList\":\"1\",\"isQuery\":null,\"queryType\":\"EQ\",\"htmlType\":\"input\",\"dictType\":\"\",\"sort\":2,\"required\":true,\"list\":true,\"usableColumn\":false,\"edit\":true,\"insert\":true,\"pk\":false,\"superColumn\":false,\"capJavaField\":\"userInfoId\",\"increment\":false,\"query\":false},{\"searchValue\":null,\"createBy\":\"admin\",\"createTime\":\"2022-06-10 18:38:14\",\"updateBy\":\"admin\",\"updateTime\":\"2022-06-10 18:58:56\",\"params\"', '{\"code\":200,\"msg\":\"操作成功\",\"data\":null}', 0, '', '2022-06-10 18:58:56');
INSERT INTO `sys_oper_log` VALUES (1535215424596480002, '菜单管理', 2, 'com.project.system.controller.SysMenuController.edit()', 'PUT', 1, 'admin', '', '/menu', '127.0.0.1', '', '{\"searchValue\":null,\"createBy\":\"admin\",\"createTime\":\"2022-06-10 07:34:36\",\"updateBy\":\"admin\",\"updateTime\":\"2022-06-10 19:00:54\",\"params\":{},\"parentName\":null,\"parentId\":1,\"children\":[],\"menuId\":\"1535042592084262912\",\"menuName\":\"人脉职业标签\",\"orderNum\":1,\"path\":\"label\",\"component\":\"system/label/index\",\"queryParam\":null,\"isFrame\":\"1\",\"isCache\":\"0\",\"menuType\":\"C\",\"visible\":\"0\",\"status\":\"0\",\"perms\":\"system:label:list\",\"icon\":\"chart\",\"remark\":\"人脉职业标签菜单\"}', '{\"code\":200,\"msg\":\"操作成功\",\"data\":null}', 0, '', '2022-06-10 19:00:55');
INSERT INTO `sys_oper_log` VALUES (1535215722496921602, '菜单管理', 2, 'com.project.system.controller.SysMenuController.edit()', 'PUT', 1, 'admin', '', '/menu', '127.0.0.1', '', '{\"searchValue\":null,\"createBy\":\"admin\",\"createTime\":\"2022-06-10 18:42:36\",\"updateBy\":\"admin\",\"updateTime\":\"2022-06-10 19:02:05\",\"params\":{},\"parentName\":null,\"parentId\":\"1534894022110633986\",\"children\":[],\"menuId\":\"1535210696680644608\",\"menuName\":\"学历管理\",\"orderNum\":1,\"path\":\"education\",\"component\":\"admin/education/index\",\"queryParam\":null,\"isFrame\":\"1\",\"isCache\":\"0\",\"menuType\":\"C\",\"visible\":\"0\",\"status\":\"0\",\"perms\":\"admin:education:list\",\"icon\":\"user\",\"remark\":\"学历管理菜单\"}', '{\"code\":200,\"msg\":\"操作成功\",\"data\":null}', 0, '', '2022-06-10 19:02:06');
INSERT INTO `sys_oper_log` VALUES (1535215778859978753, '菜单管理', 2, 'com.project.system.controller.SysMenuController.edit()', 'PUT', 1, 'admin', '', '/menu', '127.0.0.1', '', '{\"searchValue\":null,\"createBy\":\"admin\",\"createTime\":\"2022-05-31 06:31:32\",\"updateBy\":\"admin\",\"updateTime\":\"2022-06-10 19:02:19\",\"params\":{},\"parentName\":null,\"parentId\":1,\"children\":[],\"menuId\":100,\"menuName\":\"用户管理\",\"orderNum\":1,\"path\":\"user\",\"component\":\"system/user/index\",\"queryParam\":\"\",\"isFrame\":\"1\",\"isCache\":\"0\",\"menuType\":\"C\",\"visible\":\"0\",\"status\":\"0\",\"perms\":\"system:user:list\",\"icon\":\"user\",\"remark\":\"用户管理菜单\"}', '{\"code\":200,\"msg\":\"操作成功\",\"data\":null}', 0, '', '2022-06-10 19:02:19');
INSERT INTO `sys_oper_log` VALUES (1535215836405829634, '菜单管理', 2, 'com.project.system.controller.SysMenuController.edit()', 'PUT', 1, 'admin', '', '/menu', '127.0.0.1', '', '{\"searchValue\":null,\"createBy\":\"admin\",\"createTime\":\"2022-06-10 18:42:36\",\"updateBy\":\"admin\",\"updateTime\":\"2022-06-10 19:02:32\",\"params\":{},\"parentName\":null,\"parentId\":\"1534894022110633986\",\"children\":[],\"menuId\":\"1535210696680644608\",\"menuName\":\"学历管理\",\"orderNum\":1,\"path\":\"education\",\"component\":\"admin/education/index\",\"queryParam\":null,\"isFrame\":\"1\",\"isCache\":\"0\",\"menuType\":\"C\",\"visible\":\"0\",\"status\":\"0\",\"perms\":\"admin:education:list\",\"icon\":\"user\",\"remark\":\"学历管理菜单\"}', '{\"code\":200,\"msg\":\"操作成功\",\"data\":null}', 0, '', '2022-06-10 19:02:33');
INSERT INTO `sys_oper_log` VALUES (1535216119294857218, '学历管理', 1, 'com.project.admin.controller.UserEducationController.add()', 'POST', 1, 'admin', '', '/education', '127.0.0.1', '', '{\"searchValue\":\"11\",\"createBy\":null,\"createTime\":null,\"updateBy\":null,\"updateTime\":null,\"params\":{},\"id\":null,\"userInfoId\":12,\"name\":\"华中科技大学\",\"admissionTime\":\"2022-06-10 19:03:31\",\"graduationTime\":\"2022-06-10 19:03:33\",\"major\":1,\"education\":2,\"introduction\":\"1122\",\"deleted\":null}', '', 1, '\r\n### Error updating database.  Cause: java.sql.SQLSyntaxErrorException: Unknown column \'admission_time\' in \'field list\'\r\n### The error may exist in com/project/admin/mapper/UserEducationMapper.java (best guess)\r\n### The error may involve com.project.admin.mapper.UserEducationMapper.insert-Inline\r\n### The error occurred while setting parameters\r\n### SQL: INSERT INTO user_education  ( id, user_info_id, name, admission_time, graduation_time, major, education, introduction, search_value,  create_by, create_time, update_by, update_time )  VALUES  ( ?, ?, ?, ?, ?, ?, ?, ?, ?,  ?, ?, ?, ? )\r\n### Cause: java.sql.SQLSyntaxErrorException: Unknown column \'admission_time\' in \'field list\'\n; bad SQL grammar []; nested exception is java.sql.SQLSyntaxErrorException: Unknown column \'admission_time\' in \'field list\'', '2022-06-10 19:03:40');
INSERT INTO `sys_oper_log` VALUES (1535216147711266817, '学历管理', 1, 'com.project.admin.controller.UserEducationController.add()', 'POST', 1, 'admin', '', '/education', '127.0.0.1', '', '{\"searchValue\":\"11\",\"createBy\":null,\"createTime\":null,\"updateBy\":null,\"updateTime\":null,\"params\":{},\"id\":null,\"userInfoId\":12,\"name\":\"华中科技大学\",\"admissionTime\":\"2022-06-10 19:03:31\",\"graduationTime\":\"2022-06-10 19:03:33\",\"major\":1,\"education\":2,\"introduction\":\"1122\",\"deleted\":null}', '', 1, '\r\n### Error updating database.  Cause: java.sql.SQLSyntaxErrorException: Unknown column \'admission_time\' in \'field list\'\r\n### The error may exist in com/project/admin/mapper/UserEducationMapper.java (best guess)\r\n### The error may involve com.project.admin.mapper.UserEducationMapper.insert-Inline\r\n### The error occurred while setting parameters\r\n### SQL: INSERT INTO user_education  ( id, user_info_id, name, admission_time, graduation_time, major, education, introduction, search_value,  create_by, create_time, update_by, update_time )  VALUES  ( ?, ?, ?, ?, ?, ?, ?, ?, ?,  ?, ?, ?, ? )\r\n### Cause: java.sql.SQLSyntaxErrorException: Unknown column \'admission_time\' in \'field list\'\n; bad SQL grammar []; nested exception is java.sql.SQLSyntaxErrorException: Unknown column \'admission_time\' in \'field list\'', '2022-06-10 19:03:47');
INSERT INTO `sys_oper_log` VALUES (1535216326678024194, '学历管理', 1, 'com.project.admin.controller.UserEducationController.add()', 'POST', 1, 'admin', '', '/education', '127.0.0.1', '', '{\"searchValue\":\"11\",\"createBy\":null,\"createTime\":null,\"updateBy\":null,\"updateTime\":null,\"params\":{},\"id\":null,\"userInfoId\":12,\"name\":\"华中科技大学\",\"admissionTime\":\"2022-06-10 19:03:31\",\"graduationTime\":\"2022-06-10 19:03:33\",\"major\":1,\"education\":2,\"introduction\":\"1122\",\"deleted\":null}', '', 1, '\r\n### Error updating database.  Cause: java.sql.SQLSyntaxErrorException: Unknown column \'admission_time\' in \'field list\'\r\n### The error may exist in com/project/admin/mapper/UserEducationMapper.java (best guess)\r\n### The error may involve com.project.admin.mapper.UserEducationMapper.insert-Inline\r\n### The error occurred while setting parameters\r\n### SQL: INSERT INTO user_education  ( id, user_info_id, name, admission_time, graduation_time, major, education, introduction, search_value,  create_by, create_time, update_by, update_time )  VALUES  ( ?, ?, ?, ?, ?, ?, ?, ?, ?,  ?, ?, ?, ? )\r\n### Cause: java.sql.SQLSyntaxErrorException: Unknown column \'admission_time\' in \'field list\'\n; bad SQL grammar []; nested exception is java.sql.SQLSyntaxErrorException: Unknown column \'admission_time\' in \'field list\'', '2022-06-10 19:04:30');
INSERT INTO `sys_oper_log` VALUES (1535218652172128257, '菜单管理', 3, 'com.project.system.controller.SysMenuController.remove()', 'DELETE', 1, 'admin', '', '/menu/1535210696680644613', '127.0.0.1', '', '', '{\"code\":200,\"msg\":\"操作成功\",\"data\":null}', 0, '', '2022-06-10 19:13:44');
INSERT INTO `sys_oper_log` VALUES (1535218660766257153, '菜单管理', 3, 'com.project.system.controller.SysMenuController.remove()', 'DELETE', 1, 'admin', '', '/menu/1535210696680644612', '127.0.0.1', '', '', '{\"code\":200,\"msg\":\"操作成功\",\"data\":null}', 0, '', '2022-06-10 19:13:46');
INSERT INTO `sys_oper_log` VALUES (1535218668425056258, '菜单管理', 3, 'com.project.system.controller.SysMenuController.remove()', 'DELETE', 1, 'admin', '', '/menu/1535210696680644610', '127.0.0.1', '', '', '{\"code\":200,\"msg\":\"操作成功\",\"data\":null}', 0, '', '2022-06-10 19:13:48');
INSERT INTO `sys_oper_log` VALUES (1535218674196418562, '菜单管理', 3, 'com.project.system.controller.SysMenuController.remove()', 'DELETE', 1, 'admin', '', '/menu/1535210696680644609', '127.0.0.1', '', '', '{\"code\":200,\"msg\":\"操作成功\",\"data\":null}', 0, '', '2022-06-10 19:13:50');
INSERT INTO `sys_oper_log` VALUES (1535218680764698625, '菜单管理', 3, 'com.project.system.controller.SysMenuController.remove()', 'DELETE', 1, 'admin', '', '/menu/1535210696680644608', '127.0.0.1', '', '', '{\"code\":500,\"msg\":\"存在子菜单,不允许删除\",\"data\":null}', 0, '', '2022-06-10 19:13:51');
INSERT INTO `sys_oper_log` VALUES (1535218687781769218, '菜单管理', 3, 'com.project.system.controller.SysMenuController.remove()', 'DELETE', 1, 'admin', '', '/menu/1535210696680644611', '127.0.0.1', '', '', '{\"code\":200,\"msg\":\"操作成功\",\"data\":null}', 0, '', '2022-06-10 19:13:53');
INSERT INTO `sys_oper_log` VALUES (1535218702369558529, '菜单管理', 3, 'com.project.system.controller.SysMenuController.remove()', 'DELETE', 1, 'admin', '', '/menu/1535210696680644608', '127.0.0.1', '', '', '{\"code\":200,\"msg\":\"操作成功\",\"data\":null}', 0, '', '2022-06-10 19:13:56');
INSERT INTO `sys_oper_log` VALUES (1535218927507214338, '菜单管理', 1, 'com.project.system.controller.SysMenuController.add()', 'POST', 1, 'admin', '', '/menu', '127.0.0.1', '', '{\"searchValue\":null,\"createBy\":\"admin\",\"createTime\":\"2022-06-10 19:14:49\",\"updateBy\":\"admin\",\"updateTime\":\"2022-06-10 19:14:49\",\"params\":{},\"parentName\":null,\"parentId\":\"1534894022110633986\",\"children\":[],\"menuId\":\"1535218927335247873\",\"menuName\":\"我的人脉\",\"orderNum\":1,\"path\":\"contact\",\"component\":null,\"queryParam\":null,\"isFrame\":\"1\",\"isCache\":\"0\",\"menuType\":\"M\",\"visible\":\"0\",\"status\":\"0\",\"icon\":\"github\",\"remark\":null}', '{\"code\":200,\"msg\":\"操作成功\",\"data\":null}', 0, '', '2022-06-10 19:14:50');
INSERT INTO `sys_oper_log` VALUES (1535218955428696065, '菜单管理', 2, 'com.project.system.controller.SysMenuController.edit()', 'PUT', 1, 'admin', '', '/menu', '127.0.0.1', '', '{\"searchValue\":null,\"createBy\":\"admin\",\"createTime\":\"2022-06-09 21:43:46\",\"updateBy\":\"admin\",\"updateTime\":\"2022-06-10 19:14:56\",\"params\":{},\"parentName\":null,\"parentId\":0,\"children\":[],\"menuId\":\"1534894022110633986\",\"menuName\":\"你好同学\",\"orderNum\":0,\"path\":\"admin\",\"component\":null,\"queryParam\":null,\"isFrame\":\"1\",\"isCache\":\"0\",\"menuType\":\"M\",\"visible\":\"0\",\"status\":\"0\",\"icon\":\"build\",\"remark\":\"\"}', '{\"code\":200,\"msg\":\"操作成功\",\"data\":null}', 0, '', '2022-06-10 19:14:57');
INSERT INTO `sys_oper_log` VALUES (1535219008956403714, '菜单管理', 2, 'com.project.system.controller.SysMenuController.edit()', 'PUT', 1, 'admin', '', '/menu', '127.0.0.1', '', '{\"searchValue\":null,\"createBy\":\"admin\",\"createTime\":\"2022-06-10 19:14:50\",\"updateBy\":\"admin\",\"updateTime\":\"2022-06-10 19:15:09\",\"params\":{},\"parentName\":null,\"parentId\":\"1534894022110633986\",\"children\":[],\"menuId\":\"1535218927335247873\",\"menuName\":\"我的人脉\",\"orderNum\":1,\"path\":\"contact\",\"component\":null,\"queryParam\":null,\"isFrame\":\"1\",\"isCache\":\"0\",\"menuType\":\"M\",\"visible\":\"0\",\"status\":\"0\",\"icon\":\"github\",\"remark\":\"\"}', '{\"code\":200,\"msg\":\"操作成功\",\"data\":null}', 0, '', '2022-06-10 19:15:09');
INSERT INTO `sys_oper_log` VALUES (1535219092175589378, '代码生成', 6, 'com.project.gen.controller.GenController.importTableSave()', 'POST', 1, 'admin', '', '/gen/importTable', '127.0.0.1', '', '\"user_interested_to_me\"', '{\"code\":200,\"msg\":\"操作成功\",\"data\":null}', 0, '', '2022-06-10 19:15:29');
INSERT INTO `sys_oper_log` VALUES (1535219786450341890, '代码生成', 2, 'com.project.gen.controller.GenController.editSave()', 'PUT', 1, 'admin', '', '/gen', '127.0.0.1', '', '{\"searchValue\":null,\"createBy\":null,\"createTime\":null,\"updateBy\":\"admin\",\"updateTime\":\"2022-06-10 19:18:14\",\"params\":{\"treeCode\":null,\"treeName\":null,\"treeParentCode\":null,\"parentMenuId\":\"1535218927335247873\"},\"tableId\":\"1535219091211001857\",\"tableName\":\"user_interested_to_me\",\"tableComment\":\"对我感兴趣（收藏人脉卡片）\",\"subTableName\":null,\"subTableFkName\":null,\"className\":\"UserInterestedToMe\",\"tplCategory\":\"crud\",\"packageName\":\"com.project.admin.contact\",\"moduleName\":\"admin\",\"businessName\":\"interested-tome\",\"functionName\":\"对我感兴趣\",\"functionAuthor\":\"huan.li\",\"genType\":\"0\",\"genPath\":\"/\",\"pkColumn\":null,\"subTable\":null,\"columns\":[{\"searchValue\":null,\"createBy\":\"admin\",\"createTime\":\"2022-06-10 19:15:29\",\"updateBy\":\"admin\",\"updateTime\":\"2022-06-10 19:18:14\",\"params\":{},\"columnId\":\"1535219091559129090\",\"tableId\":\"1535219091211001857\",\"columnName\":\"id\",\"columnComment\":\"ID\",\"columnType\":\"bigint(20)\",\"javaType\":\"Long\",\"javaField\":\"id\",\"isPk\":\"1\",\"isIncrement\":\"1\",\"isRequired\":\"1\",\"isInsert\":null,\"isEdit\":\"1\",\"isList\":\"1\",\"isQuery\":null,\"queryType\":\"EQ\",\"htmlType\":\"input\",\"dictType\":\"\",\"sort\":1,\"required\":true,\"list\":true,\"usableColumn\":false,\"edit\":true,\"insert\":false,\"pk\":true,\"superColumn\":false,\"capJavaField\":\"id\",\"increment\":true,\"query\":false},{\"searchValue\":null,\"createBy\":\"admin\",\"createTime\":\"2022-06-10 19:15:29\",\"updateBy\":\"admin\",\"updateTime\":\"2022-06-10 19:18:14\",\"params\":{},\"columnId\":\"1535219091559129091\",\"tableId\":\"1535219091211001857\",\"columnName\":\"user_info_id\",\"columnComment\":\"当前用户\",\"columnType\":\"bigint(20)\",\"javaType\":\"Long\",\"javaField\":\"userInfoId\",\"isPk\":\"0\",\"isIncrement\":\"0\",\"isRequired\":\"1\",\"isInsert\":\"1\",\"isEdit\":\"1\",\"isList\":\"1\",\"isQuery\":\"1\",\"queryType\":\"EQ\",\"htmlType\":\"input\",\"dictType\":\"\",\"sort\":2,\"required\":true,\"list\":true,\"usableColumn\":false,\"edit\":true,\"insert\":true,\"pk\":false,\"superColumn\":false,\"capJavaField\":\"userInfoId\",\"increment\":false,\"query\":true},{\"searchValue\":null,\"createBy\":\"admin\",\"createTime\":\"2022-06-10 19:15:29\",\"updateBy\":\"admin\",\"updateTi', '{\"code\":200,\"msg\":\"操作成功\",\"data\":null}', 0, '', '2022-06-10 19:18:15');
INSERT INTO `sys_oper_log` VALUES (1535219822101925889, '代码生成', 8, 'com.project.gen.controller.GenController.batchGenCode()', 'GET', 1, 'admin', '', '/gen/batchGenCode', '127.0.0.1', '', '', '', 0, '', '2022-06-10 19:18:23');
INSERT INTO `sys_oper_log` VALUES (1535220639823433730, '代码生成', 2, 'com.project.gen.controller.GenController.editSave()', 'PUT', 1, 'admin', '', '/gen', '127.0.0.1', '', '{\"searchValue\":null,\"createBy\":null,\"createTime\":null,\"updateBy\":\"admin\",\"updateTime\":\"2022-06-10 19:21:37\",\"params\":{\"treeCode\":null,\"treeName\":null,\"treeParentCode\":null,\"parentMenuId\":\"1535218927335247873\"},\"tableId\":\"1535219091211001857\",\"tableName\":\"user_interested_to_me\",\"tableComment\":\"对我感兴趣（收藏人脉卡片）\",\"subTableName\":null,\"subTableFkName\":null,\"className\":\"UserInterestedToMe\",\"tplCategory\":\"crud\",\"packageName\":\"com.project.admin.contact\",\"moduleName\":\"admin/contact\",\"businessName\":\"interested-tome\",\"functionName\":\"对我感兴趣\",\"functionAuthor\":\"huan.li\",\"genType\":\"0\",\"genPath\":\"/\",\"pkColumn\":null,\"subTable\":null,\"columns\":[{\"searchValue\":null,\"createBy\":\"admin\",\"createTime\":\"2022-06-10 19:15:29\",\"updateBy\":\"admin\",\"updateTime\":\"2022-06-10 19:21:37\",\"params\":{},\"columnId\":\"1535219091559129090\",\"tableId\":\"1535219091211001857\",\"columnName\":\"id\",\"columnComment\":\"ID\",\"columnType\":\"bigint(20)\",\"javaType\":\"Long\",\"javaField\":\"id\",\"isPk\":\"1\",\"isIncrement\":\"1\",\"isRequired\":\"1\",\"isInsert\":null,\"isEdit\":\"1\",\"isList\":\"1\",\"isQuery\":null,\"queryType\":\"EQ\",\"htmlType\":\"input\",\"dictType\":\"\",\"sort\":1,\"required\":true,\"list\":true,\"usableColumn\":false,\"edit\":true,\"insert\":false,\"pk\":true,\"superColumn\":false,\"capJavaField\":\"id\",\"increment\":true,\"query\":false},{\"searchValue\":null,\"createBy\":\"admin\",\"createTime\":\"2022-06-10 19:15:29\",\"updateBy\":\"admin\",\"updateTime\":\"2022-06-10 19:21:37\",\"params\":{},\"columnId\":\"1535219091559129091\",\"tableId\":\"1535219091211001857\",\"columnName\":\"user_info_id\",\"columnComment\":\"当前用户\",\"columnType\":\"bigint(20)\",\"javaType\":\"Long\",\"javaField\":\"userInfoId\",\"isPk\":\"0\",\"isIncrement\":\"0\",\"isRequired\":\"1\",\"isInsert\":\"1\",\"isEdit\":\"1\",\"isList\":\"1\",\"isQuery\":\"1\",\"queryType\":\"EQ\",\"htmlType\":\"input\",\"dictType\":\"\",\"sort\":2,\"required\":true,\"list\":true,\"usableColumn\":false,\"edit\":true,\"insert\":true,\"pk\":false,\"superColumn\":false,\"capJavaField\":\"userInfoId\",\"increment\":false,\"query\":true},{\"searchValue\":null,\"createBy\":\"admin\",\"createTime\":\"2022-06-10 19:15:29\",\"updateBy\":\"admin\",\"', '{\"code\":200,\"msg\":\"操作成功\",\"data\":null}', 0, '', '2022-06-10 19:21:38');
INSERT INTO `sys_oper_log` VALUES (1535220659368890369, '代码生成', 8, 'com.project.gen.controller.GenController.batchGenCode()', 'GET', 1, 'admin', '', '/gen/batchGenCode', '127.0.0.1', '', '', '', 0, '', '2022-06-10 19:21:43');
INSERT INTO `sys_oper_log` VALUES (1535222083653865474, '代码生成', 2, 'com.project.gen.controller.GenController.editSave()', 'PUT', 1, 'admin', '', '/gen', '127.0.0.1', '', '{\"searchValue\":null,\"createBy\":null,\"createTime\":null,\"updateBy\":\"admin\",\"updateTime\":\"2022-06-10 19:27:22\",\"params\":{\"treeCode\":null,\"treeName\":null,\"treeParentCode\":null,\"parentMenuId\":\"1535218927335247873\"},\"tableId\":\"1535219091211001857\",\"tableName\":\"user_interested_to_me\",\"tableComment\":\"对我感兴趣（收藏人脉卡片）\",\"subTableName\":null,\"subTableFkName\":null,\"className\":\"UserInterestedToMe\",\"tplCategory\":\"crud\",\"packageName\":\"com.project.admin.contact\",\"moduleName\":\"admin/contact\",\"businessName\":\"interested\",\"functionName\":\"对我感兴趣\",\"functionAuthor\":\"huan.li\",\"genType\":\"0\",\"genPath\":\"/\",\"pkColumn\":null,\"subTable\":null,\"columns\":[{\"searchValue\":null,\"createBy\":\"admin\",\"createTime\":\"2022-06-10 19:15:29\",\"updateBy\":\"admin\",\"updateTime\":\"2022-06-10 19:27:22\",\"params\":{},\"columnId\":\"1535219091559129090\",\"tableId\":\"1535219091211001857\",\"columnName\":\"id\",\"columnComment\":\"ID\",\"columnType\":\"bigint(20)\",\"javaType\":\"Long\",\"javaField\":\"id\",\"isPk\":\"1\",\"isIncrement\":\"1\",\"isRequired\":\"1\",\"isInsert\":null,\"isEdit\":\"1\",\"isList\":\"1\",\"isQuery\":null,\"queryType\":\"EQ\",\"htmlType\":\"input\",\"dictType\":\"\",\"sort\":1,\"required\":true,\"list\":true,\"usableColumn\":false,\"edit\":true,\"insert\":false,\"pk\":true,\"superColumn\":false,\"capJavaField\":\"id\",\"increment\":true,\"query\":false},{\"searchValue\":null,\"createBy\":\"admin\",\"createTime\":\"2022-06-10 19:15:29\",\"updateBy\":\"admin\",\"updateTime\":\"2022-06-10 19:27:22\",\"params\":{},\"columnId\":\"1535219091559129091\",\"tableId\":\"1535219091211001857\",\"columnName\":\"user_info_id\",\"columnComment\":\"当前用户\",\"columnType\":\"bigint(20)\",\"javaType\":\"Long\",\"javaField\":\"userInfoId\",\"isPk\":\"0\",\"isIncrement\":\"0\",\"isRequired\":\"1\",\"isInsert\":\"1\",\"isEdit\":\"1\",\"isList\":\"1\",\"isQuery\":\"1\",\"queryType\":\"EQ\",\"htmlType\":\"input\",\"dictType\":\"\",\"sort\":2,\"required\":true,\"list\":true,\"usableColumn\":false,\"edit\":true,\"insert\":true,\"pk\":false,\"superColumn\":false,\"capJavaField\":\"userInfoId\",\"increment\":false,\"query\":true},{\"searchValue\":null,\"createBy\":\"admin\",\"createTime\":\"2022-06-10 19:15:29\",\"updateBy\":\"admin\",\"updat', '{\"code\":200,\"msg\":\"操作成功\",\"data\":null}', 0, '', '2022-06-10 19:27:22');
INSERT INTO `sys_oper_log` VALUES (1535222097306324993, '代码生成', 8, 'com.project.gen.controller.GenController.batchGenCode()', 'GET', 1, 'admin', '', '/gen/batchGenCode', '127.0.0.1', '', '', '', 0, '', '2022-06-10 19:27:26');
INSERT INTO `sys_oper_log` VALUES (1535231415158296578, '菜单管理', 3, 'com.project.system.controller.SysMenuController.remove()', 'DELETE', 1, 'admin', '', '/menu/1535222097247707136', '127.0.0.1', '', '', '{\"code\":500,\"msg\":\"存在子菜单,不允许删除\",\"data\":null}', 0, '', '2022-06-10 20:04:27');
INSERT INTO `sys_oper_log` VALUES (1535231426881376257, '菜单管理', 3, 'com.project.system.controller.SysMenuController.remove()', 'DELETE', 1, 'admin', '', '/menu/1535222097247707136', '127.0.0.1', '', '', '{\"code\":500,\"msg\":\"存在子菜单,不允许删除\",\"data\":null}', 0, '', '2022-06-10 20:04:30');
INSERT INTO `sys_oper_log` VALUES (1535231429725114369, '菜单管理', 3, 'com.project.system.controller.SysMenuController.remove()', 'DELETE', 1, 'admin', '', '/menu/1535222097247707136', '127.0.0.1', '', '', '{\"code\":500,\"msg\":\"存在子菜单,不允许删除\",\"data\":null}', 0, '', '2022-06-10 20:04:31');
INSERT INTO `sys_oper_log` VALUES (1535231435014131714, '菜单管理', 3, 'com.project.system.controller.SysMenuController.remove()', 'DELETE', 1, 'admin', '', '/menu/1535222097247707136', '127.0.0.1', '', '', '{\"code\":500,\"msg\":\"存在子菜单,不允许删除\",\"data\":null}', 0, '', '2022-06-10 20:04:32');
INSERT INTO `sys_oper_log` VALUES (1535231436624744450, '菜单管理', 3, 'com.project.system.controller.SysMenuController.remove()', 'DELETE', 1, 'admin', '', '/menu/1535222097247707136', '127.0.0.1', '', '', '{\"code\":500,\"msg\":\"存在子菜单,不允许删除\",\"data\":null}', 0, '', '2022-06-10 20:04:32');
INSERT INTO `sys_oper_log` VALUES (1535231441834070017, '菜单管理', 3, 'com.project.system.controller.SysMenuController.remove()', 'DELETE', 1, 'admin', '', '/menu/1535222097247707136', '127.0.0.1', '', '', '{\"code\":500,\"msg\":\"存在子菜单,不允许删除\",\"data\":null}', 0, '', '2022-06-10 20:04:34');
INSERT INTO `sys_oper_log` VALUES (1535231461509550082, '菜单管理', 3, 'com.project.system.controller.SysMenuController.remove()', 'DELETE', 1, 'admin', '', '/menu/1535222097247707137', '127.0.0.1', '', '', '{\"code\":200,\"msg\":\"操作成功\",\"data\":null}', 0, '', '2022-06-10 20:04:38');
INSERT INTO `sys_oper_log` VALUES (1535231466177810433, '菜单管理', 3, 'com.project.system.controller.SysMenuController.remove()', 'DELETE', 1, 'admin', '', '/menu/1535222097247707136', '127.0.0.1', '', '', '{\"code\":500,\"msg\":\"存在子菜单,不允许删除\",\"data\":null}', 0, '', '2022-06-10 20:04:39');
INSERT INTO `sys_oper_log` VALUES (1535231471982727170, '菜单管理', 3, 'com.project.system.controller.SysMenuController.remove()', 'DELETE', 1, 'admin', '', '/menu/1535222097247707136', '127.0.0.1', '', '', '{\"code\":500,\"msg\":\"存在子菜单,不允许删除\",\"data\":null}', 0, '', '2022-06-10 20:04:41');
INSERT INTO `sys_oper_log` VALUES (1535231488013357057, '菜单管理', 3, 'com.project.system.controller.SysMenuController.remove()', 'DELETE', 1, 'admin', '', '/menu/1535222097247707141', '127.0.0.1', '', '', '{\"code\":200,\"msg\":\"操作成功\",\"data\":null}', 0, '', '2022-06-10 20:04:45');
INSERT INTO `sys_oper_log` VALUES (1535231494854266881, '菜单管理', 3, 'com.project.system.controller.SysMenuController.remove()', 'DELETE', 1, 'admin', '', '/menu/1535222097247707139', '127.0.0.1', '', '', '{\"code\":200,\"msg\":\"操作成功\",\"data\":null}', 0, '', '2022-06-10 20:04:46');
INSERT INTO `sys_oper_log` VALUES (1535231502903136257, '菜单管理', 3, 'com.project.system.controller.SysMenuController.remove()', 'DELETE', 1, 'admin', '', '/menu/1535222097247707140', '127.0.0.1', '', '', '{\"code\":200,\"msg\":\"操作成功\",\"data\":null}', 0, '', '2022-06-10 20:04:48');
INSERT INTO `sys_oper_log` VALUES (1535231511765700610, '菜单管理', 3, 'com.project.system.controller.SysMenuController.remove()', 'DELETE', 1, 'admin', '', '/menu/1535222097247707138', '127.0.0.1', '', '', '{\"code\":200,\"msg\":\"操作成功\",\"data\":null}', 0, '', '2022-06-10 20:04:50');
INSERT INTO `sys_oper_log` VALUES (1535231522205323266, '菜单管理', 3, 'com.project.system.controller.SysMenuController.remove()', 'DELETE', 1, 'admin', '', '/menu/1535222097247707136', '127.0.0.1', '', '', '{\"code\":200,\"msg\":\"操作成功\",\"data\":null}', 0, '', '2022-06-10 20:04:53');
INSERT INTO `sys_oper_log` VALUES (1535231529532772353, '菜单管理', 3, 'com.project.system.controller.SysMenuController.remove()', 'DELETE', 1, 'admin', '', '/menu/1535219822026530821', '127.0.0.1', '', '', '{\"code\":200,\"msg\":\"操作成功\",\"data\":null}', 0, '', '2022-06-10 20:04:55');
INSERT INTO `sys_oper_log` VALUES (1535231533865488386, '菜单管理', 3, 'com.project.system.controller.SysMenuController.remove()', 'DELETE', 1, 'admin', '', '/menu/1535219822026530820', '127.0.0.1', '', '', '{\"code\":200,\"msg\":\"操作成功\",\"data\":null}', 0, '', '2022-06-10 20:04:56');
INSERT INTO `sys_oper_log` VALUES (1535231537774579714, '菜单管理', 3, 'com.project.system.controller.SysMenuController.remove()', 'DELETE', 1, 'admin', '', '/menu/1535219822026530819', '127.0.0.1', '', '', '{\"code\":200,\"msg\":\"操作成功\",\"data\":null}', 0, '', '2022-06-10 20:04:56');
INSERT INTO `sys_oper_log` VALUES (1535231555927527426, '菜单管理', 3, 'com.project.system.controller.SysMenuController.remove()', 'DELETE', 1, 'admin', '', '/menu/1535219822026530818', '127.0.0.1', '', '', '{\"code\":200,\"msg\":\"操作成功\",\"data\":null}', 0, '', '2022-06-10 20:05:01');
INSERT INTO `sys_oper_log` VALUES (1535231565259853826, '菜单管理', 3, 'com.project.system.controller.SysMenuController.remove()', 'DELETE', 1, 'admin', '', '/menu/1535219822026530817', '127.0.0.1', '', '', '{\"code\":200,\"msg\":\"操作成功\",\"data\":null}', 0, '', '2022-06-10 20:05:03');
INSERT INTO `sys_oper_log` VALUES (1535231580086718466, '菜单管理', 3, 'com.project.system.controller.SysMenuController.remove()', 'DELETE', 1, 'admin', '', '/menu/1535219822026530816', '127.0.0.1', '', '', '{\"code\":200,\"msg\":\"操作成功\",\"data\":null}', 0, '', '2022-06-10 20:05:07');
INSERT INTO `sys_oper_log` VALUES (1535231818100887553, '代码生成', 2, 'com.project.gen.controller.GenController.editSave()', 'PUT', 1, 'admin', '', '/gen', '127.0.0.1', '', '{\"searchValue\":null,\"createBy\":null,\"createTime\":null,\"updateBy\":\"admin\",\"updateTime\":\"2022-06-10 20:06:02\",\"params\":{\"treeCode\":null,\"treeName\":null,\"treeParentCode\":null,\"parentMenuId\":\"1535218927335247873\"},\"tableId\":\"1535219091211001857\",\"tableName\":\"user_interested_to_me\",\"tableComment\":\"对我感兴趣（收藏人脉卡片）\",\"subTableName\":null,\"subTableFkName\":null,\"className\":\"UserInterestedToMe\",\"tplCategory\":\"crud\",\"packageName\":\"com.admin.contact\",\"moduleName\":\"admin/contact\",\"businessName\":\"interested\",\"functionName\":\"对我感兴趣\",\"functionAuthor\":\"huan.li\",\"genType\":\"0\",\"genPath\":\"/\",\"pkColumn\":null,\"subTable\":null,\"columns\":[{\"searchValue\":null,\"createBy\":\"admin\",\"createTime\":\"2022-06-10 19:15:29\",\"updateBy\":\"admin\",\"updateTime\":\"2022-06-10 20:06:02\",\"params\":{},\"columnId\":\"1535219091559129090\",\"tableId\":\"1535219091211001857\",\"columnName\":\"id\",\"columnComment\":\"ID\",\"columnType\":\"bigint(20)\",\"javaType\":\"Long\",\"javaField\":\"id\",\"isPk\":\"1\",\"isIncrement\":\"1\",\"isRequired\":\"1\",\"isInsert\":null,\"isEdit\":\"1\",\"isList\":\"1\",\"isQuery\":null,\"queryType\":\"EQ\",\"htmlType\":\"input\",\"dictType\":\"\",\"sort\":1,\"required\":true,\"pk\":true,\"edit\":true,\"superColumn\":false,\"insert\":false,\"usableColumn\":false,\"list\":true,\"increment\":true,\"query\":false,\"capJavaField\":\"id\"},{\"searchValue\":null,\"createBy\":\"admin\",\"createTime\":\"2022-06-10 19:15:29\",\"updateBy\":\"admin\",\"updateTime\":\"2022-06-10 20:06:02\",\"params\":{},\"columnId\":\"1535219091559129091\",\"tableId\":\"1535219091211001857\",\"columnName\":\"user_info_id\",\"columnComment\":\"当前用户\",\"columnType\":\"bigint(20)\",\"javaType\":\"Long\",\"javaField\":\"userInfoId\",\"isPk\":\"0\",\"isIncrement\":\"0\",\"isRequired\":\"1\",\"isInsert\":\"1\",\"isEdit\":\"1\",\"isList\":\"1\",\"isQuery\":\"1\",\"queryType\":\"EQ\",\"htmlType\":\"input\",\"dictType\":\"\",\"sort\":2,\"required\":true,\"pk\":false,\"edit\":true,\"superColumn\":false,\"insert\":true,\"usableColumn\":false,\"list\":true,\"increment\":false,\"query\":true,\"capJavaField\":\"userInfoId\"},{\"searchValue\":null,\"createBy\":\"admin\",\"createTime\":\"2022-06-10 19:15:29\",\"updateBy\":\"admin\",\"updateTime\":\"', '{\"code\":200,\"msg\":\"操作成功\",\"data\":null}', 0, '', '2022-06-10 20:06:03');
INSERT INTO `sys_oper_log` VALUES (1535231832235692033, '代码生成', 8, 'com.project.gen.controller.GenController.batchGenCode()', 'GET', 1, 'admin', '', '/gen/batchGenCode', '127.0.0.1', '', '', '', 0, '', '2022-06-10 20:06:07');
INSERT INTO `sys_oper_log` VALUES (1535237600167063553, '代码生成', 2, 'com.project.gen.controller.GenController.editSave()', 'PUT', 1, 'admin', '', '/gen', '127.0.0.1', '', '{\"searchValue\":null,\"createBy\":null,\"createTime\":null,\"updateBy\":\"admin\",\"updateTime\":\"2022-06-10 20:29:01\",\"params\":{\"treeCode\":null,\"treeName\":null,\"treeParentCode\":null,\"parentMenuId\":\"1535218927335247873\"},\"tableId\":\"1535219091211001857\",\"tableName\":\"user_interested_to_me\",\"tableComment\":\"对我感兴趣（收藏人脉卡片）\",\"subTableName\":null,\"subTableFkName\":null,\"className\":\"UserInterestedToMe\",\"tplCategory\":\"crud\",\"packageName\":\"com.project.contact\",\"moduleName\":\"admin/contact\",\"businessName\":\"interested\",\"functionName\":\"对我感兴趣\",\"functionAuthor\":\"huan.li\",\"genType\":\"0\",\"genPath\":\"/\",\"pkColumn\":null,\"subTable\":null,\"columns\":[{\"searchValue\":null,\"createBy\":\"admin\",\"createTime\":\"2022-06-10 19:15:29\",\"updateBy\":\"admin\",\"updateTime\":\"2022-06-10 20:29:01\",\"params\":{},\"columnId\":\"1535219091559129090\",\"tableId\":\"1535219091211001857\",\"columnName\":\"id\",\"columnComment\":\"ID\",\"columnType\":\"bigint(20)\",\"javaType\":\"Long\",\"javaField\":\"id\",\"isPk\":\"1\",\"isIncrement\":\"1\",\"isRequired\":\"1\",\"isInsert\":null,\"isEdit\":\"1\",\"isList\":\"1\",\"isQuery\":null,\"queryType\":\"EQ\",\"htmlType\":\"input\",\"dictType\":\"\",\"sort\":1,\"required\":true,\"list\":true,\"pk\":true,\"edit\":true,\"superColumn\":false,\"insert\":false,\"usableColumn\":false,\"increment\":true,\"capJavaField\":\"id\",\"query\":false},{\"searchValue\":null,\"createBy\":\"admin\",\"createTime\":\"2022-06-10 19:15:29\",\"updateBy\":\"admin\",\"updateTime\":\"2022-06-10 20:29:01\",\"params\":{},\"columnId\":\"1535219091559129091\",\"tableId\":\"1535219091211001857\",\"columnName\":\"user_info_id\",\"columnComment\":\"当前用户\",\"columnType\":\"bigint(20)\",\"javaType\":\"Long\",\"javaField\":\"userInfoId\",\"isPk\":\"0\",\"isIncrement\":\"0\",\"isRequired\":\"1\",\"isInsert\":\"1\",\"isEdit\":\"1\",\"isList\":\"1\",\"isQuery\":\"1\",\"queryType\":\"EQ\",\"htmlType\":\"input\",\"dictType\":\"\",\"sort\":2,\"required\":true,\"list\":true,\"pk\":false,\"edit\":true,\"superColumn\":false,\"insert\":true,\"usableColumn\":false,\"increment\":false,\"capJavaField\":\"userInfoId\",\"query\":true},{\"searchValue\":null,\"createBy\":\"admin\",\"createTime\":\"2022-06-10 19:15:29\",\"updateBy\":\"admin\",\"updateTime\"', '{\"code\":200,\"msg\":\"操作成功\",\"data\":null}', 0, '', '2022-06-10 20:29:02');
INSERT INTO `sys_oper_log` VALUES (1535237629053235202, '代码生成', 8, 'com.project.gen.controller.GenController.batchGenCode()', 'GET', 1, 'admin', '', '/gen/batchGenCode', '127.0.0.1', '', '', '', 0, '', '2022-06-10 20:29:09');
INSERT INTO `sys_oper_log` VALUES (1535242082628034562, '菜单管理', 3, 'com.project.system.controller.SysMenuController.remove()', 'DELETE', 1, 'admin', '', '/menu/1535237628616900609', '127.0.0.1', '', '', '{\"code\":200,\"msg\":\"操作成功\",\"data\":null}', 0, '', '2022-06-10 20:46:51');
INSERT INTO `sys_oper_log` VALUES (1535242089678659585, '菜单管理', 3, 'com.project.system.controller.SysMenuController.remove()', 'DELETE', 1, 'admin', '', '/menu/1535237628616900610', '127.0.0.1', '', '', '{\"code\":200,\"msg\":\"操作成功\",\"data\":null}', 0, '', '2022-06-10 20:46:52');
INSERT INTO `sys_oper_log` VALUES (1535242096095944706, '菜单管理', 3, 'com.project.system.controller.SysMenuController.remove()', 'DELETE', 1, 'admin', '', '/menu/1535237628616900611', '127.0.0.1', '', '', '{\"code\":200,\"msg\":\"操作成功\",\"data\":null}', 0, '', '2022-06-10 20:46:54');
INSERT INTO `sys_oper_log` VALUES (1535242101070389249, '菜单管理', 3, 'com.project.system.controller.SysMenuController.remove()', 'DELETE', 1, 'admin', '', '/menu/1535237628616900612', '127.0.0.1', '', '', '{\"code\":200,\"msg\":\"操作成功\",\"data\":null}', 0, '', '2022-06-10 20:46:55');
INSERT INTO `sys_oper_log` VALUES (1535242108225871873, '菜单管理', 3, 'com.project.system.controller.SysMenuController.remove()', 'DELETE', 1, 'admin', '', '/menu/1535237628616900613', '127.0.0.1', '', '', '{\"code\":200,\"msg\":\"操作成功\",\"data\":null}', 0, '', '2022-06-10 20:46:57');
INSERT INTO `sys_oper_log` VALUES (1535242115591069697, '菜单管理', 3, 'com.project.system.controller.SysMenuController.remove()', 'DELETE', 1, 'admin', '', '/menu/1535237628616900608', '127.0.0.1', '', '', '{\"code\":200,\"msg\":\"操作成功\",\"data\":null}', 0, '', '2022-06-10 20:46:58');
INSERT INTO `sys_oper_log` VALUES (1535242479421775874, '代码生成', 2, 'com.project.gen.controller.GenController.editSave()', 'PUT', 1, 'admin', '', '/gen', '127.0.0.1', '', '{\"searchValue\":null,\"createBy\":null,\"createTime\":null,\"updateBy\":\"admin\",\"updateTime\":\"2022-06-10 20:48:24\",\"params\":{\"treeCode\":null,\"treeName\":null,\"treeParentCode\":null,\"parentMenuId\":\"1535218927335247873\"},\"tableId\":\"1535219091211001857\",\"tableName\":\"user_interested_to_me\",\"tableComment\":\"对我感兴趣（收藏人脉卡片）\",\"subTableName\":null,\"subTableFkName\":null,\"className\":\"UserInterestedToMe\",\"tplCategory\":\"crud\",\"packageName\":\"com.project.contact\",\"moduleName\":\"contact\",\"businessName\":\"interested\",\"functionName\":\"对我感兴趣\",\"functionAuthor\":\"huan.li\",\"genType\":\"0\",\"genPath\":\"/\",\"pkColumn\":null,\"subTable\":null,\"columns\":[{\"searchValue\":null,\"createBy\":\"admin\",\"createTime\":\"2022-06-10 19:15:29\",\"updateBy\":\"admin\",\"updateTime\":\"2022-06-10 20:48:24\",\"params\":{},\"columnId\":\"1535219091559129090\",\"tableId\":\"1535219091211001857\",\"columnName\":\"id\",\"columnComment\":\"ID\",\"columnType\":\"bigint(20)\",\"javaType\":\"Long\",\"javaField\":\"id\",\"isPk\":\"1\",\"isIncrement\":\"1\",\"isRequired\":\"1\",\"isInsert\":null,\"isEdit\":\"1\",\"isList\":\"1\",\"isQuery\":null,\"queryType\":\"EQ\",\"htmlType\":\"input\",\"dictType\":\"\",\"sort\":1,\"list\":true,\"pk\":true,\"insert\":false,\"usableColumn\":false,\"superColumn\":false,\"edit\":true,\"increment\":true,\"query\":false,\"capJavaField\":\"id\",\"required\":true},{\"searchValue\":null,\"createBy\":\"admin\",\"createTime\":\"2022-06-10 19:15:29\",\"updateBy\":\"admin\",\"updateTime\":\"2022-06-10 20:48:24\",\"params\":{},\"columnId\":\"1535219091559129091\",\"tableId\":\"1535219091211001857\",\"columnName\":\"user_info_id\",\"columnComment\":\"当前用户\",\"columnType\":\"bigint(20)\",\"javaType\":\"Long\",\"javaField\":\"userInfoId\",\"isPk\":\"0\",\"isIncrement\":\"0\",\"isRequired\":\"1\",\"isInsert\":\"1\",\"isEdit\":\"1\",\"isList\":\"1\",\"isQuery\":\"1\",\"queryType\":\"EQ\",\"htmlType\":\"input\",\"dictType\":\"\",\"sort\":2,\"list\":true,\"pk\":false,\"insert\":true,\"usableColumn\":false,\"superColumn\":false,\"edit\":true,\"increment\":false,\"query\":true,\"capJavaField\":\"userInfoId\",\"required\":true},{\"searchValue\":null,\"createBy\":\"admin\",\"createTime\":\"2022-06-10 19:15:29\",\"updateBy\":\"admin\",\"updateTime\":\"2022', '{\"code\":200,\"msg\":\"操作成功\",\"data\":null}', 0, '', '2022-06-10 20:48:25');
INSERT INTO `sys_oper_log` VALUES (1535242510207967233, '代码生成', 8, 'com.project.gen.controller.GenController.batchGenCode()', 'GET', 1, 'admin', '', '/gen/batchGenCode', '127.0.0.1', '', '', '', 0, '', '2022-06-10 20:48:33');
INSERT INTO `sys_oper_log` VALUES (1535245259117064193, '菜单管理', 3, 'com.project.system.controller.SysMenuController.remove()', 'DELETE', 1, 'admin', '', '/menu/1535237628616900609', '127.0.0.1', '', '', '{\"code\":200,\"msg\":\"操作成功\",\"data\":null}', 0, '', '2022-06-10 20:59:28');
INSERT INTO `sys_oper_log` VALUES (1535245263307173890, '菜单管理', 3, 'com.project.system.controller.SysMenuController.remove()', 'DELETE', 1, 'admin', '', '/menu/1535237628616900610', '127.0.0.1', '', '', '{\"code\":200,\"msg\":\"操作成功\",\"data\":null}', 0, '', '2022-06-10 20:59:29');
INSERT INTO `sys_oper_log` VALUES (1535245269833510913, '菜单管理', 3, 'com.project.system.controller.SysMenuController.remove()', 'DELETE', 1, 'admin', '', '/menu/1535237628616900611', '127.0.0.1', '', '', '{\"code\":200,\"msg\":\"操作成功\",\"data\":null}', 0, '', '2022-06-10 20:59:30');
INSERT INTO `sys_oper_log` VALUES (1535245274497576962, '菜单管理', 3, 'com.project.system.controller.SysMenuController.remove()', 'DELETE', 1, 'admin', '', '/menu/1535237628616900612', '127.0.0.1', '', '', '{\"code\":200,\"msg\":\"操作成功\",\"data\":null}', 0, '', '2022-06-10 20:59:32');
INSERT INTO `sys_oper_log` VALUES (1535245280403156993, '菜单管理', 3, 'com.project.system.controller.SysMenuController.remove()', 'DELETE', 1, 'admin', '', '/menu/1535237628616900613', '127.0.0.1', '', '', '{\"code\":200,\"msg\":\"操作成功\",\"data\":null}', 0, '', '2022-06-10 20:59:33');
INSERT INTO `sys_oper_log` VALUES (1535245285054640130, '菜单管理', 3, 'com.project.system.controller.SysMenuController.remove()', 'DELETE', 1, 'admin', '', '/menu/1535237628616900608', '127.0.0.1', '', '', '{\"code\":200,\"msg\":\"操作成功\",\"data\":null}', 0, '', '2022-06-10 20:59:34');
INSERT INTO `sys_oper_log` VALUES (1535245895736913922, '菜单管理', 2, 'com.project.system.controller.SysMenuController.edit()', 'PUT', 1, 'admin', '', '/menu', '127.0.0.1', '', '{\"searchValue\":null,\"createBy\":\"admin\",\"createTime\":\"2022-06-10 21:00:00\",\"updateBy\":\"admin\",\"updateTime\":\"2022-06-10 21:01:59\",\"params\":{},\"parentName\":null,\"parentId\":\"1535218927335247873\",\"children\":[],\"menuId\":\"1535242509843099648\",\"menuName\":\"对我感兴趣\",\"orderNum\":1,\"path\":\"interested\",\"component\":\"contact/interested/index\",\"queryParam\":null,\"isFrame\":\"1\",\"isCache\":\"0\",\"menuType\":\"C\",\"visible\":\"0\",\"status\":\"0\",\"perms\":\"contact:interested:list\",\"icon\":\"clipboard\",\"remark\":\"对我感兴趣菜单\"}', '{\"code\":200,\"msg\":\"操作成功\",\"data\":null}', 0, '', '2022-06-10 21:02:00');
INSERT INTO `sys_oper_log` VALUES (1535246127635787777, '菜单管理', 3, 'com.project.system.controller.SysMenuController.remove()', 'DELETE', 1, 'admin', '', '/menu/1535242509843099649', '127.0.0.1', '', '', '{\"code\":200,\"msg\":\"操作成功\",\"data\":null}', 0, '', '2022-06-10 21:02:55');
INSERT INTO `sys_oper_log` VALUES (1535246134703190017, '菜单管理', 3, 'com.project.system.controller.SysMenuController.remove()', 'DELETE', 1, 'admin', '', '/menu/1535242509843099650', '127.0.0.1', '', '', '{\"code\":200,\"msg\":\"操作成功\",\"data\":null}', 0, '', '2022-06-10 21:02:57');
INSERT INTO `sys_oper_log` VALUES (1535246141145640961, '菜单管理', 3, 'com.project.system.controller.SysMenuController.remove()', 'DELETE', 1, 'admin', '', '/menu/1535242509843099651', '127.0.0.1', '', '', '{\"code\":200,\"msg\":\"操作成功\",\"data\":null}', 0, '', '2022-06-10 21:02:58');
INSERT INTO `sys_oper_log` VALUES (1535246146086531073, '菜单管理', 3, 'com.project.system.controller.SysMenuController.remove()', 'DELETE', 1, 'admin', '', '/menu/1535242509843099652', '127.0.0.1', '', '', '{\"code\":200,\"msg\":\"操作成功\",\"data\":null}', 0, '', '2022-06-10 21:02:59');
INSERT INTO `sys_oper_log` VALUES (1535246162217824257, '菜单管理', 3, 'com.project.system.controller.SysMenuController.remove()', 'DELETE', 1, 'admin', '', '/menu/1535242509843099653', '127.0.0.1', '', '', '{\"code\":200,\"msg\":\"操作成功\",\"data\":null}', 0, '', '2022-06-10 21:03:03');
INSERT INTO `sys_oper_log` VALUES (1535246171470458881, '菜单管理', 3, 'com.project.system.controller.SysMenuController.remove()', 'DELETE', 1, 'admin', '', '/menu/1535242509843099648', '127.0.0.1', '', '', '{\"code\":200,\"msg\":\"操作成功\",\"data\":null}', 0, '', '2022-06-10 21:03:05');
INSERT INTO `sys_oper_log` VALUES (1535246388013985794, '菜单管理', 2, 'com.project.system.controller.SysMenuController.edit()', 'PUT', 1, 'admin', '', '/menu', '127.0.0.1', '', '{\"searchValue\":null,\"createBy\":\"admin\",\"createTime\":\"2022-06-09 21:43:46\",\"updateBy\":\"admin\",\"updateTime\":\"2022-06-10 21:03:57\",\"params\":{},\"parentName\":null,\"parentId\":0,\"children\":[],\"menuId\":\"1534894022110633986\",\"menuName\":\"人脉管理\",\"orderNum\":0,\"path\":\"contact\",\"component\":null,\"queryParam\":null,\"isFrame\":\"1\",\"isCache\":\"0\",\"menuType\":\"M\",\"visible\":\"0\",\"status\":\"0\",\"icon\":\"build\",\"remark\":\"\"}', '{\"code\":200,\"msg\":\"操作成功\",\"data\":null}', 0, '', '2022-06-10 21:03:57');
INSERT INTO `sys_oper_log` VALUES (1535246415004332034, '菜单管理', 2, 'com.project.system.controller.SysMenuController.edit()', 'PUT', 1, 'admin', '', '/menu', '127.0.0.1', '', '{\"searchValue\":null,\"createBy\":\"admin\",\"createTime\":\"2022-06-09 21:43:46\",\"updateBy\":\"admin\",\"updateTime\":\"2022-06-10 21:04:03\",\"params\":{},\"parentName\":null,\"parentId\":0,\"children\":[],\"menuId\":\"1534894022110633986\",\"menuName\":\"人脉管理\",\"orderNum\":0,\"path\":\"contact\",\"component\":null,\"queryParam\":null,\"isFrame\":\"1\",\"isCache\":\"0\",\"menuType\":\"M\",\"visible\":\"0\",\"status\":\"0\",\"icon\":\"build\",\"remark\":\"\"}', '{\"code\":200,\"msg\":\"操作成功\",\"data\":null}', 0, '', '2022-06-10 21:04:03');
INSERT INTO `sys_oper_log` VALUES (1535246437926203394, '菜单管理', 2, 'com.project.system.controller.SysMenuController.edit()', 'PUT', 1, 'admin', '', '/menu', '127.0.0.1', '', '{\"searchValue\":null,\"createBy\":\"admin\",\"createTime\":\"2022-06-09 21:43:46\",\"updateBy\":\"admin\",\"updateTime\":\"2022-06-10 21:04:08\",\"params\":{},\"parentName\":null,\"parentId\":0,\"children\":[],\"menuId\":\"1534894022110633986\",\"menuName\":\"人脉管理\",\"orderNum\":0,\"path\":\"contact\",\"component\":null,\"queryParam\":null,\"isFrame\":\"1\",\"isCache\":\"0\",\"menuType\":\"M\",\"visible\":\"0\",\"status\":\"0\",\"icon\":\"build\",\"remark\":\"\"}', '{\"code\":200,\"msg\":\"操作成功\",\"data\":null}', 0, '', '2022-06-10 21:04:09');
INSERT INTO `sys_oper_log` VALUES (1535246684995874818, '菜单管理', 2, 'com.project.system.controller.SysMenuController.edit()', 'PUT', 1, 'admin', '', '/menu', '127.0.0.1', '', '{\"searchValue\":null,\"createBy\":\"admin\",\"createTime\":\"2022-06-10 19:14:50\",\"updateBy\":\"admin\",\"updateTime\":\"2022-06-10 21:05:07\",\"params\":{},\"parentName\":null,\"parentId\":\"1534894022110633986\",\"children\":[],\"menuId\":\"1535218927335247873\",\"menuName\":\"我的人脉\",\"orderNum\":1,\"path\":\"interested\",\"component\":null,\"queryParam\":null,\"isFrame\":\"1\",\"isCache\":\"0\",\"menuType\":\"M\",\"visible\":\"0\",\"status\":\"0\",\"icon\":\"github\",\"remark\":\"\"}', '{\"code\":200,\"msg\":\"操作成功\",\"data\":null}', 0, '', '2022-06-10 21:05:08');
INSERT INTO `sys_oper_log` VALUES (1535246759935504386, '菜单管理', 3, 'com.project.system.controller.SysMenuController.remove()', 'DELETE', 1, 'admin', '', '/menu/1535218927335247873', '127.0.0.1', '', '', '{\"code\":200,\"msg\":\"操作成功\",\"data\":null}', 0, '', '2022-06-10 21:05:26');
INSERT INTO `sys_oper_log` VALUES (1535246937165819906, '代码生成', 2, 'com.project.gen.controller.GenController.editSave()', 'PUT', 1, 'admin', '', '/gen', '127.0.0.1', '', '{\"searchValue\":null,\"createBy\":null,\"createTime\":null,\"updateBy\":\"admin\",\"updateTime\":\"2022-06-10 21:06:07\",\"params\":{\"treeCode\":null,\"treeName\":null,\"treeParentCode\":null,\"parentMenuId\":\"1534894022110633986\"},\"tableId\":\"1535219091211001857\",\"tableName\":\"user_interested_to_me\",\"tableComment\":\"对我感兴趣（收藏人脉卡片）\",\"subTableName\":null,\"subTableFkName\":null,\"className\":\"UserInterestedToMe\",\"tplCategory\":\"crud\",\"packageName\":\"com.project.contact\",\"moduleName\":\"contact\",\"businessName\":\"interested\",\"functionName\":\"对我感兴趣\",\"functionAuthor\":\"huan.li\",\"genType\":\"0\",\"genPath\":\"/\",\"pkColumn\":null,\"subTable\":null,\"columns\":[{\"searchValue\":null,\"createBy\":\"admin\",\"createTime\":\"2022-06-10 19:15:29\",\"updateBy\":\"admin\",\"updateTime\":\"2022-06-10 21:06:07\",\"params\":{},\"columnId\":\"1535219091559129090\",\"tableId\":\"1535219091211001857\",\"columnName\":\"id\",\"columnComment\":\"ID\",\"columnType\":\"bigint(20)\",\"javaType\":\"Long\",\"javaField\":\"id\",\"isPk\":\"1\",\"isIncrement\":\"1\",\"isRequired\":\"1\",\"isInsert\":null,\"isEdit\":\"1\",\"isList\":\"1\",\"isQuery\":null,\"queryType\":\"EQ\",\"htmlType\":\"input\",\"dictType\":\"\",\"sort\":1,\"required\":true,\"list\":true,\"pk\":true,\"edit\":true,\"superColumn\":false,\"insert\":false,\"usableColumn\":false,\"query\":false,\"capJavaField\":\"id\",\"increment\":true},{\"searchValue\":null,\"createBy\":\"admin\",\"createTime\":\"2022-06-10 19:15:29\",\"updateBy\":\"admin\",\"updateTime\":\"2022-06-10 21:06:07\",\"params\":{},\"columnId\":\"1535219091559129091\",\"tableId\":\"1535219091211001857\",\"columnName\":\"user_info_id\",\"columnComment\":\"当前用户\",\"columnType\":\"bigint(20)\",\"javaType\":\"Long\",\"javaField\":\"userInfoId\",\"isPk\":\"0\",\"isIncrement\":\"0\",\"isRequired\":\"1\",\"isInsert\":\"1\",\"isEdit\":\"1\",\"isList\":\"1\",\"isQuery\":\"1\",\"queryType\":\"EQ\",\"htmlType\":\"input\",\"dictType\":\"\",\"sort\":2,\"required\":true,\"list\":true,\"pk\":false,\"edit\":true,\"superColumn\":false,\"insert\":true,\"usableColumn\":false,\"query\":true,\"capJavaField\":\"userInfoId\",\"increment\":false},{\"searchValue\":null,\"createBy\":\"admin\",\"createTime\":\"2022-06-10 19:15:29\",\"updateBy\":\"admin\",\"updateTime\":\"2022', '{\"code\":200,\"msg\":\"操作成功\",\"data\":null}', 0, '', '2022-06-10 21:06:08');
INSERT INTO `sys_oper_log` VALUES (1535246949320912897, '代码生成', 8, 'com.project.gen.controller.GenController.batchGenCode()', 'GET', 1, 'admin', '', '/gen/batchGenCode', '127.0.0.1', '', '', '', 0, '', '2022-06-10 21:06:11');
INSERT INTO `sys_oper_log` VALUES (1535248252755406850, '菜单管理', 2, 'com.project.system.controller.SysMenuController.edit()', 'PUT', 1, 'admin', '', '/menu', '127.0.0.1', '', '{\"searchValue\":null,\"createBy\":\"admin\",\"createTime\":\"2022-06-10 21:09:53\",\"updateBy\":\"admin\",\"updateTime\":\"2022-06-10 21:11:21\",\"params\":{},\"parentName\":null,\"parentId\":\"1534894022110633986\",\"children\":[],\"menuId\":\"1535246948960182272\",\"menuName\":\"对我感兴趣\",\"orderNum\":1,\"path\":\"interested\",\"component\":\"contact/interested/index\",\"queryParam\":null,\"isFrame\":\"1\",\"isCache\":\"0\",\"menuType\":\"C\",\"visible\":\"0\",\"status\":\"0\",\"perms\":\"contact:interested:list\",\"icon\":\"cascader\",\"remark\":\"对我感兴趣菜单\"}', '{\"code\":200,\"msg\":\"操作成功\",\"data\":null}', 0, '', '2022-06-10 21:11:22');
INSERT INTO `sys_oper_log` VALUES (1535248722337193986, '对我感兴趣', 1, 'com.project.contact.controller.UserInterestedToMeController.add()', 'POST', 1, 'admin', '', '/interested', '127.0.0.1', '', '{\"searchValue\":\"1141\",\"createBy\":null,\"createTime\":null,\"updateBy\":null,\"updateTime\":null,\"params\":{},\"id\":\"1535248721590550529\",\"userInfoId\":11,\"contactInfoId\":223322,\"deleted\":null}', '{\"code\":200,\"msg\":\"操作成功\",\"data\":null}', 0, '', '2022-06-10 21:13:14');
INSERT INTO `sys_oper_log` VALUES (1535248761470050305, '对我感兴趣', 5, 'com.project.contact.controller.UserInterestedToMeController.export()', 'POST', 1, 'admin', '', '/interested/export', '127.0.0.1', '', '{\"searchValue\":null,\"createBy\":null,\"createTime\":null,\"updateBy\":null,\"updateTime\":null,\"params\":{},\"id\":null,\"userInfoId\":null,\"contactInfoId\":null,\"deleted\":null}', '', 0, '', '2022-06-10 21:13:23');

-- ----------------------------
-- Table structure for sys_oss
-- ----------------------------
DROP TABLE IF EXISTS `sys_oss`;
CREATE TABLE `sys_oss`  (
  `oss_id` bigint(20) NOT NULL COMMENT '对象存储主键',
  `file_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '文件名',
  `original_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '原名',
  `file_suffix` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '文件后缀名',
  `url` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'URL地址',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '上传人',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '更新人',
  `service` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT 'minio' COMMENT '服务商',
  PRIMARY KEY (`oss_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = 'OSS对象存储表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_oss
-- ----------------------------

-- ----------------------------
-- Table structure for sys_oss_config
-- ----------------------------
DROP TABLE IF EXISTS `sys_oss_config`;
CREATE TABLE `sys_oss_config`  (
  `oss_config_id` bigint(20) NOT NULL COMMENT '主建',
  `config_key` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '配置key',
  `access_key` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT 'accessKey',
  `secret_key` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '秘钥',
  `bucket_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '桶名称',
  `prefix` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '前缀',
  `endpoint` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '访问站点',
  `domain` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '自定义域名',
  `is_https` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT 'N' COMMENT '是否https（Y=是,N=否）',
  `region` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '域',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '1' COMMENT '状态（0=正常,1=停用）',
  `ext1` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '扩展字段',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`oss_config_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '对象存储配置表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_oss_config
-- ----------------------------
INSERT INTO `sys_oss_config` VALUES (1, 'minio', 'ruoyi', 'ruoyi123', 'ruoyi', '', '127.0.0.1:9000', '', 'N', '', '0', '', 'admin', '2022-05-31 06:31:34', 'admin', '2022-05-31 06:31:34', NULL);
INSERT INTO `sys_oss_config` VALUES (2, 'qiniu', 'XXXXXXXXXXXXXXX', 'XXXXXXXXXXXXXXX', 'ruoyi', '', 's3-cn-north-1.qiniucs.com', '', 'N', '', '1', '', 'admin', '2022-05-31 06:31:34', 'admin', '2022-05-31 06:31:34', NULL);
INSERT INTO `sys_oss_config` VALUES (3, 'aliyun', 'XXXXXXXXXXXXXXX', 'XXXXXXXXXXXXXXX', 'ruoyi', '', 'oss-cn-beijing.aliyuncs.com', '', 'N', '', '1', '', 'admin', '2022-05-31 06:31:34', 'admin', '2022-05-31 06:31:34', NULL);
INSERT INTO `sys_oss_config` VALUES (4, 'qcloud', 'XXXXXXXXXXXXXXX', 'XXXXXXXXXXXXXXX', 'ruoyi-1250000000', '', 'cos.ap-beijing.myqcloud.com', '', 'N', 'ap-beijing', '1', '', 'admin', '2022-05-31 06:31:34', 'admin', '2022-05-31 06:31:34', NULL);
INSERT INTO `sys_oss_config` VALUES (5, 'image', 'ruoyi', 'ruoyi123', 'ruoyi', 'image', '127.0.0.1:9000', '', 'N', '', '1', '', 'admin', '2022-05-31 06:31:34', 'admin', '2022-05-31 06:31:34', NULL);

-- ----------------------------
-- Table structure for sys_post
-- ----------------------------
DROP TABLE IF EXISTS `sys_post`;
CREATE TABLE `sys_post`  (
  `post_id` bigint(20) NOT NULL COMMENT '岗位ID',
  `post_code` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '岗位编码',
  `post_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '岗位名称',
  `post_sort` int(4) NOT NULL COMMENT '显示顺序',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '状态（0正常 1停用）',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`post_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '岗位信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_post
-- ----------------------------
INSERT INTO `sys_post` VALUES (1, 'ceo', '董事长', 1, '0', 'admin', '2022-05-31 06:31:32', '', NULL, '');
INSERT INTO `sys_post` VALUES (2, 'se', '项目经理', 2, '0', 'admin', '2022-05-31 06:31:32', '', NULL, '');
INSERT INTO `sys_post` VALUES (3, 'hr', '人力资源', 3, '0', 'admin', '2022-05-31 06:31:32', '', NULL, '');
INSERT INTO `sys_post` VALUES (4, 'user', '普通员工', 4, '0', 'admin', '2022-05-31 06:31:32', '', NULL, '');

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role`  (
  `role_id` bigint(20) NOT NULL COMMENT '角色ID',
  `role_name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '角色名称',
  `role_key` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '角色权限字符串',
  `role_sort` int(4) NOT NULL COMMENT '显示顺序',
  `data_scope` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '1' COMMENT '数据范围（1：全部数据权限 2：自定数据权限 3：本部门数据权限 4：本部门及以下数据权限）',
  `menu_check_strictly` tinyint(1) NULL DEFAULT 1 COMMENT '菜单树选择项是否关联显示',
  `dept_check_strictly` tinyint(1) NULL DEFAULT 1 COMMENT '部门树选择项是否关联显示',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '角色状态（0正常 1停用）',
  `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '删除标志（0代表存在 2代表删除）',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`role_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '角色信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_role
-- ----------------------------
INSERT INTO `sys_role` VALUES (1, '超级管理员', 'admin', 1, '1', 1, 1, '0', '0', 'admin', '2022-05-31 06:31:32', '', NULL, '超级管理员');
INSERT INTO `sys_role` VALUES (2, '普通角色', 'common', 2, '2', 1, 1, '0', '0', 'admin', '2022-05-31 06:31:32', '', NULL, '普通角色');

-- ----------------------------
-- Table structure for sys_role_dept
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_dept`;
CREATE TABLE `sys_role_dept`  (
  `role_id` bigint(20) NOT NULL COMMENT '角色ID',
  `dept_id` bigint(20) NOT NULL COMMENT '部门ID',
  PRIMARY KEY (`role_id`, `dept_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '角色和部门关联表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_role_dept
-- ----------------------------
INSERT INTO `sys_role_dept` VALUES (2, 100);
INSERT INTO `sys_role_dept` VALUES (2, 101);
INSERT INTO `sys_role_dept` VALUES (2, 105);

-- ----------------------------
-- Table structure for sys_role_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_menu`;
CREATE TABLE `sys_role_menu`  (
  `role_id` bigint(20) NOT NULL COMMENT '角色ID',
  `menu_id` bigint(20) NOT NULL COMMENT '菜单ID',
  PRIMARY KEY (`role_id`, `menu_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '角色和菜单关联表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_role_menu
-- ----------------------------
INSERT INTO `sys_role_menu` VALUES (2, 1);
INSERT INTO `sys_role_menu` VALUES (2, 2);
INSERT INTO `sys_role_menu` VALUES (2, 3);
INSERT INTO `sys_role_menu` VALUES (2, 4);
INSERT INTO `sys_role_menu` VALUES (2, 100);
INSERT INTO `sys_role_menu` VALUES (2, 101);
INSERT INTO `sys_role_menu` VALUES (2, 102);
INSERT INTO `sys_role_menu` VALUES (2, 103);
INSERT INTO `sys_role_menu` VALUES (2, 104);
INSERT INTO `sys_role_menu` VALUES (2, 105);
INSERT INTO `sys_role_menu` VALUES (2, 106);
INSERT INTO `sys_role_menu` VALUES (2, 107);
INSERT INTO `sys_role_menu` VALUES (2, 108);
INSERT INTO `sys_role_menu` VALUES (2, 109);
INSERT INTO `sys_role_menu` VALUES (2, 110);
INSERT INTO `sys_role_menu` VALUES (2, 111);
INSERT INTO `sys_role_menu` VALUES (2, 112);
INSERT INTO `sys_role_menu` VALUES (2, 113);
INSERT INTO `sys_role_menu` VALUES (2, 114);
INSERT INTO `sys_role_menu` VALUES (2, 115);
INSERT INTO `sys_role_menu` VALUES (2, 116);
INSERT INTO `sys_role_menu` VALUES (2, 500);
INSERT INTO `sys_role_menu` VALUES (2, 501);
INSERT INTO `sys_role_menu` VALUES (2, 1000);
INSERT INTO `sys_role_menu` VALUES (2, 1001);
INSERT INTO `sys_role_menu` VALUES (2, 1002);
INSERT INTO `sys_role_menu` VALUES (2, 1003);
INSERT INTO `sys_role_menu` VALUES (2, 1004);
INSERT INTO `sys_role_menu` VALUES (2, 1005);
INSERT INTO `sys_role_menu` VALUES (2, 1006);
INSERT INTO `sys_role_menu` VALUES (2, 1007);
INSERT INTO `sys_role_menu` VALUES (2, 1008);
INSERT INTO `sys_role_menu` VALUES (2, 1009);
INSERT INTO `sys_role_menu` VALUES (2, 1010);
INSERT INTO `sys_role_menu` VALUES (2, 1011);
INSERT INTO `sys_role_menu` VALUES (2, 1012);
INSERT INTO `sys_role_menu` VALUES (2, 1013);
INSERT INTO `sys_role_menu` VALUES (2, 1014);
INSERT INTO `sys_role_menu` VALUES (2, 1015);
INSERT INTO `sys_role_menu` VALUES (2, 1016);
INSERT INTO `sys_role_menu` VALUES (2, 1017);
INSERT INTO `sys_role_menu` VALUES (2, 1018);
INSERT INTO `sys_role_menu` VALUES (2, 1019);
INSERT INTO `sys_role_menu` VALUES (2, 1020);
INSERT INTO `sys_role_menu` VALUES (2, 1021);
INSERT INTO `sys_role_menu` VALUES (2, 1022);
INSERT INTO `sys_role_menu` VALUES (2, 1023);
INSERT INTO `sys_role_menu` VALUES (2, 1024);
INSERT INTO `sys_role_menu` VALUES (2, 1025);
INSERT INTO `sys_role_menu` VALUES (2, 1026);
INSERT INTO `sys_role_menu` VALUES (2, 1027);
INSERT INTO `sys_role_menu` VALUES (2, 1028);
INSERT INTO `sys_role_menu` VALUES (2, 1029);
INSERT INTO `sys_role_menu` VALUES (2, 1030);
INSERT INTO `sys_role_menu` VALUES (2, 1031);
INSERT INTO `sys_role_menu` VALUES (2, 1032);
INSERT INTO `sys_role_menu` VALUES (2, 1033);
INSERT INTO `sys_role_menu` VALUES (2, 1034);
INSERT INTO `sys_role_menu` VALUES (2, 1035);
INSERT INTO `sys_role_menu` VALUES (2, 1036);
INSERT INTO `sys_role_menu` VALUES (2, 1037);
INSERT INTO `sys_role_menu` VALUES (2, 1038);
INSERT INTO `sys_role_menu` VALUES (2, 1039);
INSERT INTO `sys_role_menu` VALUES (2, 1040);
INSERT INTO `sys_role_menu` VALUES (2, 1041);
INSERT INTO `sys_role_menu` VALUES (2, 1042);
INSERT INTO `sys_role_menu` VALUES (2, 1043);
INSERT INTO `sys_role_menu` VALUES (2, 1044);
INSERT INTO `sys_role_menu` VALUES (2, 1045);
INSERT INTO `sys_role_menu` VALUES (2, 1046);
INSERT INTO `sys_role_menu` VALUES (2, 1047);
INSERT INTO `sys_role_menu` VALUES (2, 1048);
INSERT INTO `sys_role_menu` VALUES (2, 1049);
INSERT INTO `sys_role_menu` VALUES (2, 1050);
INSERT INTO `sys_role_menu` VALUES (2, 1051);
INSERT INTO `sys_role_menu` VALUES (2, 1052);
INSERT INTO `sys_role_menu` VALUES (2, 1053);
INSERT INTO `sys_role_menu` VALUES (2, 1054);
INSERT INTO `sys_role_menu` VALUES (2, 1055);
INSERT INTO `sys_role_menu` VALUES (2, 1056);
INSERT INTO `sys_role_menu` VALUES (2, 1057);
INSERT INTO `sys_role_menu` VALUES (2, 1058);
INSERT INTO `sys_role_menu` VALUES (2, 1059);
INSERT INTO `sys_role_menu` VALUES (2, 1060);

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user`  (
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `dept_id` bigint(20) NULL DEFAULT NULL COMMENT '部门ID',
  `user_name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户账号',
  `nick_name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户昵称',
  `user_type` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT 'sys_user' COMMENT '用户类型（sys_user系统用户）',
  `email` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '用户邮箱',
  `phonenumber` varchar(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '手机号码',
  `sex` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '用户性别（0男 1女 2未知）',
  `avatar` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '头像地址',
  `password` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '密码',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '帐号状态（0正常 1停用）',
  `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '删除标志（0代表存在 2代表删除）',
  `login_ip` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '最后登录IP',
  `login_date` datetime(0) NULL DEFAULT NULL COMMENT '最后登录时间',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`user_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` VALUES (1, 103, 'admin', '疯狂的狮子Li', 'sys_user', 'crazyLionLi@163.com', '15888888888', '1', '', '$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE8ByOhJIrdAu2', '0', '0', '127.0.0.1', '2022-05-31 06:31:32', 'admin', '2022-05-31 06:31:32', '', NULL, '管理员');
INSERT INTO `sys_user` VALUES (2, 105, 'lionli', '疯狂的狮子Li', 'sys_user', 'crazyLionLi@qq.com', '15666666666', '1', '', '$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE8ByOhJIrdAu2', '0', '0', '127.0.0.1', '2022-05-31 06:31:32', 'admin', '2022-05-31 06:31:32', '', NULL, '测试员');

-- ----------------------------
-- Table structure for sys_user_post
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_post`;
CREATE TABLE `sys_user_post`  (
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `post_id` bigint(20) NOT NULL COMMENT '岗位ID',
  PRIMARY KEY (`user_id`, `post_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户与岗位关联表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_user_post
-- ----------------------------
INSERT INTO `sys_user_post` VALUES (1, 1);
INSERT INTO `sys_user_post` VALUES (2, 2);

-- ----------------------------
-- Table structure for sys_user_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role`  (
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `role_id` bigint(20) NOT NULL COMMENT '角色ID',
  PRIMARY KEY (`user_id`, `role_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户和角色关联表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_user_role
-- ----------------------------
INSERT INTO `sys_user_role` VALUES (1, 1);
INSERT INTO `sys_user_role` VALUES (2, 2);

-- ----------------------------
-- Table structure for undo_log
-- ----------------------------
DROP TABLE IF EXISTS `undo_log`;
CREATE TABLE `undo_log`  (
  `branch_id` bigint(20) NOT NULL COMMENT 'branch transaction id',
  `xid` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'global transaction id',
  `context` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'undo_log context,such as serialization',
  `rollback_info` longblob NOT NULL COMMENT 'rollback info',
  `log_status` int(11) NOT NULL COMMENT '0:normal status,1:defense status',
  `log_created` datetime(6) NOT NULL COMMENT 'create datetime',
  `log_modified` datetime(6) NOT NULL COMMENT 'modify datetime',
  UNIQUE INDEX `ux_undo_log`(`xid`, `branch_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = 'AT transaction mode undo table' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of undo_log
-- ----------------------------

-- ----------------------------
-- Table structure for user_communication_message
-- ----------------------------
DROP TABLE IF EXISTS `user_communication_message`;
CREATE TABLE `user_communication_message`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `send_message_id` bigint(20) NOT NULL COMMENT '发送消息用户;用户信息ID',
  `messageContent` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '消息内容',
  `send_from_me_status` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否由我发送;0->否，1->是',
  `search_value` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '搜索值',
  `deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '逻辑删除;0->未删除，1->删除',
  `create_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `create_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '创建者',
  `update_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  `update_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '更新者',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '沟通消息' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user_communication_message
-- ----------------------------

-- ----------------------------
-- Table structure for user_contact_relation
-- ----------------------------
DROP TABLE IF EXISTS `user_contact_relation`;
CREATE TABLE `user_contact_relation`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '好友关系ID',
  `user_info_id` bigint(20) NOT NULL COMMENT '代表当前用户;用户信息ID',
  `contact_info_id` bigint(20) NOT NULL COMMENT '代表人脉用户ID;用户信息ID',
  `exchange_phone_status` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否交换手机号码;0->否，1->是',
  `search_value` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '搜索值',
  `deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '逻辑删除;0->未删除，1->删除',
  `create_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `create_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '创建者',
  `update_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  `update_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '更新者',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '好友关系' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user_contact_relation
-- ----------------------------

-- ----------------------------
-- Table structure for user_contact_relation_status
-- ----------------------------
DROP TABLE IF EXISTS `user_contact_relation_status`;
CREATE TABLE `user_contact_relation_status`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '好友状态ID',
  `user_info_id` bigint(20) NOT NULL COMMENT '代表当前用户;用户信息ID',
  `contact_info_id` bigint(20) NOT NULL COMMENT '代表人脉用户ID;用户信息ID',
  `send_from_me_status` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否是我发送的请求;0->否，1->是',
  `approved_status` tinyint(1) NOT NULL DEFAULT 0 COMMENT '审核通过状态;1->审核中，2->已通过，3->拒绝',
  `search_value` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '搜索值',
  `deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '逻辑删除;0->未删除，1->删除',
  `create_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `create_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '创建者',
  `update_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  `update_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '更新者',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '好友关系状态' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user_contact_relation_status
-- ----------------------------

-- ----------------------------
-- Table structure for user_education
-- ----------------------------
DROP TABLE IF EXISTS `user_education`;
CREATE TABLE `user_education`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '学历ID',
  `user_info_id` bigint(20) NOT NULL COMMENT '代表当前用户;用户信息ID',
  `name` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '学校名称',
  `admissionTime` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '入学时间',
  `graduationTime` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '毕业时间',
  `major` tinyint(1) NOT NULL DEFAULT 0 COMMENT '专业',
  `education` tinyint(1) NOT NULL DEFAULT 0 COMMENT '学历',
  `introduction` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '说明介绍',
  `search_value` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '搜索值',
  `deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '逻辑删除;0->未删除，1->删除',
  `create_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `create_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '创建者',
  `update_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  `update_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '更新者',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '学历' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user_education
-- ----------------------------
INSERT INTO `user_education` VALUES (1, 22, '李欢', '2013-09-01 00:00:00', '2017-01-01 00:00:00', 1, 1, '网络教育', '10', 0, '2022-06-10 19:06:52', '', '2022-06-10 19:08:23', '');

-- ----------------------------
-- Table structure for user_info
-- ----------------------------
DROP TABLE IF EXISTS `user_info`;
CREATE TABLE `user_info`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '用户信息ID',
  `name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '姓名',
  `phone` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '手机号码',
  `avatar` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '头像',
  `effectCount` int(11) NOT NULL DEFAULT 0 COMMENT '影响力',
  `visitorCount` int(11) NOT NULL DEFAULT 0 COMMENT '访客数量',
  `company` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '公司',
  `position` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '职位',
  `selfIntroduction` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '自我介绍',
  `careerDirection` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '职业方向',
  `location` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '所在位置',
  `hometown` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '家乡',
  `constellation` tinyint(1) NOT NULL DEFAULT 0 COMMENT '星座;1->白羊，2->金牛，3->双子，4->巨蝎，5->狮子',
  `email` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '邮箱',
  `search_value` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '搜索值',
  `deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '逻辑删除;0->未删除，1->删除',
  `create_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `create_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '创建者',
  `update_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  `update_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '更新者',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户信息' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user_info
-- ----------------------------

-- ----------------------------
-- Table structure for user_interested_to_me
-- ----------------------------
DROP TABLE IF EXISTS `user_interested_to_me`;
CREATE TABLE `user_interested_to_me`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '对我感兴趣ID',
  `user_info_id` bigint(20) NOT NULL COMMENT '代表当前用户;用户信息ID',
  `contact_info_id` bigint(20) NOT NULL COMMENT '代表人脉用户ID;用户信息ID',
  `search_value` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '搜索值',
  `deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '逻辑删除;0->未删除，1->删除',
  `create_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `create_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '创建者',
  `update_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  `update_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '更新者',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1535248721590550530 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '对我感兴趣（收藏人脉卡片）' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user_interested_to_me
-- ----------------------------
INSERT INTO `user_interested_to_me` VALUES (1535248721590550529, 11, 223322, '1141', 0, '2022-06-10 21:13:13', 'admin', '2022-06-10 21:13:13', 'admin');

-- ----------------------------
-- Table structure for user_occupation_label
-- ----------------------------
DROP TABLE IF EXISTS `user_occupation_label`;
CREATE TABLE `user_occupation_label`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `user_info_id` bigint(20) NOT NULL COMMENT '代表当前用户;用户信息ID',
  `occupation_label` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '职业标签',
  `search_value` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '搜索值',
  `deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '逻辑删除;0->未删除，1->删除',
  `create_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `create_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '创建者',
  `update_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  `update_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '更新者',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1535212929455362051 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '人脉职业标签' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user_occupation_label
-- ----------------------------
INSERT INTO `user_occupation_label` VALUES (1535207283829903362, 12, '很棒', '2', 0, '2022-06-10 18:28:34', 'admin', '2022-06-10 18:28:34', 'admin');
INSERT INTO `user_occupation_label` VALUES (1535212929455362050, 3322, '游戏人生', '2332', 0, '2022-06-10 18:51:00', 'admin', '2022-06-10 18:51:00', 'admin');

-- ----------------------------
-- Table structure for user_work_experience
-- ----------------------------
DROP TABLE IF EXISTS `user_work_experience`;
CREATE TABLE `user_work_experience`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '工作经历ID',
  `user_info_id` bigint(20) NOT NULL COMMENT '代表当前用户;用户信息ID',
  `company` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '公司',
  `position` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '职位',
  `entry_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '入职时间',
  `departure_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '离职时间',
  `length_of_employment` int(11) NOT NULL DEFAULT 0 COMMENT '就职时长',
  `introduction` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '说明介绍',
  `search_value` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '搜索值',
  `deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '逻辑删除;0->未删除，1->删除',
  `create_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `create_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '创建者',
  `update_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  `update_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '更新者',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '工作经历' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user_work_experience
-- ----------------------------

SET FOREIGN_KEY_CHECKS = 1;
