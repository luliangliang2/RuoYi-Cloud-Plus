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
import com.project.admin.domain.vo.UserOccupationLabelVo;
import com.project.admin.domain.bo.UserOccupationLabelBo;
import com.project.admin.service.IUserOccupationLabelService;
import com.project.common.mybatis.core.page.TableDataInfo;

import java.util.List;
import java.util.Arrays;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.servlet.http.HttpServletResponse;

/**
 * 职业标签Controller
 * 前端访问路由地址为:/admin/user-occupation-label
 * @author huan.li
 * @date 2022-06-16
 */
@Validated
@Api(value = "职业标签控制器", tags = {"职业标签管理"})
@RequiredArgsConstructor
@RestController
@RequestMapping("/user-occupation-label")
public class UserOccupationLabelController extends BaseController {

    private final IUserOccupationLabelService iUserOccupationLabelService;

    /**
     * 查询职业标签列表
     */
    @ApiOperation("查询职业标签列表")
    @SaCheckPermission("admin:user-occupation-label:list")
    @GetMapping("/list")
    public TableDataInfo<UserOccupationLabelVo> list(@Validated(QueryGroup.class) UserOccupationLabelBo bo, PageQuery pageQuery) {
        return iUserOccupationLabelService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出职业标签列表
     */
    @ApiOperation("导出职业标签列表")
    @SaCheckPermission("admin:user-occupation-label:export")
    @Log(title = "职业标签", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(@Validated UserOccupationLabelBo bo, HttpServletResponse response) {
        List<UserOccupationLabelVo> list = iUserOccupationLabelService.queryList(bo);
        ExcelUtil.exportExcel(list, "职业标签", UserOccupationLabelVo.class, response);
    }

    /**
     * 获取职业标签详细信息
     */
    @ApiOperation("获取职业标签详细信息")
    @SaCheckPermission("admin:user-occupation-label:query")
    @GetMapping("/{id}")
    public R<UserOccupationLabelVo> getInfo(@ApiParam("主键")
                                     @NotNull(message = "主键不能为空")
                                     @PathVariable("id") Long id) {
        return R.ok(iUserOccupationLabelService.queryById(id));
    }

    /**
     * 新增职业标签
     */
    @ApiOperation("新增职业标签")
    @SaCheckPermission("admin:user-occupation-label:add")
    @Log(title = "职业标签", businessType = BusinessType.INSERT)
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody UserOccupationLabelBo bo) {
        return toAjax(iUserOccupationLabelService.insertByBo(bo) ? 1 : 0);
    }

    /**
     * 修改职业标签
     */
    @ApiOperation("修改职业标签")
    @SaCheckPermission("admin:user-occupation-label:edit")
    @Log(title = "职业标签", businessType = BusinessType.UPDATE)
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody UserOccupationLabelBo bo) {
        return toAjax(iUserOccupationLabelService.updateByBo(bo) ? 1 : 0);
    }

    /**
     * 删除职业标签
     */
    @ApiOperation("删除职业标签")
    @SaCheckPermission("admin:user-occupation-label:remove")
    @Log(title = "职业标签", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@ApiParam("主键串")
                          @NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(iUserOccupationLabelService.deleteWithValidByIds(Arrays.asList(ids), true) ? 1 : 0);
    }
}
