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
import com.project.admin.domain.bo.WalletRecordBo;
import com.project.admin.domain.vo.WalletRecordVo;
import com.project.admin.domain.WalletRecord;
import com.project.admin.mapper.WalletRecordMapper;
import com.project.admin.service.IWalletRecordService;

import java.util.List;
import java.util.Map;
import java.util.Collection;

/**
 * 钱包交易记录Service业务层处理
 *
 * @author project
 * @date 2022-06-20
 */
@RequiredArgsConstructor
@Service
public class WalletRecordServiceImpl implements IWalletRecordService {

    private final WalletRecordMapper baseMapper;

    /**
     * 查询钱包交易记录
     *
     * @param id 钱包交易记录主键
     * @return 钱包交易记录
     */
    @Override
    public WalletRecordVo queryById(Long id){
        return baseMapper.selectVoById(id);
    }

    /**
     * 查询钱包交易记录列表
     *
     * @param bo 钱包交易记录
     * @return 钱包交易记录
     */
    @Override
    public TableDataInfo<WalletRecordVo> queryPageList(WalletRecordBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<WalletRecord> lqw = buildQueryWrapper(bo);
        Page<WalletRecordVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询钱包交易记录列表
     *
     * @param bo 钱包交易记录
     * @return 钱包交易记录
     */
    @Override
    public List<WalletRecordVo> queryList(WalletRecordBo bo) {
        LambdaQueryWrapper<WalletRecord> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<WalletRecord> buildQueryWrapper(WalletRecordBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<WalletRecord> lqw = Wrappers.lambdaQuery();
        lqw.eq(StringUtils.isNotBlank(bo.getNumber()), WalletRecord::getNumber, bo.getNumber());
        lqw.eq(bo.getFromId() != null, WalletRecord::getFromId, bo.getFromId());
        lqw.eq(bo.getToId() != null, WalletRecord::getToId, bo.getToId());
        lqw.eq(bo.getType() != null, WalletRecord::getType, bo.getType());
        lqw.eq(bo.getMoney() != null, WalletRecord::getMoney, bo.getMoney());
        lqw.eq(bo.getPayType() != null, WalletRecord::getPayType, bo.getPayType());
        lqw.eq(StringUtils.isNotBlank(bo.getRemark()), WalletRecord::getRemark, bo.getRemark());
        lqw.eq(bo.getPayStatus() != null, WalletRecord::getPayStatus, bo.getPayStatus());
        lqw.eq(bo.getPayTime() != null, WalletRecord::getPayTime, bo.getPayTime());
        lqw.eq(bo.getFetchStatus() != null, WalletRecord::getFetchStatus, bo.getFetchStatus());
        lqw.eq(bo.getFetchTime() != null, WalletRecord::getFetchTime, bo.getFetchTime());
        lqw.eq(bo.getCheckStatus() != null, WalletRecord::getCheckStatus, bo.getCheckStatus());
        lqw.eq(bo.getCheckTime() != null, WalletRecord::getCheckTime, bo.getCheckTime());
        lqw.eq(bo.getDeleted() != null, WalletRecord::getDeleted, bo.getDeleted());
        lqw.eq(StringUtils.isNotBlank(bo.getSearchValue()), WalletRecord::getSearchValue, bo.getSearchValue());
        return lqw;
    }

    /**
     * 新增钱包交易记录
     *
     * @param bo 钱包交易记录
     * @return 结果
     */
    @Override
    public Boolean insertByBo(WalletRecordBo bo) {
        WalletRecord add = BeanUtil.toBean(bo, WalletRecord.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改钱包交易记录
     *
     * @param bo 钱包交易记录
     * @return 结果
     */
    @Override
    public Boolean updateByBo(WalletRecordBo bo) {
        WalletRecord update = BeanUtil.toBean(bo, WalletRecord.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     *
     * @param entity 实体类数据
     */
    private void validEntityBeforeSave(WalletRecord entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 批量删除钱包交易记录
     *
     * @param ids 需要删除的钱包交易记录主键
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
