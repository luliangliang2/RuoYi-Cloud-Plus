package org.dromara.manager.service;

import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.manager.domain.bo.BizRadarBo;
import org.dromara.manager.domain.vo.BizRadarVo;

import java.util.Collection;
import java.util.List;

/**
 * 上装雷达Service接口
 *
 * @author LionLi
 * @date 2026-05-22
 */
public interface IBizRadarService {

    /**
     * 查询上装雷达
     *
     * @param radarId 主键
     * @return 上装雷达
     */
    BizRadarVo queryById(Long radarId);

    /**
     * 分页查询上装雷达列表
     *
     * @param bo 查询条件
     * @param pageQuery 分页参数
     * @return 上装雷达分页列表
     */
    TableDataInfo<BizRadarVo> queryPageList(BizRadarBo bo, PageQuery pageQuery);

    /**
     * 查询符合条件的上装雷达列表
     *
     * @param bo 查询条件
     * @return 上装雷达列表
     */
    List<BizRadarVo> queryList(BizRadarBo bo);

    /**
     * 查询可绑定雷达列表
     *
     * @param vehicleId 车辆ID
     * @param keyword 关键字
     * @return 上装雷达列表
     */
    List<BizRadarVo> queryBindableList(Long vehicleId, String keyword);

    /**
     * 新增上装雷达
     *
     * @param bo 上装雷达
     * @return 是否新增成功
     */
    Boolean insertByBo(BizRadarBo bo);

    /**
     * 修改上装雷达
     *
     * @param bo 上装雷达
     * @return 是否修改成功
     */
    Boolean updateByBo(BizRadarBo bo);

    /**
     * 校验并批量删除上装雷达信息
     *
     * @param ids 待删除的主键集合
     * @param isValid 是否进行有效性校验
     * @return 是否删除成功
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

}
