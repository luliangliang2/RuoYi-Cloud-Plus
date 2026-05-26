INSERT INTO biz_tree_category_bind (bind_id, tenant_id, business_type, business_id, tree_id, node_id, del_flag, create_time)
SELECT UUID_SHORT(), v.tenant_id, 'vehicle', v.id, v.tree_id, v.category_node_id, '0', SYSDATE()
FROM biz_vehicle v
WHERE v.del_flag = 0
  AND v.category_node_id IS NOT NULL
  AND NOT EXISTS (
      SELECT 1 FROM biz_tree_category_bind b
      WHERE b.tenant_id = v.tenant_id
        AND b.business_type = 'vehicle'
        AND b.business_id = v.id
        AND b.node_id = v.category_node_id
        AND b.del_flag = '0'
  );

INSERT INTO biz_tree_category_bind (bind_id, tenant_id, business_type, business_id, tree_id, node_id, del_flag, create_time)
SELECT UUID_SHORT(), c.tenant_id, 'camera', c.camera_id, c.tree_id, c.category_node_id, '0', SYSDATE()
FROM biz_camera c
WHERE c.del_flag = '0'
  AND c.category_node_id IS NOT NULL
  AND NOT EXISTS (
      SELECT 1 FROM biz_tree_category_bind b
      WHERE b.tenant_id = c.tenant_id
        AND b.business_type = 'camera'
        AND b.business_id = c.camera_id
        AND b.node_id = c.category_node_id
        AND b.del_flag = '0'
  );

INSERT INTO biz_tree_category_bind (bind_id, tenant_id, business_type, business_id, tree_id, node_id, del_flag, create_time)
SELECT UUID_SHORT(), r.tenant_id, 'radar', r.radar_id, r.tree_id, r.category_node_id, '0', SYSDATE()
FROM biz_radar r
WHERE r.del_flag = '0'
  AND r.category_node_id IS NOT NULL
  AND NOT EXISTS (
      SELECT 1 FROM biz_tree_category_bind b
      WHERE b.tenant_id = r.tenant_id
        AND b.business_type = 'radar'
        AND b.business_id = r.radar_id
        AND b.node_id = r.category_node_id
        AND b.del_flag = '0'
  );

INSERT INTO biz_tree_category_bind (bind_id, tenant_id, business_type, business_id, tree_id, node_id, del_flag, create_time)
SELECT UUID_SHORT(), s.tenant_id, 'simCard', s.sim_id, s.tree_id, s.category_node_id, '0', SYSDATE()
FROM biz_sim_card s
WHERE s.del_flag = '0'
  AND s.category_node_id IS NOT NULL
  AND NOT EXISTS (
      SELECT 1 FROM biz_tree_category_bind b
      WHERE b.tenant_id = s.tenant_id
        AND b.business_type = 'simCard'
        AND b.business_id = s.sim_id
        AND b.node_id = s.category_node_id
        AND b.del_flag = '0'
  );

INSERT INTO biz_tree_category_bind (bind_id, tenant_id, business_type, business_id, tree_id, node_id, del_flag, create_time)
SELECT UUID_SHORT(), c.tenant_id, 'commandConfig', c.command_id, c.tree_id, c.category_node_id, '0', SYSDATE()
FROM biz_command_config c
WHERE c.del_flag = '0'
  AND c.category_node_id IS NOT NULL
  AND NOT EXISTS (
      SELECT 1 FROM biz_tree_category_bind b
      WHERE b.tenant_id = c.tenant_id
        AND b.business_type = 'commandConfig'
        AND b.business_id = c.command_id
        AND b.node_id = c.category_node_id
        AND b.del_flag = '0'
  );

INSERT INTO biz_tree_category_bind (bind_id, tenant_id, business_type, business_id, tree_id, node_id, del_flag, create_time)
SELECT UUID_SHORT(), a.tenant_id, 'sceneArea', a.area_id, a.tree_id, a.category_node_id, '0', SYSDATE()
FROM biz_scene_area a
WHERE a.del_flag = '0'
  AND a.category_node_id IS NOT NULL
  AND NOT EXISTS (
      SELECT 1 FROM biz_tree_category_bind b
      WHERE b.tenant_id = a.tenant_id
        AND b.business_type = 'sceneArea'
        AND b.business_id = a.area_id
        AND b.node_id = a.category_node_id
        AND b.del_flag = '0'
  );

INSERT INTO biz_tree_category_bind (bind_id, tenant_id, business_type, business_id, tree_id, node_id, del_flag, create_time)
SELECT UUID_SHORT(), r.tenant_id, 'sceneRoute', r.route_id, r.tree_id, r.category_node_id, '0', SYSDATE()
FROM biz_scene_route r
WHERE r.del_flag = '0'
  AND r.category_node_id IS NOT NULL
  AND NOT EXISTS (
      SELECT 1 FROM biz_tree_category_bind b
      WHERE b.tenant_id = r.tenant_id
        AND b.business_type = 'sceneRoute'
        AND b.business_id = r.route_id
        AND b.node_id = r.category_node_id
        AND b.del_flag = '0'
  );

INSERT INTO biz_tree_category_bind (bind_id, tenant_id, business_type, business_id, tree_id, node_id, del_flag, create_time)
SELECT UUID_SHORT(), p.tenant_id, 'scenePoint', p.point_id, p.tree_id, p.category_node_id, '0', SYSDATE()
FROM biz_scene_point p
WHERE p.del_flag = '0'
  AND p.category_node_id IS NOT NULL
  AND NOT EXISTS (
      SELECT 1 FROM biz_tree_category_bind b
      WHERE b.tenant_id = p.tenant_id
        AND b.business_type = 'scenePoint'
        AND b.business_id = p.point_id
        AND b.node_id = p.category_node_id
        AND b.del_flag = '0'
  );
