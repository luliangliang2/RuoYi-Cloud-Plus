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
import com.project.admin.domain.vo.UserInterestedToMeVo;
import com.project.admin.domain.bo.UserInterestedToMeBo;
import com.project.admin.service.IUserInterestedToMeService;
import com.project.common.mybatis.core.page.TableDataInfo;

import java.util.List;
import java.util.Arrays;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.servlet.http.HttpServletResponse;

/**
 * 对我感兴趣Controller
 * 前端访问路由地址为:/admin/user-interested-to-me
 * @author huan.li
 * @date 2022-06-16
 */
@Validated
@Api(value = "对我感兴趣控制器", tags = {"对我感兴趣管理"})
@RequiredArgsConstructor
@RestController
@RequestMapping("/user-interested-to-me")
public class UserInterestedToMeController extends BaseController {

    private final IUserInterestedToMeService iUserInterestedToMeService;

    /**
     * 查询对我感兴趣列表
     */
    @ApiOperation("查询对我感兴趣列表")
    @SaCheckPermission("admin:user-interested-to-me:list")
    @GetMapping("/list")
    public TableDataInfo<UserInterestedToMeVo> list(@Validated(QueryGroup.class) UserInterestedToMeBo bo, PageQuery pageQuery) {
        return iUserInterestedToMeService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出对我感兴趣列表
     */
    @ApiOperation("导出对我感兴趣列表")
    @SaCheckPermission("admin:user-interested-to-me:export")
    @Log(title = "对我感兴趣", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(@Validated UserInterestedToMeBo bo, HttpServletResponse response) {
        List<UserInterestedToMeVo> list = iUserInterestedToMeService.queryList(bo);
        ExcelUtil.exportExcel(list, "对我感兴趣", UserInterestedToMeVo.class, response);
    }

    /**
     * 获取对我感兴趣详细信息
     */
    @ApiOperation("获取对我感兴趣详细信息")
    @SaCheckPermission("admin:user-interested-to-me:query")
    @GetMapping("/{id}")
    public R<UserInterestedToMeVo> getInfo(@ApiParam("主键")
                                     @NotNull(message = "主键不能为空")
                                     @PathVariable("id") Long id) {
        return R.ok(iUserInterestedToMeService.queryById(id));
    }

    /**
     * 新增对我感兴趣
     */
    @ApiOperation("新增对我感兴趣")
    @SaCheckPermission("admin:user-interested-to-me:add")
    @Log(title = "对我感兴趣", businessType = BusinessType.INSERT)
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody UserInterestedToMeBo bo) {
        return toAjax(iUserInterestedToMeService.insertByBo(bo) ? 1 : 0);
    }

    /**
     * 修改对我感兴趣
     */
    @ApiOperation("修改对我感兴趣")
    @SaCheckPermission("admin:user-interested-to-me:edit")
    @Log(title = "对我感兴趣", businessType = BusinessType.UPDATE)
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody UserInterestedToMeBo bo) {
        return toAjax(iUserInterestedToMeService.updateByBo(bo) ? 1 : 0);
    }

    /**
     * 删除对我感兴趣
     */
    @ApiOperation("删除对我感兴趣")
    @SaCheckPermission("admin:user-interested-to-me:remove")
    @Log(title = "对我感兴趣", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@ApiParam("主键串")
                          @NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(iUserInterestedToMeService.deleteWithValidByIds(Arrays.asList(ids), true) ? 1 : 0);
    }
}
