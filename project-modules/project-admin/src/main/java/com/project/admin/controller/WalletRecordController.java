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
import com.project.admin.domain.vo.WalletRecordVo;
import com.project.admin.domain.bo.WalletRecordBo;
import com.project.admin.service.IWalletRecordService;
import com.project.common.mybatis.core.page.TableDataInfo;

import java.util.List;
import java.util.Arrays;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.servlet.http.HttpServletResponse;

/**
 * 钱包交易记录Controller
 * 前端访问路由地址为:/admin/record
 * @author project
 * @date 2022-06-20
 */
@Validated
@Api(value = "钱包交易记录控制器", tags = {"钱包交易记录管理"})
@RequiredArgsConstructor
@RestController
@RequestMapping("/record")
public class WalletRecordController extends BaseController {

    private final IWalletRecordService iWalletRecordService;

    /**
     * 查询钱包交易记录列表
     */
    @ApiOperation("查询钱包交易记录列表")
    @SaCheckPermission("admin:record:list")
    @GetMapping("/list")
    public TableDataInfo<WalletRecordVo> list(@Validated(QueryGroup.class) WalletRecordBo bo, PageQuery pageQuery) {
        return iWalletRecordService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出钱包交易记录列表
     */
    @ApiOperation("导出钱包交易记录列表")
    @SaCheckPermission("admin:record:export")
    @Log(title = "钱包交易记录", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(@Validated WalletRecordBo bo, HttpServletResponse response) {
        List<WalletRecordVo> list = iWalletRecordService.queryList(bo);
        ExcelUtil.exportExcel(list, "钱包交易记录", WalletRecordVo.class, response);
    }

    /**
     * 获取钱包交易记录详细信息
     */
    @ApiOperation("获取钱包交易记录详细信息")
    @SaCheckPermission("admin:record:query")
    @GetMapping("/{id}")
    public R<WalletRecordVo> getInfo(@ApiParam("主键")
                                     @NotNull(message = "主键不能为空")
                                     @PathVariable("id") Long id) {
        return R.ok(iWalletRecordService.queryById(id));
    }

    /**
     * 新增钱包交易记录
     */
    @ApiOperation("新增钱包交易记录")
    @SaCheckPermission("admin:record:add")
    @Log(title = "钱包交易记录", businessType = BusinessType.INSERT)
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody WalletRecordBo bo) {
        return toAjax(iWalletRecordService.insertByBo(bo) ? 1 : 0);
    }

    /**
     * 修改钱包交易记录
     */
    @ApiOperation("修改钱包交易记录")
    @SaCheckPermission("admin:record:edit")
    @Log(title = "钱包交易记录", businessType = BusinessType.UPDATE)
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody WalletRecordBo bo) {
        return toAjax(iWalletRecordService.updateByBo(bo) ? 1 : 0);
    }

    /**
     * 删除钱包交易记录
     */
    @ApiOperation("删除钱包交易记录")
    @SaCheckPermission("admin:record:remove")
    @Log(title = "钱包交易记录", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@ApiParam("主键串")
                          @NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(iWalletRecordService.deleteWithValidByIds(Arrays.asList(ids), true) ? 1 : 0);
    }
}
