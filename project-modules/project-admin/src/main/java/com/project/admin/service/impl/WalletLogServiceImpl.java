package com.project.admin.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.project.common.core.utils.StringUtils;
import com.project.common.mybatis.core.page.PageQuery;
import com.project.common.mybatis.core.page.TableDataInfo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.project.admin.domain.bo.WalletLogBo;
import com.project.admin.domain.vo.WalletLogVo;
import com.project.admin.domain.WalletLog;
import com.project.admin.mapper.WalletLogMapper;
import com.project.admin.service.IWalletLogService;

import java.util.List;
import java.util.Map;
import java.util.Collection;

/**
 * 钱包变动日志Service业务层处理
 *
 * @author project
 * @date 2022-06-20
 */
@RequiredArgsConstructor
@Service
public class WalletLogServiceImpl implements IWalletLogService {

    private final WalletLogMapper baseMapper;

    /**
     * 查询钱包变动日志
     *
     * @param id 钱包变动日志主键
     * @return 钱包变动日志
     */
    @Override
    public WalletLogVo queryById(Long id){
        return baseMapper.selectVoById(id);
    }

    /**
     * 查询钱包变动日志列表
     *
     * @param bo 钱包变动日志
     * @return 钱包变动日志
     */
    @Override
    public TableDataInfo<WalletLogVo> queryPageList(WalletLogBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<WalletLog> lqw = buildQueryWrapper(bo);
        Page<WalletLogVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询钱包变动日志列表
     *
     * @param bo 钱包变动日志
     * @return 钱包变动日志
     */
    @Override
    public List<WalletLogVo> queryList(WalletLogBo bo) {
        LambdaQueryWrapper<WalletLog> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<WalletLog> buildQueryWrapper(WalletLogBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<WalletLog> lqw = Wrappers.lambdaQuery();
        lqw.eq(StringUtils.isNotBlank(bo.getNumber()), WalletLog::getNumber, bo.getNumber());
        lqw.eq(bo.getUserId() != null, WalletLog::getUserId, bo.getUserId());
        lqw.eq(bo.getBeforeMoney() != null, WalletLog::getBeforeMoney, bo.getBeforeMoney());
        lqw.eq(bo.getAfterMoney() != null, WalletLog::getAfterMoney, bo.getAfterMoney());
        lqw.eq(bo.getTargetType() != null, WalletLog::getTargetType, bo.getTargetType());
        lqw.eq(bo.getTargetId() != null, WalletLog::getTargetId, bo.getTargetId());
        lqw.eq(bo.getStatus() != null, WalletLog::getStatus, bo.getStatus());
        lqw.eq(bo.getResultType() != null, WalletLog::getResultType, bo.getResultType());
        lqw.eq(StringUtils.isNotBlank(bo.getRemark()), WalletLog::getRemark, bo.getRemark());
        lqw.eq(bo.getDeleted() != null, WalletLog::getDeleted, bo.getDeleted());
        lqw.eq(StringUtils.isNotBlank(bo.getSearchValue()), WalletLog::getSearchValue, bo.getSearchValue());
        return lqw;
    }

    /**
     * 新增钱包变动日志
     *
     * @param bo 钱包变动日志
     * @return 结果
     */
    @Override
    public Boolean insertByBo(WalletLogBo bo) {
        WalletLog add = BeanUtil.toBean(bo, WalletLog.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改钱包变动日志
     *
     * @param bo 钱包变动日志
     * @return 结果
     */
    @Override
    public Boolean updateByBo(WalletLogBo bo) {
        WalletLog update = BeanUtil.toBean(bo, WalletLog.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     *
     * @param entity 实体类数据
     */
    private void validEntityBeforeSave(WalletLog entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 批量删除钱包变动日志
     *
     * @param ids 需要删除的钱包变动日志主键
     * @return 结果
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if(isValid){
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return baseMapper.deleteBatchIds(ids) > 0;
    }
}
