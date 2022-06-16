package com.project.admin.service;

import com.project.admin.domain.UserFriendGroup;
import com.project.admin.domain.vo.UserFriendGroupVo;
import com.project.admin.domain.bo.UserFriendGroupBo;
import com.project.common.mybatis.core.page.PageQuery;
import com.project.common.mybatis.core.page.TableDataInfo;

import java.util.Collection;
import java.util.List;

/**
 * 好友分组Service接口
 *
 * @author huan.li
 * @date 2022-06-16
 */
public interface IUserFriendGroupService {

    /**
     * 查询好友分组
     *
     * @param id 好友分组主键
     * @return 好友分组
     */
    UserFriendGroupVo queryById(Long id);

    /**
     * 查询好友分组列表
     *
     * @param userFriendGroup 好友分组
     * @return 好友分组集合
     */
    TableDataInfo<UserFriendGroupVo> queryPageList(UserFriendGroupBo bo, PageQuery pageQuery);

    /**
     * 查询好友分组列表
     *
     * @param userFriendGroup 好友分组
     * @return 好友分组集合
     */
    List<UserFriendGroupVo> queryList(UserFriendGroupBo bo);

    /**
     * 修改好友分组
     *
     * @param userFriendGroup 好友分组
     * @return 结果
     */
    Boolean insertByBo(UserFriendGroupBo bo);

    /**
     * 修改好友分组
     *
     * @param userFriendGroup 好友分组
     * @return 结果
     */
    Boolean updateByBo(UserFriendGroupBo bo);

    /**
     * 校验并批量删除好友分组信息
     *
     * @param ids 需要删除的好友分组主键集合
     * @param isValid 是否校验,true-删除前校验,false-不校验
     * @return 结果
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}
