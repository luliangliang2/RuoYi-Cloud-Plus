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
import com.project.admin.domain.bo.UserInfoBo;
import com.project.admin.domain.vo.UserInfoVo;
import com.project.admin.domain.UserInfo;
import com.project.admin.mapper.UserInfoMapper;
import com.project.admin.service.IUserInfoService;

import java.util.List;
import java.util.Map;
import java.util.Collection;

/**
 * 用户信息Service业务层处理
 *
 * @author huan.li
 * @date 2022-06-15
 */
@RequiredArgsConstructor
@Service
public class UserInfoServiceImpl implements IUserInfoService {

    private final UserInfoMapper baseMapper;

    /**
     * 查询用户信息
     *
     * @param id 用户信息主键
     * @return 用户信息
     */
    @Override
    public UserInfoVo queryById(Long id){
        return baseMapper.selectVoById(id);
    }

    /**
     * 查询用户信息列表
     *
     * @param bo 用户信息
     * @return 用户信息
     */
    @Override
    public TableDataInfo<UserInfoVo> queryPageList(UserInfoBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<UserInfo> lqw = buildQueryWrapper(bo);
        Page<UserInfoVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询用户信息列表
     *
     * @param bo 用户信息
     * @return 用户信息
     */
    @Override
    public List<UserInfoVo> queryList(UserInfoBo bo) {
        LambdaQueryWrapper<UserInfo> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<UserInfo> buildQueryWrapper(UserInfoBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<UserInfo> lqw = Wrappers.lambdaQuery();
        lqw.like(StringUtils.isNotBlank(bo.getName()), UserInfo::getName, bo.getName());
        lqw.eq(StringUtils.isNotBlank(bo.getPhone()), UserInfo::getPhone, bo.getPhone());
        lqw.eq(StringUtils.isNotBlank(bo.getAvatar()), UserInfo::getAvatar, bo.getAvatar());
        lqw.eq(bo.getEffectCount() != null, UserInfo::getEffectCount, bo.getEffectCount());
        lqw.eq(bo.getVisitorCount() != null, UserInfo::getVisitorCount, bo.getVisitorCount());
        lqw.eq(StringUtils.isNotBlank(bo.getCompany()), UserInfo::getCompany, bo.getCompany());
        lqw.eq(StringUtils.isNotBlank(bo.getPosition()), UserInfo::getPosition, bo.getPosition());
        lqw.eq(StringUtils.isNotBlank(bo.getSelfIntroduction()), UserInfo::getSelfIntroduction, bo.getSelfIntroduction());
        lqw.eq(StringUtils.isNotBlank(bo.getCareerDirection()), UserInfo::getCareerDirection, bo.getCareerDirection());
        lqw.eq(StringUtils.isNotBlank(bo.getLocation()), UserInfo::getLocation, bo.getLocation());
        lqw.eq(StringUtils.isNotBlank(bo.getHometown()), UserInfo::getHometown, bo.getHometown());
        lqw.eq(bo.getConstellation() != null, UserInfo::getConstellation, bo.getConstellation());
        lqw.eq(StringUtils.isNotBlank(bo.getEmail()), UserInfo::getEmail, bo.getEmail());
        lqw.eq(StringUtils.isNotBlank(bo.getSearchValue()), UserInfo::getSearchValue, bo.getSearchValue());
        lqw.eq(bo.getDeleted() != null, UserInfo::getDeleted, bo.getDeleted());
        return lqw;
    }

    /**
     * 新增用户信息
     *
     * @param bo 用户信息
     * @return 结果
     */
    @Override
    public Boolean insertByBo(UserInfoBo bo) {
        UserInfo add = BeanUtil.toBean(bo, UserInfo.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改用户信息
     *
     * @param bo 用户信息
     * @return 结果
     */
    @Override
    public Boolean updateByBo(UserInfoBo bo) {
        UserInfo update = BeanUtil.toBean(bo, UserInfo.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     *
     * @param entity 实体类数据
     */
    private void validEntityBeforeSave(UserInfo entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 批量删除用户信息
     *
     * @param ids 需要删除的用户信息主键
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
