package org.dromara.manager.service.support;

import lombok.RequiredArgsConstructor;
import org.dromara.common.redis.utils.RedisUtils;
import org.dromara.common.tenant.helper.TenantHelper;
import org.dromara.manager.constant.RobotTaskConstants;
import org.dromara.manager.domain.BizRobotTask;
import org.dromara.manager.domain.BizRobotTaskAction;
import org.dromara.manager.domain.BizRobotTaskPoint;
import org.dromara.manager.domain.vo.RobotTaskRuntimeStatusVo;
import org.dromara.manager.domain.vo.RobotTaskStepRuntimeVo;
import org.dromara.manager.domain.vo.RobotTaskVehicleBindVo;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * 机器人任务运行态缓存。
 *
 * @author LionLi
 * @date 2026-06-19
 */
@RequiredArgsConstructor
@Service
public class RobotTaskRuntimeCacheService {

    private static final Duration RUNTIME_TTL = Duration.ofDays(RobotTaskConstants.RUNTIME_TTL_DAYS);

    public void initRuntime(BizRobotTask task) {
        RobotTaskRuntimeStatusVo status = buildRuntime(task);
        RedisUtils.setCacheObject(runtimeKey(task.getTenantId(), task.getTaskId()), status, RUNTIME_TTL);
        bindVehicle(task);
    }

    public void initStepRuntime(BizRobotTask task, List<BizRobotTaskPoint> points, List<BizRobotTaskAction> actions) {
        runWithTaskTenant(task, () -> {
            deleteStepRuntime(task);
            if (points != null && !points.isEmpty()) {
                Map<String, RobotTaskStepRuntimeVo> pointMap = points.stream()
                    .map(this::buildPointStep)
                    .collect(Collectors.toMap(RobotTaskStepRuntimeVo::getStepKey, Function.identity(), (l, r) -> l));
                RedisUtils.setCacheMap(pointRuntimeKey(task.getTenantId(), task.getTaskId()), pointMap);
                RedisUtils.expire(pointRuntimeKey(task.getTenantId(), task.getTaskId()), RUNTIME_TTL);
            }
            if (actions != null && !actions.isEmpty()) {
                Map<String, RobotTaskStepRuntimeVo> actionMap = actions.stream()
                    .map(this::buildActionStep)
                    .collect(Collectors.toMap(RobotTaskStepRuntimeVo::getStepKey, Function.identity(), (l, r) -> l));
                RedisUtils.setCacheMap(actionRuntimeKey(task.getTenantId(), task.getTaskId()), actionMap);
                RedisUtils.expire(actionRuntimeKey(task.getTenantId(), task.getTaskId()), RUNTIME_TTL);
            }
        });
    }

    public void updateRuntime(String tenantId, RobotTaskRuntimeStatusVo status) {
        status.setLastReportTime(new Date());
        RedisUtils.setCacheObject(runtimeKey(tenantId, status.getTaskId()), status, RUNTIME_TTL);
    }

    public RobotTaskRuntimeStatusVo getRuntime(String tenantId, Long taskId) {
        return RedisUtils.getCacheObject(runtimeKey(tenantId, taskId));
    }

    public List<RobotTaskRuntimeStatusVo> listRuntime(String tenantId, Collection<Long> taskIds) {
        if (taskIds == null || taskIds.isEmpty()) {
            return List.of();
        }
        List<RobotTaskRuntimeStatusVo> list = new ArrayList<>();
        for (Long taskId : taskIds) {
            RobotTaskRuntimeStatusVo status = getRuntime(tenantId, taskId);
            if (status != null) {
                list.add(status);
            }
        }
        return list;
    }

    public void bindVehicle(BizRobotTask task) {
        if (task.getVehicleId() == null && task.getVin() == null) {
            return;
        }
        RobotTaskVehicleBindVo bind = new RobotTaskVehicleBindVo();
        bind.setTaskId(task.getTaskId());
        bind.setTaskNo(task.getTaskNo());
        bind.setTaskName(task.getTaskName());
        bind.setTaskStatus(task.getTaskStatus());
        bind.setVehicleId(task.getVehicleId());
        bind.setVin(task.getVin());
        bind.setPlateNo(task.getPlateNo());
        bind.setBindType(task.getAssignMode());
        bind.setBindTime(new Date());

        RedisUtils.setCacheObject(taskVehicleByTaskIdKey(task.getTenantId(), task.getTaskId()), bind, RUNTIME_TTL);
        RedisUtils.setCacheObject(taskVehicleByTaskNoKey(task.getTenantId(), task.getTaskNo()), bind, RUNTIME_TTL);
        if (task.getVehicleId() != null) {
            RedisUtils.setCacheObject(vehicleTaskByVehicleIdKey(task.getTenantId(), task.getVehicleId()), bind, RUNTIME_TTL);
        }
        if (task.getVin() != null) {
            RedisUtils.setCacheObject(vehicleTaskByVinKey(task.getTenantId(), task.getVin()), bind, RUNTIME_TTL);
        }
    }

    public void unbindVehicle(BizRobotTask task) {
        runWithTaskTenant(task, () -> unbindVehicleInternal(task));
    }

    public void deleteRuntime(BizRobotTask task) {
        runWithTaskTenant(task, () -> {
            deleteKeyVariants(task.getTenantId(), runtimeKey(task.getTenantId(), task.getTaskId()));
            deleteStepRuntime(task);
            unbindVehicleInternal(task);
        });
    }

    public void deleteRuntimeByTaskId(String tenantId, Long taskId) {
        runWithTenantId(tenantId, () -> {
            deleteKeyVariants(tenantId, runtimeKey(tenantId, taskId));
            deleteKeyVariants(tenantId, pointRuntimeKey(tenantId, taskId));
            deleteKeyVariants(tenantId, actionRuntimeKey(tenantId, taskId));
            deleteKeyVariants(tenantId, taskVehicleByTaskIdKey(tenantId, taskId));
        });
    }

    public RobotTaskStepRuntimeVo updatePointStep(BizRobotTask task, Integer loopNo, Integer pointSeq, String status,
                                                   String reportPayload, String message) {
        return updateStep(task, pointRuntimeKey(task.getTenantId(), task.getTaskId()),
            pointStepKey(loopNo, pointSeq), status, reportPayload, message);
    }

    public RobotTaskStepRuntimeVo updateActionStep(BizRobotTask task, Integer loopNo, Integer pointSeq, Integer actionSeq,
                                                    String status, String reportPayload, String message) {
        return updateStep(task, actionRuntimeKey(task.getTenantId(), task.getTaskId()),
            actionStepKey(loopNo, pointSeq, actionSeq), status, reportPayload, message);
    }

    public int calcProgress(String tenantId, Long taskId) {
        return supplyWithTenantId(tenantId, () -> {
            Map<String, RobotTaskStepRuntimeVo> actions = RedisUtils.getCacheMap(actionRuntimeKey(tenantId, taskId));
            Map<String, RobotTaskStepRuntimeVo> points = RedisUtils.getCacheMap(pointRuntimeKey(tenantId, taskId));
            return calcProgress(points, actions);
        });
    }

    public boolean hasFailStep(String tenantId, Long taskId) {
        return supplyWithTenantId(tenantId, () -> hasStatus(actionRuntimeKey(tenantId, taskId), RobotTaskConstants.STEP_STATUS_FAIL)
            || hasStatus(pointRuntimeKey(tenantId, taskId), RobotTaskConstants.STEP_STATUS_FAIL));
    }

    public boolean isAllDone(String tenantId, Long taskId) {
        return supplyWithTenantId(tenantId, () -> {
            Map<String, RobotTaskStepRuntimeVo> actions = RedisUtils.getCacheMap(actionRuntimeKey(tenantId, taskId));
            Map<String, RobotTaskStepRuntimeVo> points = RedisUtils.getCacheMap(pointRuntimeKey(tenantId, taskId));
            return hasSteps(points, actions) && isAllDone(points) && isAllDone(actions);
        });
    }

    public void releaseVehicle(BizRobotTask task) {
        runWithTaskTenant(task, () -> {
            if (task.getVehicleId() != null) {
                deleteKeyVariants(task.getTenantId(), vehicleTaskByVehicleIdKey(task.getTenantId(), task.getVehicleId()));
            }
            if (task.getVin() != null) {
                deleteKeyVariants(task.getTenantId(), vehicleTaskByVinKey(task.getTenantId(), task.getVin()));
            }
            RobotTaskVehicleBindVo bind = buildVehicleBind(task);
            RedisUtils.setCacheObject(taskVehicleByTaskIdKey(task.getTenantId(), task.getTaskId()), bind, RUNTIME_TTL);
            RedisUtils.setCacheObject(taskVehicleByTaskNoKey(task.getTenantId(), task.getTaskNo()), bind, RUNTIME_TTL);
        });
    }

    public RobotTaskVehicleBindVo getVehicleByTaskId(String tenantId, Long taskId) {
        return RedisUtils.getCacheObject(taskVehicleByTaskIdKey(tenantId, taskId));
    }

    public RobotTaskVehicleBindVo getVehicleByTaskNo(String tenantId, String taskNo) {
        return RedisUtils.getCacheObject(taskVehicleByTaskNoKey(tenantId, taskNo));
    }

    public RobotTaskVehicleBindVo getTaskByVehicleId(String tenantId, Long vehicleId) {
        return RedisUtils.getCacheObject(vehicleTaskByVehicleIdKey(tenantId, vehicleId));
    }

    public RobotTaskVehicleBindVo getTaskByVin(String tenantId, String vin) {
        return RedisUtils.getCacheObject(vehicleTaskByVinKey(tenantId, vin));
    }

    public RobotTaskVehicleBindVo getActiveTaskByVehicle(String tenantId, Long vehicleId, String vin) {
        RobotTaskVehicleBindVo byVehicle = vehicleId == null ? null : getTaskByVehicleId(tenantId, vehicleId);
        if (isActiveBind(byVehicle)) {
            return byVehicle;
        }
        RobotTaskVehicleBindVo byVin = vin == null ? null : getTaskByVin(tenantId, vin);
        return isActiveBind(byVin) ? byVin : null;
    }

    public boolean hasRunningTaskByVehicle(String tenantId, Long vehicleId, String vin) {
        return getActiveTaskByVehicle(tenantId, vehicleId, vin) != null;
    }

    private boolean isActiveBind(RobotTaskVehicleBindVo bind) {
        if (bind == null) {
            return false;
        }
        return Objects.equals(bind.getTaskStatus(), RobotTaskConstants.TASK_STATUS_PENDING)
            || Objects.equals(bind.getTaskStatus(), RobotTaskConstants.TASK_STATUS_RUNNING);
    }

    private RobotTaskRuntimeStatusVo buildRuntime(BizRobotTask task) {
        RobotTaskRuntimeStatusVo status = new RobotTaskRuntimeStatusVo();
        status.setTaskId(task.getTaskId());
        status.setTaskNo(task.getTaskNo());
        status.setTaskStatus(task.getTaskStatus());
        status.setCurrentLoopNo(defaultInt(task.getCurrentLoopNo()));
        status.setLoopCount(defaultInt(task.getLoopCount()));
        status.setCurrentPointSeq(defaultInt(task.getCurrentPointSeq()));
        status.setCurrentActionSeq(defaultInt(task.getCurrentActionSeq()));
        status.setProgress(0);
        status.setLastReportTime(new Date());
        status.setErrorMessage(task.getErrorMessage());
        status.setVehicleId(task.getVehicleId());
        status.setVin(task.getVin());
        status.setPlateNo(task.getPlateNo());
        return status;
    }

    private RobotTaskVehicleBindVo buildVehicleBind(BizRobotTask task) {
        RobotTaskVehicleBindVo bind = new RobotTaskVehicleBindVo();
        bind.setTaskId(task.getTaskId());
        bind.setTaskNo(task.getTaskNo());
        bind.setTaskName(task.getTaskName());
        bind.setTaskStatus(task.getTaskStatus());
        bind.setVehicleId(task.getVehicleId());
        bind.setVin(task.getVin());
        bind.setPlateNo(task.getPlateNo());
        bind.setBindType(task.getAssignMode());
        bind.setBindTime(new Date());
        return bind;
    }

    private RobotTaskStepRuntimeVo updateStep(BizRobotTask task, String key, String stepKey, String status,
                                               String reportPayload, String message) {
        return runWithTaskTenant(task, () -> {
            RobotTaskStepRuntimeVo step = RedisUtils.getCacheMapValue(key, stepKey);
            if (step == null) {
                return null;
            }
            Date now = new Date();
            step.setStatus(status);
            step.setReportPayload(reportPayload);
            step.setMessage(message);
            step.setUpdateTime(now);
            if (Objects.equals(status, RobotTaskConstants.STEP_STATUS_RUNNING) && step.getStartTime() == null) {
                step.setStartTime(now);
            }
            if (isDoneStatus(status)) {
                step.setFinishTime(now);
            }
            RedisUtils.setCacheMapValue(key, stepKey, step);
            RedisUtils.expire(key, RUNTIME_TTL);
            return step;
        });
    }

    private int calcProgress(Map<String, RobotTaskStepRuntimeVo> steps) {
        if (steps == null || steps.isEmpty()) {
            return 0;
        }
        long done = steps.values().stream()
            .filter(step -> isDoneStatus(step.getStatus()))
            .count();
        return (int) Math.min(100, done * 100 / steps.size());
    }

    private int calcProgress(Map<String, RobotTaskStepRuntimeVo> points, Map<String, RobotTaskStepRuntimeVo> actions) {
        long total = size(points) + size(actions);
        if (total == 0) {
            return 0;
        }
        long done = countDone(points) + countDone(actions);
        return (int) Math.min(100, done * 100 / total);
    }

    private boolean hasStatus(String key, String status) {
        Map<String, RobotTaskStepRuntimeVo> steps = RedisUtils.getCacheMap(key);
        return steps != null && steps.values().stream().anyMatch(step -> Objects.equals(step.getStatus(), status));
    }

    private boolean isAllDone(Map<String, RobotTaskStepRuntimeVo> steps) {
        if (steps == null || steps.isEmpty()) {
            return true;
        }
        return steps.values().stream().allMatch(step -> isDoneStatus(step.getStatus()));
    }

    private boolean hasSteps(Map<String, RobotTaskStepRuntimeVo> points, Map<String, RobotTaskStepRuntimeVo> actions) {
        return size(points) + size(actions) > 0;
    }

    private int size(Map<String, RobotTaskStepRuntimeVo> steps) {
        return steps == null ? 0 : steps.size();
    }

    private long countDone(Map<String, RobotTaskStepRuntimeVo> steps) {
        if (steps == null || steps.isEmpty()) {
            return 0;
        }
        return steps.values().stream()
            .filter(step -> isDoneStatus(step.getStatus()))
            .count();
    }

    private boolean isDoneStatus(String status) {
        return Objects.equals(status, RobotTaskConstants.STEP_STATUS_SUCCESS)
            || Objects.equals(status, RobotTaskConstants.STEP_STATUS_FAIL)
            || Objects.equals(status, RobotTaskConstants.STEP_STATUS_SKIPPED);
    }

    private RobotTaskStepRuntimeVo buildPointStep(BizRobotTaskPoint point) {
        RobotTaskStepRuntimeVo step = new RobotTaskStepRuntimeVo();
        step.setTaskId(point.getTaskId());
        step.setTaskNo(point.getTaskNo());
        step.setStepType(RobotTaskConstants.STEP_TYPE_POINT);
        step.setStepKey(pointStepKey(point.getLoopNo(), point.getPointSeq()));
        step.setLoopNo(point.getLoopNo());
        step.setTaskPointId(point.getTaskPointId());
        step.setPointId(point.getPointId());
        step.setPointName(point.getPointName());
        step.setPointSeq(point.getPointSeq());
        step.setStatus(point.getPointStatus());
        step.setReportPayload(point.getReportPayload());
        step.setStartTime(point.getArriveTime());
        step.setFinishTime(point.getFinishTime());
        step.setUpdateTime(new Date());
        return step;
    }

    private RobotTaskStepRuntimeVo buildActionStep(BizRobotTaskAction action) {
        RobotTaskStepRuntimeVo step = new RobotTaskStepRuntimeVo();
        step.setTaskId(action.getTaskId());
        step.setTaskNo(action.getTaskNo());
        step.setStepType(RobotTaskConstants.STEP_TYPE_ACTION);
        step.setStepKey(actionStepKey(action.getLoopNo(), action.getPointSeq(), action.getActionSeq()));
        step.setLoopNo(action.getLoopNo());
        step.setTaskPointId(action.getTaskPointId());
        step.setTaskActionId(action.getTaskActionId());
        step.setPointId(action.getPointId());
        step.setPointSeq(action.getPointSeq());
        step.setActionId(action.getActionId());
        step.setActionCode(action.getActionCode());
        step.setActionName(action.getActionName());
        step.setActionSeq(action.getActionSeq());
        step.setStatus(action.getActionStatus());
        step.setReportPayload(action.getReportPayload());
        step.setMessage(action.getErrorMessage());
        step.setStartTime(action.getStartTime());
        step.setFinishTime(action.getFinishTime());
        step.setUpdateTime(new Date());
        return step;
    }

    private void deleteStepRuntime(BizRobotTask task) {
        deleteKeyVariants(task.getTenantId(), pointRuntimeKey(task.getTenantId(), task.getTaskId()));
        deleteKeyVariants(task.getTenantId(), actionRuntimeKey(task.getTenantId(), task.getTaskId()));
    }

    private void unbindVehicleInternal(BizRobotTask task) {
        deleteKeyVariants(task.getTenantId(), taskVehicleByTaskIdKey(task.getTenantId(), task.getTaskId()));
        deleteKeyVariants(task.getTenantId(), taskVehicleByTaskNoKey(task.getTenantId(), task.getTaskNo()));
        if (task.getVehicleId() != null) {
            deleteKeyVariants(task.getTenantId(), vehicleTaskByVehicleIdKey(task.getTenantId(), task.getVehicleId()));
        }
        if (task.getVin() != null) {
            deleteKeyVariants(task.getTenantId(), vehicleTaskByVinKey(task.getTenantId(), task.getVin()));
        }
    }

    private void deleteKeyVariants(String tenantId, String key) {
        if (key == null) {
            return;
        }
        RedisUtils.deleteObject(key);
        String tenantPrefix = tenantId + ":";
        if (tenantId != null && !key.startsWith(tenantPrefix)) {
            RedisUtils.deleteObject(tenantPrefix + key);
        }
    }

    private void runWithTaskTenant(BizRobotTask task, Runnable runnable) {
        if (task == null || task.getTenantId() == null || task.getTenantId().isBlank()) {
            runnable.run();
            return;
        }
        TenantHelper.dynamic(task.getTenantId(), runnable);
    }

    private <T> T runWithTaskTenant(BizRobotTask task, Supplier<T> supplier) {
        if (task == null || task.getTenantId() == null || task.getTenantId().isBlank()) {
            return supplier.get();
        }
        return TenantHelper.dynamic(task.getTenantId(), supplier);
    }

    private void runWithTenantId(String tenantId, Runnable runnable) {
        if (tenantId == null || tenantId.isBlank()) {
            runnable.run();
            return;
        }
        TenantHelper.dynamic(tenantId, runnable);
    }

    private <T> T supplyWithTenantId(String tenantId, Supplier<T> supplier) {
        if (tenantId == null || tenantId.isBlank()) {
            return supplier.get();
        }
        return TenantHelper.dynamic(tenantId, supplier);
    }

    private Integer defaultInt(Integer value) {
        return value == null ? 0 : value;
    }

    private String runtimeKey(String tenantId, Long taskId) {
        return "robot:task:runtime:" + tenantId + ":" + taskId;
    }

    private String pointRuntimeKey(String tenantId, Long taskId) {
        return "robot:task:runtime:point:" + tenantId + ":" + taskId;
    }

    private String actionRuntimeKey(String tenantId, Long taskId) {
        return "robot:task:runtime:action:" + tenantId + ":" + taskId;
    }

    private String pointStepKey(Integer loopNo, Integer pointSeq) {
        return loopNo + ":" + pointSeq;
    }

    private String actionStepKey(Integer loopNo, Integer pointSeq, Integer actionSeq) {
        return loopNo + ":" + pointSeq + ":" + actionSeq;
    }

    private String taskVehicleByTaskIdKey(String tenantId, Long taskId) {
        return "robot:task:vehicle:taskId:" + tenantId + ":" + taskId;
    }

    private String taskVehicleByTaskNoKey(String tenantId, String taskNo) {
        return "robot:task:vehicle:taskNo:" + tenantId + ":" + taskNo;
    }

    private String vehicleTaskByVehicleIdKey(String tenantId, Long vehicleId) {
        return "robot:vehicle:task:vehicleId:" + tenantId + ":" + vehicleId;
    }

    private String vehicleTaskByVinKey(String tenantId, String vin) {
        return "robot:vehicle:task:vin:" + tenantId + ":" + vin;
    }

}
