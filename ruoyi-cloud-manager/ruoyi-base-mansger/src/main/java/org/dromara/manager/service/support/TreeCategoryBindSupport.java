package org.dromara.manager.service.support;

import lombok.RequiredArgsConstructor;
import org.dromara.manager.domain.BizTreeCategoryBind;
import org.dromara.manager.mapper.BizTreeCategoryBindMapper;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 分类业务绑定支持
 *
 * @author LionLi
 * @date 2026-05-25
 */
@RequiredArgsConstructor
@Component
public class TreeCategoryBindSupport {

    private final BizTreeCategoryBindMapper categoryBindMapper;

    /**
     * 归一化分类节点集合，兼容旧的单分类字段。
     */
    public List<Long> normalize(Long categoryNodeId, List<Long> categoryNodeIds) {
        List<Long> nodeIds = new ArrayList<>();
        if (categoryNodeIds != null) {
            nodeIds.addAll(categoryNodeIds.stream().filter(Objects::nonNull).distinct().toList());
        } else if (categoryNodeId != null) {
            nodeIds.add(categoryNodeId);
        }
        return nodeIds;
    }

    /**
     * 保存业务绑定分类。
     */
    public void save(String businessType, Long businessId, Long treeId, List<Long> nodeIds) {
        categoryBindMapper.deleteByBusiness(businessType, businessId);
        if (nodeIds == null || nodeIds.isEmpty()) {
            return;
        }
        for (Long nodeId : nodeIds) {
            BizTreeCategoryBind bind = new BizTreeCategoryBind();
            bind.setBusinessType(businessType);
            bind.setBusinessId(businessId);
            bind.setTreeId(treeId);
            bind.setNodeId(nodeId);
            categoryBindMapper.insert(bind);
        }
    }

    /**
     * 查询业务绑定分类。
     */
    public List<Long> getNodeIds(String businessType, Long businessId, Long fallbackNodeId) {
        List<Long> nodeIds = categoryBindMapper.selectNodeIds(businessType, businessId);
        if (nodeIds.isEmpty() && fallbackNodeId != null) {
            return List.of(fallbackNodeId);
        }
        return nodeIds;
    }

    /**
     * 删除多个业务的分类绑定。
     */
    public void deleteByBusinessIds(String businessType, java.util.Collection<Long> businessIds) {
        categoryBindMapper.deleteByBusinessIds(businessType, businessIds);
    }

}
