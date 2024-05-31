package org.dromara.workflow.dubbo;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;
import org.dromara.common.core.utils.StringUtils;
import org.dromara.workflow.api.RemoteActHiProcinstService;
import org.dromara.workflow.api.domain.dto.ProcessInstanceDTO;
import org.dromara.workflow.common.enums.BusinessStatusEnum;
import org.dromara.workflow.domain.ActHiProcinst;
import org.dromara.workflow.service.IActHiProcinstService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 流程实例服务
 *
 * @Author ZETA
 * @Date 2024/5/27
 */
@Slf4j
@Service
@RequiredArgsConstructor
@DubboService
public class RemoteActHiProcinstServiceImpl implements RemoteActHiProcinstService {

    private final IActHiProcinstService actHiProcinstService;

    @Override
    public ProcessInstanceDTO getProcessInstance(String businessKey) {
        if (StringUtils.isBlank(businessKey)) {
            return null;
        }
        ActHiProcinst actHiProcinst = actHiProcinstService.selectByBusinessKey(businessKey);
        if (actHiProcinst == null) {
            ProcessInstanceDTO processInstanceDTO = new ProcessInstanceDTO();
            processInstanceDTO.setBusinessKey(businessKey);
            processInstanceDTO.setBusinessStatus(BusinessStatusEnum.DRAFT.getStatus());
            return processInstanceDTO;
        }
        ProcessInstanceDTO processInstanceDTO = BeanUtil.toBean(actHiProcinst, ProcessInstanceDTO.class);
        processInstanceDTO.setBusinessStatusName(BusinessStatusEnum.findByStatus(processInstanceDTO.getBusinessStatus()));

        return processInstanceDTO;
    }

    @Override
    public List<ProcessInstanceDTO> getProcessInstances(List<String> idList) {
        if (CollUtil.isEmpty(idList)) {
            return null;
        }
        List<ActHiProcinst> actHiProcinstList = actHiProcinstService.selectByBusinessKeyIn(idList);
        List<ProcessInstanceDTO> result = BeanUtil.copyToList(actHiProcinstList, ProcessInstanceDTO.class);
        result.forEach(dto -> dto.setBusinessStatusName(BusinessStatusEnum.findByStatus(dto.getBusinessStatus())));

        return result;
    }
}
