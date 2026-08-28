package org.dromara.manager.api;

import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.manager.api.domain.bo.BizVehicleBo;
import org.dromara.manager.api.domain.vo.BizVehicleVo;
import org.dromara.common.mybatis.core.page.PageQuery;

import java.util.Collection;
import java.util.List;

/**
 * 车辆管理Service接口
 *
 * @author LionLi
 * @date 2026-05-21
 */
public interface IBizVehicleService {

    /**
     * 查询车辆管理
     *
     * @param id 主键
     * @return 车辆管理
     */
    BizVehicleVo queryById(Long id);

    /**
     * 分页查询车辆管理列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 车辆管理分页列表
     */
    TableDataInfo<BizVehicleVo> queryPageList(BizVehicleBo bo, PageQuery pageQuery);

    /**
     * 查询符合条件的车辆管理列表
     *
     * @param bo 查询条件
     * @return 车辆管理列表
     */
    List<BizVehicleVo> queryList(BizVehicleBo bo);

    /**
     * 新增车辆管理
     *
     * @param bo 车辆管理
     * @return 是否新增成功
     */
    Boolean insertByBo(BizVehicleBo bo);

    /**
     * 修改车辆管理
     *
     * @param bo 车辆管理
     * @return 是否修改成功
     */
    Boolean updateByBo(BizVehicleBo bo);

    /**
     * 校验并批量删除车辆管理信息
     *
     * @param ids     待删除的主键集合
     * @param isValid 是否进行有效性校验
     * @return 是否删除成功
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}
