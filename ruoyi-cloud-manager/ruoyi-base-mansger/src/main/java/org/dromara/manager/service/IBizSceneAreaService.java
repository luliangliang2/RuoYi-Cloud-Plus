package org.dromara.manager.service;

import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.manager.domain.bo.BizSceneAreaBo;
import org.dromara.manager.domain.vo.BizSceneAreaVo;

import java.util.Collection;
import java.util.List;

/**
 * 场景区域Service接口
 *
 * @author LionLi
 * @date 2026-05-24
 */
public interface IBizSceneAreaService {

    /**
     * 查询场景区域
     *
     * @param areaId 主键
     * @return 场景区域
     */
    BizSceneAreaVo queryById(Long areaId);

    /**
     * 分页查询场景区域列表
     *
     * @param bo 查询条件
     * @param pageQuery 分页参数
     * @return 场景区域分页列表
     */
    TableDataInfo<BizSceneAreaVo> queryPageList(BizSceneAreaBo bo, PageQuery pageQuery);

    /**
     * 查询符合条件的场景区域列表
     *
     * @param bo 查询条件
     * @return 场景区域列表
     */
    List<BizSceneAreaVo> queryList(BizSceneAreaBo bo);

    /**
     * 新增场景区域
     *
     * @param bo 场景区域
     * @return 是否新增成功
     */
    Boolean insertByBo(BizSceneAreaBo bo);

    /**
     * 修改场景区域
     *
     * @param bo 场景区域
     * @return 是否修改成功
     */
    Boolean updateByBo(BizSceneAreaBo bo);

    /**
     * 校验并批量删除场景区域信息
     *
     * @param ids 待删除的主键集合
     * @param isValid 是否进行有效性校验
     * @return 是否删除成功
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

}
