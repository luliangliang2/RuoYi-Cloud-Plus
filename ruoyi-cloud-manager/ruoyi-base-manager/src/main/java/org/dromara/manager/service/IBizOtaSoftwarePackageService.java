package org.dromara.manager.service;

import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.manager.domain.bo.BizOtaSoftwarePackageBo;
import org.dromara.manager.domain.vo.BizOtaSoftwarePackageVo;

import java.util.Collection;
import java.util.List;

/**
 * OTA软件包Service接口
 *
 * @author LionLi
 * @date 2026-05-24
 */
public interface IBizOtaSoftwarePackageService {

    /**
     * 查询OTA软件包
     *
     * @param packageId 主键
     * @return OTA软件包
     */
    BizOtaSoftwarePackageVo queryById(Long packageId);

    /**
     * 分页查询OTA软件包列表
     *
     * @param bo 查询条件
     * @param pageQuery 分页参数
     * @return OTA软件包分页列表
     */
    TableDataInfo<BizOtaSoftwarePackageVo> queryPageList(BizOtaSoftwarePackageBo bo, PageQuery pageQuery);

    /**
     * 查询符合条件的OTA软件包列表
     *
     * @param bo 查询条件
     * @return OTA软件包列表
     */
    List<BizOtaSoftwarePackageVo> queryList(BizOtaSoftwarePackageBo bo);

    /**
     * 新增OTA软件包
     *
     * @param bo OTA软件包
     * @return 是否新增成功
     */
    Boolean insertByBo(BizOtaSoftwarePackageBo bo);

    /**
     * 修改OTA软件包
     *
     * @param bo OTA软件包
     * @return 是否修改成功
     */
    Boolean updateByBo(BizOtaSoftwarePackageBo bo);

    /**
     * 校验并批量删除OTA软件包信息
     *
     * @param ids 待删除的主键集合
     * @param isValid 是否进行有效性校验
     * @return 是否删除成功
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

}
