package org.dromara.manager.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.dromara.common.mybatis.core.mapper.BaseMapperPlus;
import org.dromara.manager.api.domain.BizVehicle;
import org.dromara.manager.api.domain.bo.BizVehicleBo;
import org.dromara.manager.api.domain.vo.BizVehicleVo;

import java.util.List;

/**
 * 车辆管理Mapper接口
 *
 * @author LionLi
 * @date 2026-05-21
 */
public interface BizVehicleMapper extends BaseMapperPlus<BizVehicle, BizVehicleVo> {

    /**
     * 查询车辆管理
     *
     * @param id 主键
     * @return 车辆管理
     */
    BizVehicleVo selectVehicleVoById(@Param("id") Long id);

    /**
     * 分页查询车辆管理列表
     *
     * @param page 分页对象
     * @param bo 查询条件
     * @return 车辆管理列表
     */
    Page<BizVehicleVo> selectVehiclePage(@Param("page") Page<BizVehicleVo> page, @Param("bo") BizVehicleBo bo);

    /**
     * 查询车辆管理列表
     *
     * @param bo 查询条件
     * @return 车辆管理列表
     */
    List<BizVehicleVo> selectVehicleList(@Param("bo") BizVehicleBo bo);

}
