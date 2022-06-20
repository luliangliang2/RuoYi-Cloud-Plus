package com.project.admin.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.project.common.core.domain.R;
import com.project.common.core.validate.AddGroup;
import com.project.common.core.validate.EditGroup;
import com.project.common.core.validate.QueryGroup;
import com.project.common.core.web.controller.BaseController;
import com.project.common.excel.utils.ExcelUtil;
import com.project.common.log.annotation.Log;
import com.project.common.log.enums.BusinessType;
import com.project.common.mybatis.core.page.PageQuery;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import com.project.admin.domain.vo.WalletVo;
import com.project.admin.domain.bo.WalletBo;
import com.project.admin.service.IWalletService;
import com.project.common.mybatis.core.page.TableDataInfo;

import java.util.List;
import java.util.Arrays;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.servlet.http.HttpServletResponse;

/**
 * 用户钱包Controller
 * 前端访问路由地址为:/admin/wallet
 * @author project
 * @date 2022-06-19
 */
@Validated
@Api(value = "用户钱包控制器", tags = {"用户钱包管理"})
@RequiredArgsConstructor
@RestController
@RequestMapping("/wallet")
public class WalletController extends BaseController {

    private final IWalletService iWalletService;

    /**
     * 查询用户钱包列表
     */
    @ApiOperation("查询用户钱包列表")
    @SaCheckPermission("admin:wallet:list")
    @GetMapping("/list")
    public TableDataInfo<WalletVo> list(@Validated(QueryGroup.class) WalletBo bo, PageQuery pageQuery) {
        return iWalletService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出用户钱包列表
     */
    @ApiOperation("导出用户钱包列表")
    @SaCheckPermission("admin:wallet:export")
    @Log(title = "用户钱包", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(@Validated WalletBo bo, HttpServletResponse response) {
        List<WalletVo> list = iWalletService.queryList(bo);
        ExcelUtil.exportExcel(list, "用户钱包", WalletVo.class, response);
    }

    /**
     * 获取用户钱包详细信息
     */
    @ApiOperation("获取用户钱包详细信息")
    @SaCheckPermission("admin:wallet:query")
    @GetMapping("/{id}")
    public R<WalletVo> getInfo(@ApiParam("主键")
                                     @NotNull(message = "主键不能为空")
                                     @PathVariable("id") Long id) {
        return R.ok(iWalletService.queryById(id));
    }

    /**
     * 新增用户钱包
     */
    @ApiOperation("新增用户钱包")
    @SaCheckPermission("admin:wallet:add")
    @Log(title = "用户钱包", businessType = BusinessType.INSERT)
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody WalletBo bo) {
        return toAjax(iWalletService.insertByBo(bo) ? 1 : 0);
    }

    /**
     * 修改用户钱包
     */
    @ApiOperation("修改用户钱包")
    @SaCheckPermission("admin:wallet:edit")
    @Log(title = "用户钱包", businessType = BusinessType.UPDATE)
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody WalletBo bo) {
        return toAjax(iWalletService.updateByBo(bo) ? 1 : 0);
    }

    /**
     * 删除用户钱包
     */
    @ApiOperation("删除用户钱包")
    @SaCheckPermission("admin:wallet:remove")
    @Log(title = "用户钱包", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@ApiParam("主键串")
                          @NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(iWalletService.deleteWithValidByIds(Arrays.asList(ids), true) ? 1 : 0);
    }
}
