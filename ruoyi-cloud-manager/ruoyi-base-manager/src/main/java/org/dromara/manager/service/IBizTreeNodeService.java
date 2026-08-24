package org.dromara.manager.service;

import org.dromara.manager.domain.bo.BizTreeNodeBo;
import org.dromara.manager.domain.vo.BizTreeNodeVo;

import java.util.Collection;
import java.util.List;

/**
 * 维护树节点Service接口
 *
 * @author LionLi
 * @date 2026-05-22
 */
public interface IBizTreeNodeService {

    /**
     * 查询维护树节点
     *
     * @param nodeId 主键
     * @return 维护树节点
     */
    BizTreeNodeVo queryById(Long nodeId);

    /**
     * 查询符合条件的维护树节点列表
     *
     * @param bo 查询条件
     * @return 维护树节点列表
     */
    List<BizTreeNodeVo> queryList(BizTreeNodeBo bo);

    /**
     * 查询指定树的可选父节点列表
     *
     * @param treeId 树ID
     * @param nodeId 当前节点ID
     * @return 维护树节点列表
     */
    List<BizTreeNodeVo> querySelectableList(Long treeId, Long nodeId);

    /**
     * 新增维护树节点
     *
     * @param bo 维护树节点
     * @return 是否新增成功
     */
    Boolean insertByBo(BizTreeNodeBo bo);

    /**
     * 修改维护树节点
     *
     * @param bo 维护树节点
     * @return 是否修改成功
     */
    Boolean updateByBo(BizTreeNodeBo bo);

    /**
     * 校验并批量删除维护树节点信息
     *
     * @param ids     待删除的主键集合
     * @param isValid 是否进行有效性校验
     * @return 是否删除成功
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

}
