package com.project.admin.service;

import com.project.admin.domain.UserInfo;
import com.project.admin.domain.vo.UserInfoVo;
import com.project.admin.domain.bo.UserInfoBo;
import com.project.common.mybatis.core.page.PageQuery;
import com.project.common.mybatis.core.page.TableDataInfo;

import java.util.Collection;
import java.util.List;

/**
 * 用户信息Service接口
 *
 * @author huan.li
 * @date 2022-06-16
 */
public interface IUserInfoService {

    /**
     * 查询用户信息
     *
     * @param id 用户信息主键
     * @return 用户信息
     */
    UserInfoVo queryById(Long id);

    /**
     * 查询用户信息列表
     *
     * @param userInfo 用户信息
     * @return 用户信息集合
     */
    TableDataInfo<UserInfoVo> queryPageList(UserInfoBo bo, PageQuery pageQuery);

    /**
     * 查询用户信息列表
     *
     * @param userInfo 用户信息
     * @return 用户信息集合
     */
    List<UserInfoVo> queryList(UserInfoBo bo);

    /**
     * 修改用户信息
     *
     * @param userInfo 用户信息
     * @return 结果
     */
    Boolean insertByBo(UserInfoBo bo);

    /**
     * 修改用户信息
     *
     * @param userInfo 用户信息
     * @return 结果
     */
    Boolean updateByBo(UserInfoBo bo);

    /**
     * 校验并批量删除用户信息信息
     *
     * @param ids 需要删除的用户信息主键集合
     * @param isValid 是否校验,true-删除前校验,false-不校验
     * @return 结果
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}
