CREATE TABLE IF NOT EXISTS biz_tree_category_bind
(
    bind_id       BIGINT       NOT NULL COMMENT '绑定ID',
    tenant_id     VARCHAR(20)  DEFAULT '000000' COMMENT '租户编号',
    business_type VARCHAR(64)  NOT NULL COMMENT '业务类型',
    business_id   BIGINT       NOT NULL COMMENT '业务ID',
    tree_id       BIGINT       DEFAULT NULL COMMENT '分类树ID',
    node_id       BIGINT       NOT NULL COMMENT '分类节点ID',
    del_flag      CHAR(1)      DEFAULT '0' COMMENT '删除标志（0代表存在 1代表删除）',
    create_dept   BIGINT       DEFAULT NULL COMMENT '创建部门',
    create_by     BIGINT       DEFAULT NULL COMMENT '创建者',
    create_time   DATETIME     DEFAULT NULL COMMENT '创建时间',
    update_by     BIGINT       DEFAULT NULL COMMENT '更新者',
    update_time   DATETIME     DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (bind_id),
    KEY idx_biz_tree_category_bind_business (tenant_id, business_type, business_id, del_flag),
    KEY idx_biz_tree_category_bind_node (tenant_id, business_type, node_id, del_flag)
) ENGINE=InnoDB COMMENT='分类业务绑定表';

INSERT INTO biz_tree_category_bind (bind_id, tenant_id, business_type, business_id, tree_id, node_id, del_flag, create_time)
SELECT UUID_SHORT(), v.tenant_id, 'vehicle', v.id, v.tree_id, v.category_node_id, '0', SYSDATE()
FROM biz_vehicle v
WHERE v.del_flag = 0
  AND v.category_node_id IS NOT NULL
  AND NOT EXISTS (
      SELECT 1
      FROM biz_tree_category_bind b
      WHERE b.tenant_id = v.tenant_id
        AND b.business_type = 'vehicle'
        AND b.business_id = v.id
        AND b.node_id = v.category_node_id
        AND b.del_flag = '0'
  );
