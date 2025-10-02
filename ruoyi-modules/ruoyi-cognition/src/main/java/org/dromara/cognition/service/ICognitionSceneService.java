package org.dromara.cognition.service;

import org.dromara.cognition.domain.CognitionScene;
import org.dromara.cognition.domain.vo.CognitionSceneVo;
import org.dromara.cognition.domain.bo.CognitionSceneBo;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.common.mybatis.core.page.PageQuery;

import java.util.Collection;
import java.util.List;

/**
 * 认知场景Service接口
 *
 * @author zhang
 * @date 2025-10-02
 */
public interface ICognitionSceneService {

    /**
     * 查询认知场景
     *
     * @param id 主键
     * @return 认知场景
     */
    CognitionSceneVo queryById(Long id);

    /**
     * 分页查询认知场景列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 认知场景分页列表
     */
    TableDataInfo<CognitionSceneVo> queryPageList(CognitionSceneBo bo, PageQuery pageQuery);

    /**
     * 查询符合条件的认知场景列表
     *
     * @param bo 查询条件
     * @return 认知场景列表
     */
    List<CognitionSceneVo> queryList(CognitionSceneBo bo);

    /**
     * 新增认知场景
     *
     * @param bo 认知场景
     * @return 是否新增成功
     */
    Boolean insertByBo(CognitionSceneBo bo);

    /**
     * 修改认知场景
     *
     * @param bo 认知场景
     * @return 是否修改成功
     */
    Boolean updateByBo(CognitionSceneBo bo);

    /**
     * 校验并批量删除认知场景信息
     *
     * @param ids     待删除的主键集合
     * @param isValid 是否进行有效性校验
     * @return 是否删除成功
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}
