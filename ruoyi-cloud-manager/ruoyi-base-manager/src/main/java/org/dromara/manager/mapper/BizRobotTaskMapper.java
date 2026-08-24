package org.dromara.manager.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.dromara.common.mybatis.core.mapper.BaseMapperPlus;
import org.dromara.manager.domain.BizRobotTask;
import org.dromara.manager.domain.vo.BizRobotTaskVo;

import java.util.List;

/**
 * 机器人任务执行Mapper接口
 *
 * @author LionLi
 * @date 2026-06-19
 */
public interface BizRobotTaskMapper extends BaseMapperPlus<BizRobotTask, BizRobotTaskVo> {

    List<BizRobotTaskVo> selectRobotTaskList(@Param("bo") BizRobotTask bo);

    Page<BizRobotTaskVo> selectRobotTaskPage(@Param("page") Page<BizRobotTaskVo> page, @Param("bo") BizRobotTask bo);

}
