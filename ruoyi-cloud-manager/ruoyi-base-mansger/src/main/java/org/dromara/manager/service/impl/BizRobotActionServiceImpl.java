package org.dromara.manager.service.impl;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dromara.common.core.exception.ServiceException;
import org.dromara.common.core.utils.MapstructUtils;
import org.dromara.common.core.utils.StringUtils;
import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.manager.domain.BizRobotAction;
import org.dromara.manager.domain.bo.BizRobotActionBo;
import org.dromara.manager.domain.vo.BizRobotActionVo;
import org.dromara.manager.mapper.BizRobotActionMapper;
import org.dromara.manager.service.IBizRobotActionService;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

/**
 * 机器人动作定义Service业务层处理
 *
 * @author LionLi
 * @date 2026-06-12
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class BizRobotActionServiceImpl implements IBizRobotActionService {

    private static final String ACTION_TYPE_TRIGGER = "trigger";
    private static final String ACTION_TYPE_CONTINUOUS = "continuous";

    private final BizRobotActionMapper baseMapper;

    /**
     * 查询机器人动作定义
     *
     * @param actionId 主键
     * @return 机器人动作定义
     */
    @Override
    public BizRobotActionVo queryById(Long actionId) {
        return baseMapper.selectVoById(actionId);
    }

    /**
     * 分页查询机器人动作定义列表
     *
     * @param bo 查询条件
     * @param pageQuery 分页参数
     * @return 机器人动作定义分页列表
     */
    @Override
    public TableDataInfo<BizRobotActionVo> queryPageList(BizRobotActionBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<BizRobotAction> lqw = buildQueryWrapper(bo);
        Page<BizRobotActionVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询符合条件的机器人动作定义列表
     *
     * @param bo 查询条件
     * @return 机器人动作定义列表
     */
    @Override
    public List<BizRobotActionVo> queryList(BizRobotActionBo bo) {
        LambdaQueryWrapper<BizRobotAction> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<BizRobotAction> buildQueryWrapper(BizRobotActionBo bo) {
        LambdaQueryWrapper<BizRobotAction> lqw = Wrappers.lambdaQuery();
        lqw.like(StringUtils.isNotBlank(bo.getActionCode()), BizRobotAction::getActionCode, bo.getActionCode());
        lqw.like(StringUtils.isNotBlank(bo.getActionName()), BizRobotAction::getActionName, bo.getActionName());
        lqw.eq(StringUtils.isNotBlank(bo.getActionType()), BizRobotAction::getActionType, bo.getActionType());
        lqw.eq(StringUtils.isNotBlank(bo.getStatus()), BizRobotAction::getStatus, bo.getStatus());
        lqw.orderByAsc(BizRobotAction::getSortOrder);
        lqw.orderByDesc(BizRobotAction::getCreateTime);
        lqw.orderByDesc(BizRobotAction::getActionId);
        return lqw;
    }

    /**
     * 新增机器人动作定义
     *
     * @param bo 机器人动作定义
     * @return 是否新增成功
     */
    @Override
    public Boolean insertByBo(BizRobotActionBo bo) {
        BizRobotAction add = MapstructUtils.convert(bo, BizRobotAction.class);
        fillDefaultValue(add);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setActionId(add.getActionId());
        }
        return flag;
    }

    /**
     * 修改机器人动作定义
     *
     * @param bo 机器人动作定义
     * @return 是否修改成功
     */
    @Override
    public Boolean updateByBo(BizRobotActionBo bo) {
        BizRobotAction update = MapstructUtils.convert(bo, BizRobotAction.class);
        fillDefaultValue(update);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    private void fillDefaultValue(BizRobotAction entity) {
        if (StringUtils.isBlank(entity.getStatus())) {
            entity.setStatus("0");
        }
        if (entity.getSortOrder() == null) {
            entity.setSortOrder(0);
        }
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(BizRobotAction entity) {
        Long count = baseMapper.selectCount(new LambdaQueryWrapper<BizRobotAction>()
            .eq(BizRobotAction::getActionCode, entity.getActionCode())
            .ne(entity.getActionId() != null, BizRobotAction::getActionId, entity.getActionId()));
        if (count > 0) {
            throw new ServiceException("动作编码已存在");
        }
        if (!ACTION_TYPE_TRIGGER.equals(entity.getActionType()) && !ACTION_TYPE_CONTINUOUS.equals(entity.getActionType())) {
            throw new ServiceException("动作类型不正确");
        }
        if (StringUtils.isNotBlank(entity.getParamsTemplate()) && !JSONUtil.isTypeJSON(entity.getParamsTemplate())) {
            throw new ServiceException("动作参数模板JSON格式不正确");
        }
    }

    /**
     * 校验并批量删除机器人动作定义信息
     *
     * @param ids 待删除的主键集合
     * @param isValid 是否进行有效性校验
     * @return 是否删除成功
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        return baseMapper.deleteByIds(ids) > 0;
    }

}
