-- ----------------------------
-- 维护树分类类型与选择模式
-- ----------------------------
ALTER TABLE biz_tree_def
    ADD COLUMN select_mode VARCHAR(16) DEFAULT 'single' COMMENT '选择模式（single单选 multiple多选）' AFTER tree_type;

UPDATE biz_tree_def
SET tree_type = 'business'
WHERE tree_type IS NULL
   OR tree_type = ''
   OR tree_type = 'common';

UPDATE biz_tree_def
SET select_mode = 'single'
WHERE select_mode IS NULL
   OR select_mode = '';
