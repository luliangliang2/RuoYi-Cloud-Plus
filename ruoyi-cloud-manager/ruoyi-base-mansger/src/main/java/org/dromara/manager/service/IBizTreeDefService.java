package org.dromara.manager.service;

import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.manager.domain.bo.BizTreeDefBo;
import org.dromara.manager.domain.vo.BizTreeDefVo;

import java.util.Collection;
import java.util.List;

/**
 * 维护树定义Service接口
 *
 * @author LionLi
 * @date 2026-05-22
 */
public interface IBizTreeDefService {

    /**
     * 查询维护树定义
     *
     * @param treeId 主键
     * @return 维护树定义
     */
    BizTreeDefVo queryById(Long treeId);

    /**
     * 分页查询维护树定义列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 维护树定义分页列表
     */
    TableDataInfo<BizTreeDefVo> queryPageList(BizTreeDefBo bo, PageQuery pageQuery);

    /**
     * 查询符合条件的维护树定义列表
     *
     * @param bo 查询条件
     * @return 维护树定义列表
     */
    List<BizTreeDefVo> queryList(BizTreeDefBo bo);

    /**
     * 新增维护树定义
     *
     * @param bo 维护树定义
     * @return 是否新增成功
     */
    Boolean insertByBo(BizTreeDefBo bo);

    /**
     * 修改维护树定义
     *
     * @param bo 维护树定义
     * @return 是否修改成功
     */
    Boolean updateByBo(BizTreeDefBo bo);

    /**
     * 校验并批量删除维护树定义信息
     *
     * @param ids     待删除的主键集合
     * @param isValid 是否进行有效性校验
     * @return 是否删除成功
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

}
