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
//import com.project.admin.domain.vo.UserFriendRelationStatusVo;
//import com.project.admin.domain.bo.UserFriendRelationStatusBo;
//import com.project.admin.service.IUserFriendRelationStatusService;
//import com.project.common.mybatis.core.page.TableDataInfo;
//
//import java.util.List;
//import java.util.Arrays;
//import javax.validation.constraints.NotEmpty;
//import javax.validation.constraints.NotNull;
//import javax.servlet.http.HttpServletResponse;
//
///**
// * 关系状态Controller
// * 前端访问路由地址为:/admin/user-friend-relation-status
// * @author huan.li
// * @date 2022-06-16
// */
//@Validated
//@Api(value = "关系状态控制器", tags = {"关系状态管理"})
//@RequiredArgsConstructor
//@RestController
//@RequestMapping("/user-friend-relation-status")
//public class UserFriendRelationStatusController extends BaseController {
//
//    private final IUserFriendRelationStatusService iUserFriendRelationStatusService;
//
//    /**
//     * 查询关系状态列表
//     */
//    @ApiOperation("查询关系状态列表")
//    @SaCheckPermission("admin:user-friend-relation-status:list")
//    @GetMapping("/list")
//    public TableDataInfo<UserFriendRelationStatusVo> list(@Validated(QueryGroup.class) UserFriendRelationStatusBo bo, PageQuery pageQuery) {
//        return iUserFriendRelationStatusService.queryPageList(bo, pageQuery);
//    }
//
//    /**
//     * 导出关系状态列表
//     */
//    @ApiOperation("导出关系状态列表")
//    @SaCheckPermission("admin:user-friend-relation-status:export")
//    @Log(title = "关系状态", businessType = BusinessType.EXPORT)
//    @PostMapping("/export")
//    public void export(@Validated UserFriendRelationStatusBo bo, HttpServletResponse response) {
//        List<UserFriendRelationStatusVo> list = iUserFriendRelationStatusService.queryList(bo);
//        ExcelUtil.exportExcel(list, "关系状态", UserFriendRelationStatusVo.class, response);
//    }
//
//    /**
//     * 获取关系状态详细信息
//     */
//    @ApiOperation("获取关系状态详细信息")
//    @SaCheckPermission("admin:user-friend-relation-status:query")
//    @GetMapping("/{id}")
//    public R<UserFriendRelationStatusVo> getInfo(@ApiParam("主键")
//                                     @NotNull(message = "主键不能为空")
//                                     @PathVariable("id") Long id) {
//        return R.ok(iUserFriendRelationStatusService.queryById(id));
//    }
//
//    /**
//     * 新增关系状态
//     */
//    @ApiOperation("新增关系状态")
//    @SaCheckPermission("admin:user-friend-relation-status:add")
//    @Log(title = "关系状态", businessType = BusinessType.INSERT)
//    @PostMapping()
//    public R<Void> add(@Validated(AddGroup.class) @RequestBody UserFriendRelationStatusBo bo) {
//        return toAjax(iUserFriendRelationStatusService.insertByBo(bo) ? 1 : 0);
//    }
//
//    /**
//     * 修改关系状态
//     */
//    @ApiOperation("修改关系状态")
//    @SaCheckPermission("admin:user-friend-relation-status:edit")
//    @Log(title = "关系状态", businessType = BusinessType.UPDATE)
//    @PutMapping()
//    public R<Void> edit(@Validated(EditGroup.class) @RequestBody UserFriendRelationStatusBo bo) {
//        return toAjax(iUserFriendRelationStatusService.updateByBo(bo) ? 1 : 0);
//    }
//
//    /**
//     * 删除关系状态
//     */
//    @ApiOperation("删除关系状态")
//    @SaCheckPermission("admin:user-friend-relation-status:remove")
//    @Log(title = "关系状态", businessType = BusinessType.DELETE)
//    @DeleteMapping("/{ids}")
//    public R<Void> remove(@ApiParam("主键串")
//                          @NotEmpty(message = "主键不能为空")
//                          @PathVariable Long[] ids) {
//        return toAjax(iUserFriendRelationStatusService.deleteWithValidByIds(Arrays.asList(ids), true) ? 1 : 0);
//    }
//}
