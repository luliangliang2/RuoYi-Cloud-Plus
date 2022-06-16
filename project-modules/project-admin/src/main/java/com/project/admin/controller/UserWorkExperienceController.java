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
import com.project.admin.domain.vo.UserWorkExperienceVo;
import com.project.admin.domain.bo.UserWorkExperienceBo;
import com.project.admin.service.IUserWorkExperienceService;
import com.project.common.mybatis.core.page.TableDataInfo;

import java.util.List;
import java.util.Arrays;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.servlet.http.HttpServletResponse;

/**
 * 工作经历Controller
 * 前端访问路由地址为:/admin/user-work-experience
 * @author huan.li
 * @date 2022-06-16
 */
@Validated
@Api(value = "工作经历控制器", tags = {"工作经历管理"})
@RequiredArgsConstructor
@RestController
@RequestMapping("/user-work-experience")
public class UserWorkExperienceController extends BaseController {

    private final IUserWorkExperienceService iUserWorkExperienceService;

    /**
     * 查询工作经历列表
     */
    @ApiOperation("查询工作经历列表")
    @SaCheckPermission("admin:user-work-experience:list")
    @GetMapping("/list")
    public TableDataInfo<UserWorkExperienceVo> list(@Validated(QueryGroup.class) UserWorkExperienceBo bo, PageQuery pageQuery) {
        return iUserWorkExperienceService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出工作经历列表
     */
    @ApiOperation("导出工作经历列表")
    @SaCheckPermission("admin:user-work-experience:export")
    @Log(title = "工作经历", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(@Validated UserWorkExperienceBo bo, HttpServletResponse response) {
        List<UserWorkExperienceVo> list = iUserWorkExperienceService.queryList(bo);
        ExcelUtil.exportExcel(list, "工作经历", UserWorkExperienceVo.class, response);
    }

    /**
     * 获取工作经历详细信息
     */
    @ApiOperation("获取工作经历详细信息")
    @SaCheckPermission("admin:user-work-experience:query")
    @GetMapping("/{id}")
    public R<UserWorkExperienceVo> getInfo(@ApiParam("主键")
                                     @NotNull(message = "主键不能为空")
                                     @PathVariable("id") Long id) {
        return R.ok(iUserWorkExperienceService.queryById(id));
    }

    /**
     * 新增工作经历
     */
    @ApiOperation("新增工作经历")
    @SaCheckPermission("admin:user-work-experience:add")
    @Log(title = "工作经历", businessType = BusinessType.INSERT)
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody UserWorkExperienceBo bo) {
        return toAjax(iUserWorkExperienceService.insertByBo(bo) ? 1 : 0);
    }

    /**
     * 修改工作经历
     */
    @ApiOperation("修改工作经历")
    @SaCheckPermission("admin:user-work-experience:edit")
    @Log(title = "工作经历", businessType = BusinessType.UPDATE)
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody UserWorkExperienceBo bo) {
        return toAjax(iUserWorkExperienceService.updateByBo(bo) ? 1 : 0);
    }

    /**
     * 删除工作经历
     */
    @ApiOperation("删除工作经历")
    @SaCheckPermission("admin:user-work-experience:remove")
    @Log(title = "工作经历", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@ApiParam("主键串")
                          @NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(iUserWorkExperienceService.deleteWithValidByIds(Arrays.asList(ids), true) ? 1 : 0);
    }
}
