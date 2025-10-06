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
import org.dromara.cognition.domain.vo.CognitionUserProgressVo;
import org.dromara.cognition.domain.bo.CognitionUserProgressBo;
import org.dromara.cognition.service.ICognitionUserProgressService;
import org.dromara.common.mybatis.core.page.TableDataInfo;

/**
 * 用户场景学习进度
 * 前端访问路由地址为:/cognition/userProgress
 *
 * @author zhang
 * @date 2025-10-03
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/userProgress")
public class CognitionUserProgressController extends BaseController {

    private final ICognitionUserProgressService cognitionUserProgressService;

    /**
     * 查询用户场景学习进度列表
     */
    @SaCheckPermission("cognition:userProgress:list")
    @GetMapping("/list")
    public TableDataInfo<CognitionUserProgressVo> list(CognitionUserProgressBo bo, PageQuery pageQuery) {
        return cognitionUserProgressService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出用户场景学习进度列表
     */
    @SaCheckPermission("cognition:userProgress:export")
    @Log(title = "用户场景学习进度", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(CognitionUserProgressBo bo, HttpServletResponse response) {
        List<CognitionUserProgressVo> list = cognitionUserProgressService.queryList(bo);
        ExcelUtil.exportExcel(list, "用户场景学习进度", CognitionUserProgressVo.class, response);
    }

    /**
     * 获取用户场景学习进度详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("cognition:userProgress:query")
    @GetMapping("/{id}")
    public R<CognitionUserProgressVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable("id") Long id) {
        return R.ok(cognitionUserProgressService.queryById(id));
    }

    /**
     * 新增用户场景学习进度
     */
    @SaCheckPermission("cognition:userProgress:add")
    @Log(title = "用户场景学习进度", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody CognitionUserProgressBo bo) {
        return toAjax(cognitionUserProgressService.insertByBo(bo));
    }

    /**
     * 修改用户场景学习进度
     */
    @SaCheckPermission("cognition:userProgress:edit")
    @Log(title = "用户场景学习进度", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody CognitionUserProgressBo bo) {
        return toAjax(cognitionUserProgressService.updateByBo(bo));
    }

    /**
     * 修改用户场景学习进度
     */
    @SaCheckPermission("cognition:userProgress:edit")
    @Log(title = "用户场景学习进度", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping("/changeUserProgress")
    public R<Void> changeUserProgress(@RequestBody CognitionUserProgressBo bo) {
        return toAjax(cognitionUserProgressService.changeUserProgress(bo));
    }

    /**
     * 删除用户场景学习进度
     *
     * @param ids 主键串
     */
    @SaCheckPermission("cognition:userProgress:remove")
    @Log(title = "用户场景学习进度", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable("ids") Long[] ids) {
        return toAjax(cognitionUserProgressService.deleteWithValidByIds(List.of(ids), true));
    }
}
