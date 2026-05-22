package org.dromara.manager.mapper;

import org.apache.ibatis.annotations.Param;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.dromara.common.mybatis.core.mapper.BaseMapperPlus;
import org.dromara.manager.domain.BizCamera;
import org.dromara.manager.domain.vo.BizCameraVo;

import java.util.Collection;
import java.util.List;

/**
 * 上装相机Mapper接口
 *
 * @author LionLi
 * @date 2026-05-22
 */
public interface BizCameraMapper extends BaseMapperPlus<BizCamera, BizCameraVo> {

    /**
     * 查询上装相机列表
     *
     * @param bo 查询条件
     * @return 上装相机列表
     */
    List<BizCameraVo> selectCameraList(@Param("bo") BizCamera bo);

    /**
     * 分页查询上装相机列表
     *
     * @param page 分页对象
     * @param bo 查询条件
     * @return 上装相机列表
     */
    Page<BizCameraVo> selectCameraPage(@Param("page") Page<BizCameraVo> page, @Param("bo") BizCamera bo);

    /**
     * 查询未绑定或当前车辆已绑定的相机
     *
     * @param vehicleId 当前车辆ID
     * @param keyword 关键字
     * @return 上装相机列表
     */
    List<BizCameraVo> selectBindableList(@Param("vehicleId") Long vehicleId, @Param("keyword") String keyword);

    /**
     * 根据ID批量查询相机
     *
     * @param cameraIds 相机ID
     * @return 上装相机列表
     */
    List<BizCameraVo> selectCameraVoByIds(@Param("cameraIds") Collection<Long> cameraIds);

}
