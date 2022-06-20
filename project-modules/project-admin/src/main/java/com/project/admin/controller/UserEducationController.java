//package com.project.admin.controller;
//
//import cn.dev33.satoken.annotation.SaCheckPermission;
//import com.project.common.core.domain.R;
//import com.project.common.core.validate.AddGroup;
//import com.project.common.core.validate.EditGroup;
//import com.project.common.core.validate.QueryGroup;
//import com.project.common.core.web.controller.BaseController;
//import com.project.common.excel.utils.ExcelUtil;
//import com.project.common.log.annotation.Log;
//import com.project.common.log.enums.BusinessType;
//import com.project.common.mybatis.core.page.PageQuery;
//import io.swagger.annotations.Api;
//import io.swagger.annotations.ApiOperation;
//import io.swagger.annotations.ApiParam;
//import lombok.RequiredArgsConstructor;
//import org.springframework.validation.annotation.Validated;
//import org.springframework.web.bind.annotation.*;
//import com.project.admin.domain.vo.UserEducationVo;
//import com.project.admin.domain.bo.UserEducationBo;
//import com.project.admin.service.IUserEducationService;
//import com.project.common.mybatis.core.page.TableDataInfo;
//
//import java.util.List;
//import java.util.Arrays;
//import javax.validation.constraints.NotEmpty;
//import javax.validation.constraints.NotNull;
//import javax.servlet.http.HttpServletResponse;
//
///**
// * 学历Controller
// * 前端访问路由地址为:/admin/user-education
// * @author huan.li
// * @date 2022-06-16
// */
//@Validated
//@Api(value = "学历控制器", tags = {"学历管理"})
//@RequiredArgsConstructor
//@RestController
//@RequestMapping("/user-education")
//public class UserEducationController extends BaseController {
//
//    private final IUserEducationService iUserEducationService;
//
//    /**
//     * 查询学历列表
//     */
//    @ApiOperation("查询学历列表")
//    @SaCheckPermission("admin:user-education:list")
//    @GetMapping("/list")
//    public TableDataInfo<UserEducationVo> list(@Validated(QueryGroup.class) UserEducationBo bo, PageQuery pageQuery) {
//        return iUserEducationService.queryPageList(bo, pageQuery);
//    }
//
//    /**
//     * 导出学历列表
//     */
//    @ApiOperation("导出学历列表")
//    @SaCheckPermission("admin:user-education:export")
//    @Log(title = "学历", businessType = BusinessType.EXPORT)
//    @PostMapping("/export")
//    public void export(@Validated UserEducationBo bo, HttpServletResponse response) {
//        List<UserEducationVo> list = iUserEducationService.queryList(bo);
//        ExcelUtil.exportExcel(list, "学历", UserEducationVo.class, response);
//    }
//
//    /**
//     * 获取学历详细信息
//     */
//    @ApiOperation("获取学历详细信息")
//    @SaCheckPermission("admin:user-education:query")
//    @GetMapping("/{id}")
//    public R<UserEducationVo> getInfo(@ApiParam("主键")
//                                     @NotNull(message = "主键不能为空")
//                                     @PathVariable("id") Long id) {
//        return R.ok(iUserEducationService.queryById(id));
//    }
//
//    /**
//     * 新增学历
//     */
//    @ApiOperation("新增学历")
//    @SaCheckPermission("admin:user-education:add")
//    @Log(title = "学历", businessType = BusinessType.INSERT)
//    @PostMapping()
//    public R<Void> add(@Validated(AddGroup.class) @RequestBody UserEducationBo bo) {
//        return toAjax(iUserEducationService.insertByBo(bo) ? 1 : 0);
//    }
//
//    /**
//     * 修改学历
//     */
//    @ApiOperation("修改学历")
//    @SaCheckPermission("admin:user-education:edit")
//    @Log(title = "学历", businessType = BusinessType.UPDATE)
//    @PutMapping()
//    public R<Void> edit(@Validated(EditGroup.class) @RequestBody UserEducationBo bo) {
//        return toAjax(iUserEducationService.updateByBo(bo) ? 1 : 0);
//    }
//
//    /**
//     * 删除学历
//     */
//    @ApiOperation("删除学历")
//    @SaCheckPermission("admin:user-education:remove")
//    @Log(title = "学历", businessType = BusinessType.DELETE)
//    @DeleteMapping("/{ids}")
//    public R<Void> remove(@ApiParam("主键串")
//                          @NotEmpty(message = "主键不能为空")
//                          @PathVariable Long[] ids) {
//        return toAjax(iUserEducationService.deleteWithValidByIds(Arrays.asList(ids), true) ? 1 : 0);
//    }
//}
