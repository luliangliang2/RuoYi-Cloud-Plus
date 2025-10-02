package org.dromara.cognition.service;

import org.dromara.cognition.domain.CognitionUserProgress;
import org.dromara.cognition.domain.vo.CognitionUserProgressVo;
import org.dromara.cognition.domain.bo.CognitionUserProgressBo;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.common.mybatis.core.page.PageQuery;

import java.util.Collection;
import java.util.List;

/**
 * 用户场景学习进度Service接口
 *
 * @author zhang
 * @date 2025-10-02
 */
public interface ICognitionUserProgressService {

    /**
     * 查询用户场景学习进度
     *
     * @param id 主键
     * @return 用户场景学习进度
     */
    CognitionUserProgressVo queryById(Long id);

    /**
     * 分页查询用户场景学习进度列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 用户场景学习进度分页列表
     */
    TableDataInfo<CognitionUserProgressVo> queryPageList(CognitionUserProgressBo bo, PageQuery pageQuery);

    /**
     * 查询符合条件的用户场景学习进度列表
     *
     * @param bo 查询条件
     * @return 用户场景学习进度列表
     */
    List<CognitionUserProgressVo> queryList(CognitionUserProgressBo bo);

    /**
     * 新增用户场景学习进度
     *
     * @param bo 用户场景学习进度
     * @return 是否新增成功
     */
    Boolean insertByBo(CognitionUserProgressBo bo);

    /**
     * 修改用户场景学习进度
     *
     * @param bo 用户场景学习进度
     * @return 是否修改成功
     */
    Boolean updateByBo(CognitionUserProgressBo bo);

    /**
     * 校验并批量删除用户场景学习进度信息
     *
     * @param ids     待删除的主键集合
     * @param isValid 是否进行有效性校验
     * @return 是否删除成功
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}
