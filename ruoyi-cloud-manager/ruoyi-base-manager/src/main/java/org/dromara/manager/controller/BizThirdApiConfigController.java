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
import org.dromara.manager.domain.bo.BizThirdApiConfigBo;
import org.dromara.manager.domain.vo.BizThirdApiConfigVo;
import org.dromara.manager.service.IBizThirdApiConfigService;
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
 * 第三方API配置
 * 前端访问路由地址为:/manager/thirdApi
 *
 * @author LionLi
 * @date 2026-05-22
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/thirdApi")
public class BizThirdApiConfigController extends BaseController {

    private final IBizThirdApiConfigService thirdApiConfigService;

    /**
     * 查询第三方API配置分页列表
     */
    @SaCheckPermission("manager:thirdApi:list")
    @GetMapping("/list")
    public TableDataInfo<BizThirdApiConfigVo> list(BizThirdApiConfigBo bo, PageQuery pageQuery) {
        return thirdApiConfigService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出第三方API配置列表
     */
    @SaCheckPermission("manager:thirdApi:export")
    @Log(title = "第三方API配置", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(BizThirdApiConfigBo bo, HttpServletResponse response) {
        List<BizThirdApiConfigVo> list = thirdApiConfigService.queryList(bo);
        ExcelUtil.exportExcel(list, "第三方API配置数据", BizThirdApiConfigVo.class, response);
    }

    /**
     * 获取第三方API配置详细信息
     *
     * @param configId 主键
     */
    @SaCheckPermission("manager:thirdApi:query")
    @GetMapping("/{configId}")
    public R<BizThirdApiConfigVo> getInfo(@NotNull(message = "主键不能为空")
                                          @PathVariable("configId") Long configId) {
        return R.ok(thirdApiConfigService.queryById(configId));
    }

    /**
     * 新增第三方API配置
     */
    @SaCheckPermission("manager:thirdApi:add")
    @Log(title = "第三方API配置", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping
    public R<Void> add(@Validated(AddGroup.class) @RequestBody BizThirdApiConfigBo bo) {
        return toAjax(thirdApiConfigService.insertByBo(bo));
    }

    /**
     * 修改第三方API配置
     */
    @SaCheckPermission("manager:thirdApi:edit")
    @Log(title = "第三方API配置", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody BizThirdApiConfigBo bo) {
        return toAjax(thirdApiConfigService.updateByBo(bo));
    }

    /**
     * 删除第三方API配置
     *
     * @param ids 主键串
     */
    @SaCheckPermission("manager:thirdApi:remove")
    @Log(title = "第三方API配置", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable("ids") Long[] ids) {
        return toAjax(thirdApiConfigService.deleteWithValidByIds(List.of(ids), true));
    }

}
