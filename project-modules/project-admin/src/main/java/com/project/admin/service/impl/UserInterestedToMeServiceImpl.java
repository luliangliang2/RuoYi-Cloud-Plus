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
import com.project.admin.domain.bo.UserInterestedToMeBo;
import com.project.admin.domain.vo.UserInterestedToMeVo;
import com.project.admin.domain.UserInterestedToMe;
import com.project.admin.mapper.UserInterestedToMeMapper;
import com.project.admin.service.IUserInterestedToMeService;

import java.util.List;
import java.util.Map;
import java.util.Collection;

/**
 * 对我感兴趣Service业务层处理
 *
 * @author huan.li
 * @date 2022-06-16
 */
@RequiredArgsConstructor
@Service
public class UserInterestedToMeServiceImpl implements IUserInterestedToMeService {

    private final UserInterestedToMeMapper baseMapper;

    /**
     * 查询对我感兴趣
     *
     * @param id 对我感兴趣主键
     * @return 对我感兴趣
     */
    @Override
    public UserInterestedToMeVo queryById(Long id){
        return baseMapper.selectVoById(id);
    }

    /**
     * 查询对我感兴趣列表
     *
     * @param bo 对我感兴趣
     * @return 对我感兴趣
     */
    @Override
    public TableDataInfo<UserInterestedToMeVo> queryPageList(UserInterestedToMeBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<UserInterestedToMe> lqw = buildQueryWrapper(bo);
        Page<UserInterestedToMeVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询对我感兴趣列表
     *
     * @param bo 对我感兴趣
     * @return 对我感兴趣
     */
    @Override
    public List<UserInterestedToMeVo> queryList(UserInterestedToMeBo bo) {
        LambdaQueryWrapper<UserInterestedToMe> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<UserInterestedToMe> buildQueryWrapper(UserInterestedToMeBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<UserInterestedToMe> lqw = Wrappers.lambdaQuery();
        lqw.eq(bo.getUserInfoId() != null, UserInterestedToMe::getUserInfoId, bo.getUserInfoId());
        lqw.eq(bo.getFriendInfoId() != null, UserInterestedToMe::getFriendInfoId, bo.getFriendInfoId());
        lqw.eq(StringUtils.isNotBlank(bo.getSearchValue()), UserInterestedToMe::getSearchValue, bo.getSearchValue());
        lqw.eq(bo.getDeleted() != null, UserInterestedToMe::getDeleted, bo.getDeleted());
        return lqw;
    }

    /**
     * 新增对我感兴趣
     *
     * @param bo 对我感兴趣
     * @return 结果
     */
    @Override
    public Boolean insertByBo(UserInterestedToMeBo bo) {
        UserInterestedToMe add = BeanUtil.toBean(bo, UserInterestedToMe.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改对我感兴趣
     *
     * @param bo 对我感兴趣
     * @return 结果
     */
    @Override
    public Boolean updateByBo(UserInterestedToMeBo bo) {
        UserInterestedToMe update = BeanUtil.toBean(bo, UserInterestedToMe.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     *
     * @param entity 实体类数据
     */
    private void validEntityBeforeSave(UserInterestedToMe entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 批量删除对我感兴趣
     *
     * @param ids 需要删除的对我感兴趣主键
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
