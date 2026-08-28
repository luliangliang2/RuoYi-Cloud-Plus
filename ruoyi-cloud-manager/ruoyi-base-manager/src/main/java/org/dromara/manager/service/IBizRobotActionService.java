package org.dromara.manager.service;

import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.manager.domain.bo.BizRobotActionBo;
import org.dromara.manager.domain.vo.BizRobotActionVo;

import java.util.Collection;
import java.util.List;

/**
 * 机器人动作定义Service接口
 *
 * @author LionLi
 * @date 2026-06-12
 */
public interface IBizRobotActionService {

    /**
     * 查询机器人动作定义
     *
     * @param actionId 主键
     * @return 机器人动作定义
     */
    BizRobotActionVo queryById(Long actionId);

    /**
     * 分页查询机器人动作定义列表
     *
     * @param bo 查询条件
     * @param pageQuery 分页参数
     * @return 机器人动作定义分页列表
     */
    TableDataInfo<BizRobotActionVo> queryPageList(BizRobotActionBo bo, PageQuery pageQuery);

    /**
     * 查询符合条件的机器人动作定义列表
     *
     * @param bo 查询条件
     * @return 机器人动作定义列表
     */
    List<BizRobotActionVo> queryList(BizRobotActionBo bo);

    /**
     * 新增机器人动作定义
     *
     * @param bo 机器人动作定义
     * @return 是否新增成功
     */
    Boolean insertByBo(BizRobotActionBo bo);

    /**
     * 修改机器人动作定义
     *
     * @param bo 机器人动作定义
     * @return 是否修改成功
     */
    Boolean updateByBo(BizRobotActionBo bo);

    /**
     * 校验并批量删除机器人动作定义信息
     *
     * @param ids 待删除的主键集合
     * @param isValid 是否进行有效性校验
     * @return 是否删除成功
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

}
