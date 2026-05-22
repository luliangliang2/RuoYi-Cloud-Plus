package org.dromara.manager.mapper;

import org.apache.ibatis.annotations.Param;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.dromara.common.mybatis.core.mapper.BaseMapperPlus;
import org.dromara.manager.domain.BizRadar;
import org.dromara.manager.domain.vo.BizRadarVo;

import java.util.Collection;
import java.util.List;

/**
 * 上装雷达Mapper接口
 *
 * @author LionLi
 * @date 2026-05-22
 */
public interface BizRadarMapper extends BaseMapperPlus<BizRadar, BizRadarVo> {

    /**
     * 查询上装雷达列表
     *
     * @param bo 查询条件
     * @return 上装雷达列表
     */
    List<BizRadarVo> selectRadarList(@Param("bo") BizRadar bo);

    /**
     * 分页查询上装雷达列表
     *
     * @param page 分页对象
     * @param bo 查询条件
     * @return 上装雷达列表
     */
    Page<BizRadarVo> selectRadarPage(@Param("page") Page<BizRadarVo> page, @Param("bo") BizRadar bo);

    /**
     * 查询未绑定或当前车辆已绑定的雷达
     *
     * @param vehicleId 当前车辆ID
     * @param keyword 关键字
     * @return 上装雷达列表
     */
    List<BizRadarVo> selectBindableList(@Param("vehicleId") Long vehicleId, @Param("keyword") String keyword);

    /**
     * 根据ID批量查询雷达
     *
     * @param radarIds 雷达ID
     * @return 上装雷达列表
     */
    List<BizRadarVo> selectRadarVoByIds(@Param("radarIds") Collection<Long> radarIds);

}
