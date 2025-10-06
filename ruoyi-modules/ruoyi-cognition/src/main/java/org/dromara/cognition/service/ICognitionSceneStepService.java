package org.dromara.cognition.service;

import java.util.Collection;
import java.util.List;
import org.dromara.cognition.domain.bo.CognitionSceneStepBo;
import org.dromara.cognition.domain.vo.CognitionSceneStepVo;
import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;

/**
 * 认知场景步骤Service接口
 *
 * @author zhang
 * @date 2025-10-02
 */
public interface ICognitionSceneStepService {

    /**
     * 查询认知场景步骤
     *
     * @param id 主键
     * @return 认知场景步骤
     */
    CognitionSceneStepVo queryById(Long id);

    /**
     * 分页查询认知场景步骤列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 认知场景步骤分页列表
     */
    TableDataInfo<CognitionSceneStepVo> queryPageList(CognitionSceneStepBo bo, PageQuery pageQuery);

    /**
     * 查询符合条件的认知场景步骤列表
     *
     * @param bo 查询条件
     * @return 认知场景步骤列表
     */
    List<CognitionSceneStepVo> queryList(CognitionSceneStepBo bo);

    /**
     * 新增认知场景步骤
     *
     * @param bo 认知场景步骤
     * @return 是否新增成功
     */
    Boolean insertByBo(CognitionSceneStepBo bo);

    /**
     * 修改认知场景步骤
     *
     * @param bo 认知场景步骤
     * @return 是否修改成功
     */
    Boolean updateByBo(CognitionSceneStepBo bo);

    /**
     * 修改步骤顺序
     *
     * @param bos CognitionSceneStepBo
     * @return 是否修改成功
     */
    Boolean updateStepOrder(List<CognitionSceneStepBo> bos);

    /**
     * 校验并批量删除认知场景步骤信息
     *
     * @param ids     待删除的主键集合
     * @param isValid 是否进行有效性校验
     * @return 是否删除成功
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}
