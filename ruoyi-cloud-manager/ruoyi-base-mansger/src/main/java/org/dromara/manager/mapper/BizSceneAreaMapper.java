package org.dromara.manager.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.dromara.common.mybatis.core.mapper.BaseMapperPlus;
import org.dromara.manager.domain.BizSceneArea;
import org.dromara.manager.domain.vo.BizSceneAreaVo;

import java.util.List;

/**
 * 场景区域Mapper接口
 *
 * @author LionLi
 * @date 2026-05-24
 */
public interface BizSceneAreaMapper extends BaseMapperPlus<BizSceneArea, BizSceneAreaVo> {

    /**
     * 查询场景区域列表
     *
     * @param bo 查询条件
     * @return 场景区域列表
     */
    List<BizSceneAreaVo> selectSceneAreaList(@Param("bo") BizSceneArea bo);

    /**
     * 分页查询场景区域列表
     *
     * @param page 分页对象
     * @param bo 查询条件
     * @return 场景区域列表
     */
    Page<BizSceneAreaVo> selectSceneAreaPage(@Param("page") Page<BizSceneAreaVo> page, @Param("bo") BizSceneArea bo);

}
