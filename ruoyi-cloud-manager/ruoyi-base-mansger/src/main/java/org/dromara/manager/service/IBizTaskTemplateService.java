package org.dromara.manager.service;

import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.manager.domain.bo.BizTaskTemplateBo;
import org.dromara.manager.domain.vo.BizScenePointVo;
import org.dromara.manager.domain.vo.BizTaskTemplateVo;

import java.util.Collection;
import java.util.List;

/**
 * 任务模板Service接口
 *
 * @author LionLi
 * @date 2026-06-12
 */
public interface IBizTaskTemplateService {

    /**
     * 查询任务模板
     *
     * @param templateId 主键
     * @return 任务模板
     */
    BizTaskTemplateVo queryById(Long templateId);

    /**
     * 分页查询任务模板列表
     *
     * @param bo 查询条件
     * @param pageQuery 分页参数
     * @return 任务模板分页列表
     */
    TableDataInfo<BizTaskTemplateVo> queryPageList(BizTaskTemplateBo bo, PageQuery pageQuery);

    /**
     * 查询符合条件的任务模板列表
     *
     * @param bo 查询条件
     * @return 任务模板列表
     */
    List<BizTaskTemplateVo> queryList(BizTaskTemplateBo bo);

    /**
     * 查询路线点位
     *
     * @param routeId 路线ID
     * @return 点位列表
     */
    List<BizScenePointVo> queryRoutePoints(Long routeId);

    /**
     * 新增任务模板
     *
     * @param bo 任务模板
     * @return 是否新增成功
     */
    Boolean insertByBo(BizTaskTemplateBo bo);

    /**
     * 修改任务模板
     *
     * @param bo 任务模板
     * @return 是否修改成功
     */
    Boolean updateByBo(BizTaskTemplateBo bo);

    /**
     * 校验并批量删除任务模板信息
     *
     * @param ids 待删除的主键集合
     * @param isValid 是否进行有效性校验
     * @return 是否删除成功
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

}
