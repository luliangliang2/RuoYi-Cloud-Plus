CREATE TABLE cognition_scene
(
    id             BIGINT       NOT NULL COMMENT '主键',
    scene_name     VARCHAR(200) NOT NULL COMMENT '场景名称',
    description    TEXT NULL COMMENT '场景描述',
    cover_image_id BIGINT NULL COMMENT '封面图片ID',

    tenant_id      VARCHAR(20) DEFAULT '000000' COMMENT '租户编号',
    create_dept    BIGINT NULL DEFAULT NULL COMMENT '创建部门',
    create_time    DATETIME NULL DEFAULT NULL COMMENT '创建时间',
    create_by      BIGINT NULL DEFAULT NULL COMMENT '创建人',
    update_time    DATETIME NULL DEFAULT NULL COMMENT '更新时间',
    update_by      BIGINT NULL DEFAULT NULL COMMENT '更新人',
    del_flag       INT         DEFAULT 0 COMMENT '删除标志',

    PRIMARY KEY (id) USING BTREE
) ENGINE=InnoDB COMMENT='认知场景表';

CREATE TABLE cognition_scene_step
(
    id          BIGINT       NOT NULL COMMENT '主键',
    scene_id    BIGINT       NOT NULL COMMENT '场景ID',
    step_order  INT          NOT NULL COMMENT '步骤序号',
    title       VARCHAR(200) NOT NULL COMMENT '步骤标题',
    description TEXT NULL COMMENT '步骤讲解文字',

    image_id    BIGINT NULL COMMENT '步骤图片ID',
    video_id    BIGINT NULL COMMENT '步骤视频ID',

    tenant_id   VARCHAR(20) DEFAULT '000000' COMMENT '租户编号',
    create_dept BIGINT NULL DEFAULT NULL COMMENT '创建部门',
    create_time DATETIME NULL DEFAULT NULL COMMENT '创建时间',
    create_by   BIGINT NULL DEFAULT NULL COMMENT '创建人',
    update_time DATETIME NULL DEFAULT NULL COMMENT '更新时间',
    update_by   BIGINT NULL DEFAULT NULL COMMENT '更新人',
    del_flag    INT         DEFAULT 0 COMMENT '删除标志',

    PRIMARY KEY (id) USING BTREE,
    KEY         idx_scene (scene_id),
    KEY         idx_order (scene_id, step_order)
) ENGINE=InnoDB COMMENT='认知场景步骤表';

CREATE TABLE cognition_user_progress
(
    id           BIGINT NOT NULL COMMENT '主键',
    scene_id     BIGINT NOT NULL COMMENT '场景ID',
    step_id      BIGINT NULL COMMENT '当前步骤ID',
    user_id      BIGINT NOT NULL COMMENT '用户ID',
    is_completed TINYINT     DEFAULT 0 COMMENT '是否完成整个场景 0未完成 1已完成',

    tenant_id    VARCHAR(20) DEFAULT '000000' COMMENT '租户编号',
    create_time  DATETIME NULL DEFAULT NULL COMMENT '创建时间',
    update_time  DATETIME NULL DEFAULT NULL COMMENT '更新时间',

    PRIMARY KEY (id) USING BTREE,
    KEY          idx_user_scene (user_id, scene_id)
) ENGINE=InnoDB COMMENT='用户场景学习进度表';
