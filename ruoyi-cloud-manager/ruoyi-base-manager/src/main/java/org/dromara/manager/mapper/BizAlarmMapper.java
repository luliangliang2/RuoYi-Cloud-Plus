package org.dromara.manager.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import org.apache.ibatis.annotations.Param;
import org.dromara.manager.domain.bo.BizAlarmBo;
import org.dromara.manager.domain.vo.BizAlarmVo;

import java.util.List;

/**
 * 告警Mapper接口
 *
 * @author LionLi
 * @date 2026-05-28
 */
@DS("taos")
@InterceptorIgnore(tenantLine = "true", dataPermission = "true")
public interface BizAlarmMapper {

    /**
     * 查询告警分页列表
     *
     * @param bo 查询条件
     * @param vins 有权限车辆VIN集合
     * @param offset 偏移量
     * @param limit 每页条数
     * @return 告警列表
     */
    List<BizAlarmVo> selectAlarmList(@Param("bo") BizAlarmBo bo,
                                     @Param("vins") List<String> vins,
                                     @Param("offset") long offset,
                                     @Param("limit") long limit);

    /**
     * 查询告警数量
     *
     * @param bo 查询条件
     * @param vins 有权限车辆VIN集合
     * @return 告警数量
     */
    Long selectAlarmCount(@Param("bo") BizAlarmBo bo, @Param("vins") List<String> vins);

}
