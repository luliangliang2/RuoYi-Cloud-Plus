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
import com.project.admin.domain.bo.UserFriendGroupBo;
import com.project.admin.domain.vo.UserFriendGroupVo;
import com.project.admin.domain.UserFriendGroup;
import com.project.admin.mapper.UserFriendGroupMapper;
import com.project.admin.service.IUserFriendGroupService;

import java.util.List;
import java.util.Map;
import java.util.Collection;

/**
 * 好友分组Service业务层处理
 *
 * @author huan.li
 * @date 2022-06-16
 */
@RequiredArgsConstructor
@Service
public class UserFriendGroupServiceImpl implements IUserFriendGroupService {

    private final UserFriendGroupMapper baseMapper;

    /**
     * 查询好友分组
     *
     * @param id 好友分组主键
     * @return 好友分组
     */
    @Override
    public UserFriendGroupVo queryById(Long id){
        return baseMapper.selectVoById(id);
    }

    /**
     * 查询好友分组列表
     *
     * @param bo 好友分组
     * @return 好友分组
     */
    @Override
    public TableDataInfo<UserFriendGroupVo> queryPageList(UserFriendGroupBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<UserFriendGroup> lqw = buildQueryWrapper(bo);
        Page<UserFriendGroupVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询好友分组列表
     *
     * @param bo 好友分组
     * @return 好友分组
     */
    @Override
    public List<UserFriendGroupVo> queryList(UserFriendGroupBo bo) {
        LambdaQueryWrapper<UserFriendGroup> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<UserFriendGroup> buildQueryWrapper(UserFriendGroupBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<UserFriendGroup> lqw = Wrappers.lambdaQuery();
        lqw.like(StringUtils.isNotBlank(bo.getGroupName()), UserFriendGroup::getGroupName, bo.getGroupName());
        lqw.eq(StringUtils.isNotBlank(bo.getSearchValue()), UserFriendGroup::getSearchValue, bo.getSearchValue());
        lqw.eq(bo.getDeleted() != null, UserFriendGroup::getDeleted, bo.getDeleted());
        return lqw;
    }

    /**
     * 新增好友分组
     *
     * @param bo 好友分组
     * @return 结果
     */
    @Override
    public Boolean insertByBo(UserFriendGroupBo bo) {
        UserFriendGroup add = BeanUtil.toBean(bo, UserFriendGroup.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改好友分组
     *
     * @param bo 好友分组
     * @return 结果
     */
    @Override
    public Boolean updateByBo(UserFriendGroupBo bo) {
        UserFriendGroup update = BeanUtil.toBean(bo, UserFriendGroup.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     *
     * @param entity 实体类数据
     */
    private void validEntityBeforeSave(UserFriendGroup entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 批量删除好友分组
     *
     * @param ids 需要删除的好友分组主键
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
