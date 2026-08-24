package org.dromara.manager.mapper;

import org.apache.ibatis.annotations.Param;
import org.dromara.common.mybatis.core.mapper.BaseMapperPlus;
import org.dromara.manager.domain.BizRobotTaskAction;
import org.dromara.manager.domain.vo.BizRobotTaskActionVo;

import java.util.List;

/**
 * 机器人任务动作执行实例Mapper接口
 *
 * @author LionLi
 * @date 2026-06-19
 */
public interface BizRobotTaskActionMapper extends BaseMapperPlus<BizRobotTaskAction, BizRobotTaskActionVo> {

    List<BizRobotTaskActionVo> selectByTaskId(@Param("taskId") Long taskId);

}
