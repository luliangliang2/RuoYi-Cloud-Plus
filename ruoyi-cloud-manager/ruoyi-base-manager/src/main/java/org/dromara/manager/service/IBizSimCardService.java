package org.dromara.manager.service;

import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.manager.domain.bo.BizSimCardBo;
import org.dromara.manager.domain.vo.BizSimCardVo;

import java.util.Collection;
import java.util.List;

/**
 * SIM卡Service接口
 *
 * @author LionLi
 * @date 2026-05-22
 */
public interface IBizSimCardService {

    /**
     * 查询SIM卡
     *
     * @param simId 主键
     * @return SIM卡
     */
    BizSimCardVo queryById(Long simId);

    /**
     * 分页查询SIM卡列表
     *
     * @param bo 查询条件
     * @param pageQuery 分页参数
     * @return SIM卡分页列表
     */
    TableDataInfo<BizSimCardVo> queryPageList(BizSimCardBo bo, PageQuery pageQuery);

    /**
     * 查询符合条件的SIM卡列表
     *
     * @param bo 查询条件
     * @return SIM卡列表
     */
    List<BizSimCardVo> queryList(BizSimCardBo bo);

    /**
     * 查询可绑定SIM卡列表
     *
     * @param vehicleId 车辆ID
     * @param keyword 关键字
     * @return SIM卡列表
     */
    List<BizSimCardVo> queryBindableList(Long vehicleId, String keyword);

    /**
     * 新增SIM卡
     *
     * @param bo SIM卡
     * @return 是否新增成功
     */
    Boolean insertByBo(BizSimCardBo bo);

    /**
     * 修改SIM卡
     *
     * @param bo SIM卡
     * @return 是否修改成功
     */
    Boolean updateByBo(BizSimCardBo bo);

    /**
     * 校验并批量删除SIM卡信息
     *
     * @param ids 待删除的主键集合
     * @param isValid 是否进行有效性校验
     * @return 是否删除成功
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

}
