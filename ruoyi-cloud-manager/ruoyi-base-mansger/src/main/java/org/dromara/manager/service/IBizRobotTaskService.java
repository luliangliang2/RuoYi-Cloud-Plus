package org.dromara.manager.service;

import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.manager.domain.bo.BizRobotTaskBo;
import org.dromara.manager.domain.bo.RobotTaskStepReportBo;
import org.dromara.manager.domain.vo.BizRobotTaskVo;
import org.dromara.manager.domain.vo.RobotTaskRuntimeStatusVo;
import org.dromara.manager.domain.vo.RobotTaskVehicleBindVo;

import java.util.Collection;
import java.util.List;

/**
 * 机器人任务执行Service接口
 *
 * @author LionLi
 * @date 2026-06-19
 */
public interface IBizRobotTaskService {

    BizRobotTaskVo queryById(Long taskId);

    TableDataInfo<BizRobotTaskVo> queryPageList(BizRobotTaskBo bo, PageQuery pageQuery);

    List<BizRobotTaskVo> queryList(BizRobotTaskBo bo);

    Boolean insertByBo(BizRobotTaskBo bo);

    Boolean updateByBo(BizRobotTaskBo bo);

    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

    Boolean startTask(Long taskId);

    Boolean cancelTask(Long taskId);

    List<RobotTaskRuntimeStatusVo> queryRuntimeStatus(List<Long> taskIds);

    Boolean reportStep(RobotTaskStepReportBo bo);

    RobotTaskVehicleBindVo getVehicleByTaskId(Long taskId);

    RobotTaskVehicleBindVo getVehicleByTaskNo(String taskNo);

    RobotTaskVehicleBindVo getTaskByVehicleId(Long vehicleId);

    RobotTaskVehicleBindVo getTaskByVin(String vin);

}
