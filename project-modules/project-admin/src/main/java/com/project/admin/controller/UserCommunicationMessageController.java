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
//import com.project.admin.domain.vo.UserCommunicationMessageVo;
//import com.project.admin.domain.bo.UserCommunicationMessageBo;
//import com.project.admin.service.IUserCommunicationMessageService;
//import com.project.common.mybatis.core.page.TableDataInfo;
//
//import java.util.List;
//import java.util.Arrays;
//import javax.validation.constraints.NotEmpty;
//import javax.validation.constraints.NotNull;
//import javax.servlet.http.HttpServletResponse;
//
///**
// * 沟通消息Controller
// * 前端访问路由地址为:/admin/user-communication-message
// * @author huan.li
// * @date 2022-06-16
// */
//@Validated
//@Api(value = "沟通消息控制器", tags = {"沟通消息管理"})
//@RequiredArgsConstructor
//@RestController
//@RequestMapping("/user-communication-message")
//public class UserCommunicationMessageController extends BaseController {
//
//    private final IUserCommunicationMessageService iUserCommunicationMessageService;
//
//    /**
//     * 查询沟通消息列表
//     */
//    @ApiOperation("查询沟通消息列表")
//    @SaCheckPermission("admin:user-communication-message:list")
//    @GetMapping("/list")
//    public TableDataInfo<UserCommunicationMessageVo> list(@Validated(QueryGroup.class) UserCommunicationMessageBo bo, PageQuery pageQuery) {
//        return iUserCommunicationMessageService.queryPageList(bo, pageQuery);
//    }
//
//    /**
//     * 导出沟通消息列表
//     */
//    @ApiOperation("导出沟通消息列表")
//    @SaCheckPermission("admin:user-communication-message:export")
//    @Log(title = "沟通消息", businessType = BusinessType.EXPORT)
//    @PostMapping("/export")
//    public void export(@Validated UserCommunicationMessageBo bo, HttpServletResponse response) {
//        List<UserCommunicationMessageVo> list = iUserCommunicationMessageService.queryList(bo);
//        ExcelUtil.exportExcel(list, "沟通消息", UserCommunicationMessageVo.class, response);
//    }
//
//    /**
//     * 获取沟通消息详细信息
//     */
//    @ApiOperation("获取沟通消息详细信息")
//    @SaCheckPermission("admin:user-communication-message:query")
//    @GetMapping("/{id}")
//    public R<UserCommunicationMessageVo> getInfo(@ApiParam("主键")
//                                     @NotNull(message = "主键不能为空")
//                                     @PathVariable("id") Long id) {
//        return R.ok(iUserCommunicationMessageService.queryById(id));
//    }
//
//    /**
//     * 新增沟通消息
//     */
//    @ApiOperation("新增沟通消息")
//    @SaCheckPermission("admin:user-communication-message:add")
//    @Log(title = "沟通消息", businessType = BusinessType.INSERT)
//    @PostMapping()
//    public R<Void> add(@Validated(AddGroup.class) @RequestBody UserCommunicationMessageBo bo) {
//        return toAjax(iUserCommunicationMessageService.insertByBo(bo) ? 1 : 0);
//    }
//
//    /**
//     * 修改沟通消息
//     */
//    @ApiOperation("修改沟通消息")
//    @SaCheckPermission("admin:user-communication-message:edit")
//    @Log(title = "沟通消息", businessType = BusinessType.UPDATE)
//    @PutMapping()
//    public R<Void> edit(@Validated(EditGroup.class) @RequestBody UserCommunicationMessageBo bo) {
//        return toAjax(iUserCommunicationMessageService.updateByBo(bo) ? 1 : 0);
//    }
//
//    /**
//     * 删除沟通消息
//     */
//    @ApiOperation("删除沟通消息")
//    @SaCheckPermission("admin:user-communication-message:remove")
//    @Log(title = "沟通消息", businessType = BusinessType.DELETE)
//    @DeleteMapping("/{ids}")
//    public R<Void> remove(@ApiParam("主键串")
//                          @NotEmpty(message = "主键不能为空")
//                          @PathVariable Long[] ids) {
//        return toAjax(iUserCommunicationMessageService.deleteWithValidByIds(Arrays.asList(ids), true) ? 1 : 0);
//    }
//}
