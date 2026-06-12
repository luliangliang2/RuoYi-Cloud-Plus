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
import org.dromara.manager.domain.bo.BizRobotActionBo;
import org.dromara.manager.domain.vo.BizRobotActionVo;
import org.dromara.manager.service.IBizRobotActionService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 机器人动作定义
 * 前端访问路由地址为:/manager/robotAction
 *
 * @author LionLi
 * @date 2026-06-12
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/robotAction")
public class BizRobotActionController extends BaseController {

    private final IBizRobotActionService robotActionService;

    /**
     * 查询机器人动作定义分页列表
     */
    @SaCheckPermission("manager:robotAction:list")
    @GetMapping("/list")
    public TableDataInfo<BizRobotActionVo> list(BizRobotActionBo bo, PageQuery pageQuery) {
        return robotActionService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出机器人动作定义列表
     */
    @SaCheckPermission("manager:robotAction:export")
    @Log(title = "机器人动作定义", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(BizRobotActionBo bo, HttpServletResponse response) {
        List<BizRobotActionVo> list = robotActionService.queryList(bo);
        ExcelUtil.exportExcel(list, "机器人动作定义数据", BizRobotActionVo.class, response);
    }

    /**
     * 获取机器人动作定义详细信息
     *
     * @param actionId 主键
     */
    @SaCheckPermission("manager:robotAction:query")
    @GetMapping("/{actionId}")
    public R<BizRobotActionVo> getInfo(@NotNull(message = "主键不能为空")
                                       @PathVariable("actionId") Long actionId) {
        return R.ok(robotActionService.queryById(actionId));
    }

    /**
     * 新增机器人动作定义
     */
    @SaCheckPermission("manager:robotAction:add")
    @Log(title = "机器人动作定义", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping
    public R<Void> add(@Validated(AddGroup.class) @RequestBody BizRobotActionBo bo) {
        return toAjax(robotActionService.insertByBo(bo));
    }

    /**
     * 修改机器人动作定义
     */
    @SaCheckPermission("manager:robotAction:edit")
    @Log(title = "机器人动作定义", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody BizRobotActionBo bo) {
        return toAjax(robotActionService.updateByBo(bo));
    }

    /**
     * 删除机器人动作定义
     *
     * @param ids 主键串
     */
    @SaCheckPermission("manager:robotAction:remove")
    @Log(title = "机器人动作定义", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable("ids") Long[] ids) {
        return toAjax(robotActionService.deleteWithValidByIds(List.of(ids), true));
    }

}
