package org.dromara.manager.service;

import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.manager.domain.bo.BizCommandConfigBo;
import org.dromara.manager.domain.vo.BizCommandConfigVo;

import java.util.Collection;
import java.util.List;

/**
 * 指令配置Service接口
 *
 * @author LionLi
 * @date 2026-05-23
 */
public interface IBizCommandConfigService {

    /**
     * 查询指令配置
     *
     * @param commandId 主键
     * @return 指令配置
     */
    BizCommandConfigVo queryById(Long commandId);

    /**
     * 分页查询指令配置列表
     *
     * @param bo 查询条件
     * @param pageQuery 分页参数
     * @return 指令配置分页列表
     */
    TableDataInfo<BizCommandConfigVo> queryPageList(BizCommandConfigBo bo, PageQuery pageQuery);

    /**
     * 查询符合条件的指令配置列表
     *
     * @param bo 查询条件
     * @return 指令配置列表
     */
    List<BizCommandConfigVo> queryList(BizCommandConfigBo bo);

    /**
     * 新增指令配置
     *
     * @param bo 指令配置
     * @return 是否新增成功
     */
    Boolean insertByBo(BizCommandConfigBo bo);

    /**
     * 修改指令配置
     *
     * @param bo 指令配置
     * @return 是否修改成功
     */
    Boolean updateByBo(BizCommandConfigBo bo);

    /**
     * 校验并批量删除指令配置信息
     *
     * @param ids 待删除的主键集合
     * @param isValid 是否进行有效性校验
     * @return 是否删除成功
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

}
