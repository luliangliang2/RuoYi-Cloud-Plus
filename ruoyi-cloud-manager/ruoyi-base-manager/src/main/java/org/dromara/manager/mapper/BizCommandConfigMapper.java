package org.dromara.manager.mapper;

import org.apache.ibatis.annotations.Param;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.dromara.common.mybatis.core.mapper.BaseMapperPlus;
import org.dromara.manager.domain.BizCommandConfig;
import org.dromara.manager.domain.vo.BizCommandConfigVo;

import java.util.List;

/**
 * 指令配置Mapper接口
 *
 * @author LionLi
 * @date 2026-05-23
 */
public interface BizCommandConfigMapper extends BaseMapperPlus<BizCommandConfig, BizCommandConfigVo> {

    /**
     * 查询指令配置列表
     *
     * @param bo 查询条件
     * @return 指令配置列表
     */
    List<BizCommandConfigVo> selectCommandConfigList(@Param("bo") BizCommandConfig bo);

    /**
     * 分页查询指令配置列表
     *
     * @param page 分页对象
     * @param bo 查询条件
     * @return 指令配置列表
     */
    Page<BizCommandConfigVo> selectCommandConfigPage(@Param("page") Page<BizCommandConfigVo> page, @Param("bo") BizCommandConfig bo);

}
