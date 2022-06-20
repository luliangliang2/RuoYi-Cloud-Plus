package com.project.admin.service;

import com.project.admin.domain.WalletLog;
import com.project.admin.domain.vo.WalletLogVo;
import com.project.admin.domain.bo.WalletLogBo;
import com.project.common.mybatis.core.page.PageQuery;
import com.project.common.mybatis.core.page.TableDataInfo;

import java.util.Collection;
import java.util.List;

/**
 * 钱包变动日志Service接口
 *
 * @author project
 * @date 2022-06-20
 */
public interface IWalletLogService {

    /**
     * 查询钱包变动日志
     *
     * @param id 钱包变动日志主键
     * @return 钱包变动日志
     */
    WalletLogVo queryById(Long id);

    /**
     * 查询钱包变动日志列表
     *
     * @param walletLog 钱包变动日志
     * @return 钱包变动日志集合
     */
    TableDataInfo<WalletLogVo> queryPageList(WalletLogBo bo, PageQuery pageQuery);

    /**
     * 查询钱包变动日志列表
     *
     * @param walletLog 钱包变动日志
     * @return 钱包变动日志集合
     */
    List<WalletLogVo> queryList(WalletLogBo bo);

    /**
     * 修改钱包变动日志
     *
     * @param walletLog 钱包变动日志
     * @return 结果
     */
    Boolean insertByBo(WalletLogBo bo);

    /**
     * 修改钱包变动日志
     *
     * @param walletLog 钱包变动日志
     * @return 结果
     */
    Boolean updateByBo(WalletLogBo bo);

    /**
     * 校验并批量删除钱包变动日志信息
     *
     * @param ids 需要删除的钱包变动日志主键集合
     * @param isValid 是否校验,true-删除前校验,false-不校验
     * @return 结果
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}
