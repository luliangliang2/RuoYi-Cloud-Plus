package org.dromara.manager.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.dromara.common.core.domain.R;
import org.dromara.common.core.validate.AddGroup;
import org.dromara.common.core.validate.EditGroup;
import org.dromara.common.excel.utils.ExcelUtil;
import org.dromara.common.idempotent.annotation.RepeatSubmit;
import org.dromara.common.log.annotation.Log;
import org.dromara.common.log.enums.BusinessType;
import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.common.web.core.BaseController;
import org.dromara.manager.domain.bo.BizSceneRouteBo;
import org.dromara.manager.domain.vo.BizSceneRouteVo;
import org.dromara.manager.service.IBizSceneRouteService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 场景路线
 * 前端访问路由地址为:/manager/scene/route
 *
 * @author LionLi
 * @date 2026-05-24
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/scene/route")
public class BizSceneRouteController extends BaseController {

    private final IBizSceneRouteService sceneRouteService;

    /**
     * 查询场景路线分页列表
     */
    @SaCheckPermission("manager:sceneRoute:list")
    @GetMapping("/list")
    public TableDataInfo<BizSceneRouteVo> list(BizSceneRouteBo bo, PageQuery pageQuery) {
        return sceneRouteService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出场景路线列表
     */
    @SaCheckPermission("manager:sceneRoute:export")
    @Log(title = "场景路线", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(BizSceneRouteBo bo, HttpServletResponse response) {
        List<BizSceneRouteVo> list = sceneRouteService.queryList(bo);
        ExcelUtil.exportExcel(list, "场景路线数据", BizSceneRouteVo.class, response);
    }

    /**
     * 获取场景路线详细信息
     *
     * @param routeId 主键
     */
    @SaCheckPermission("manager:sceneRoute:query")
    @GetMapping("/{routeId}")
    public R<BizSceneRouteVo> getInfo(@NotNull(message = "主键不能为空")
                                      @PathVariable("routeId") Long routeId) {
        return R.ok(sceneRouteService.queryById(routeId));
    }

    /**
     * 新增场景路线
     */
    @SaCheckPermission("manager:sceneRoute:add")
    @Log(title = "场景路线", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping
    public R<Void> add(@Validated(AddGroup.class) @RequestBody BizSceneRouteBo bo) {
        return toAjax(sceneRouteService.insertByBo(bo));
    }

    /**
     * 修改场景路线
     */
    @SaCheckPermission("manager:sceneRoute:edit")
    @Log(title = "场景路线", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody BizSceneRouteBo bo) {
        return toAjax(sceneRouteService.updateByBo(bo));
    }

    /**
     * 删除场景路线
     *
     * @param ids 主键串
     */
    @SaCheckPermission("manager:sceneRoute:remove")
    @Log(title = "场景路线", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable("ids") Long[] ids) {
        return toAjax(sceneRouteService.deleteWithValidByIds(List.of(ids), true));
    }

}
