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
import com.project.admin.domain.vo.WalletLogVo;
import com.project.admin.domain.bo.WalletLogBo;
import com.project.admin.service.IWalletLogService;
import com.project.common.mybatis.core.page.TableDataInfo;

import java.util.List;
import java.util.Arrays;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.servlet.http.HttpServletResponse;

/**
 * 钱包变动日志Controller
 * 前端访问路由地址为:/admin/log
 * @author project
 * @date 2022-06-20
 */
@Validated
@Api(value = "钱包变动日志控制器", tags = {"钱包变动日志管理"})
@RequiredArgsConstructor
@RestController
@RequestMapping("/log")
public class WalletLogController extends BaseController {

    private final IWalletLogService iWalletLogService;

    /**
     * 查询钱包变动日志列表
     */
    @ApiOperation("查询钱包变动日志列表")
    @SaCheckPermission("admin:log:list")
    @GetMapping("/list")
    public TableDataInfo<WalletLogVo> list(@Validated(QueryGroup.class) WalletLogBo bo, PageQuery pageQuery) {
        return iWalletLogService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出钱包变动日志列表
     */
    @ApiOperation("导出钱包变动日志列表")
    @SaCheckPermission("admin:log:export")
    @Log(title = "钱包变动日志", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(@Validated WalletLogBo bo, HttpServletResponse response) {
        List<WalletLogVo> list = iWalletLogService.queryList(bo);
        ExcelUtil.exportExcel(list, "钱包变动日志", WalletLogVo.class, response);
    }

    /**
     * 获取钱包变动日志详细信息
     */
    @ApiOperation("获取钱包变动日志详细信息")
    @SaCheckPermission("admin:log:query")
    @GetMapping("/{id}")
    public R<WalletLogVo> getInfo(@ApiParam("主键")
                                     @NotNull(message = "主键不能为空")
                                     @PathVariable("id") Long id) {
        return R.ok(iWalletLogService.queryById(id));
    }

    /**
     * 新增钱包变动日志
     */
    @ApiOperation("新增钱包变动日志")
    @SaCheckPermission("admin:log:add")
    @Log(title = "钱包变动日志", businessType = BusinessType.INSERT)
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody WalletLogBo bo) {
        return toAjax(iWalletLogService.insertByBo(bo) ? 1 : 0);
    }

    /**
     * 修改钱包变动日志
     */
    @ApiOperation("修改钱包变动日志")
    @SaCheckPermission("admin:log:edit")
    @Log(title = "钱包变动日志", businessType = BusinessType.UPDATE)
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody WalletLogBo bo) {
        return toAjax(iWalletLogService.updateByBo(bo) ? 1 : 0);
    }

    /**
     * 删除钱包变动日志
     */
    @ApiOperation("删除钱包变动日志")
    @SaCheckPermission("admin:log:remove")
    @Log(title = "钱包变动日志", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@ApiParam("主键串")
                          @NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(iWalletLogService.deleteWithValidByIds(Arrays.asList(ids), true) ? 1 : 0);
    }
}
