package org.dromara.manager.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.dromara.common.mybatis.core.mapper.BaseMapperPlus;
import org.dromara.common.mybatis.helper.DataBaseHelper;
import org.dromara.manager.domain.BizTreeNode;
import org.dromara.manager.domain.vo.BizTreeNodeVo;

import java.util.List;

/**
 * 维护树节点Mapper接口
 *
 * @author LionLi
 * @date 2026-05-22
 */
public interface BizTreeNodeMapper extends BaseMapperPlus<BizTreeNode, BizTreeNodeVo> {

    /**
     * 查询节点及子节点
     *
     * @param treeId 树ID
     * @param nodeId 节点ID
     * @return 节点列表
     */
    default List<BizTreeNode> selectNodeAndChildById(Long treeId, Long nodeId) {
        return this.selectList(new LambdaQueryWrapper<BizTreeNode>()
            .eq(BizTreeNode::getTreeId, treeId)
            .and(wrapper -> wrapper
                .eq(BizTreeNode::getNodeId, nodeId)
                .or()
                .apply(DataBaseHelper.findInSet(nodeId, "ancestors"))));
    }

}
