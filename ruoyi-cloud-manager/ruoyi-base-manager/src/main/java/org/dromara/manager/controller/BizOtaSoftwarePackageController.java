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
import org.dromara.manager.domain.bo.BizOtaSoftwarePackageBo;
import org.dromara.manager.domain.vo.BizOtaSoftwarePackageVo;
import org.dromara.manager.service.IBizOtaSoftwarePackageService;
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
 * OTA软件包
 * 前端访问路由地址为:/manager/ota/software
 *
 * @author LionLi
 * @date 2026-05-24
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/ota/software")
public class BizOtaSoftwarePackageController extends BaseController {

    private final IBizOtaSoftwarePackageService otaSoftwarePackageService;

    /**
     * 查询OTA软件包分页列表
     */
    @SaCheckPermission("manager:otaSoftware:list")
    @GetMapping("/list")
    public TableDataInfo<BizOtaSoftwarePackageVo> list(BizOtaSoftwarePackageBo bo, PageQuery pageQuery) {
        return otaSoftwarePackageService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出OTA软件包列表
     */
    @SaCheckPermission("manager:otaSoftware:export")
    @Log(title = "OTA软件包", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(BizOtaSoftwarePackageBo bo, HttpServletResponse response) {
        List<BizOtaSoftwarePackageVo> list = otaSoftwarePackageService.queryList(bo);
        ExcelUtil.exportExcel(list, "OTA软件包数据", BizOtaSoftwarePackageVo.class, response);
    }

    /**
     * 获取OTA软件包详细信息
     *
     * @param packageId 主键
     */
    @SaCheckPermission("manager:otaSoftware:query")
    @GetMapping("/{packageId}")
    public R<BizOtaSoftwarePackageVo> getInfo(@NotNull(message = "主键不能为空")
                                              @PathVariable("packageId") Long packageId) {
        return R.ok(otaSoftwarePackageService.queryById(packageId));
    }

    /**
     * 新增OTA软件包
     */
    @SaCheckPermission("manager:otaSoftware:add")
    @Log(title = "OTA软件包", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping
    public R<Void> add(@Validated(AddGroup.class) @RequestBody BizOtaSoftwarePackageBo bo) {
        return toAjax(otaSoftwarePackageService.insertByBo(bo));
    }

    /**
     * 修改OTA软件包
     */
    @SaCheckPermission("manager:otaSoftware:edit")
    @Log(title = "OTA软件包", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody BizOtaSoftwarePackageBo bo) {
        return toAjax(otaSoftwarePackageService.updateByBo(bo));
    }

    /**
     * 删除OTA软件包
     *
     * @param ids 主键串
     */
    @SaCheckPermission("manager:otaSoftware:remove")
    @Log(title = "OTA软件包", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable("ids") Long[] ids) {
        return toAjax(otaSoftwarePackageService.deleteWithValidByIds(List.of(ids), true));
    }

}
