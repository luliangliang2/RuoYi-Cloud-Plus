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
import org.dromara.common.web.core.BaseController;
import org.dromara.manager.domain.bo.BizTreeNodeBo;
import org.dromara.manager.domain.vo.BizTreeNodeVo;
import org.dromara.manager.service.IBizTreeNodeService;
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

import java.util.List;

/**
 * 维护树节点
 * 前端访问路由地址为:/manager/tree/node
 *
 * @author LionLi
 * @date 2026-05-22
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/tree/node")
public class BizTreeNodeController extends BaseController {

    private final IBizTreeNodeService treeNodeService;

    /**
     * 查询维护树节点列表
     */
    @SaCheckPermission("manager:treeNode:list")
    @GetMapping("/list")
    public R<List<BizTreeNodeVo>> list(BizTreeNodeBo bo) {
        return R.ok(treeNodeService.queryList(bo));
    }

    /**
     * 查询可选父节点列表
     */
    @SaCheckPermission("manager:treeNode:query")
    @GetMapping("/selectable")
    public R<List<BizTreeNodeVo>> selectable(@NotNull(message = "树ID不能为空")
                                             @RequestParam Long treeId,
                                             @RequestParam(required = false) Long nodeId) {
        return R.ok(treeNodeService.querySelectableList(treeId, nodeId));
    }

    /**
     * 导出维护树节点列表
     */
    @SaCheckPermission("manager:treeNode:export")
    @Log(title = "维护树节点", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(BizTreeNodeBo bo, HttpServletResponse response) {
        List<BizTreeNodeVo> list = treeNodeService.queryList(bo);
        ExcelUtil.exportExcel(list, "维护树节点数据", BizTreeNodeVo.class, response);
    }

    /**
     * 获取维护树节点详细信息
     *
     * @param nodeId 主键
     */
    @SaCheckPermission("manager:treeNode:query")
    @GetMapping("/{nodeId}")
    public R<BizTreeNodeVo> getInfo(@NotNull(message = "主键不能为空")
                                    @PathVariable("nodeId") Long nodeId) {
        return R.ok(treeNodeService.queryById(nodeId));
    }

    /**
     * 新增维护树节点
     */
    @SaCheckPermission("manager:treeNode:add")
    @Log(title = "维护树节点", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping
    public R<Void> add(@Validated(AddGroup.class) @RequestBody BizTreeNodeBo bo) {
        return toAjax(treeNodeService.insertByBo(bo));
    }

    /**
     * 修改维护树节点
     */
    @SaCheckPermission("manager:treeNode:edit")
    @Log(title = "维护树节点", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody BizTreeNodeBo bo) {
        return toAjax(treeNodeService.updateByBo(bo));
    }

    /**
     * 删除维护树节点
     *
     * @param ids 主键串
     */
    @SaCheckPermission("manager:treeNode:remove")
    @Log(title = "维护树节点", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable("ids") Long[] ids) {
        return toAjax(treeNodeService.deleteWithValidByIds(List.of(ids), true));
    }

}
