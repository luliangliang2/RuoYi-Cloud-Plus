package org.dromara.manager.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dromara.common.core.constant.Constants;
import org.dromara.common.core.exception.ServiceException;
import org.dromara.common.core.utils.MapstructUtils;
import org.dromara.common.core.utils.StringUtils;
import org.dromara.common.mybatis.helper.DataBaseHelper;
import org.dromara.manager.domain.BizTreeDef;
import org.dromara.manager.domain.BizTreeNode;
import org.dromara.manager.domain.bo.BizTreeNodeBo;
import org.dromara.manager.domain.vo.BizTreeNodeVo;
import org.dromara.manager.mapper.BizTreeDefMapper;
import org.dromara.manager.mapper.BizTreeNodeMapper;
import org.dromara.manager.service.IBizTreeNodeService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * 维护树节点Service业务层处理
 *
 * @author LionLi
 * @date 2026-05-22
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class BizTreeNodeServiceImpl implements IBizTreeNodeService {

    private final BizTreeDefMapper treeDefMapper;
    private final BizTreeNodeMapper baseMapper;

    /**
     * 查询维护树节点
     *
     * @param nodeId 主键
     * @return 维护树节点
     */
    @Override
    public BizTreeNodeVo queryById(Long nodeId) {
        return baseMapper.selectVoById(nodeId);
    }

    /**
     * 查询符合条件的维护树节点列表
     *
     * @param bo 查询条件
     * @return 维护树节点列表
     */
    @Override
    public List<BizTreeNodeVo> queryList(BizTreeNodeBo bo) {
        LambdaQueryWrapper<BizTreeNode> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    /**
     * 查询指定树的可选父节点列表
     *
     * @param treeId 树ID
     * @param nodeId 当前节点ID
     * @return 维护树节点列表
     */
    @Override
    public List<BizTreeNodeVo> querySelectableList(Long treeId, Long nodeId) {
        LambdaQueryWrapper<BizTreeNode> lqw = Wrappers.lambdaQuery();
        lqw.eq(BizTreeNode::getTreeId, treeId);
        lqw.orderByAsc(BizTreeNode::getParentId);
        lqw.orderByAsc(BizTreeNode::getOrderNum);
        List<BizTreeNodeVo> list = baseMapper.selectVoList(lqw);
        if (nodeId == null) {
            return list;
        }
        String nodeIdStr = Convert.toStr(nodeId);
        return list.stream()
            .filter(node -> !Objects.equals(node.getNodeId(), nodeId))
            .filter(node -> !StringUtils.splitList(node.getAncestors(), StringUtils.SEPARATOR).contains(nodeIdStr))
            .toList();
    }

    private LambdaQueryWrapper<BizTreeNode> buildQueryWrapper(BizTreeNodeBo bo) {
        LambdaQueryWrapper<BizTreeNode> lqw = Wrappers.lambdaQuery();
        lqw.eq(bo.getTreeId() != null, BizTreeNode::getTreeId, bo.getTreeId());
        lqw.eq(bo.getParentId() != null, BizTreeNode::getParentId, bo.getParentId());
        lqw.like(StringUtils.isNotBlank(bo.getNodeCode()), BizTreeNode::getNodeCode, bo.getNodeCode());
        lqw.like(StringUtils.isNotBlank(bo.getNodeName()), BizTreeNode::getNodeName, bo.getNodeName());
        lqw.eq(StringUtils.isNotBlank(bo.getNodeType()), BizTreeNode::getNodeType, bo.getNodeType());
        lqw.eq(StringUtils.isNotBlank(bo.getStatus()), BizTreeNode::getStatus, bo.getStatus());
        lqw.orderByAsc(BizTreeNode::getParentId);
        lqw.orderByAsc(BizTreeNode::getOrderNum);
        lqw.orderByAsc(BizTreeNode::getNodeCode);
        return lqw;
    }

    /**
     * 新增维护树节点
     *
     * @param bo 维护树节点
     * @return 是否新增成功
     */
    @Override
    public Boolean insertByBo(BizTreeNodeBo bo) {
        BizTreeNode add = MapstructUtils.convert(bo, BizTreeNode.class);
        fillDefaultValue(add);
        setupTreePath(add);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setNodeId(add.getNodeId());
            refreshParentLeafFlag(add.getTreeId(), add.getParentId());
        }
        return flag;
    }

    /**
     * 修改维护树节点
     *
     * @param bo 维护树节点
     * @return 是否修改成功
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean updateByBo(BizTreeNodeBo bo) {
        BizTreeNode old = baseMapper.selectById(bo.getNodeId());
        if (old == null) {
            throw new ServiceException("节点不存在");
        }
        BizTreeNode update = MapstructUtils.convert(bo, BizTreeNode.class);
        fillDefaultValue(update);
        setupTreePath(update);
        validEntityBeforeSave(update);
        boolean flag = baseMapper.updateById(update) > 0;
        if (flag) {
            updateChildrenPath(old, update);
            refreshParentLeafFlag(old.getTreeId(), old.getParentId());
            refreshParentLeafFlag(update.getTreeId(), update.getParentId());
        }
        return flag;
    }

    private void fillDefaultValue(BizTreeNode entity) {
        if (entity.getParentId() == null) {
            entity.setParentId(Constants.TOP_PARENT_ID);
        }
        if (entity.getOrderNum() == null) {
            entity.setOrderNum(0);
        }
        if (StringUtils.isBlank(entity.getLeafFlag())) {
            entity.setLeafFlag("1");
        }
        if (StringUtils.isBlank(entity.getStatus())) {
            entity.setStatus("0");
        }
    }

    private void setupTreePath(BizTreeNode entity) {
        if (Constants.TOP_PARENT_ID.equals(entity.getParentId())) {
            entity.setAncestors(Convert.toStr(Constants.TOP_PARENT_ID));
            entity.setLevelNo(1);
            return;
        }
        BizTreeNode parent = baseMapper.selectById(entity.getParentId());
        if (parent == null || !Objects.equals(parent.getTreeId(), entity.getTreeId())) {
            throw new ServiceException("上级节点不存在");
        }
        entity.setAncestors(parent.getAncestors() + StringUtils.SEPARATOR + parent.getNodeId());
        entity.setLevelNo(parent.getLevelNo() + 1);
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(BizTreeNode entity) {
        BizTreeDef treeDef = treeDefMapper.selectById(entity.getTreeId());
        if (treeDef == null) {
            throw new ServiceException("维护树不存在");
        }
        if (entity.getNodeId() != null && Objects.equals(entity.getNodeId(), entity.getParentId())) {
            throw new ServiceException("上级节点不能选择自己");
        }
        if (entity.getNodeId() != null && entity.getParentId() != null) {
            BizTreeNode parent = baseMapper.selectById(entity.getParentId());
            if (parent != null && StringUtils.splitList(parent.getAncestors(), StringUtils.SEPARATOR).contains(Convert.toStr(entity.getNodeId()))) {
                throw new ServiceException("上级节点不能选择自己的子节点");
            }
        }
        Long codeCount = baseMapper.selectCount(new LambdaQueryWrapper<BizTreeNode>()
            .eq(BizTreeNode::getTreeId, entity.getTreeId())
            .eq(BizTreeNode::getNodeCode, entity.getNodeCode())
            .ne(entity.getNodeId() != null, BizTreeNode::getNodeId, entity.getNodeId()));
        if (codeCount > 0) {
            throw new ServiceException("同一棵树下节点编码已存在");
        }
        if ("1".equals(treeDef.getRootMode()) && Constants.TOP_PARENT_ID.equals(entity.getParentId())) {
            Long rootCount = baseMapper.selectCount(new LambdaQueryWrapper<BizTreeNode>()
                .eq(BizTreeNode::getTreeId, entity.getTreeId())
                .eq(BizTreeNode::getParentId, Constants.TOP_PARENT_ID)
                .ne(entity.getNodeId() != null, BizTreeNode::getNodeId, entity.getNodeId()));
            if (rootCount > 0) {
                throw new ServiceException("该树为单根模式，只允许存在一个根节点");
            }
        }
    }

    private void updateChildrenPath(BizTreeNode oldNode, BizTreeNode newNode) {
        if (Objects.equals(oldNode.getAncestors(), newNode.getAncestors())
            && Objects.equals(oldNode.getLevelNo(), newNode.getLevelNo())) {
            return;
        }
        List<BizTreeNode> children = baseMapper.selectList(new LambdaQueryWrapper<BizTreeNode>()
            .eq(BizTreeNode::getTreeId, newNode.getTreeId())
            .apply(DataBaseHelper.findInSet(newNode.getNodeId(), "ancestors")));
        if (CollUtil.isEmpty(children)) {
            return;
        }
        String oldPrefix = oldNode.getAncestors() + StringUtils.SEPARATOR + oldNode.getNodeId();
        String newPrefix = newNode.getAncestors() + StringUtils.SEPARATOR + newNode.getNodeId();
        int levelDiff = newNode.getLevelNo() - oldNode.getLevelNo();
        for (BizTreeNode child : children) {
            child.setAncestors(child.getAncestors().replaceFirst("^" + oldPrefix, newPrefix));
            child.setLevelNo(child.getLevelNo() + levelDiff);
            baseMapper.updateById(child);
        }
    }

    private void refreshParentLeafFlag(Long treeId, Long parentId) {
        if (parentId == null || Constants.TOP_PARENT_ID.equals(parentId)) {
            return;
        }
        Long childCount = baseMapper.selectCount(new LambdaQueryWrapper<BizTreeNode>()
            .eq(BizTreeNode::getTreeId, treeId)
            .eq(BizTreeNode::getParentId, parentId));
        BizTreeNode update = new BizTreeNode();
        update.setNodeId(parentId);
        update.setLeafFlag(childCount > 0 ? "0" : "1");
        baseMapper.updateById(update);
    }

    /**
     * 校验并批量删除维护树节点信息
     *
     * @param ids     待删除的主键集合
     * @param isValid 是否进行有效性校验
     * @return 是否删除成功
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if (isValid) {
            Long count = baseMapper.selectCount(new LambdaQueryWrapper<BizTreeNode>()
                .in(BizTreeNode::getParentId, ids));
            if (count > 0) {
                throw new ServiceException("存在子节点，不允许删除");
            }
        }
        List<BizTreeNode> nodes = baseMapper.selectList(new LambdaQueryWrapper<BizTreeNode>()
            .in(BizTreeNode::getNodeId, ids));
        boolean flag = baseMapper.deleteByIds(ids) > 0;
        if (flag) {
            for (BizTreeNode node : nodes) {
                refreshParentLeafFlag(node.getTreeId(), node.getParentId());
            }
        }
        return flag;
    }

}
