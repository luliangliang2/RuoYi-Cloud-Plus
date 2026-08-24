package org.dromara.manager.service;

import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.manager.domain.bo.BizThirdApiConfigBo;
import org.dromara.manager.domain.vo.BizThirdApiConfigVo;

import java.util.Collection;
import java.util.List;

/**
 * 第三方API配置Service接口
 *
 * @author LionLi
 * @date 2026-05-22
 */
public interface IBizThirdApiConfigService {

    /**
     * 查询第三方API配置
     *
     * @param configId 主键
     * @return 第三方API配置
     */
    BizThirdApiConfigVo queryById(Long configId);

    /**
     * 分页查询第三方API配置列表
     *
     * @param bo 查询条件
     * @param pageQuery 分页参数
     * @return 第三方API配置分页列表
     */
    TableDataInfo<BizThirdApiConfigVo> queryPageList(BizThirdApiConfigBo bo, PageQuery pageQuery);

    /**
     * 查询符合条件的第三方API配置列表
     *
     * @param bo 查询条件
     * @return 第三方API配置列表
     */
    List<BizThirdApiConfigVo> queryList(BizThirdApiConfigBo bo);

    /**
     * 新增第三方API配置
     *
     * @param bo 第三方API配置
     * @return 是否新增成功
     */
    Boolean insertByBo(BizThirdApiConfigBo bo);

    /**
     * 修改第三方API配置
     *
     * @param bo 第三方API配置
     * @return 是否修改成功
     */
    Boolean updateByBo(BizThirdApiConfigBo bo);

    /**
     * 校验并批量删除第三方API配置信息
     *
     * @param ids 待删除的主键集合
     * @param isValid 是否进行有效性校验
     * @return 是否删除成功
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

}
