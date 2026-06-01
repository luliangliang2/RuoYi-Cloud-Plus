package org.dromara.manager.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.RequiredArgsConstructor;
import org.dromara.common.core.constant.Constants;
import org.dromara.common.core.utils.StringUtils;
import org.dromara.manager.api.domain.bo.BizVehicleBo;
import org.dromara.manager.api.domain.vo.BizVehicleVo;
import org.dromara.manager.domain.BizTreeDef;
import org.dromara.manager.domain.BizTreeNode;
import org.dromara.manager.domain.vo.BizVehicleMonitorTreeVo;
import org.dromara.manager.mapper.BizTreeDefMapper;
import org.dromara.manager.mapper.BizTreeNodeMapper;
import org.dromara.manager.service.IBizVehicleMonitorService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 车辆监控Service业务层处理
 *
 * @author LionLi
 * @date 2026-06-01
 */
@RequiredArgsConstructor
@Service
public class BizVehicleMonitorServiceImpl implements IBizVehicleMonitorService {

    private static final String MODULE_CODE = "vehicle";
    private static final String CATEGORY_ICON = "heroicons:folder";
    private static final String VEHICLE_ICON = "heroicons:truck-solid";

    private final BizTreeDefMapper treeDefMapper;
    private final BizTreeNodeMapper treeNodeMapper;
    private final BizVehicleServiceImpl vehicleService;

    @Override
    public List<BizVehicleMonitorTreeVo> queryVehicleTree(Long treeId, String keyword) {
        BizTreeDef treeDef = resolveTreeDef(treeId);
        if (treeDef == null) {
            return List.of();
        }

        List<BizTreeNode> nodes = treeNodeMapper.selectList(new LambdaQueryWrapper<BizTreeNode>()
            .eq(BizTreeNode::getTreeId, treeDef.getTreeId())
            .eq(BizTreeNode::getStatus, "0")
            .orderByAsc(BizTreeNode::getParentId)
            .orderByAsc(BizTreeNode::getOrderNum)
            .orderByAsc(BizTreeNode::getNodeCode));

        Map<Long, BizVehicleMonitorTreeVo> nodeMap = new LinkedHashMap<>();
        for (BizTreeNode node : nodes) {
            nodeMap.put(node.getNodeId(), buildCategoryNode(node));
        }

        List<BizVehicleMonitorTreeVo> rootList = buildCategoryTree(nodes, nodeMap);
        BizVehicleBo bo = new BizVehicleBo();
        bo.setTreeId(treeDef.getTreeId());
        List<BizVehicleVo> vehicles = vehicleService.queryList(bo).stream()
            .filter(vehicle -> matchKeyword(vehicle, keyword))
            .toList();
        appendVehicleLeaves(rootList, nodeMap, treeDef.getTreeId(), vehicles);
        removeEmptyCategory(rootList);
        return rootList;
    }

    private BizTreeDef resolveTreeDef(Long treeId) {
        if (treeId != null) {
            return treeDefMapper.selectById(treeId);
        }
        return treeDefMapper.selectOne(new LambdaQueryWrapper<BizTreeDef>()
            .eq(BizTreeDef::getModuleCode, MODULE_CODE)
            .eq(BizTreeDef::getStatus, "0")
            .orderByAsc(BizTreeDef::getTreeCode)
            .last("limit 1"));
    }

    private List<BizVehicleMonitorTreeVo> buildCategoryTree(List<BizTreeNode> nodes,
                                                            Map<Long, BizVehicleMonitorTreeVo> nodeMap) {
        List<BizVehicleMonitorTreeVo> rootList = new ArrayList<>();
        for (BizTreeNode node : nodes) {
            BizVehicleMonitorTreeVo current = nodeMap.get(node.getNodeId());
            if (Constants.TOP_PARENT_ID.equals(node.getParentId()) || !nodeMap.containsKey(node.getParentId())) {
                rootList.add(current);
            } else {
                nodeMap.get(node.getParentId()).getChildren().add(current);
            }
        }
        return rootList;
    }

    private BizVehicleMonitorTreeVo buildCategoryNode(BizTreeNode node) {
        BizVehicleMonitorTreeVo vo = new BizVehicleMonitorTreeVo();
        vo.setKey("node-" + node.getNodeId());
        vo.setTitle(node.getNodeName());
        vo.setType("category");
        vo.setIcon(CATEGORY_ICON);
        vo.setTreeId(node.getTreeId());
        vo.setNodeId(node.getNodeId());
        return vo;
    }

    private void appendVehicleLeaves(List<BizVehicleMonitorTreeVo> rootList,
                                     Map<Long, BizVehicleMonitorTreeVo> nodeMap,
                                     Long treeId,
                                     List<BizVehicleVo> vehicles) {
        BizVehicleMonitorTreeVo uncategorized = null;
        for (BizVehicleVo vehicle : vehicles) {
            List<Long> nodeIds = CollUtil.isNotEmpty(vehicle.getCategoryNodeIds())
                ? vehicle.getCategoryNodeIds()
                : vehicle.getCategoryNodeId() == null ? List.of() : List.of(vehicle.getCategoryNodeId());
            List<Long> matchedNodeIds = nodeIds.stream()
                .filter(nodeMap::containsKey)
                .distinct()
                .toList();
            if (matchedNodeIds.isEmpty()) {
                if (uncategorized == null) {
                    uncategorized = buildUncategorizedNode(treeId);
                    rootList.add(uncategorized);
                }
                uncategorized.getChildren().add(buildVehicleNode(vehicle));
                continue;
            }
            for (Long nodeId : matchedNodeIds) {
                nodeMap.get(nodeId).getChildren().add(buildVehicleNode(vehicle));
            }
        }
    }

    private BizVehicleMonitorTreeVo buildUncategorizedNode(Long treeId) {
        BizVehicleMonitorTreeVo vo = new BizVehicleMonitorTreeVo();
        vo.setKey("node-uncategorized");
        vo.setTitle("未分类车辆");
        vo.setType("category");
        vo.setIcon(CATEGORY_ICON);
        vo.setTreeId(treeId);
        return vo;
    }

    private BizVehicleMonitorTreeVo buildVehicleNode(BizVehicleVo vehicle) {
        if (vehicle.getOnline() == null) {
            vehicle.setOnline(false);
        }
        BizVehicleMonitorTreeVo vo = new BizVehicleMonitorTreeVo();
        vo.setKey("vehicle-" + vehicle.getId());
        vo.setTitle(StringUtils.blankToDefault(vehicle.getPlateNo(), vehicle.getVin()));
        vo.setType("vehicle");
        vo.setIcon(VEHICLE_ICON);
        vo.setTreeId(vehicle.getTreeId());
        vo.setNodeId(vehicle.getCategoryNodeId());
        vo.setVehicleId(vehicle.getId());
        vo.setVehicle(vehicle);
        return vo;
    }

    private boolean matchKeyword(BizVehicleVo vehicle, String keyword) {
        if (StringUtils.isBlank(keyword)) {
            return true;
        }
        return StringUtils.containsIgnoreCase(vehicle.getVin(), keyword)
            || StringUtils.containsIgnoreCase(vehicle.getPlateNo(), keyword);
    }

    private boolean removeEmptyCategory(List<BizVehicleMonitorTreeVo> nodes) {
        nodes.removeIf(node -> {
            if (Objects.equals(node.getType(), "vehicle")) {
                return false;
            }
            removeEmptyCategory(node.getChildren());
            return node.getChildren().isEmpty();
        });
        nodes.sort(Comparator.comparing(BizVehicleMonitorTreeVo::getType).thenComparing(BizVehicleMonitorTreeVo::getTitle));
        return nodes.isEmpty();
    }

}
