package org.dromara.demo.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.apache.dubbo.config.annotation.DubboReference;
import org.dromara.common.core.utils.MapstructUtils;
import org.dromara.common.core.utils.StreamUtils;
import org.dromara.common.core.utils.StringUtils;
import org.dromara.common.mybatis.core.domain.BaseEntity;
import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.demo.domain.TestLeave;
import org.dromara.demo.domain.bo.TestLeaveBo;
import org.dromara.demo.domain.vo.TestLeaveVo;
import org.dromara.demo.mapper.TestLeaveMapper;
import org.dromara.demo.service.ITestLeaveService;
import org.dromara.workflow.api.RemoteActHiProcinstService;
import org.dromara.workflow.api.RemoteActProcessInstanceService;
import org.dromara.workflow.api.domain.dto.ProcessInstanceDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

/**
 * 请假Service业务层处理
 *
 * @author may
 * @date 2023-07-21
 */
@RequiredArgsConstructor
@Service
public class TestLeaveServiceImpl implements ITestLeaveService {

    private final TestLeaveMapper baseMapper;

    @DubboReference
    private RemoteActHiProcinstService actHiProcinstService;
    @DubboReference
    private final RemoteActProcessInstanceService actProcessInstanceService;

    /**
     * 查询请假
     */
    @Override
    public TestLeaveVo queryById(Long id) {
        TestLeaveVo testLeaveVo = baseMapper.selectVoById(id);
        ProcessInstanceDTO processInstance = actHiProcinstService.getProcessInstance(String.valueOf(id));
        testLeaveVo.setProcessInstanceVo(processInstance);
        return testLeaveVo;
    }

    /**
     * 查询请假列表
     */
    @Override
    public TableDataInfo<TestLeaveVo> queryPageList(TestLeaveBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<TestLeave> lqw = buildQueryWrapper(bo);
        Page<TestLeaveVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        TableDataInfo<TestLeaveVo> build = TableDataInfo.build(result);
        List<TestLeaveVo> rows = build.getRows();
        if (CollUtil.isNotEmpty(rows)) {
            List<String> ids = StreamUtils.toList(rows, e -> String.valueOf(e.getId()));
            List<ProcessInstanceDTO> processInstances = actHiProcinstService.getProcessInstances(ids);
            for (TestLeaveVo e : rows) {
                ProcessInstanceDTO processInstanceDTO = null;
                for (ProcessInstanceDTO processInstance : processInstances) {
                    if (String.valueOf(e.getId()).equals(processInstance.getBusinessKey())) {
                        processInstanceDTO = processInstance;
                        break;
                    }
                }
                e.setProcessInstanceVo(processInstanceDTO);
            }
        }
        return build;
    }

    /**
     * 查询请假列表
     */
    @Override
    public List<TestLeaveVo> queryList(TestLeaveBo bo) {
        LambdaQueryWrapper<TestLeave> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<TestLeave> buildQueryWrapper(TestLeaveBo bo) {
        LambdaQueryWrapper<TestLeave> lqw = Wrappers.lambdaQuery();
        lqw.eq(StringUtils.isNotBlank(bo.getLeaveType()), TestLeave::getLeaveType, bo.getLeaveType());
        lqw.ge(bo.getStartLeaveDays() != null, TestLeave::getLeaveDays, bo.getStartLeaveDays());
        lqw.le(bo.getEndLeaveDays() != null, TestLeave::getLeaveDays, bo.getEndLeaveDays());
        lqw.orderByDesc(BaseEntity::getCreateTime);
        return lqw;
    }

    /**
     * 新增请假
     */
    @Override
    public TestLeaveVo insertByBo(TestLeaveBo bo) {
        TestLeave add = MapstructUtils.convert(bo, TestLeave.class);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        TestLeaveVo testLeaveVo = MapstructUtils.convert(add, TestLeaveVo.class);
        ProcessInstanceDTO processInstance = actHiProcinstService.getProcessInstance(String.valueOf(add.getId()));
        testLeaveVo.setProcessInstanceVo(processInstance);
        return testLeaveVo;
    }

    /**
     * 修改请假
     */
    @Override
    public TestLeaveVo updateByBo(TestLeaveBo bo) {
        TestLeave update = MapstructUtils.convert(bo, TestLeave.class);
        baseMapper.updateById(update);
        TestLeaveVo testLeaveVo = MapstructUtils.convert(update, TestLeaveVo.class);
        ProcessInstanceDTO processInstance = actHiProcinstService.getProcessInstance(String.valueOf(update.getId()));
        testLeaveVo.setProcessInstanceVo(processInstance);
        return testLeaveVo;
    }

    /**
     * 批量删除请假
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteWithValidByIds(Collection<Long> ids) {
        List<String> idList = StreamUtils.toList(ids, String::valueOf);
        actProcessInstanceService.deleteRunAndHisInstanceByBusinessKeys(idList);
        return baseMapper.deleteBatchIds(ids) > 0;
    }
}
