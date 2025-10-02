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
import org.dromara.cognition.domain.vo.CognitionSceneVo;
import org.dromara.cognition.domain.bo.CognitionSceneBo;
import org.dromara.cognition.service.ICognitionSceneService;
import org.dromara.common.mybatis.core.page.TableDataInfo;

/**
 * 认知场景
 * 前端访问路由地址为:/cognition/scene
 *
 * @author zhang
 * @date 2025-10-02
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/scene")
public class CognitionSceneController extends BaseController {

    private final ICognitionSceneService cognitionSceneService;

    /**
     * 查询认知场景列表
     */
    @SaCheckPermission("cognition:scene:list")
    @GetMapping("/list")
    public TableDataInfo<CognitionSceneVo> list(CognitionSceneBo bo, PageQuery pageQuery) {
        return cognitionSceneService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出认知场景列表
     */
    @SaCheckPermission("cognition:scene:export")
    @Log(title = "认知场景", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(CognitionSceneBo bo, HttpServletResponse response) {
        List<CognitionSceneVo> list = cognitionSceneService.queryList(bo);
        ExcelUtil.exportExcel(list, "认知场景", CognitionSceneVo.class, response);
    }

    /**
     * 获取认知场景详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("cognition:scene:query")
    @GetMapping("/{id}")
    public R<CognitionSceneVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable("id") Long id) {
        return R.ok(cognitionSceneService.queryById(id));
    }

    /**
     * 新增认知场景
     */
    @SaCheckPermission("cognition:scene:add")
    @Log(title = "认知场景", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody CognitionSceneBo bo) {
        return toAjax(cognitionSceneService.insertByBo(bo));
    }

    /**
     * 修改认知场景
     */
    @SaCheckPermission("cognition:scene:edit")
    @Log(title = "认知场景", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody CognitionSceneBo bo) {
        return toAjax(cognitionSceneService.updateByBo(bo));
    }

    /**
     * 删除认知场景
     *
     * @param ids 主键串
     */
    @SaCheckPermission("cognition:scene:remove")
    @Log(title = "认知场景", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable("ids") Long[] ids) {
        return toAjax(cognitionSceneService.deleteWithValidByIds(List.of(ids), true));
    }
}
