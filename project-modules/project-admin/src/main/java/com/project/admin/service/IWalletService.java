package com.project.admin.service;

import com.project.admin.domain.Wallet;
import com.project.admin.domain.vo.WalletVo;
import com.project.admin.domain.bo.WalletBo;
import com.project.common.mybatis.core.page.PageQuery;
import com.project.common.mybatis.core.page.TableDataInfo;

import java.util.Collection;
import java.util.List;

/**
 * 用户钱包Service接口
 *
 * @author project
 * @date 2022-06-19
 */
public interface IWalletService {

    /**
     * 查询用户钱包
     *
     * @param id 用户钱包主键
     * @return 用户钱包
     */
    WalletVo queryById(Long id);

    /**
     * 查询用户钱包列表
     *
     * @param wallet 用户钱包
     * @return 用户钱包集合
     */
    TableDataInfo<WalletVo> queryPageList(WalletBo bo, PageQuery pageQuery);

    /**
     * 查询用户钱包列表
     *
     * @param wallet 用户钱包
     * @return 用户钱包集合
     */
    List<WalletVo> queryList(WalletBo bo);

    /**
     * 修改用户钱包
     *
     * @param wallet 用户钱包
     * @return 结果
     */
    Boolean insertByBo(WalletBo bo);

    /**
     * 修改用户钱包
     *
     * @param wallet 用户钱包
     * @return 结果
     */
    Boolean updateByBo(WalletBo bo);

    /**
     * 校验并批量删除用户钱包信息
     *
     * @param ids 需要删除的用户钱包主键集合
     * @param isValid 是否校验,true-删除前校验,false-不校验
     * @return 结果
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}
