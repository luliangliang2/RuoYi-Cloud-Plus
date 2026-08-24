package org.dromara.manager.mapper;

import org.apache.ibatis.annotations.Param;
import org.dromara.common.mybatis.core.mapper.BaseMapperPlus;
import org.dromara.manager.domain.BizRobotTaskPoint;
import org.dromara.manager.domain.vo.BizRobotTaskPointVo;

import java.util.List;

/**
 * 机器人任务点位执行实例Mapper接口
 *
 * @author LionLi
 * @date 2026-06-19
 */
public interface BizRobotTaskPointMapper extends BaseMapperPlus<BizRobotTaskPoint, BizRobotTaskPointVo> {

    List<BizRobotTaskPointVo> selectByTaskId(@Param("taskId") Long taskId);

}
