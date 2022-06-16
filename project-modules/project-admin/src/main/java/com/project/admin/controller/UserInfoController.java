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
import com.project.admin.domain.vo.UserInfoVo;
import com.project.admin.domain.bo.UserInfoBo;
import com.project.admin.service.IUserInfoService;
import com.project.common.mybatis.core.page.TableDataInfo;

import java.util.List;
import java.util.Arrays;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.servlet.http.HttpServletResponse;

/**
 * 用户信息Controller
 * 前端访问路由地址为:/admin/user-info
 * @author huan.li
 * @date 2022-06-16
 */
@Validated
@Api(value = "用户信息控制器", tags = {"用户信息管理"})
@RequiredArgsConstructor
@RestController
@RequestMapping("/user-info")
public class UserInfoController extends BaseController {

    private final IUserInfoService iUserInfoService;

    /**
     * 查询用户信息列表
     */
    @ApiOperation("查询用户信息列表")
    @SaCheckPermission("admin:user-info:list")
    @GetMapping("/list")
    public TableDataInfo<UserInfoVo> list(@Validated(QueryGroup.class) UserInfoBo bo, PageQuery pageQuery) {
        return iUserInfoService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出用户信息列表
     */
    @ApiOperation("导出用户信息列表")
    @SaCheckPermission("admin:user-info:export")
    @Log(title = "用户信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(@Validated UserInfoBo bo, HttpServletResponse response) {
        List<UserInfoVo> list = iUserInfoService.queryList(bo);
        ExcelUtil.exportExcel(list, "用户信息", UserInfoVo.class, response);
    }

    /**
     * 获取用户信息详细信息
     */
    @ApiOperation("获取用户信息详细信息")
    @SaCheckPermission("admin:user-info:query")
    @GetMapping("/{id}")
    public R<UserInfoVo> getInfo(@ApiParam("主键")
                                     @NotNull(message = "主键不能为空")
                                     @PathVariable("id") Long id) {
        return R.ok(iUserInfoService.queryById(id));
    }

    /**
     * 新增用户信息
     */
    @ApiOperation("新增用户信息")
    @SaCheckPermission("admin:user-info:add")
    @Log(title = "用户信息", businessType = BusinessType.INSERT)
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody UserInfoBo bo) {
        return toAjax(iUserInfoService.insertByBo(bo) ? 1 : 0);
    }

    /**
     * 修改用户信息
     */
    @ApiOperation("修改用户信息")
    @SaCheckPermission("admin:user-info:edit")
    @Log(title = "用户信息", businessType = BusinessType.UPDATE)
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody UserInfoBo bo) {
        return toAjax(iUserInfoService.updateByBo(bo) ? 1 : 0);
    }

    /**
     * 删除用户信息
     */
    @ApiOperation("删除用户信息")
    @SaCheckPermission("admin:user-info:remove")
    @Log(title = "用户信息", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@ApiParam("主键串")
                          @NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(iUserInfoService.deleteWithValidByIds(Arrays.asList(ids), true) ? 1 : 0);
    }
}
