package org.dromara.manager.service;

import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.manager.domain.bo.BizSceneRouteBo;
import org.dromara.manager.domain.vo.BizSceneRouteVo;

import java.util.Collection;
import java.util.List;

/**
 * 场景路线Service接口
 *
 * @author LionLi
 * @date 2026-05-24
 */
public interface IBizSceneRouteService {

    /**
     * 查询场景路线
     *
     * @param routeId 主键
     * @return 场景路线
     */
    BizSceneRouteVo queryById(Long routeId);

    /**
     * 分页查询场景路线列表
     *
     * @param bo 查询条件
     * @param pageQuery 分页参数
     * @return 场景路线分页列表
     */
    TableDataInfo<BizSceneRouteVo> queryPageList(BizSceneRouteBo bo, PageQuery pageQuery);

    /**
     * 查询符合条件的场景路线列表
     *
     * @param bo 查询条件
     * @return 场景路线列表
     */
    List<BizSceneRouteVo> queryList(BizSceneRouteBo bo);

    /**
     * 新增场景路线
     *
     * @param bo 场景路线
     * @return 是否新增成功
     */
    Boolean insertByBo(BizSceneRouteBo bo);

    /**
     * 修改场景路线
     *
     * @param bo 场景路线
     * @return 是否修改成功
     */
    Boolean updateByBo(BizSceneRouteBo bo);

    /**
     * 校验并批量删除场景路线信息
     *
     * @param ids 待删除的主键集合
     * @param isValid 是否进行有效性校验
     * @return 是否删除成功
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

}
