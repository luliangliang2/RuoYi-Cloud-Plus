package org.dromara.manager.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.dromara.common.core.domain.R;
import org.dromara.common.core.validate.AddGroup;
import org.dromara.common.core.validate.EditGroup;
import org.dromara.common.excel.utils.ExcelUtil;
import org.dromara.common.idempotent.annotation.RepeatSubmit;
import org.dromara.common.log.annotation.Log;
import org.dromara.common.log.enums.BusinessType;
import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.common.web.core.BaseController;
import org.dromara.manager.domain.bo.BizRobotTaskBo;
import org.dromara.manager.domain.bo.RobotTaskStepReportBo;
import org.dromara.manager.domain.vo.BizRobotTaskVo;
import org.dromara.manager.domain.vo.RobotTaskRuntimeStatusVo;
import org.dromara.manager.domain.vo.RobotTaskVehicleBindVo;
import org.dromara.manager.service.IBizRobotTaskService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

/**
 * 机器人任务执行
 * 前端访问路由地址为:/manager/robotTask
 *
 * @author LionLi
 * @date 2026-06-19
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/robotTask")
public class BizRobotTaskController extends BaseController {

    private final IBizRobotTaskService robotTaskService;

    /**
     * 查询机器人任务执行分页列表
     */
    @SaCheckPermission("manager:robotTask:list")
    @GetMapping("/list")
    public TableDataInfo<BizRobotTaskVo> list(BizRobotTaskBo bo, PageQuery pageQuery) {
        return robotTaskService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出机器人任务执行列表
     */
    @SaCheckPermission("manager:robotTask:export")
    @Log(title = "机器人任务执行", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(BizRobotTaskBo bo, HttpServletResponse response) {
        List<BizRobotTaskVo> list = robotTaskService.queryList(bo);
        ExcelUtil.exportExcel(list, "机器人任务执行数据", BizRobotTaskVo.class, response);
    }

    /**
     * 获取机器人任务执行详细信息
     *
     * @param taskId 主键
     */
    @SaCheckPermission("manager:robotTask:query")
    @GetMapping("/{taskId}")
    public R<BizRobotTaskVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable("taskId") Long taskId) {
        return R.ok(robotTaskService.queryById(taskId));
    }

    /**
     * 新增机器人任务执行
     */
    @SaCheckPermission("manager:robotTask:add")
    @Log(title = "机器人任务执行", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping
    public R<Void> add(@Validated(AddGroup.class) @RequestBody BizRobotTaskBo bo) {
        return toAjax(robotTaskService.insertByBo(bo));
    }

    /**
     * 修改机器人任务执行
     */
    @SaCheckPermission("manager:robotTask:edit")
    @Log(title = "机器人任务执行", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody BizRobotTaskBo bo) {
        return toAjax(robotTaskService.updateByBo(bo));
    }

    /**
     * 删除机器人任务执行
     *
     * @param ids 主键串
     */
    @SaCheckPermission("manager:robotTask:remove")
    @Log(title = "机器人任务执行", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable("ids") Long[] ids) {
        return toAjax(robotTaskService.deleteWithValidByIds(List.of(ids), true));
    }

    /**
     * 开始任务
     */
    @SaCheckPermission("manager:robotTask:start")
    @Log(title = "机器人任务执行", businessType = BusinessType.UPDATE)
    @PutMapping("/{taskId}/start")
    public R<Void> start(@NotNull(message = "主键不能为空") @PathVariable Long taskId) {
        return toAjax(robotTaskService.startTask(taskId));
    }

    /**
     * 取消任务
     */
    @SaCheckPermission("manager:robotTask:cancel")
    @Log(title = "机器人任务执行", businessType = BusinessType.UPDATE)
    @PutMapping("/{taskId}/cancel")
    public R<Void> cancel(@NotNull(message = "主键不能为空") @PathVariable Long taskId) {
        return toAjax(robotTaskService.cancelTask(taskId));
    }

    /**
     * 批量查询 Redis 运行态状态。
     */
    @SaCheckPermission("manager:robotTask:list")
    @GetMapping("/runtime/status")
    public R<List<RobotTaskRuntimeStatusVo>> runtimeStatus(@RequestParam("taskIds") Long[] taskIds) {
        return R.ok(robotTaskService.queryRuntimeStatus(Arrays.asList(taskIds)));
    }

    /**
     * 任务步骤上报。MQTT 消费器或 magic-api 可复用该入参调用 service。
     */
    @SaCheckPermission("manager:robotTask:edit")
    @PostMapping("/runtime/report")
    public R<Void> report(@Validated @RequestBody RobotTaskStepReportBo bo) {
        return toAjax(robotTaskService.reportStep(bo));
    }

    /**
     * 任务ID查车辆绑定。
     */
    @SaCheckPermission("manager:robotTask:query")
    @GetMapping("/runtime/vehicle/task-id/{taskId}")
    public R<RobotTaskVehicleBindVo> vehicleByTaskId(@PathVariable Long taskId) {
        return R.ok(robotTaskService.getVehicleByTaskId(taskId));
    }

    /**
     * 任务编号查车辆绑定。
     */
    @SaCheckPermission("manager:robotTask:query")
    @GetMapping("/runtime/vehicle/task-no/{taskNo}")
    public R<RobotTaskVehicleBindVo> vehicleByTaskNo(@PathVariable String taskNo) {
        return R.ok(robotTaskService.getVehicleByTaskNo(taskNo));
    }

    /**
     * 车辆ID查任务绑定。
     */
    @SaCheckPermission("manager:robotTask:query")
    @GetMapping("/runtime/task/vehicle-id/{vehicleId}")
    public R<RobotTaskVehicleBindVo> taskByVehicleId(@PathVariable Long vehicleId) {
        return R.ok(robotTaskService.getTaskByVehicleId(vehicleId));
    }

    /**
     * VIN查任务绑定。
     */
    @SaCheckPermission("manager:robotTask:query")
    @GetMapping("/runtime/task/vin/{vin}")
    public R<RobotTaskVehicleBindVo> taskByVin(@PathVariable String vin) {
        return R.ok(robotTaskService.getTaskByVin(vin));
    }

}
