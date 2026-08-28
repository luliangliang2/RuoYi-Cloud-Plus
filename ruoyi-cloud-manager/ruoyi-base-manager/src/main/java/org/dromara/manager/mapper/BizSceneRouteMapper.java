package org.dromara.manager.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.dromara.common.mybatis.core.mapper.BaseMapperPlus;
import org.dromara.manager.domain.BizSceneRoute;
import org.dromara.manager.domain.vo.BizSceneRouteVo;

import java.util.List;

/**
 * 场景路线Mapper接口
 *
 * @author LionLi
 * @date 2026-05-24
 */
public interface BizSceneRouteMapper extends BaseMapperPlus<BizSceneRoute, BizSceneRouteVo> {

    /**
     * 查询场景路线列表
     *
     * @param bo 查询条件
     * @return 场景路线列表
     */
    List<BizSceneRouteVo> selectSceneRouteList(@Param("bo") BizSceneRoute bo);

    /**
     * 分页查询场景路线列表
     *
     * @param page 分页对象
     * @param bo 查询条件
     * @return 场景路线列表
     */
    Page<BizSceneRouteVo> selectSceneRoutePage(@Param("page") Page<BizSceneRouteVo> page, @Param("bo") BizSceneRoute bo);

}
