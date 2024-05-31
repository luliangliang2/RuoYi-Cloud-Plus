package org.dromara.workflow.dubbo;

import cn.hutool.core.bean.BeanUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;
import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.workflow.api.RemoteActProcessInstanceService;
import org.dromara.workflow.api.domain.bo.ProcessInstanceBo;
import org.dromara.workflow.api.domain.bo.ProcessInvalidBo;
import org.dromara.workflow.api.domain.bo.TaskUrgingBo;
import org.dromara.workflow.api.domain.dto.ActHistoryInfoDTO;
import org.dromara.workflow.api.domain.dto.ProcessInstanceDTO;
import org.dromara.workflow.domain.vo.ActHistoryInfoVo;
import org.dromara.workflow.domain.vo.ProcessInstanceVo;
import org.dromara.workflow.service.IActProcessInstanceService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 流程实例服务
 *
 * @Author ZETA
 * @Date 2024/5/29
 */
@Slf4j
@Service
@RequiredArgsConstructor
@DubboService
public class RemoteActProcessInstanceServiceImpl implements RemoteActProcessInstanceService {

    private final IActProcessInstanceService service;

    @Override
    public String getHistoryImage(String processInstanceId) {
        return service.getHistoryImage(processInstanceId);
    }

    @Override
    public Map<String, Object> getHistoryList(String processInstanceId) {
        return service.getHistoryList(processInstanceId);
    }

    @Override
    public List<ProcessInstanceDTO> getRunning(ProcessInstanceBo processInstanceBo) {
        TableDataInfo<ProcessInstanceVo> page = service.getPageByRunning(processInstanceBo, PageQuery.DEFAULT_PAGE);
        return BeanUtil.copyToList(page.getRows(), ProcessInstanceDTO.class);
    }

    @Override
    public List<ProcessInstanceDTO> getFinish(ProcessInstanceBo processInstanceBo) {
        TableDataInfo<ProcessInstanceVo> page = service.getPageByFinish(processInstanceBo, PageQuery.DEFAULT_PAGE);
        return BeanUtil.copyToList(page.getRows(), ProcessInstanceDTO.class);
    }

    @Override
    public List<ActHistoryInfoDTO> getHistoryRecord(String processInstanceId) {
        List<ActHistoryInfoVo> list = service.getHistoryRecord(processInstanceId);
        return BeanUtil.copyToList(list, ActHistoryInfoDTO.class);
    }

    @Override
    public boolean deleteRunInstance(ProcessInvalidBo processInvalidBo) {
        return service.deleteRunInstance(processInvalidBo);
    }

    @Override
    public boolean deleteRunAndHisInstance(List<String> processInstanceIds) {
        return service.deleteRunAndHisInstance(processInstanceIds);
    }

    @Override
    public boolean deleteRunAndHisInstanceByBusinessKeys(List<String> businessKeys) {
        return service.deleteRunAndHisInstanceByBusinessKeys(businessKeys);
    }

    @Override
    public boolean deleteFinishAndHisInstance(List<String> processInstanceIds) {
        return service.deleteFinishAndHisInstance(processInstanceIds);
    }

    @Override
    public boolean cancelProcessApply(String processInstanceId) {
        return service.cancelProcessApply(processInstanceId);
    }

    @Override
    public List<ProcessInstanceDTO> getCurrent(ProcessInstanceBo processInstanceBo) {
        TableDataInfo<ProcessInstanceVo> page = service.getPageByCurrent(processInstanceBo, PageQuery.DEFAULT_PAGE);
        return BeanUtil.copyToList(page.getRows(), ProcessInstanceDTO.class);
    }

    @Override
    public boolean taskUrging(TaskUrgingBo taskUrgingBo) {
        return service.taskUrging(taskUrgingBo);
    }
}
