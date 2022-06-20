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
import com.project.admin.domain.bo.WalletBo;
import com.project.admin.domain.vo.WalletVo;
import com.project.admin.domain.Wallet;
import com.project.admin.mapper.WalletMapper;
import com.project.admin.service.IWalletService;

import java.util.List;
import java.util.Map;
import java.util.Collection;

/**
 * 用户钱包Service业务层处理
 *
 * @author project
 * @date 2022-06-19
 */
@RequiredArgsConstructor
@Service
public class WalletServiceImpl implements IWalletService {

    private final WalletMapper baseMapper;

    /**
     * 查询用户钱包
     *
     * @param id 用户钱包主键
     * @return 用户钱包
     */
    @Override
    public WalletVo queryById(Long id){
        return baseMapper.selectVoById(id);
    }

    /**
     * 查询用户钱包列表
     *
     * @param bo 用户钱包
     * @return 用户钱包
     */
    @Override
    public TableDataInfo<WalletVo> queryPageList(WalletBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<Wallet> lqw = buildQueryWrapper(bo);
        Page<WalletVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询用户钱包列表
     *
     * @param bo 用户钱包
     * @return 用户钱包
     */
    @Override
    public List<WalletVo> queryList(WalletBo bo) {
        LambdaQueryWrapper<Wallet> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<Wallet> buildQueryWrapper(WalletBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<Wallet> lqw = Wrappers.lambdaQuery();
        lqw.eq(bo.getUserId() != null, Wallet::getUserId, bo.getUserId());
        lqw.eq(bo.getBalance() != null, Wallet::getBalance, bo.getBalance());
        lqw.eq(bo.getDeleted() != null, Wallet::getDeleted, bo.getDeleted());
        lqw.eq(StringUtils.isNotBlank(bo.getSearchValue()), Wallet::getSearchValue, bo.getSearchValue());
        return lqw;
    }

    /**
     * 新增用户钱包
     *
     * @param bo 用户钱包
     * @return 结果
     */
    @Override
    public Boolean insertByBo(WalletBo bo) {
        Wallet add = BeanUtil.toBean(bo, Wallet.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改用户钱包
     *
     * @param bo 用户钱包
     * @return 结果
     */
    @Override
    public Boolean updateByBo(WalletBo bo) {
        Wallet update = BeanUtil.toBean(bo, Wallet.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     *
     * @param entity 实体类数据
     */
    private void validEntityBeforeSave(Wallet entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 批量删除用户钱包
     *
     * @param ids 需要删除的用户钱包主键
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
