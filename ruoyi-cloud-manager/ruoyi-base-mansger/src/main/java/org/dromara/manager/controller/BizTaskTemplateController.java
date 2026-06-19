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
import org.dromara.manager.domain.bo.BizTaskTemplateBo;
import org.dromara.manager.domain.vo.BizScenePointVo;
import org.dromara.manager.domain.vo.BizTaskTemplateVo;
import org.dromara.manager.service.IBizTaskTemplateService;
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
import java.util.Map;

/**
 * 任务模板
 * 前端访问路由地址为:/manager/taskTemplate
 *
 * @author LionLi
 * @date 2026-06-12
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/taskTemplate")
public class BizTaskTemplateController extends BaseController {

    private final IBizTaskTemplateService taskTemplateService;

    /**
     * 查询任务模板分页列表
     */
    @SaCheckPermission("manager:taskTemplate:list")
    @GetMapping("/list")
    public TableDataInfo<BizTaskTemplateVo> list(BizTaskTemplateBo bo, PageQuery pageQuery) {
        return taskTemplateService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出任务模板列表
     */
    @SaCheckPermission("manager:taskTemplate:export")
    @Log(title = "任务模板", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(BizTaskTemplateBo bo, HttpServletResponse response) {
        List<BizTaskTemplateVo> list = taskTemplateService.queryList(bo);
        ExcelUtil.exportExcel(list, "任务模板数据", BizTaskTemplateVo.class, response);
    }

    /**
     * 获取任务模板详细信息
     *
     * @param templateId 主键
     */
    @SaCheckPermission("manager:taskTemplate:query")
    @GetMapping("/{templateId}")
    public R<BizTaskTemplateVo> getInfo(@NotNull(message = "主键不能为空")
                                        @PathVariable("templateId") Long templateId) {
        return R.ok(taskTemplateService.queryById(templateId));
    }

    /**
     * 查询路线点位
     *
     * @param routeId 路线ID
     */
    @SaCheckPermission("manager:taskTemplate:query")
    @GetMapping("/route/{routeId}/points")
    public R<List<BizScenePointVo>> routePoints(@NotNull(message = "路线ID不能为空")
                                                @PathVariable("routeId") Long routeId) {
        return R.ok(taskTemplateService.queryRoutePoints(routeId));
    }

    /**
     * 预览任务模板下发指令
     *
     * @param templateId 主键
     */
    @SaCheckPermission("manager:taskTemplate:preview")
    @GetMapping("/{templateId}/command/preview")
    public R<Map<String, Object>> previewCommand(@NotNull(message = "主键不能为空")
                                                 @PathVariable("templateId") Long templateId) {
        return R.ok(taskTemplateService.previewCommand(templateId));
    }

    /**
     * 新增任务模板
     */
    @SaCheckPermission("manager:taskTemplate:add")
    @Log(title = "任务模板", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping
    public R<Void> add(@Validated(AddGroup.class) @RequestBody BizTaskTemplateBo bo) {
        return toAjax(taskTemplateService.insertByBo(bo));
    }

    /**
     * 修改任务模板
     */
    @SaCheckPermission("manager:taskTemplate:edit")
    @Log(title = "任务模板", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody BizTaskTemplateBo bo) {
        return toAjax(taskTemplateService.updateByBo(bo));
    }

    /**
     * 删除任务模板
     *
     * @param ids 主键串
     */
    @SaCheckPermission("manager:taskTemplate:remove")
    @Log(title = "任务模板", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable("ids") Long[] ids) {
        return toAjax(taskTemplateService.deleteWithValidByIds(List.of(ids), true));
    }

}
