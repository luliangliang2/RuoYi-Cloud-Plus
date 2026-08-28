package org.dromara.manager.service.impl;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dromara.common.core.exception.ServiceException;
import org.dromara.common.core.utils.MapstructUtils;
import org.dromara.common.core.utils.StringUtils;
import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.manager.domain.BizRobotAction;
import org.dromara.manager.domain.BizScenePoint;
import org.dromara.manager.domain.BizSceneRoute;
import org.dromara.manager.domain.BizTaskTemplate;
import org.dromara.manager.domain.BizTaskTemplatePoint;
import org.dromara.manager.domain.BizTaskTemplatePointAction;
import org.dromara.manager.domain.bo.BizTaskTemplateActionBo;
import org.dromara.manager.domain.bo.BizTaskTemplateBo;
import org.dromara.manager.domain.bo.BizTaskTemplatePointBo;
import org.dromara.manager.domain.vo.BizScenePointVo;
import org.dromara.manager.domain.vo.BizTaskTemplateActionVo;
import org.dromara.manager.domain.vo.BizTaskTemplatePointVo;
import org.dromara.manager.domain.vo.BizTaskTemplateVo;
import org.dromara.manager.mapper.BizRobotActionMapper;
import org.dromara.manager.mapper.BizScenePointMapper;
import org.dromara.manager.mapper.BizSceneRouteMapper;
import org.dromara.manager.mapper.BizTaskTemplateMapper;
import org.dromara.manager.mapper.BizTaskTemplatePointActionMapper;
import org.dromara.manager.mapper.BizTaskTemplatePointMapper;
import org.dromara.manager.service.IBizTaskTemplateService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 任务模板Service业务层处理
 *
 * @author LionLi
 * @date 2026-06-12
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class BizTaskTemplateServiceImpl implements IBizTaskTemplateService {

    private final BizTaskTemplateMapper baseMapper;
    private final BizTaskTemplatePointMapper templatePointMapper;
    private final BizTaskTemplatePointActionMapper templateActionMapper;
    private final BizSceneRouteMapper sceneRouteMapper;
    private final BizScenePointMapper scenePointMapper;
    private final BizRobotActionMapper robotActionMapper;

    /**
     * 查询任务模板
     *
     * @param templateId 主键
     * @return 任务模板
     */
    @Override
    public BizTaskTemplateVo queryById(Long templateId) {
        BizTaskTemplateVo vo = baseMapper.selectVoById(templateId);
        if (vo == null) {
            return null;
        }
        BizSceneRoute route = sceneRouteMapper.selectById(vo.getRouteId());
        if (route != null) {
            vo.setRouteName(route.getRouteName());
        }
        fillDetails(vo);
        return vo;
    }

    /**
     * 分页查询任务模板列表
     *
     * @param bo 查询条件
     * @param pageQuery 分页参数
     * @return 任务模板分页列表
     */
    @Override
    public TableDataInfo<BizTaskTemplateVo> queryPageList(BizTaskTemplateBo bo, PageQuery pageQuery) {
        BizTaskTemplate query = MapstructUtils.convert(bo, BizTaskTemplate.class);
        Page<BizTaskTemplateVo> result = baseMapper.selectTaskTemplatePage(pageQuery.build(), query);
        return TableDataInfo.build(result);
    }

    /**
     * 查询符合条件的任务模板列表
     *
     * @param bo 查询条件
     * @return 任务模板列表
     */
    @Override
    public List<BizTaskTemplateVo> queryList(BizTaskTemplateBo bo) {
        BizTaskTemplate query = MapstructUtils.convert(bo, BizTaskTemplate.class);
        return baseMapper.selectTaskTemplateList(query);
    }

    /**
     * 查询路线点位
     *
     * @param routeId 路线ID
     * @return 点位列表
     */
    @Override
    public List<BizScenePointVo> queryRoutePoints(Long routeId) {
        BizSceneRoute route = sceneRouteMapper.selectById(routeId);
        if (route == null) {
            throw new ServiceException("路线不存在");
        }
        BizScenePoint query = new BizScenePoint();
        query.setRouteId(routeId);
        query.setStatus("0");
        return scenePointMapper.selectScenePointList(query);
    }

    /**
     * 预览任务模板下发指令
     *
     * @param templateId 模板ID
     * @return 指令JSON
     */
    @Override
    public Map<String, Object> previewCommand(Long templateId) {
        BizTaskTemplateVo template = queryById(templateId);
        if (template == null) {
            throw new ServiceException("任务模板不存在");
        }
        Map<String, Object> root = new LinkedHashMap<>();
        root.put("commandType", "taskTemplate");
        root.put("templateId", template.getTemplateId());
        root.put("templateCode", template.getTemplateCode());
        root.put("templateName", template.getTemplateName());
        root.put("routeId", template.getRouteId());
        root.put("routeName", template.getRouteName());
        root.put("templateDesc", template.getTemplateDesc());
        root.put("points", buildPreviewPoints(template.getPoints()));
        return root;
    }

    /**
     * 新增任务模板
     *
     * @param bo 任务模板
     * @return 是否新增成功
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean insertByBo(BizTaskTemplateBo bo) {
        BizTaskTemplate add = MapstructUtils.convert(bo, BizTaskTemplate.class);
        fillDefaultValue(add);
        validEntityBeforeSave(add, bo.getPoints());
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setTemplateId(add.getTemplateId());
            saveDetails(add.getTemplateId(), add.getRouteId(), bo.getPoints());
        }
        return flag;
    }

    /**
     * 修改任务模板
     *
     * @param bo 任务模板
     * @return 是否修改成功
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean updateByBo(BizTaskTemplateBo bo) {
        BizTaskTemplate update = MapstructUtils.convert(bo, BizTaskTemplate.class);
        fillDefaultValue(update);
        validEntityBeforeSave(update, bo.getPoints());
        boolean flag = baseMapper.updateById(update) > 0;
        if (flag) {
            deleteDetails(List.of(update.getTemplateId()));
            saveDetails(update.getTemplateId(), update.getRouteId(), bo.getPoints());
        }
        return flag;
    }

    private void fillDefaultValue(BizTaskTemplate entity) {
        if (StringUtils.isBlank(entity.getStatus())) {
            entity.setStatus("0");
        }
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(BizTaskTemplate entity, List<BizTaskTemplatePointBo> points) {
        BizSceneRoute route = sceneRouteMapper.selectById(entity.getRouteId());
        if (route == null) {
            throw new ServiceException("路线不存在");
        }
        Long count = baseMapper.selectCount(new LambdaQueryWrapper<BizTaskTemplate>()
            .eq(BizTaskTemplate::getTemplateCode, entity.getTemplateCode())
            .ne(entity.getTemplateId() != null, BizTaskTemplate::getTemplateId, entity.getTemplateId()));
        if (count > 0) {
            throw new ServiceException("模板编码已存在");
        }
        if (points == null || points.isEmpty()) {
            throw new ServiceException("请至少编排一个站点");
        }
        List<Long> pointIds = points.stream()
            .map(BizTaskTemplatePointBo::getPointId)
            .filter(Objects::nonNull)
            .toList();
        if (pointIds.size() != new HashSet<>(pointIds).size()) {
            throw new ServiceException("编排站点不能重复");
        }
        Long pointCount = scenePointMapper.selectCount(new LambdaQueryWrapper<BizScenePoint>()
            .eq(BizScenePoint::getRouteId, entity.getRouteId())
            .in(BizScenePoint::getPointId, pointIds));
        if (!Objects.equals(pointCount, (long) pointIds.size())) {
            throw new ServiceException("存在不属于当前路线的站点");
        }

        Set<Long> actionIds = points.stream()
            .filter(point -> point.getActions() != null)
            .flatMap(point -> point.getActions().stream())
            .map(BizTaskTemplateActionBo::getActionId)
            .filter(Objects::nonNull)
            .collect(Collectors.toSet());
        if (!actionIds.isEmpty()) {
            Long actionCount = robotActionMapper.selectCount(new LambdaQueryWrapper<BizRobotAction>()
                .in(BizRobotAction::getActionId, actionIds)
                .eq(BizRobotAction::getStatus, "0"));
            if (!Objects.equals(actionCount, (long) actionIds.size())) {
                throw new ServiceException("存在无效或停用的动作");
            }
        }
        for (BizTaskTemplatePointBo point : points) {
            if (point.getActions() == null) {
                continue;
            }
            for (BizTaskTemplateActionBo action : point.getActions()) {
                if (StringUtils.isNotBlank(action.getActionParams()) && !JSONUtil.isTypeJSON(action.getActionParams())) {
                    throw new ServiceException("动作参数JSON格式不正确");
                }
            }
        }
    }

    private void saveDetails(Long templateId, Long routeId, List<BizTaskTemplatePointBo> points) {
        List<BizTaskTemplatePointBo> sortedPoints = new ArrayList<>(points);
        sortedPoints.sort(Comparator.comparing(point -> defaultSequence(point.getSequence())));
        List<Long> pointIds = sortedPoints.stream().map(BizTaskTemplatePointBo::getPointId).toList();
        Map<Long, BizScenePoint> pointMap = scenePointMapper.selectList(new LambdaQueryWrapper<BizScenePoint>()
                .in(BizScenePoint::getPointId, pointIds))
            .stream()
            .collect(Collectors.toMap(BizScenePoint::getPointId, Function.identity()));

        Set<Long> actionIds = sortedPoints.stream()
            .filter(point -> point.getActions() != null)
            .flatMap(point -> point.getActions().stream())
            .map(BizTaskTemplateActionBo::getActionId)
            .filter(Objects::nonNull)
            .collect(Collectors.toSet());
        Map<Long, BizRobotAction> actionMap = actionIds.isEmpty()
            ? Collections.emptyMap()
            : robotActionMapper.selectList(new LambdaQueryWrapper<BizRobotAction>().in(BizRobotAction::getActionId, actionIds))
                .stream()
                .collect(Collectors.toMap(BizRobotAction::getActionId, Function.identity()));

        int pointSequence = 1;
        for (BizTaskTemplatePointBo pointBo : sortedPoints) {
            BizScenePoint scenePoint = pointMap.get(pointBo.getPointId());
            BizTaskTemplatePoint point = new BizTaskTemplatePoint();
            point.setTemplateId(templateId);
            point.setRouteId(routeId);
            point.setPointId(pointBo.getPointId());
            point.setPointName(scenePoint.getPointName());
            point.setSequence(pointBo.getSequence() == null ? pointSequence : pointBo.getSequence());
            point.setRequiredFlag(StringUtils.blankToDefault(pointBo.getRequiredFlag(), "1"));
            point.setRemark(pointBo.getRemark());
            templatePointMapper.insert(point);

            saveActions(templateId, point.getTemplatePointId(), pointBo.getPointId(), pointBo.getActions(), actionMap);
            pointSequence++;
        }
    }

    private void saveActions(Long templateId, Long templatePointId, Long pointId, List<BizTaskTemplateActionBo> actions,
                             Map<Long, BizRobotAction> actionMap) {
        if (actions == null || actions.isEmpty()) {
            return;
        }
        List<BizTaskTemplateActionBo> sortedActions = new ArrayList<>(actions);
        sortedActions.sort(Comparator.comparing(action -> defaultSequence(action.getSequence())));
        int actionSequence = 1;
        for (BizTaskTemplateActionBo actionBo : sortedActions) {
            BizRobotAction action = actionMap.get(actionBo.getActionId());
            BizTaskTemplatePointAction templateAction = new BizTaskTemplatePointAction();
            templateAction.setTemplateId(templateId);
            templateAction.setTemplatePointId(templatePointId);
            templateAction.setPointId(pointId);
            templateAction.setActionId(actionBo.getActionId());
            templateAction.setActionCode(action.getActionCode());
            templateAction.setActionName(action.getActionName());
            templateAction.setActionType(action.getActionType());
            templateAction.setSequence(actionBo.getSequence() == null ? actionSequence : actionBo.getSequence());
            templateAction.setActionParams(actionBo.getActionParams());
            templateAction.setRemark(actionBo.getRemark());
            templateActionMapper.insert(templateAction);
            actionSequence++;
        }
    }

    private Integer defaultSequence(Integer sequence) {
        return sequence == null ? Integer.MAX_VALUE : sequence;
    }

    private List<Map<String, Object>> buildPreviewPoints(List<BizTaskTemplatePointVo> points) {
        if (points == null || points.isEmpty()) {
            return Collections.emptyList();
        }
        List<BizTaskTemplatePointVo> sortedPoints = new ArrayList<>(points);
        sortedPoints.sort(Comparator.comparing(point -> defaultSequence(point.getSequence())));
        List<Map<String, Object>> result = new ArrayList<>();
        int pointSequence = 1;
        for (BizTaskTemplatePointVo point : sortedPoints) {
            Map<String, Object> pointJson = new LinkedHashMap<>();
            pointJson.put("templatePointId", point.getTemplatePointId());
            pointJson.put("pointId", point.getPointId());
            pointJson.put("pointName", point.getPointName());
            pointJson.put("sequence", point.getSequence() == null ? pointSequence : point.getSequence());
            pointJson.put("requiredFlag", point.getRequiredFlag());
            pointJson.put("gcj02Lng", point.getGcj02Lng());
            pointJson.put("gcj02Lat", point.getGcj02Lat());
            pointJson.put("bd09Lng", point.getBd09Lng());
            pointJson.put("bd09Lat", point.getBd09Lat());
            pointJson.put("wgs84Lng", point.getWgs84Lng());
            pointJson.put("wgs84Lat", point.getWgs84Lat());
            pointJson.put("actions", buildPreviewActions(point.getActions()));
            result.add(pointJson);
            pointSequence++;
        }
        return result;
    }

    private List<Map<String, Object>> buildPreviewActions(List<BizTaskTemplateActionVo> actions) {
        if (actions == null || actions.isEmpty()) {
            return Collections.emptyList();
        }
        List<BizTaskTemplateActionVo> sortedActions = new ArrayList<>(actions);
        sortedActions.sort(Comparator.comparing(action -> defaultSequence(action.getSequence())));
        List<Map<String, Object>> result = new ArrayList<>();
        int actionSequence = 1;
        for (BizTaskTemplateActionVo action : sortedActions) {
            Map<String, Object> actionJson = new LinkedHashMap<>();
            actionJson.put("templateActionId", action.getTemplateActionId());
            actionJson.put("actionId", action.getActionId());
            actionJson.put("actionCode", action.getActionCode());
            actionJson.put("actionName", action.getActionName());
            actionJson.put("actionType", action.getActionType());
            actionJson.put("sequence", action.getSequence() == null ? actionSequence : action.getSequence());
            actionJson.put("params", StringUtils.isBlank(action.getActionParams()) ? null : JSONUtil.parse(action.getActionParams()));
            result.add(actionJson);
            actionSequence++;
        }
        return result;
    }

    private void fillDetails(BizTaskTemplateVo vo) {
        List<BizTaskTemplatePointVo> points = templatePointMapper.selectByTemplateId(vo.getTemplateId());
        List<BizTaskTemplateActionVo> actions = templateActionMapper.selectByTemplateId(vo.getTemplateId());
        Map<Long, List<BizTaskTemplateActionVo>> actionMap = new HashMap<>();
        for (BizTaskTemplateActionVo action : actions) {
            actionMap.computeIfAbsent(action.getTemplatePointId(), key -> new ArrayList<>()).add(action);
        }
        for (BizTaskTemplatePointVo point : points) {
            point.setActions(actionMap.getOrDefault(point.getTemplatePointId(), Collections.emptyList()));
        }
        vo.setPoints(points);
    }

    /**
     * 校验并批量删除任务模板信息
     *
     * @param ids 待删除的主键集合
     * @param isValid 是否进行有效性校验
     * @return 是否删除成功
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        deleteDetails(ids);
        return baseMapper.deleteByIds(ids) > 0;
    }

    private void deleteDetails(Collection<Long> templateIds) {
        templateActionMapper.delete(new LambdaQueryWrapper<BizTaskTemplatePointAction>()
            .in(BizTaskTemplatePointAction::getTemplateId, templateIds));
        templatePointMapper.delete(new LambdaQueryWrapper<BizTaskTemplatePoint>()
            .in(BizTaskTemplatePoint::getTemplateId, templateIds));
    }

}
