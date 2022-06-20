package com.project.admin.service;

import com.project.admin.domain.WalletRecord;
import com.project.admin.domain.vo.WalletRecordVo;
import com.project.admin.domain.bo.WalletRecordBo;
import com.project.common.mybatis.core.page.PageQuery;
import com.project.common.mybatis.core.page.TableDataInfo;

import java.util.Collection;
import java.util.List;

/**
 * 钱包交易记录Service接口
 *
 * @author project
 * @date 2022-06-20
 */
public interface IWalletRecordService {

    /**
     * 查询钱包交易记录
     *
     * @param id 钱包交易记录主键
     * @return 钱包交易记录
     */
    WalletRecordVo queryById(Long id);

    /**
     * 查询钱包交易记录列表
     *
     * @param walletRecord 钱包交易记录
     * @return 钱包交易记录集合
     */
    TableDataInfo<WalletRecordVo> queryPageList(WalletRecordBo bo, PageQuery pageQuery);

    /**
     * 查询钱包交易记录列表
     *
     * @param walletRecord 钱包交易记录
     * @return 钱包交易记录集合
     */
    List<WalletRecordVo> queryList(WalletRecordBo bo);

    /**
     * 修改钱包交易记录
     *
     * @param walletRecord 钱包交易记录
     * @return 结果
     */
    Boolean insertByBo(WalletRecordBo bo);

    /**
     * 修改钱包交易记录
     *
     * @param walletRecord 钱包交易记录
     * @return 结果
     */
    Boolean updateByBo(WalletRecordBo bo);

    /**
     * 校验并批量删除钱包交易记录信息
     *
     * @param ids 需要删除的钱包交易记录主键集合
     * @param isValid 是否校验,true-删除前校验,false-不校验
     * @return 结果
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}
