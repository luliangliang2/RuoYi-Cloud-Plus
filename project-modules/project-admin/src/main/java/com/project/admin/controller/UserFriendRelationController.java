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
import com.project.admin.domain.vo.UserFriendRelationVo;
import com.project.admin.domain.bo.UserFriendRelationBo;
import com.project.admin.service.IUserFriendRelationService;
import com.project.common.mybatis.core.page.TableDataInfo;

import java.util.List;
import java.util.Arrays;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.servlet.http.HttpServletResponse;

/**
 * 好友关系Controller
 * 前端访问路由地址为:/admin/user-friend-relation
 * @author huan.li
 * @date 2022-06-16
 */
@Validated
@Api(value = "好友关系控制器", tags = {"好友关系管理"})
@RequiredArgsConstructor
@RestController
@RequestMapping("/user-friend-relation")
public class UserFriendRelationController extends BaseController {

    private final IUserFriendRelationService iUserFriendRelationService;

    /**
     * 查询好友关系列表
     */
    @ApiOperation("查询好友关系列表")
    @SaCheckPermission("admin:user-friend-relation:list")
    @GetMapping("/list")
    public TableDataInfo<UserFriendRelationVo> list(@Validated(QueryGroup.class) UserFriendRelationBo bo, PageQuery pageQuery) {
        return iUserFriendRelationService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出好友关系列表
     */
    @ApiOperation("导出好友关系列表")
    @SaCheckPermission("admin:user-friend-relation:export")
    @Log(title = "好友关系", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(@Validated UserFriendRelationBo bo, HttpServletResponse response) {
        List<UserFriendRelationVo> list = iUserFriendRelationService.queryList(bo);
        ExcelUtil.exportExcel(list, "好友关系", UserFriendRelationVo.class, response);
    }

    /**
     * 获取好友关系详细信息
     */
    @ApiOperation("获取好友关系详细信息")
    @SaCheckPermission("admin:user-friend-relation:query")
    @GetMapping("/{id}")
    public R<UserFriendRelationVo> getInfo(@ApiParam("主键")
                                     @NotNull(message = "主键不能为空")
                                     @PathVariable("id") Long id) {
        return R.ok(iUserFriendRelationService.queryById(id));
    }

    /**
     * 新增好友关系
     */
    @ApiOperation("新增好友关系")
    @SaCheckPermission("admin:user-friend-relation:add")
    @Log(title = "好友关系", businessType = BusinessType.INSERT)
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody UserFriendRelationBo bo) {
        return toAjax(iUserFriendRelationService.insertByBo(bo) ? 1 : 0);
    }

    /**
     * 修改好友关系
     */
    @ApiOperation("修改好友关系")
    @SaCheckPermission("admin:user-friend-relation:edit")
    @Log(title = "好友关系", businessType = BusinessType.UPDATE)
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody UserFriendRelationBo bo) {
        return toAjax(iUserFriendRelationService.updateByBo(bo) ? 1 : 0);
    }

    /**
     * 删除好友关系
     */
    @ApiOperation("删除好友关系")
    @SaCheckPermission("admin:user-friend-relation:remove")
    @Log(title = "好友关系", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@ApiParam("主键串")
                          @NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(iUserFriendRelationService.deleteWithValidByIds(Arrays.asList(ids), true) ? 1 : 0);
    }
}
