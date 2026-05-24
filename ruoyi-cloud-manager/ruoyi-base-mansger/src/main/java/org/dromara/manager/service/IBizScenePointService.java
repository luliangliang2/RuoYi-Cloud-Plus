package org.dromara.manager.service;

import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.manager.domain.bo.BizScenePointBo;
import org.dromara.manager.domain.vo.BizScenePointVo;

import java.util.Collection;
import java.util.List;

/**
 * 场景点位Service接口
 *
 * @author LionLi
 * @date 2026-05-24
 */
public interface IBizScenePointService {

    /**
     * 查询场景点位
     *
     * @param pointId 主键
     * @return 场景点位
     */
    BizScenePointVo queryById(Long pointId);

    /**
     * 分页查询场景点位列表
     *
     * @param bo 查询条件
     * @param pageQuery 分页参数
     * @return 场景点位分页列表
     */
    TableDataInfo<BizScenePointVo> queryPageList(BizScenePointBo bo, PageQuery pageQuery);

    /**
     * 查询符合条件的场景点位列表
     *
     * @param bo 查询条件
     * @return 场景点位列表
     */
    List<BizScenePointVo> queryList(BizScenePointBo bo);

    /**
     * 新增场景点位
     *
     * @param bo 场景点位
     * @return 是否新增成功
     */
    Boolean insertByBo(BizScenePointBo bo);

    /**
     * 修改场景点位
     *
     * @param bo 场景点位
     * @return 是否修改成功
     */
    Boolean updateByBo(BizScenePointBo bo);

    /**
     * 校验并批量删除场景点位信息
     *
     * @param ids 待删除的主键集合
     * @param isValid 是否进行有效性校验
     * @return 是否删除成功
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

}
