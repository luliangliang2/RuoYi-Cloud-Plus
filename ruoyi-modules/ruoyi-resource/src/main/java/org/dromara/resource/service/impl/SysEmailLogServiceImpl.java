package org.dromara.resource.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.dromara.common.core.utils.MapstructUtils;
import org.dromara.common.core.utils.StringUtils;
import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.resource.domain.SysEmailLog;
import org.dromara.resource.domain.bo.SysEmailLogBo;
import org.dromara.resource.domain.vo.SysEmailLogVo;
import org.dromara.resource.mapper.SysEmailLogMapper;
import org.dromara.resource.service.ISysEmailLogService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 邮件日志Service业务层处理
 *
 * @author 2100
 */
@RequiredArgsConstructor
@Service
public class SysEmailLogServiceImpl implements ISysEmailLogService {

    private final SysEmailLogMapper baseMapper;

    /**
     * 根据邮件日志主键查询单个邮件日志
     *
     * @param emailId 邮件日志主键
     * @return 查询到的邮件日志，如果不存在则返回 null
     */
    @Override
    public SysEmailLogVo queryById(Long emailId) {
        return baseMapper.selectVoById(emailId);
    }

    /**
     * 分页查询邮件日志列表
     *
     * @param bo        包含查询条件的业务对象
     * @param pageQuery 分页查询参数
     * @return 分页查询结果
     */
    @Override
    public TableDataInfo<SysEmailLogVo> queryPageList(SysEmailLogBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<SysEmailLog> lqw = buildQueryWrapper(bo);
        Page<SysEmailLogVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询邮件日志列表
     *
     * @param bo 包含查询条件的业务对象
     * @return 邮件日志列表
     */
    @Override
    public List<SysEmailLogVo> queryList(SysEmailLogBo bo) {
        LambdaQueryWrapper<SysEmailLog> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    /**
     * 构建 LambdaQueryWrapper 查询条件
     *
     * @param bo 包含查询条件的业务对象
     * @return LambdaQueryWrapper 查询条件
     */
    private LambdaQueryWrapper<SysEmailLog> buildQueryWrapper(SysEmailLogBo bo) {
        Map<String, Object> params = bo.getParams();
        // 设置查询条件
        LambdaQueryWrapper<SysEmailLog> lqw = Wrappers.lambdaQuery();
        lqw.eq(StringUtils.isNotBlank(bo.getBusinessLevel()), SysEmailLog::getBusinessLevel, bo.getBusinessLevel());
        lqw.eq(StringUtils.isNotBlank(bo.getMessageType()), SysEmailLog::getMessageType, bo.getMessageType());
        lqw.like(StringUtils.isNotBlank(bo.getMessageId()), SysEmailLog::getMessageId, bo.getMessageId());
        lqw.like(StringUtils.isNotBlank(bo.getTos()), SysEmailLog::getTos, bo.getTos());
        lqw.like(StringUtils.isNotBlank(bo.getCcs()), SysEmailLog::getCcs, bo.getCcs());
        lqw.like(StringUtils.isNotBlank(bo.getBccs()), SysEmailLog::getBccs, bo.getBccs());
        lqw.like(StringUtils.isNotBlank(bo.getSubject()), SysEmailLog::getSubject, bo.getSubject());
        lqw.eq(StringUtils.isNotBlank(bo.getEmailType()), SysEmailLog::getEmailType, bo.getEmailType());
        lqw.eq(StringUtils.isNotBlank(bo.getStatus()), SysEmailLog::getStatus, bo.getStatus());

        // 处理时间范围查询
        lqw.between(params.get("beginCreateTime") != null && params.get("endCreateTime") != null,
            SysEmailLog::getCreateTime, params.get("beginCreateTime"), params.get("endCreateTime"));
        return lqw;
    }

    /**
     * 插入邮件日志数据
     *
     * @param bo 包含邮件日志信息的业务对象
     * @return 插入是否成功，true 表示成功，false 表示失败
     */
    @Override
    public Boolean insertByBo(SysEmailLogBo bo) {
        SysEmailLog add = MapstructUtils.convert(bo, SysEmailLog.class);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setEmailId(add.getEmailId());
        }
        return flag;
    }

}
