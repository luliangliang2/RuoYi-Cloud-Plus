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
import org.dromara.manager.domain.bo.BizTreeDefBo;
import org.dromara.manager.domain.vo.BizTreeDefVo;
import org.dromara.manager.service.IBizTreeDefService;
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
 * 维护树定义
 * 前端访问路由地址为:/manager/tree
 *
 * @author LionLi
 * @date 2026-05-22
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/tree")
public class BizTreeDefController extends BaseController {

    private final IBizTreeDefService treeDefService;

    /**
     * 查询维护树定义分页列表
     */
    @SaCheckPermission("manager:tree:list")
    @GetMapping("/list")
    public TableDataInfo<BizTreeDefVo> list(BizTreeDefBo bo, PageQuery pageQuery) {
        return treeDefService.queryPageList(bo, pageQuery);
    }

    /**
     * 查询维护树定义下拉列表
     */
    @SaCheckPermission("manager:tree:list")
    @GetMapping("/options")
    public R<List<BizTreeDefVo>> options(BizTreeDefBo bo) {
        return R.ok(treeDefService.queryList(bo));
    }

    /**
     * 导出维护树定义列表
     */
    @SaCheckPermission("manager:tree:export")
    @Log(title = "维护树管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(BizTreeDefBo bo, HttpServletResponse response) {
        List<BizTreeDefVo> list = treeDefService.queryList(bo);
        ExcelUtil.exportExcel(list, "维护树数据", BizTreeDefVo.class, response);
    }

    /**
     * 获取维护树定义详细信息
     *
     * @param treeId 主键
     */
    @SaCheckPermission("manager:tree:query")
    @GetMapping("/{treeId}")
    public R<BizTreeDefVo> getInfo(@NotNull(message = "主键不能为空")
                                   @PathVariable("treeId") Long treeId) {
        return R.ok(treeDefService.queryById(treeId));
    }

    /**
     * 新增维护树定义
     */
    @SaCheckPermission("manager:tree:add")
    @Log(title = "维护树管理", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping
    public R<Void> add(@Validated(AddGroup.class) @RequestBody BizTreeDefBo bo) {
        return toAjax(treeDefService.insertByBo(bo));
    }

    /**
     * 修改维护树定义
     */
    @SaCheckPermission("manager:tree:edit")
    @Log(title = "维护树管理", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody BizTreeDefBo bo) {
        return toAjax(treeDefService.updateByBo(bo));
    }

    /**
     * 删除维护树定义
     *
     * @param ids 主键串
     */
    @SaCheckPermission("manager:tree:remove")
    @Log(title = "维护树管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable("ids") Long[] ids) {
        return toAjax(treeDefService.deleteWithValidByIds(List.of(ids), true));
    }

}
