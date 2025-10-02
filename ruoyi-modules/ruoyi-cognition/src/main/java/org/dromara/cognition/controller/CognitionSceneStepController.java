package org.dromara.cognition.controller;

import java.util.List;

import lombok.RequiredArgsConstructor;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.*;
import cn.dev33.satoken.annotation.SaCheckPermission;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.annotation.Validated;
import org.dromara.common.idempotent.annotation.RepeatSubmit;
import org.dromara.common.log.annotation.Log;
import org.dromara.common.web.core.BaseController;
import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.core.domain.R;
import org.dromara.common.core.validate.AddGroup;
import org.dromara.common.core.validate.EditGroup;
import org.dromara.common.log.enums.BusinessType;
import org.dromara.common.excel.utils.ExcelUtil;
import org.dromara.cognition.domain.vo.CognitionSceneStepVo;
import org.dromara.cognition.domain.bo.CognitionSceneStepBo;
import org.dromara.cognition.service.ICognitionSceneStepService;
import org.dromara.common.mybatis.core.page.TableDataInfo;

/**
 * 认知场景步骤 前端访问路由地址为:/cognition/sceneStep
 *
 * @author zhang
 * @date 2025-10-02
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/sceneStep")
public class CognitionSceneStepController extends BaseController {

    private final ICognitionSceneStepService cognitionSceneStepService;

    /**
     * 查询认知场景步骤列表
     */
    @SaCheckPermission("cognition:sceneStep:list")
    @GetMapping("/list")
    public TableDataInfo<CognitionSceneStepVo> list(CognitionSceneStepBo bo, PageQuery pageQuery) {
        return cognitionSceneStepService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出认知场景步骤列表
     */
    @SaCheckPermission("cognition:sceneStep:export")
    @Log(title = "认知场景步骤", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(CognitionSceneStepBo bo, HttpServletResponse response) {
        List<CognitionSceneStepVo> list = cognitionSceneStepService.queryList(bo);
        ExcelUtil.exportExcel(list, "认知场景步骤", CognitionSceneStepVo.class, response);
    }

    /**
     * 获取认知场景步骤详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("cognition:sceneStep:query")
    @GetMapping("/{id}")
    public R<CognitionSceneStepVo> getInfo(@NotNull(message = "主键不能为空") @PathVariable("id") Long id) {
        return R.ok(cognitionSceneStepService.queryById(id));
    }

    /**
     * 新增认知场景步骤
     */
    @SaCheckPermission("cognition:sceneStep:add")
    @Log(title = "认知场景步骤", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody CognitionSceneStepBo bo) {
        return toAjax(cognitionSceneStepService.insertByBo(bo));
    }

    /**
     * 修改认知场景步骤
     */
    @SaCheckPermission("cognition:sceneStep:edit")
    @Log(title = "认知场景步骤", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody CognitionSceneStepBo bo) {
        return toAjax(cognitionSceneStepService.updateByBo(bo));
    }

    /**
     * 修改认知场景步骤顺序
     */
    @SaCheckPermission("cognition:sceneStep:editList")
    @Log(title = "认知场景步骤", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping("/updateStepOrder")
    public R<Void> updateStepOrder(@RequestBody List<CognitionSceneStepBo> stepList) {
        return toAjax(cognitionSceneStepService.updateStepOrder(stepList));
    }

    /**
     * 删除认知场景步骤
     *
     * @param ids 主键串
     */
    @SaCheckPermission("cognition:sceneStep:remove")
    @Log(title = "认知场景步骤", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空") @PathVariable("ids") Long[] ids) {
        return toAjax(cognitionSceneStepService.deleteWithValidByIds(List.of(ids), true));
    }
}
