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
import com.project.admin.domain.bo.UserEducationBo;
import com.project.admin.domain.vo.UserEducationVo;
import com.project.admin.domain.UserEducation;
import com.project.admin.mapper.UserEducationMapper;
import com.project.admin.service.IUserEducationService;

import java.util.List;
import java.util.Map;
import java.util.Collection;

/**
 * 学历Service业务层处理
 *
 * @author huan.li
 * @date 2022-06-16
 */
@RequiredArgsConstructor
@Service
public class UserEducationServiceImpl implements IUserEducationService {

    private final UserEducationMapper baseMapper;

    /**
     * 查询学历
     *
     * @param id 学历主键
     * @return 学历
     */
    @Override
    public UserEducationVo queryById(Long id){
        return baseMapper.selectVoById(id);
    }

    /**
     * 查询学历列表
     *
     * @param bo 学历
     * @return 学历
     */
    @Override
    public TableDataInfo<UserEducationVo> queryPageList(UserEducationBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<UserEducation> lqw = buildQueryWrapper(bo);
        Page<UserEducationVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询学历列表
     *
     * @param bo 学历
     * @return 学历
     */
    @Override
    public List<UserEducationVo> queryList(UserEducationBo bo) {
        LambdaQueryWrapper<UserEducation> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<UserEducation> buildQueryWrapper(UserEducationBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<UserEducation> lqw = Wrappers.lambdaQuery();
        lqw.eq(bo.getUserInfoId() != null, UserEducation::getUserInfoId, bo.getUserInfoId());
        lqw.like(StringUtils.isNotBlank(bo.getName()), UserEducation::getName, bo.getName());
        lqw.eq(bo.getAdmissionTime() != null, UserEducation::getAdmissionTime, bo.getAdmissionTime());
        lqw.eq(bo.getGraduationTime() != null, UserEducation::getGraduationTime, bo.getGraduationTime());
        lqw.eq(StringUtils.isNotBlank(bo.getMajor()), UserEducation::getMajor, bo.getMajor());
        lqw.eq(bo.getGeadeLevel() != null, UserEducation::getGeadeLevel, bo.getGeadeLevel());
        lqw.eq(StringUtils.isNotBlank(bo.getIntroduction()), UserEducation::getIntroduction, bo.getIntroduction());
        lqw.eq(StringUtils.isNotBlank(bo.getSearchValue()), UserEducation::getSearchValue, bo.getSearchValue());
        lqw.eq(bo.getDeleted() != null, UserEducation::getDeleted, bo.getDeleted());
        return lqw;
    }

    /**
     * 新增学历
     *
     * @param bo 学历
     * @return 结果
     */
    @Override
    public Boolean insertByBo(UserEducationBo bo) {
        UserEducation add = BeanUtil.toBean(bo, UserEducation.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改学历
     *
     * @param bo 学历
     * @return 结果
     */
    @Override
    public Boolean updateByBo(UserEducationBo bo) {
        UserEducation update = BeanUtil.toBean(bo, UserEducation.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     *
     * @param entity 实体类数据
     */
    private void validEntityBeforeSave(UserEducation entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 批量删除学历
     *
     * @param ids 需要删除的学历主键
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
