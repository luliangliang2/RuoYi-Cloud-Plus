package com.project.system.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.project.common.core.utils.StringUtils;
import com.project.common.mybatis.core.page.PageQuery;
import com.project.common.mybatis.core.page.TableDataInfo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.project.system.domain.bo.UserOccupationLabelBo;
import com.project.system.domain.vo.UserOccupationLabelVo;
import com.project.system.domain.UserOccupationLabel;
import com.project.system.mapper.UserOccupationLabelMapper;
import com.project.system.service.IUserOccupationLabelService;

import java.util.List;
import java.util.Map;
import java.util.Collection;

/**
 * 人脉职业标签Service业务层处理
 *
 * @author project
 * @date 2022-06-10
 */
@RequiredArgsConstructor
@Service
public class UserOccupationLabelServiceImpl implements IUserOccupationLabelService {

    private final UserOccupationLabelMapper baseMapper;

    /**
     * 查询人脉职业标签
     *
     * @param id 人脉职业标签主键
     * @return 人脉职业标签
     */
    @Override
    public UserOccupationLabelVo queryById(Long id){
        return baseMapper.selectVoById(id);
    }

    /**
     * 查询人脉职业标签列表
     *
     * @param bo 人脉职业标签
     * @return 人脉职业标签
     */
    @Override
    public TableDataInfo<UserOccupationLabelVo> queryPageList(UserOccupationLabelBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<UserOccupationLabel> lqw = buildQueryWrapper(bo);
        Page<UserOccupationLabelVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询人脉职业标签列表
     *
     * @param bo 人脉职业标签
     * @return 人脉职业标签
     */
    @Override
    public List<UserOccupationLabelVo> queryList(UserOccupationLabelBo bo) {
        LambdaQueryWrapper<UserOccupationLabel> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<UserOccupationLabel> buildQueryWrapper(UserOccupationLabelBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<UserOccupationLabel> lqw = Wrappers.lambdaQuery();
        lqw.eq(bo.getUserInfoId() != null, UserOccupationLabel::getUserInfoId, bo.getUserInfoId());
        lqw.eq(StringUtils.isNotBlank(bo.getOccupationLabel()), UserOccupationLabel::getOccupationLabel, bo.getOccupationLabel());
        lqw.eq(StringUtils.isNotBlank(bo.getSearchValue()), UserOccupationLabel::getSearchValue, bo.getSearchValue());
        lqw.eq(bo.getDeleted() != null, UserOccupationLabel::getDeleted, bo.getDeleted());
        return lqw;
    }

    /**
     * 新增人脉职业标签
     *
     * @param bo 人脉职业标签
     * @return 结果
     */
    @Override
    public Boolean insertByBo(UserOccupationLabelBo bo) {
        UserOccupationLabel add = BeanUtil.toBean(bo, UserOccupationLabel.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改人脉职业标签
     *
     * @param bo 人脉职业标签
     * @return 结果
     */
    @Override
    public Boolean updateByBo(UserOccupationLabelBo bo) {
        UserOccupationLabel update = BeanUtil.toBean(bo, UserOccupationLabel.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     *
     * @param entity 实体类数据
     */
    private void validEntityBeforeSave(UserOccupationLabel entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 批量删除人脉职业标签
     *
     * @param ids 需要删除的人脉职业标签主键
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
