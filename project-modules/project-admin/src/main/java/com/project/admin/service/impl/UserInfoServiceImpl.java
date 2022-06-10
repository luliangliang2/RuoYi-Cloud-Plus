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
 * 用户详情Service业务层处理
 *
 * @author huan.li
 * @date 2022-06-10
 */
@RequiredArgsConstructor
@Service
public class UserInfoServiceImpl implements IUserInfoService {

    private final UserInfoMapper baseMapper;

    /**
     * 查询用户详情
     *
     * @param id 用户详情主键
     * @return 用户详情
     */
    @Override
    public UserInfoVo queryById(Long id){
        return baseMapper.selectVoById(id);
    }

    /**
     * 查询用户详情列表
     *
     * @param bo 用户详情
     * @return 用户详情
     */
    @Override
    public TableDataInfo<UserInfoVo> queryPageList(UserInfoBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<UserInfo> lqw = buildQueryWrapper(bo);
        Page<UserInfoVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询用户详情列表
     *
     * @param bo 用户详情
     * @return 用户详情
     */
    @Override
    public List<UserInfoVo> queryList(UserInfoBo bo) {
        LambdaQueryWrapper<UserInfo> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<UserInfo> buildQueryWrapper(UserInfoBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<UserInfo> lqw = Wrappers.lambdaQuery();
        lqw.eq(bo.getUserLoginId() != null, UserInfo::getUserLoginId, bo.getUserLoginId());
        lqw.eq(StringUtils.isNotBlank(bo.getHeadUrl()), UserInfo::getHeadUrl, bo.getHeadUrl());
        lqw.like(StringUtils.isNotBlank(bo.getNickName()), UserInfo::getNickName, bo.getNickName());
        lqw.like(StringUtils.isNotBlank(bo.getRealName()), UserInfo::getRealName, bo.getRealName());
        lqw.eq(StringUtils.isNotBlank(bo.getIdCardNo()), UserInfo::getIdCardNo, bo.getIdCardNo());
        lqw.eq(StringUtils.isNotBlank(bo.getPhone()), UserInfo::getPhone, bo.getPhone());
        lqw.eq(StringUtils.isNotBlank(bo.getSchool()), UserInfo::getSchool, bo.getSchool());
        lqw.eq(StringUtils.isNotBlank(bo.getCollege()), UserInfo::getCollege, bo.getCollege());
        lqw.eq(StringUtils.isNotBlank(bo.getGrade()), UserInfo::getGrade, bo.getGrade());
        lqw.eq(StringUtils.isNotBlank(bo.getMajor()), UserInfo::getMajor, bo.getMajor());
        lqw.eq(StringUtils.isNotBlank(bo.getPersonalSignature()), UserInfo::getPersonalSignature, bo.getPersonalSignature());
        lqw.eq(StringUtils.isNotBlank(bo.getHometown()), UserInfo::getHometown, bo.getHometown());
        return lqw;
    }

    /**
     * 新增用户详情
     *
     * @param bo 用户详情
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
     * 修改用户详情
     *
     * @param bo 用户详情
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
     * 批量删除用户详情
     *
     * @param ids 需要删除的用户详情主键
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
