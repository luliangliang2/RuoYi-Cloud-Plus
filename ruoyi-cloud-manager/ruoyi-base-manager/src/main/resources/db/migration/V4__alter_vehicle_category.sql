-- ----------------------------
-- 车辆分类字段
-- ----------------------------
ALTER TABLE biz_vehicle
    ADD COLUMN tree_id BIGINT DEFAULT NULL COMMENT '分类树ID' AFTER tenant_id,
    ADD COLUMN category_node_id BIGINT DEFAULT NULL COMMENT '分类节点ID' AFTER tree_id;

ALTER TABLE biz_vehicle
    ADD KEY idx_biz_vehicle_category (tenant_id, tree_id, category_node_id);
