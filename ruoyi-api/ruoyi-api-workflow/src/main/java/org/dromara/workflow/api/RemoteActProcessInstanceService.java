package org.dromara.workflow.api;

import org.dromara.workflow.api.domain.bo.ProcessInstanceBo;
import org.dromara.workflow.api.domain.bo.ProcessInvalidBo;
import org.dromara.workflow.api.domain.bo.TaskUrgingBo;
import org.dromara.workflow.api.domain.dto.ActHistoryInfoDTO;
import org.dromara.workflow.api.domain.dto.ProcessInstanceDTO;

import java.util.List;
import java.util.Map;

/**
 * 流程实例 服务
 *
 * @Author ZETA
 * @Date 2024/5/27
 */
public interface RemoteActProcessInstanceService {

    /**
     * 通过流程实例id获取历史流程图
     *
     * @param processInstanceId 流程实例id
     * @return 结果
     */
    String getHistoryImage(String processInstanceId);

    /**
     * 通过流程实例id获取历史流程图运行中，历史等节点
     *
     * @param processInstanceId 流程实例id
     * @return 结果
     */
    Map<String, Object> getHistoryList(String processInstanceId);

    /**
     * 查询正在运行的流程实例
     *
     * @param processInstanceBo 参数
     * @return 结果
     */
    List<ProcessInstanceDTO> getRunning(ProcessInstanceBo processInstanceBo);

    /**
     * 查询已结束的流程实例
     *
     * @param processInstanceBo 参数
     * @return 结果
     */
    List<ProcessInstanceDTO> getFinish(ProcessInstanceBo processInstanceBo);

    /**
     * 获取审批记录
     *
     * @param processInstanceId 流程实例id
     * @return 结果
     */
    List<ActHistoryInfoDTO> getHistoryRecord(String processInstanceId);

    /**
     * 作废流程实例，不会删除历史记录(删除运行中的实例)
     *
     * @param processInvalidBo 参数
     * @return 结果
     */
    boolean deleteRunInstance(ProcessInvalidBo processInvalidBo);

    /**
     * 运行中的实例 删除程实例，删除历史记录，删除业务与流程关联信息
     *
     * @param processInstanceIds 流程实例id
     * @return 结果
     */
    boolean deleteRunAndHisInstance(List<String> processInstanceIds);

    /**
     * 按照业务id删除 运行中的实例 删除程实例，删除历史记录，删除业务与流程关联信息
     *
     * @param businessKeys 业务id
     * @return 结果
     */
    boolean deleteRunAndHisInstanceByBusinessKeys(List<String> businessKeys);

    /**
     * 已完成的实例 删除程实例，删除历史记录，删除业务与流程关联信息
     *
     * @param processInstanceIds 流程实例id
     * @return 结果
     */
    boolean deleteFinishAndHisInstance(List<String> processInstanceIds);

    /**
     * 撤销流程申请
     *
     * @param processInstanceId 流程实例id
     * @return 结果
     */
    boolean cancelProcessApply(String processInstanceId);

    /**
     * 查询当前登录人单据
     *
     * @param processInstanceBo 参数
     * @return 结果
     */
    List<ProcessInstanceDTO> getCurrent(ProcessInstanceBo processInstanceBo);

    /**
     * 任务催办(给当前任务办理人发送站内信，邮件，短信等)
     *
     * @param taskUrgingBo 任务催办
     * @return 结果
     */
    boolean taskUrging(TaskUrgingBo taskUrgingBo);
}
