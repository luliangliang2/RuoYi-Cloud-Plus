package org.dromara.manager.mapper;

import org.apache.ibatis.annotations.Param;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.dromara.common.mybatis.core.mapper.BaseMapperPlus;
import org.dromara.manager.domain.BizSimCard;
import org.dromara.manager.domain.vo.BizSimCardVo;

import java.util.Collection;
import java.util.List;

/**
 * SIM卡Mapper接口
 *
 * @author LionLi
 * @date 2026-05-22
 */
public interface BizSimCardMapper extends BaseMapperPlus<BizSimCard, BizSimCardVo> {

    /**
     * 查询SIM卡列表
     *
     * @param bo 查询条件
     * @return SIM卡列表
     */
    List<BizSimCardVo> selectSimCardList(@Param("bo") BizSimCard bo);

    /**
     * 分页查询SIM卡列表
     *
     * @param page 分页对象
     * @param bo 查询条件
     * @return SIM卡列表
     */
    Page<BizSimCardVo> selectSimCardPage(@Param("page") Page<BizSimCardVo> page, @Param("bo") BizSimCard bo);

    /**
     * 查询未绑定或当前车辆已绑定的SIM卡
     *
     * @param vehicleId 当前车辆ID
     * @param keyword 关键字
     * @return SIM卡列表
     */
    List<BizSimCardVo> selectBindableList(@Param("vehicleId") Long vehicleId, @Param("keyword") String keyword);

    /**
     * 根据ID批量查询SIM卡
     *
     * @param simIds SIM卡ID
     * @return SIM卡列表
     */
    List<BizSimCardVo> selectSimCardVoByIds(@Param("simIds") Collection<Long> simIds);

}
