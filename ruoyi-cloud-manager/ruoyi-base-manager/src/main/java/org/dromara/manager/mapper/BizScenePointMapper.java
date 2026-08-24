package org.dromara.manager.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.dromara.common.mybatis.core.mapper.BaseMapperPlus;
import org.dromara.manager.domain.BizScenePoint;
import org.dromara.manager.domain.vo.BizScenePointVo;

import java.util.List;

/**
 * 场景点位Mapper接口
 *
 * @author LionLi
 * @date 2026-05-24
 */
public interface BizScenePointMapper extends BaseMapperPlus<BizScenePoint, BizScenePointVo> {

    /**
     * 查询场景点位列表
     *
     * @param bo 查询条件
     * @return 场景点位列表
     */
    List<BizScenePointVo> selectScenePointList(@Param("bo") BizScenePoint bo);

    /**
     * 分页查询场景点位列表
     *
     * @param page 分页对象
     * @param bo 查询条件
     * @return 场景点位列表
     */
    Page<BizScenePointVo> selectScenePointPage(@Param("page") Page<BizScenePointVo> page, @Param("bo") BizScenePoint bo);

}
