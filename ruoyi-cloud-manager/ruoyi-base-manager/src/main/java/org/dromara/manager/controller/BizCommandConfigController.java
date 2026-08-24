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
import org.dromara.manager.domain.bo.BizCommandConfigBo;
import org.dromara.manager.domain.vo.BizCommandConfigVo;
import org.dromara.manager.service.IBizCommandConfigService;
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
 * 指令配置
 * 前端访问路由地址为:/manager/commandConfig
 *
 * @author LionLi
 * @date 2026-05-23
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/commandConfig")
public class BizCommandConfigController extends BaseController {

    private final IBizCommandConfigService commandConfigService;

    /**
     * 查询指令配置分页列表
     */
    @SaCheckPermission("manager:commandConfig:list")
    @GetMapping("/list")
    public TableDataInfo<BizCommandConfigVo> list(BizCommandConfigBo bo, PageQuery pageQuery) {
        return commandConfigService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出指令配置列表
     */
    @SaCheckPermission("manager:commandConfig:export")
    @Log(title = "指令配置", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(BizCommandConfigBo bo, HttpServletResponse response) {
        List<BizCommandConfigVo> list = commandConfigService.queryList(bo);
        ExcelUtil.exportExcel(list, "指令配置数据", BizCommandConfigVo.class, response);
    }

    /**
     * 获取指令配置详细信息
     *
     * @param commandId 主键
     */
    @SaCheckPermission("manager:commandConfig:query")
    @GetMapping("/{commandId}")
    public R<BizCommandConfigVo> getInfo(@NotNull(message = "主键不能为空")
                                         @PathVariable("commandId") Long commandId) {
        return R.ok(commandConfigService.queryById(commandId));
    }

    /**
     * 新增指令配置
     */
    @SaCheckPermission("manager:commandConfig:add")
    @Log(title = "指令配置", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping
    public R<Void> add(@Validated(AddGroup.class) @RequestBody BizCommandConfigBo bo) {
        return toAjax(commandConfigService.insertByBo(bo));
    }

    /**
     * 修改指令配置
     */
    @SaCheckPermission("manager:commandConfig:edit")
    @Log(title = "指令配置", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody BizCommandConfigBo bo) {
        return toAjax(commandConfigService.updateByBo(bo));
    }

    /**
     * 删除指令配置
     *
     * @param ids 主键串
     */
    @SaCheckPermission("manager:commandConfig:remove")
    @Log(title = "指令配置", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable("ids") Long[] ids) {
        return toAjax(commandConfigService.deleteWithValidByIds(List.of(ids), true));
    }

}
