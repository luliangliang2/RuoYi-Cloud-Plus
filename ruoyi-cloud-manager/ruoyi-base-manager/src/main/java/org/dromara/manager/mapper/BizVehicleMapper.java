package org.dromara.manager.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.dromara.common.mybatis.annotation.DataColumn;
import org.dromara.common.mybatis.annotation.DataPermission;
import org.dromara.common.mybatis.core.mapper.BaseMapperPlus;
import org.dromara.manager.api.domain.BizVehicle;
import org.dromara.manager.api.domain.bo.BizVehicleBo;
import org.dromara.manager.api.domain.vo.BizVehicleVo;
import org.dromara.manager.domain.bo.BizAlarmBo;

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

    /**
     * 查询当前用户有权限的车辆VIN集合
     *
     * @param bo 告警查询条件
     * @return 车辆VIN集合
     */
    @DataPermission({
        @DataColumn(key = "deptName", value = "v.create_dept"),
        @DataColumn(key = "userName", value = "v.create_by")
    })
    List<String> selectAuthorizedVinList(@Param("bo") BizAlarmBo bo);

}
