package org.dromara.manager.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dromara.common.core.exception.ServiceException;
import org.dromara.common.core.utils.MapstructUtils;
import org.dromara.common.core.utils.StringUtils;
import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.common.satoken.utils.LoginHelper;
import org.dromara.manager.api.domain.vo.BizVehicleVo;
import org.dromara.manager.constant.RobotTaskConstants;
import org.dromara.manager.domain.BizRobotAction;
import org.dromara.manager.domain.BizRobotTask;
import org.dromara.manager.domain.BizRobotTaskAction;
import org.dromara.manager.domain.BizRobotTaskPoint;
import org.dromara.manager.domain.BizScenePoint;
import org.dromara.manager.domain.BizSceneRoute;
import org.dromara.manager.domain.BizTaskTemplate;
import org.dromara.manager.domain.bo.BizRobotTaskBo;
import org.dromara.manager.domain.bo.BizTaskTemplateActionBo;
import org.dromara.manager.domain.bo.BizTaskTemplatePointBo;
import org.dromara.manager.domain.bo.RobotTaskStepReportBo;
import org.dromara.manager.domain.vo.BizRobotTaskActionVo;
import org.dromara.manager.domain.vo.BizRobotTaskPointVo;
import org.dromara.manager.domain.vo.BizRobotTaskVo;
import org.dromara.manager.domain.vo.BizTaskTemplateActionVo;
import org.dromara.manager.domain.vo.BizTaskTemplatePointVo;
import org.dromara.manager.domain.vo.RobotTaskRuntimeStatusVo;
import org.dromara.manager.domain.vo.RobotTaskVehicleBindVo;
import org.dromara.manager.mapper.BizRobotActionMapper;
import org.dromara.manager.mapper.BizRobotTaskActionMapper;
import org.dromara.manager.mapper.BizRobotTaskMapper;
import org.dromara.manager.mapper.BizRobotTaskPointMapper;
import org.dromara.manager.mapper.BizScenePointMapper;
import org.dromara.manager.mapper.BizSceneRouteMapper;
import org.dromara.manager.mapper.BizTaskTemplateMapper;
import org.dromara.manager.mapper.BizTaskTemplatePointActionMapper;
import org.dromara.manager.mapper.BizTaskTemplatePointMapper;
import org.dromara.manager.mapper.BizVehicleMapper;
import org.dromara.manager.service.IBizRobotTaskService;
import org.dromara.manager.service.support.RobotTaskNoGenerator;
import org.dromara.manager.service.support.RobotTaskRuntimeCacheService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 机器人任务执行Service业务层处理
 *
 * @author LionLi
 * @date 2026-06-19
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class BizRobotTaskServiceImpl implements IBizRobotTaskService {

    private final BizRobotTaskMapper baseMapper;
    private final BizRobotTaskPointMapper taskPointMapper;
    private final BizRobotTaskActionMapper taskActionMapper;
    private final BizTaskTemplateMapper taskTemplateMapper;
    private final BizTaskTemplatePointMapper templatePointMapper;
    private final BizTaskTemplatePointActionMapper templateActionMapper;
    private final BizSceneRouteMapper sceneRouteMapper;
    private final BizScenePointMapper scenePointMapper;
    private final BizRobotActionMapper robotActionMapper;
    private final BizVehicleMapper vehicleMapper;
    private final RobotTaskNoGenerator taskNoGenerator;
    private final RobotTaskRuntimeCacheService runtimeCacheService;

    @Override
    public BizRobotTaskVo queryById(Long taskId) {
        BizRobotTaskVo vo = baseMapper.selectVoById(taskId);
        if (vo == null) {
            return null;
        }
        fillRouteAndTemplateName(vo);
        fillDetails(vo);
        return vo;
    }

    @Override
    public TableDataInfo<BizRobotTaskVo> queryPageList(BizRobotTaskBo bo, PageQuery pageQuery) {
        BizRobotTask query = MapstructUtils.convert(bo, BizRobotTask.class);
        Page<BizRobotTaskVo> result = baseMapper.selectRobotTaskPage(pageQuery.build(), query);
        return TableDataInfo.build(result);
    }

    @Override
    public List<BizRobotTaskVo> queryList(BizRobotTaskBo bo) {
        BizRobotTask query = MapstructUtils.convert(bo, BizRobotTask.class);
        return baseMapper.selectRobotTaskList(query);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean insertByBo(BizRobotTaskBo bo) {
        BizRobotTask add = MapstructUtils.convert(bo, BizRobotTask.class);
        fillDefaultValue(add);
        fillVehicleSnapshot(add);
        List<BizTaskTemplatePointBo> points = buildPlanPoints(add, bo.getPoints());
        validEntityBeforeSave(add, points);
        add.setTaskNo(taskNoGenerator.nextNo());
        add.setTaskStatus(RobotTaskConstants.TASK_STATUS_PENDING);
        add.setCurrentLoopNo(0);
        add.setCurrentPointSeq(0);
        add.setCurrentActionSeq(0);
        add.setCommandJson(buildCommandJson(add, points));
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setTaskId(add.getTaskId());
            saveDetails(add, points);
            runtimeCacheService.initRuntime(add);
        }
        return flag;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean updateByBo(BizRobotTaskBo bo) {
        BizRobotTask exist = baseMapper.selectById(bo.getTaskId());
        if (exist == null) {
            throw new ServiceException("任务不存在");
        }
        if (!Objects.equals(exist.getTaskStatus(), RobotTaskConstants.TASK_STATUS_PENDING)) {
            throw new ServiceException("只有未开始任务允许修改");
        }
        BizRobotTask update = MapstructUtils.convert(bo, BizRobotTask.class);
        fillDefaultValue(update);
        update.setTaskNo(exist.getTaskNo());
        update.setTaskStatus(RobotTaskConstants.TASK_STATUS_PENDING);
        fillVehicleSnapshot(update);
        List<BizTaskTemplatePointBo> points = buildPlanPoints(update, bo.getPoints());
        validEntityBeforeSave(update, points);
        update.setCommandJson(buildCommandJson(update, points));
        boolean flag = baseMapper.updateById(update) > 0;
        if (flag) {
            deleteDetails(List.of(update.getTaskId()));
            saveDetails(update, points);
            runtimeCacheService.deleteRuntime(exist);
            BizRobotTask refreshed = baseMapper.selectById(update.getTaskId());
            runtimeCacheService.initRuntime(refreshed);
        }
        return flag;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if (isValid) {
            Long count = baseMapper.selectCount(new LambdaQueryWrapper<BizRobotTask>()
                .in(BizRobotTask::getTaskId, ids)
                .in(BizRobotTask::getTaskStatus, List.of(RobotTaskConstants.TASK_STATUS_RUNNING)));
            if (count > 0) {
                throw new ServiceException("进行中的任务不能删除");
            }
        }
        List<BizRobotTask> tasks = baseMapper.selectBatchIds(ids);
        deleteDetails(ids);
        boolean flag = baseMapper.deleteByIds(ids) > 0;
        if (flag) {
            tasks.forEach(runtimeCacheService::deleteRuntime);
            String tenantId = LoginHelper.getTenantId();
            ids.forEach(taskId -> runtimeCacheService.deleteRuntimeByTaskId(tenantId, taskId));
        }
        return flag;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean startTask(Long taskId) {
        BizRobotTask task = baseMapper.selectById(taskId);
        if (task == null) {
            throw new ServiceException("任务不存在");
        }
        if (!Objects.equals(task.getTaskStatus(), RobotTaskConstants.TASK_STATUS_PENDING)) {
            throw new ServiceException("只有未开始任务可以启动");
        }
        Date now = new Date();
        task.setTaskStatus(RobotTaskConstants.TASK_STATUS_RUNNING);
        task.setActualStartTime(now);
        task.setCurrentLoopNo(1);
        task.setCurrentPointSeq(1);
        task.setCurrentActionSeq(0);
        boolean flag = baseMapper.updateById(task) > 0;
        if (flag) {
            taskPointMapper.update(null, new LambdaUpdateWrapper<BizRobotTaskPoint>()
                .eq(BizRobotTaskPoint::getTaskId, taskId)
                .eq(BizRobotTaskPoint::getLoopNo, 1)
                .eq(BizRobotTaskPoint::getPointSeq, 1)
                .set(BizRobotTaskPoint::getPointStatus, RobotTaskConstants.STEP_STATUS_RUNNING)
                .set(BizRobotTaskPoint::getArriveTime, now));
            List<BizRobotTaskPoint> points = taskPointMapper.selectList(new LambdaQueryWrapper<BizRobotTaskPoint>()
                .eq(BizRobotTaskPoint::getTaskId, taskId));
            List<BizRobotTaskAction> actions = taskActionMapper.selectList(new LambdaQueryWrapper<BizRobotTaskAction>()
                .eq(BizRobotTaskAction::getTaskId, taskId));
            runtimeCacheService.initStepRuntime(task, points, actions);
            syncRuntimeFromTask(task, 0, null);
        }
        return flag;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean cancelTask(Long taskId) {
        BizRobotTask task = baseMapper.selectById(taskId);
        if (task == null) {
            throw new ServiceException("任务不存在");
        }
        if (Objects.equals(task.getTaskStatus(), RobotTaskConstants.TASK_STATUS_COMPLETED)
            || Objects.equals(task.getTaskStatus(), RobotTaskConstants.TASK_STATUS_CANCELED)) {
            throw new ServiceException("任务已结束");
        }
        task.setTaskStatus(RobotTaskConstants.TASK_STATUS_CANCELED);
        task.setFinishTime(new Date());
        boolean flag = baseMapper.updateById(task) > 0;
        if (flag) {
            syncRuntimeFromTask(task, calcProgress(task.getTaskId()), "任务已取消");
            runtimeCacheService.releaseVehicle(task);
        }
        return flag;
    }

    @Override
    public List<RobotTaskRuntimeStatusVo> queryRuntimeStatus(List<Long> taskIds) {
        String tenantId = LoginHelper.getTenantId();
        return runtimeCacheService.listRuntime(tenantId, taskIds);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean reportStep(RobotTaskStepReportBo bo) {
        BizRobotTask task = resolveTask(bo);
        if (task == null) {
            throw new ServiceException("任务不存在或未绑定车辆");
        }
        if (!Objects.equals(task.getTaskStatus(), RobotTaskConstants.TASK_STATUS_RUNNING)) {
            throw new ServiceException("任务不是进行中状态");
        }
        String stepType = StringUtils.blankToDefault(bo.getStepType(), RobotTaskConstants.STEP_TYPE_ACTION);
        String status = bo.getStatus();
        if (Objects.equals(stepType, RobotTaskConstants.STEP_TYPE_POINT)) {
            updatePointStatus(task, bo, status);
        } else {
            updateActionStatus(task, bo, status);
        }
        aggregateTaskStatus(task, bo);
        return true;
    }

    @Override
    public RobotTaskVehicleBindVo getVehicleByTaskId(Long taskId) {
        return runtimeCacheService.getVehicleByTaskId(LoginHelper.getTenantId(), taskId);
    }

    @Override
    public RobotTaskVehicleBindVo getVehicleByTaskNo(String taskNo) {
        return runtimeCacheService.getVehicleByTaskNo(LoginHelper.getTenantId(), taskNo);
    }

    @Override
    public RobotTaskVehicleBindVo getTaskByVehicleId(Long vehicleId) {
        return runtimeCacheService.getTaskByVehicleId(LoginHelper.getTenantId(), vehicleId);
    }

    @Override
    public RobotTaskVehicleBindVo getTaskByVin(String vin) {
        return runtimeCacheService.getTaskByVin(LoginHelper.getTenantId(), vin);
    }

    private void fillDefaultValue(BizRobotTask entity) {
        if (StringUtils.isBlank(entity.getTenantId())) {
            entity.setTenantId(LoginHelper.getTenantId());
        }
        entity.setLoopFlag(StringUtils.blankToDefault(entity.getLoopFlag(), RobotTaskConstants.FLAG_NO));
        entity.setScheduleFlag(StringUtils.blankToDefault(entity.getScheduleFlag(), RobotTaskConstants.FLAG_NO));
        entity.setAssignMode(StringUtils.blankToDefault(entity.getAssignMode(), RobotTaskConstants.ASSIGN_MODE_ASSIGN));
        int loopCount = entity.getLoopCount() == null ? 1 : entity.getLoopCount();
        entity.setLoopCount(Objects.equals(entity.getLoopFlag(), RobotTaskConstants.FLAG_YES) ? Math.max(loopCount, 1) : 1);
        if (!Objects.equals(entity.getScheduleFlag(), RobotTaskConstants.FLAG_YES)) {
            entity.setStartTime(null);
        }
    }

    private void fillVehicleSnapshot(BizRobotTask entity) {
        if (entity.getVehicleId() == null) {
            if (Objects.equals(entity.getAssignMode(), RobotTaskConstants.ASSIGN_MODE_ASSIGN)) {
                throw new ServiceException("指派任务必须选择车辆");
            }
            return;
        }
        BizVehicleVo vehicle = vehicleMapper.selectVehicleVoById(entity.getVehicleId());
        if (vehicle == null) {
            throw new ServiceException("车辆不存在或无权限");
        }
        entity.setVin(vehicle.getVin());
        entity.setPlateNo(vehicle.getPlateNo());
    }

    private List<BizTaskTemplatePointBo> buildPlanPoints(BizRobotTask task, List<BizTaskTemplatePointBo> inputPoints) {
        if (Objects.equals(task.getTaskType(), RobotTaskConstants.TASK_TYPE_TEMPLATE)) {
            if (task.getTemplateId() == null) {
                throw new ServiceException("模板任务必须选择任务模板");
            }
            BizTaskTemplate template = taskTemplateMapper.selectById(task.getTemplateId());
            if (template == null) {
                throw new ServiceException("任务模板不存在");
            }
            task.setRouteId(template.getRouteId());
            return convertTemplatePoints(task.getTemplateId());
        }
        if (Objects.equals(task.getTaskType(), RobotTaskConstants.TASK_TYPE_TEMPORARY)) {
            if (task.getRouteId() == null) {
                throw new ServiceException("临时任务必须选择路线");
            }
            if (inputPoints == null || inputPoints.isEmpty()) {
                throw new ServiceException("临时任务请至少编排一个站点");
            }
            return inputPoints;
        }
        throw new ServiceException("任务类型不正确");
    }

    private List<BizTaskTemplatePointBo> convertTemplatePoints(Long templateId) {
        List<BizTaskTemplatePointVo> points = templatePointMapper.selectByTemplateId(templateId);
        List<BizTaskTemplateActionVo> actions = templateActionMapper.selectByTemplateId(templateId);
        Map<Long, List<BizTaskTemplateActionVo>> actionMap = actions.stream()
            .collect(Collectors.groupingBy(BizTaskTemplateActionVo::getTemplatePointId));
        List<BizTaskTemplatePointBo> result = new ArrayList<>();
        for (BizTaskTemplatePointVo point : points) {
            BizTaskTemplatePointBo pointBo = new BizTaskTemplatePointBo();
            pointBo.setPointId(point.getPointId());
            pointBo.setSequence(point.getSequence());
            pointBo.setRequiredFlag(point.getRequiredFlag());
            pointBo.setRemark(point.getRemark());
            List<BizTaskTemplateActionBo> actionBos = actionMap.getOrDefault(point.getTemplatePointId(), List.of())
                .stream()
                .map(action -> {
                    BizTaskTemplateActionBo actionBo = new BizTaskTemplateActionBo();
                    actionBo.setActionId(action.getActionId());
                    actionBo.setSequence(action.getSequence());
                    actionBo.setActionParams(action.getActionParams());
                    actionBo.setRemark(action.getRemark());
                    return actionBo;
                })
                .collect(Collectors.toList());
            pointBo.setActions(actionBos);
            result.add(pointBo);
        }
        return result;
    }

    private void validEntityBeforeSave(BizRobotTask entity, List<BizTaskTemplatePointBo> points) {
        BizSceneRoute route = sceneRouteMapper.selectById(entity.getRouteId());
        if (route == null) {
            throw new ServiceException("路线不存在");
        }
        if (Objects.equals(entity.getScheduleFlag(), RobotTaskConstants.FLAG_YES) && entity.getStartTime() == null) {
            throw new ServiceException("请设置定时开始时间");
        }
        if (points == null || points.isEmpty()) {
            throw new ServiceException("请至少编排一个站点");
        }
        RobotTaskVehicleBindVo activeBind = runtimeCacheService.getActiveTaskByVehicle(entity.getTenantId(), entity.getVehicleId(), entity.getVin());
        if (activeBind != null && !Objects.equals(activeBind.getTaskId(), entity.getTaskId())) {
            throw new ServiceException("车辆已有未结束任务");
        }
        List<Long> pointIds = points.stream().map(BizTaskTemplatePointBo::getPointId).filter(Objects::nonNull).toList();
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

    private void saveDetails(BizRobotTask task, List<BizTaskTemplatePointBo> points) {
        List<BizTaskTemplatePointBo> sortedPoints = new ArrayList<>(points);
        sortedPoints.sort(Comparator.comparing(point -> defaultSequence(point.getSequence())));
        Map<Long, BizScenePoint> pointMap = loadPointMap(sortedPoints);
        Map<Long, BizRobotAction> actionMap = loadActionMap(sortedPoints);
        for (int loopNo = 1; loopNo <= task.getLoopCount(); loopNo++) {
            int pointSequence = 1;
            for (BizTaskTemplatePointBo pointBo : sortedPoints) {
                BizScenePoint scenePoint = pointMap.get(pointBo.getPointId());
                int pointSeq = pointBo.getSequence() == null ? pointSequence : pointBo.getSequence();
                BizRobotTaskPoint taskPoint = new BizRobotTaskPoint();
                taskPoint.setTaskId(task.getTaskId());
                taskPoint.setTaskNo(task.getTaskNo());
                taskPoint.setLoopNo(loopNo);
                taskPoint.setRouteId(task.getRouteId());
                taskPoint.setPointId(pointBo.getPointId());
                taskPoint.setPointName(scenePoint.getPointName());
                taskPoint.setPointSeq(pointSeq);
                taskPoint.setRequiredFlag(StringUtils.blankToDefault(pointBo.getRequiredFlag(), "1"));
                taskPoint.setGcj02Lng(scenePoint.getGcj02Lng());
                taskPoint.setGcj02Lat(scenePoint.getGcj02Lat());
                taskPoint.setBd09Lng(scenePoint.getBd09Lng());
                taskPoint.setBd09Lat(scenePoint.getBd09Lat());
                taskPoint.setWgs84Lng(scenePoint.getWgs84Lng());
                taskPoint.setWgs84Lat(scenePoint.getWgs84Lat());
                taskPoint.setPointStatus(RobotTaskConstants.STEP_STATUS_PENDING);
                taskPoint.setRemark(pointBo.getRemark());
                taskPointMapper.insert(taskPoint);
                saveActions(task, taskPoint, pointBo.getActions(), actionMap);
                pointSequence++;
            }
        }
    }

    private void saveActions(BizRobotTask task, BizRobotTaskPoint taskPoint, List<BizTaskTemplateActionBo> actions,
                             Map<Long, BizRobotAction> actionMap) {
        if (actions == null || actions.isEmpty()) {
            return;
        }
        List<BizTaskTemplateActionBo> sortedActions = new ArrayList<>(actions);
        sortedActions.sort(Comparator.comparing(action -> defaultSequence(action.getSequence())));
        int actionSequence = 1;
        for (BizTaskTemplateActionBo actionBo : sortedActions) {
            BizRobotAction action = actionMap.get(actionBo.getActionId());
            BizRobotTaskAction taskAction = new BizRobotTaskAction();
            taskAction.setTaskId(task.getTaskId());
            taskAction.setTaskNo(task.getTaskNo());
            taskAction.setTaskPointId(taskPoint.getTaskPointId());
            taskAction.setLoopNo(taskPoint.getLoopNo());
            taskAction.setPointId(taskPoint.getPointId());
            taskAction.setPointSeq(taskPoint.getPointSeq());
            taskAction.setActionId(actionBo.getActionId());
            taskAction.setActionCode(action.getActionCode());
            taskAction.setActionName(action.getActionName());
            taskAction.setActionType(action.getActionType());
            taskAction.setActionSeq(actionBo.getSequence() == null ? actionSequence : actionBo.getSequence());
            taskAction.setActionParams(actionBo.getActionParams());
            taskAction.setActionStatus(RobotTaskConstants.STEP_STATUS_PENDING);
            taskAction.setRemark(actionBo.getRemark());
            taskActionMapper.insert(taskAction);
            actionSequence++;
        }
    }

    private String buildCommandJson(BizRobotTask task, List<BizTaskTemplatePointBo> points) {
        Map<Long, BizScenePoint> pointMap = loadPointMap(points);
        Map<Long, BizRobotAction> actionMap = loadActionMap(points);
        JSONObject root = JSONUtil.createObj();
        root.set("taskNo", task.getTaskNo());
        root.set("taskName", task.getTaskName());
        root.set("taskType", task.getTaskType());
        root.set("routeId", task.getRouteId());
        root.set("vehicleId", task.getVehicleId());
        root.set("vin", task.getVin());
        root.set("loopCount", task.getLoopCount());
        root.set("startTime", task.getStartTime() == null ? null : DateUtil.formatDateTime(task.getStartTime()));
        JSONArray pointArray = JSONUtil.createArray();
        List<BizTaskTemplatePointBo> sortedPoints = new ArrayList<>(points);
        sortedPoints.sort(Comparator.comparing(point -> defaultSequence(point.getSequence())));
        int pointSequence = 1;
        for (BizTaskTemplatePointBo pointBo : sortedPoints) {
            BizScenePoint scenePoint = pointMap.get(pointBo.getPointId());
            JSONObject point = JSONUtil.createObj();
            point.set("pointId", pointBo.getPointId());
            point.set("pointName", scenePoint.getPointName());
            point.set("sequence", pointBo.getSequence() == null ? pointSequence : pointBo.getSequence());
            point.set("gcj02Lng", scenePoint.getGcj02Lng());
            point.set("gcj02Lat", scenePoint.getGcj02Lat());
            JSONArray actionArray = JSONUtil.createArray();
            if (pointBo.getActions() != null) {
                List<BizTaskTemplateActionBo> sortedActions = new ArrayList<>(pointBo.getActions());
                sortedActions.sort(Comparator.comparing(action -> defaultSequence(action.getSequence())));
                int actionSequence = 1;
                for (BizTaskTemplateActionBo actionBo : sortedActions) {
                    BizRobotAction action = actionMap.get(actionBo.getActionId());
                    JSONObject actionJson = JSONUtil.createObj();
                    actionJson.set("actionId", actionBo.getActionId());
                    actionJson.set("actionCode", action.getActionCode());
                    actionJson.set("actionName", action.getActionName());
                    actionJson.set("actionType", action.getActionType());
                    actionJson.set("sequence", actionBo.getSequence() == null ? actionSequence : actionBo.getSequence());
                    actionJson.set("params", StringUtils.isBlank(actionBo.getActionParams()) ? null : JSONUtil.parse(actionBo.getActionParams()));
                    actionArray.add(actionJson);
                    actionSequence++;
                }
            }
            point.set("actions", actionArray);
            pointArray.add(point);
            pointSequence++;
        }
        root.set("points", pointArray);
        return root.toString();
    }

    private Map<Long, BizScenePoint> loadPointMap(List<BizTaskTemplatePointBo> points) {
        List<Long> pointIds = points.stream().map(BizTaskTemplatePointBo::getPointId).filter(Objects::nonNull).distinct().toList();
        if (pointIds.isEmpty()) {
            return Map.of();
        }
        return scenePointMapper.selectList(new LambdaQueryWrapper<BizScenePoint>().in(BizScenePoint::getPointId, pointIds))
            .stream()
            .collect(Collectors.toMap(BizScenePoint::getPointId, Function.identity(), (l, r) -> l));
    }

    private Map<Long, BizRobotAction> loadActionMap(List<BizTaskTemplatePointBo> points) {
        Set<Long> actionIds = points.stream()
            .filter(point -> point.getActions() != null)
            .flatMap(point -> point.getActions().stream())
            .map(BizTaskTemplateActionBo::getActionId)
            .filter(Objects::nonNull)
            .collect(Collectors.toSet());
        if (actionIds.isEmpty()) {
            return Map.of();
        }
        return robotActionMapper.selectList(new LambdaQueryWrapper<BizRobotAction>().in(BizRobotAction::getActionId, actionIds))
            .stream()
            .collect(Collectors.toMap(BizRobotAction::getActionId, Function.identity(), (l, r) -> l));
    }

    private Integer defaultSequence(Integer sequence) {
        return sequence == null ? Integer.MAX_VALUE : sequence;
    }

    private void fillRouteAndTemplateName(BizRobotTaskVo vo) {
        if (vo.getRouteId() != null && StringUtils.isBlank(vo.getRouteName())) {
            BizSceneRoute route = sceneRouteMapper.selectById(vo.getRouteId());
            if (route != null) {
                vo.setRouteName(route.getRouteName());
            }
        }
        if (vo.getTemplateId() != null && StringUtils.isBlank(vo.getTemplateName())) {
            BizTaskTemplate template = taskTemplateMapper.selectById(vo.getTemplateId());
            if (template != null) {
                vo.setTemplateName(template.getTemplateName());
            }
        }
    }

    private void fillDetails(BizRobotTaskVo vo) {
        List<BizRobotTaskPointVo> points = taskPointMapper.selectByTaskId(vo.getTaskId());
        List<BizRobotTaskActionVo> actions = taskActionMapper.selectByTaskId(vo.getTaskId());
        Map<Long, List<BizRobotTaskActionVo>> actionMap = actions.stream()
            .collect(Collectors.groupingBy(BizRobotTaskActionVo::getTaskPointId));
        for (BizRobotTaskPointVo point : points) {
            point.setActions(actionMap.getOrDefault(point.getTaskPointId(), List.of()));
        }
        vo.setPoints(points);
    }

    private void deleteDetails(Collection<Long> taskIds) {
        taskActionMapper.delete(new LambdaQueryWrapper<BizRobotTaskAction>().in(BizRobotTaskAction::getTaskId, taskIds));
        taskPointMapper.delete(new LambdaQueryWrapper<BizRobotTaskPoint>().in(BizRobotTaskPoint::getTaskId, taskIds));
    }

    private BizRobotTask resolveTask(RobotTaskStepReportBo bo) {
        if (bo.getTaskId() != null) {
            return baseMapper.selectById(bo.getTaskId());
        }
        if (StringUtils.isNotBlank(bo.getTaskNo())) {
            return baseMapper.selectOne(new LambdaQueryWrapper<BizRobotTask>().eq(BizRobotTask::getTaskNo, bo.getTaskNo()));
        }
        String tenantId = LoginHelper.getTenantId();
        RobotTaskVehicleBindVo bind = null;
        if (bo.getVehicleId() != null) {
            bind = runtimeCacheService.getTaskByVehicleId(tenantId, bo.getVehicleId());
        }
        if (bind == null && StringUtils.isNotBlank(bo.getVin())) {
            bind = runtimeCacheService.getTaskByVin(tenantId, bo.getVin());
        }
        return bind == null ? null : baseMapper.selectById(bind.getTaskId());
    }

    private void updatePointStatus(BizRobotTask task, RobotTaskStepReportBo bo, String status) {
        Integer loopNo = defaultReportNo(bo.getLoopNo(), task.getCurrentLoopNo(), 1);
        Integer pointSeq = defaultReportNo(bo.getPointSeq(), task.getCurrentPointSeq(), 1);
        LambdaUpdateWrapper<BizRobotTaskPoint> wrapper = new LambdaUpdateWrapper<BizRobotTaskPoint>()
            .eq(BizRobotTaskPoint::getTaskId, task.getTaskId())
            .eq(BizRobotTaskPoint::getLoopNo, loopNo)
            .eq(BizRobotTaskPoint::getPointSeq, pointSeq)
            .set(BizRobotTaskPoint::getPointStatus, status)
            .set(BizRobotTaskPoint::getReportPayload, bo.getReportPayload());
        if (Objects.equals(status, RobotTaskConstants.STEP_STATUS_RUNNING)) {
            wrapper.set(BizRobotTaskPoint::getArriveTime, new Date());
        }
        if (Objects.equals(status, RobotTaskConstants.STEP_STATUS_SUCCESS)
            || Objects.equals(status, RobotTaskConstants.STEP_STATUS_FAIL)
            || Objects.equals(status, RobotTaskConstants.STEP_STATUS_SKIPPED)) {
            wrapper.set(BizRobotTaskPoint::getFinishTime, new Date());
        }
        taskPointMapper.update(null, wrapper);
        runtimeCacheService.updatePointStep(task, loopNo, pointSeq, status, bo.getReportPayload(), bo.getMessage());
    }

    private void updateActionStatus(BizRobotTask task, RobotTaskStepReportBo bo, String status) {
        Integer loopNo = defaultReportNo(bo.getLoopNo(), task.getCurrentLoopNo(), 1);
        Integer pointSeq = defaultReportNo(bo.getPointSeq(), task.getCurrentPointSeq(), 1);
        Integer actionSeq = defaultReportNo(bo.getActionSeq(), task.getCurrentActionSeq(), 1);
        LambdaUpdateWrapper<BizRobotTaskAction> wrapper = new LambdaUpdateWrapper<BizRobotTaskAction>()
            .eq(BizRobotTaskAction::getTaskId, task.getTaskId())
            .eq(BizRobotTaskAction::getLoopNo, loopNo)
            .eq(BizRobotTaskAction::getPointSeq, pointSeq)
            .eq(BizRobotTaskAction::getActionSeq, actionSeq)
            .set(BizRobotTaskAction::getActionStatus, status)
            .set(BizRobotTaskAction::getReportPayload, bo.getReportPayload())
            .set(BizRobotTaskAction::getErrorMessage, bo.getMessage());
        if (Objects.equals(status, RobotTaskConstants.STEP_STATUS_RUNNING)) {
            wrapper.set(BizRobotTaskAction::getStartTime, new Date());
        }
        if (Objects.equals(status, RobotTaskConstants.STEP_STATUS_SUCCESS)
            || Objects.equals(status, RobotTaskConstants.STEP_STATUS_FAIL)
            || Objects.equals(status, RobotTaskConstants.STEP_STATUS_SKIPPED)) {
            wrapper.set(BizRobotTaskAction::getFinishTime, new Date());
        }
        taskActionMapper.update(null, wrapper);
        runtimeCacheService.updateActionStep(task, loopNo, pointSeq, actionSeq, status, bo.getReportPayload(), bo.getMessage());
    }

    private void aggregateTaskStatus(BizRobotTask task, RobotTaskStepReportBo bo) {
        if (Objects.equals(bo.getStatus(), RobotTaskConstants.STEP_STATUS_FAIL)
            || runtimeCacheService.hasFailStep(task.getTenantId(), task.getTaskId())) {
            task.setTaskStatus(RobotTaskConstants.TASK_STATUS_ABNORMAL);
            task.setErrorMessage(StringUtils.blankToDefault(bo.getMessage(), "步骤执行失败"));
            task.setFinishTime(new Date());
            baseMapper.updateById(task);
            syncRuntimeFromTask(task, calcProgress(task.getTaskId()), task.getErrorMessage());
            runtimeCacheService.releaseVehicle(task);
            return;
        }

        if (runtimeCacheService.isAllDone(task.getTenantId(), task.getTaskId())) {
            task.setTaskStatus(RobotTaskConstants.TASK_STATUS_COMPLETED);
            task.setFinishTime(new Date());
            baseMapper.updateById(task);
            syncRuntimeFromTask(task, 100, null);
            runtimeCacheService.releaseVehicle(task);
            return;
        }

        if (Objects.equals(bo.getStatus(), RobotTaskConstants.STEP_STATUS_SUCCESS)
            || Objects.equals(bo.getStatus(), RobotTaskConstants.STEP_STATUS_SKIPPED)) {
            advanceCurrentPosition(task);
        }
        syncRuntimeFromTask(task, calcProgress(task.getTaskId()), null);
    }

    private void advanceCurrentPosition(BizRobotTask task) {
        BizRobotTaskAction nextAction = taskActionMapper.selectOne(new LambdaQueryWrapper<BizRobotTaskAction>()
            .eq(BizRobotTaskAction::getTaskId, task.getTaskId())
            .eq(BizRobotTaskAction::getActionStatus, RobotTaskConstants.STEP_STATUS_PENDING)
            .orderByAsc(BizRobotTaskAction::getLoopNo, BizRobotTaskAction::getPointSeq, BizRobotTaskAction::getActionSeq)
            .last("limit 1"));
        if (nextAction == null) {
            return;
        }
        task.setCurrentLoopNo(nextAction.getLoopNo());
        task.setCurrentPointSeq(nextAction.getPointSeq());
        task.setCurrentActionSeq(nextAction.getActionSeq());
        baseMapper.updateById(task);
    }

    private int calcProgress(Long taskId) {
        BizRobotTask task = baseMapper.selectById(taskId);
        if (task != null) {
            int redisProgress = runtimeCacheService.calcProgress(task.getTenantId(), taskId);
            if (redisProgress > 0) {
                return redisProgress;
            }
        }
        Long total = taskActionMapper.selectCount(new LambdaQueryWrapper<BizRobotTaskAction>().eq(BizRobotTaskAction::getTaskId, taskId));
        if (total == 0) {
            Long totalPoints = taskPointMapper.selectCount(new LambdaQueryWrapper<BizRobotTaskPoint>().eq(BizRobotTaskPoint::getTaskId, taskId));
            if (totalPoints == 0) {
                return 0;
            }
            Long donePoints = taskPointMapper.selectCount(new LambdaQueryWrapper<BizRobotTaskPoint>()
                .eq(BizRobotTaskPoint::getTaskId, taskId)
                .in(BizRobotTaskPoint::getPointStatus, List.of(
                    RobotTaskConstants.STEP_STATUS_SUCCESS,
                    RobotTaskConstants.STEP_STATUS_FAIL,
                    RobotTaskConstants.STEP_STATUS_SKIPPED)));
            return (int) Math.min(100, donePoints * 100 / totalPoints);
        }
        Long done = taskActionMapper.selectCount(new LambdaQueryWrapper<BizRobotTaskAction>()
            .eq(BizRobotTaskAction::getTaskId, taskId)
            .in(BizRobotTaskAction::getActionStatus, List.of(
                RobotTaskConstants.STEP_STATUS_SUCCESS,
                RobotTaskConstants.STEP_STATUS_FAIL,
                RobotTaskConstants.STEP_STATUS_SKIPPED)));
        return (int) Math.min(100, done * 100 / total);
    }

    private void syncRuntimeFromTask(BizRobotTask task, Integer progress, String errorMessage) {
        RobotTaskRuntimeStatusVo status = new RobotTaskRuntimeStatusVo();
        status.setTaskId(task.getTaskId());
        status.setTaskNo(task.getTaskNo());
        status.setTaskStatus(task.getTaskStatus());
        status.setCurrentLoopNo(task.getCurrentLoopNo());
        status.setLoopCount(task.getLoopCount());
        status.setCurrentPointSeq(task.getCurrentPointSeq());
        status.setCurrentActionSeq(task.getCurrentActionSeq());
        status.setProgress(progress);
        status.setErrorMessage(errorMessage);
        status.setVehicleId(task.getVehicleId());
        status.setVin(task.getVin());
        status.setPlateNo(task.getPlateNo());
        fillCurrentNames(status);
        runtimeCacheService.updateRuntime(task.getTenantId(), status);
        runtimeCacheService.bindVehicle(task);
    }

    private void fillCurrentNames(RobotTaskRuntimeStatusVo status) {
        if (status.getTaskId() == null || status.getCurrentLoopNo() == null || status.getCurrentPointSeq() == null) {
            return;
        }
        BizRobotTaskPoint point = taskPointMapper.selectOne(new LambdaQueryWrapper<BizRobotTaskPoint>()
            .eq(BizRobotTaskPoint::getTaskId, status.getTaskId())
            .eq(BizRobotTaskPoint::getLoopNo, status.getCurrentLoopNo())
            .eq(BizRobotTaskPoint::getPointSeq, status.getCurrentPointSeq())
            .last("limit 1"));
        if (point != null) {
            status.setCurrentPointName(point.getPointName());
        }
        if (status.getCurrentActionSeq() != null && status.getCurrentActionSeq() > 0) {
            BizRobotTaskAction action = taskActionMapper.selectOne(new LambdaQueryWrapper<BizRobotTaskAction>()
                .eq(BizRobotTaskAction::getTaskId, status.getTaskId())
                .eq(BizRobotTaskAction::getLoopNo, status.getCurrentLoopNo())
                .eq(BizRobotTaskAction::getPointSeq, status.getCurrentPointSeq())
                .eq(BizRobotTaskAction::getActionSeq, status.getCurrentActionSeq())
                .last("limit 1"));
            if (action != null) {
                status.setCurrentActionName(action.getActionName());
            }
        }
    }

    private Integer defaultReportNo(Integer reportNo, Integer currentNo, Integer defaultNo) {
        if (reportNo != null) {
            return reportNo;
        }
        return currentNo == null || currentNo == 0 ? defaultNo : currentNo;
    }

}
