package com.project.admin.contact.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.project.admin.contact.domain.UserCommunicationMessage;
import com.project.admin.contact.domain.bo.UserCommunicationMessageBo;
import com.project.admin.contact.domain.vo.UserCommunicationMessageVo;
import com.project.admin.contact.mapper.UserCommunicationMessageMapper;
import com.project.admin.contact.service.IUserCommunicationMessageService;
import com.project.common.core.utils.StringUtils;
import com.project.common.mybatis.core.page.PageQuery;
import com.project.common.mybatis.core.page.TableDataInfo;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Map;
import java.util.Collection;

/**
 * 沟通消息Service业务层处理
 *
 * @author project
 * @date 2022-06-05
 */
@RequiredArgsConstructor
@Service
public class UserCommunicationMessageServiceImpl implements IUserCommunicationMessageService {

    private final UserCommunicationMessageMapper baseMapper;

    /**
     * 查询沟通消息
     *
     * @param id 沟通消息主键
     * @return 沟通消息
     */
    @Override
    public UserCommunicationMessageVo queryById(Long id){
        return baseMapper.selectVoById(id);
    }

    /**
     * 查询沟通消息列表
     *
     * @param bo 沟通消息
     * @return 沟通消息
     */
    @Override
    public TableDataInfo<UserCommunicationMessageVo> queryPageList(UserCommunicationMessageBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<UserCommunicationMessage> lqw = buildQueryWrapper(bo);
        Page<UserCommunicationMessageVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询沟通消息列表
     *
     * @param bo 沟通消息
     * @return 沟通消息
     */
    @Override
    public List<UserCommunicationMessageVo> queryList(UserCommunicationMessageBo bo) {
        LambdaQueryWrapper<UserCommunicationMessage> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<UserCommunicationMessage> buildQueryWrapper(UserCommunicationMessageBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<UserCommunicationMessage> lqw = Wrappers.lambdaQuery();
        lqw.eq(bo.getDeleted() != null, UserCommunicationMessage::getDeleted, bo.getDeleted());
        lqw.eq(bo.getUserInfoId() != null, UserCommunicationMessage::getUserInfoId, bo.getUserInfoId());
        lqw.eq(bo.getContactInfoId() != null, UserCommunicationMessage::getContactInfoId, bo.getContactInfoId());
        lqw.eq(StringUtils.isNotBlank(bo.getMessageContent()), UserCommunicationMessage::getMessageContent, bo.getMessageContent());
        return lqw;
    }

    /**
     * 新增沟通消息
     *
     * @param bo 沟通消息
     * @return 结果
     */
    @Override
    public Boolean insertByBo(UserCommunicationMessageBo bo) {
        UserCommunicationMessage add = BeanUtil.toBean(bo, UserCommunicationMessage.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改沟通消息
     *
     * @param bo 沟通消息
     * @return 结果
     */
    @Override
    public Boolean updateByBo(UserCommunicationMessageBo bo) {
        UserCommunicationMessage update = BeanUtil.toBean(bo, UserCommunicationMessage.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     *
     * @param entity 实体类数据
     */
    private void validEntityBeforeSave(UserCommunicationMessage entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 批量删除沟通消息
     *
     * @param ids 需要删除的沟通消息主键
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
