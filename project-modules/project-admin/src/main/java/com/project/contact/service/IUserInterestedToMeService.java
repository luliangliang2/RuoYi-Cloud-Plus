package com.project.contact.service;

import com.project.contact.domain.UserInterestedToMe;
import com.project.contact.domain.vo.UserInterestedToMeVo;
import com.project.contact.domain.bo.UserInterestedToMeBo;
import com.project.common.mybatis.core.page.PageQuery;
import com.project.common.mybatis.core.page.TableDataInfo;

import java.util.Collection;
import java.util.List;

/**
 * 对我感兴趣Service接口
 *
 * @author huan.li
 * @date 2022-06-10
 */
public interface IUserInterestedToMeService {

    /**
     * 查询对我感兴趣
     *
     * @param id 对我感兴趣主键
     * @return 对我感兴趣
     */
    UserInterestedToMeVo queryById(Long id);

    /**
     * 查询对我感兴趣列表
     *
     * @param userInterestedToMe 对我感兴趣
     * @return 对我感兴趣集合
     */
    TableDataInfo<UserInterestedToMeVo> queryPageList(UserInterestedToMeBo bo, PageQuery pageQuery);

    /**
     * 查询对我感兴趣列表
     *
     * @param userInterestedToMe 对我感兴趣
     * @return 对我感兴趣集合
     */
    List<UserInterestedToMeVo> queryList(UserInterestedToMeBo bo);

    /**
     * 修改对我感兴趣
     *
     * @param userInterestedToMe 对我感兴趣
     * @return 结果
     */
    Boolean insertByBo(UserInterestedToMeBo bo);

    /**
     * 修改对我感兴趣
     *
     * @param userInterestedToMe 对我感兴趣
     * @return 结果
     */
    Boolean updateByBo(UserInterestedToMeBo bo);

    /**
     * 校验并批量删除对我感兴趣信息
     *
     * @param ids 需要删除的对我感兴趣主键集合
     * @param isValid 是否校验,true-删除前校验,false-不校验
     * @return 结果
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}
