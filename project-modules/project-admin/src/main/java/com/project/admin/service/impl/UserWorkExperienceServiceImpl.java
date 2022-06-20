//package com.project.admin.service.impl;
//
//import cn.hutool.core.bean.BeanUtil;
//import com.project.common.core.utils.StringUtils;
//import com.project.common.mybatis.core.page.PageQuery;
//import com.project.common.mybatis.core.page.TableDataInfo;
//import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
//import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
//import com.baomidou.mybatisplus.core.toolkit.Wrappers;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//import com.project.admin.domain.bo.UserWorkExperienceBo;
//import com.project.admin.domain.vo.UserWorkExperienceVo;
//import com.project.admin.domain.UserWorkExperience;
//import com.project.admin.mapper.UserWorkExperienceMapper;
//import com.project.admin.service.IUserWorkExperienceService;
//
//import java.util.List;
//import java.util.Map;
//import java.util.Collection;
//
///**
// * 工作经历Service业务层处理
// *
// * @author huan.li
// * @date 2022-06-16
// */
//@RequiredArgsConstructor
//@Service
//public class UserWorkExperienceServiceImpl implements IUserWorkExperienceService {
//
//    private final UserWorkExperienceMapper baseMapper;
//
//    /**
//     * 查询工作经历
//     *
//     * @param id 工作经历主键
//     * @return 工作经历
//     */
//    @Override
//    public UserWorkExperienceVo queryById(Long id){
//        return baseMapper.selectVoById(id);
//    }
//
//    /**
//     * 查询工作经历列表
//     *
//     * @param bo 工作经历
//     * @return 工作经历
//     */
//    @Override
//    public TableDataInfo<UserWorkExperienceVo> queryPageList(UserWorkExperienceBo bo, PageQuery pageQuery) {
//        LambdaQueryWrapper<UserWorkExperience> lqw = buildQueryWrapper(bo);
//        Page<UserWorkExperienceVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
//        return TableDataInfo.build(result);
//    }
//
//    /**
//     * 查询工作经历列表
//     *
//     * @param bo 工作经历
//     * @return 工作经历
//     */
//    @Override
//    public List<UserWorkExperienceVo> queryList(UserWorkExperienceBo bo) {
//        LambdaQueryWrapper<UserWorkExperience> lqw = buildQueryWrapper(bo);
//        return baseMapper.selectVoList(lqw);
//    }
//
//    private LambdaQueryWrapper<UserWorkExperience> buildQueryWrapper(UserWorkExperienceBo bo) {
//        Map<String, Object> params = bo.getParams();
//        LambdaQueryWrapper<UserWorkExperience> lqw = Wrappers.lambdaQuery();
//        lqw.eq(bo.getUserInfoId() != null, UserWorkExperience::getUserInfoId, bo.getUserInfoId());
//        lqw.like(StringUtils.isNotBlank(bo.getCompany()), UserWorkExperience::getCompany, bo.getCompany());
//        lqw.eq(StringUtils.isNotBlank(bo.getPosition()), UserWorkExperience::getPosition, bo.getPosition());
//        lqw.eq(bo.getEntryTime() != null, UserWorkExperience::getEntryTime, bo.getEntryTime());
//        lqw.eq(bo.getDepartureTime() != null, UserWorkExperience::getDepartureTime, bo.getDepartureTime());
//        lqw.between(params.get("beginLengthOfEmployment") != null && params.get("endLengthOfEmployment") != null,
//            UserWorkExperience::getLengthOfEmployment ,params.get("beginLengthOfEmployment"), params.get("endLengthOfEmployment"));
//        lqw.eq(StringUtils.isNotBlank(bo.getIntroduction()), UserWorkExperience::getIntroduction, bo.getIntroduction());
//        lqw.eq(StringUtils.isNotBlank(bo.getSearchValue()), UserWorkExperience::getSearchValue, bo.getSearchValue());
//        lqw.eq(bo.getDeleted() != null, UserWorkExperience::getDeleted, bo.getDeleted());
//        return lqw;
//    }
//
//    /**
//     * 新增工作经历
//     *
//     * @param bo 工作经历
//     * @return 结果
//     */
//    @Override
//    public Boolean insertByBo(UserWorkExperienceBo bo) {
//        UserWorkExperience add = BeanUtil.toBean(bo, UserWorkExperience.class);
//        validEntityBeforeSave(add);
//        boolean flag = baseMapper.insert(add) > 0;
//        if (flag) {
//            bo.setId(add.getId());
//        }
//        return flag;
//    }
//
//    /**
//     * 修改工作经历
//     *
//     * @param bo 工作经历
//     * @return 结果
//     */
//    @Override
//    public Boolean updateByBo(UserWorkExperienceBo bo) {
//        UserWorkExperience update = BeanUtil.toBean(bo, UserWorkExperience.class);
//        validEntityBeforeSave(update);
//        return baseMapper.updateById(update) > 0;
//    }
//
//    /**
//     * 保存前的数据校验
//     *
//     * @param entity 实体类数据
//     */
//    private void validEntityBeforeSave(UserWorkExperience entity){
//        //TODO 做一些数据校验,如唯一约束
//    }
//
//    /**
//     * 批量删除工作经历
//     *
//     * @param ids 需要删除的工作经历主键
//     * @return 结果
//     */
//    @Override
//    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
//        if(isValid){
//            //TODO 做一些业务上的校验,判断是否需要校验
//        }
//        return baseMapper.deleteBatchIds(ids) > 0;
//    }
//}
