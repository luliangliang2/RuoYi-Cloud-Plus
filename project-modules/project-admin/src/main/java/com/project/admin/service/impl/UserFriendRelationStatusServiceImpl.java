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
import com.project.admin.domain.bo.UserFriendRelationStatusBo;
import com.project.admin.domain.vo.UserFriendRelationStatusVo;
import com.project.admin.domain.UserFriendRelationStatus;
import com.project.admin.mapper.UserFriendRelationStatusMapper;
import com.project.admin.service.IUserFriendRelationStatusService;

import java.util.List;
import java.util.Map;
import java.util.Collection;

/**
 * 关系状态Service业务层处理
 *
 * @author huan.li
 * @date 2022-06-16
 */
@RequiredArgsConstructor
@Service
public class UserFriendRelationStatusServiceImpl implements IUserFriendRelationStatusService {

    private final UserFriendRelationStatusMapper baseMapper;

    /**
     * 查询关系状态
     *
     * @param id 关系状态主键
     * @return 关系状态
     */
    @Override
    public UserFriendRelationStatusVo queryById(Long id){
        return baseMapper.selectVoById(id);
    }

    /**
     * 查询关系状态列表
     *
     * @param bo 关系状态
     * @return 关系状态
     */
    @Override
    public TableDataInfo<UserFriendRelationStatusVo> queryPageList(UserFriendRelationStatusBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<UserFriendRelationStatus> lqw = buildQueryWrapper(bo);
        Page<UserFriendRelationStatusVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询关系状态列表
     *
     * @param bo 关系状态
     * @return 关系状态
     */
    @Override
    public List<UserFriendRelationStatusVo> queryList(UserFriendRelationStatusBo bo) {
        LambdaQueryWrapper<UserFriendRelationStatus> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<UserFriendRelationStatus> buildQueryWrapper(UserFriendRelationStatusBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<UserFriendRelationStatus> lqw = Wrappers.lambdaQuery();
        lqw.eq(bo.getApprovedStatus() != null, UserFriendRelationStatus::getApprovedStatus, bo.getApprovedStatus());
        lqw.eq(StringUtils.isNotBlank(bo.getSearchValue()), UserFriendRelationStatus::getSearchValue, bo.getSearchValue());
        lqw.eq(bo.getDeleted() != null, UserFriendRelationStatus::getDeleted, bo.getDeleted());
        return lqw;
    }

    /**
     * 新增关系状态
     *
     * @param bo 关系状态
     * @return 结果
     */
    @Override
    public Boolean insertByBo(UserFriendRelationStatusBo bo) {
        UserFriendRelationStatus add = BeanUtil.toBean(bo, UserFriendRelationStatus.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改关系状态
     *
     * @param bo 关系状态
     * @return 结果
     */
    @Override
    public Boolean updateByBo(UserFriendRelationStatusBo bo) {
        UserFriendRelationStatus update = BeanUtil.toBean(bo, UserFriendRelationStatus.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     *
     * @param entity 实体类数据
     */
    private void validEntityBeforeSave(UserFriendRelationStatus entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 批量删除关系状态
     *
     * @param ids 需要删除的关系状态主键
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
