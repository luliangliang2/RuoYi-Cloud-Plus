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
import org.dromara.manager.domain.bo.BizScenePointBo;
import org.dromara.manager.domain.vo.BizScenePointVo;
import org.dromara.manager.service.IBizScenePointService;
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
 * 场景点位
 * 前端访问路由地址为:/manager/scene/point
 *
 * @author LionLi
 * @date 2026-05-24
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/scene/point")
public class BizScenePointController extends BaseController {

    private final IBizScenePointService scenePointService;

    /**
     * 查询场景点位分页列表
     */
    @SaCheckPermission("manager:scenePoint:list")
    @GetMapping("/list")
    public TableDataInfo<BizScenePointVo> list(BizScenePointBo bo, PageQuery pageQuery) {
        return scenePointService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出场景点位列表
     */
    @SaCheckPermission("manager:scenePoint:export")
    @Log(title = "场景点位", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(BizScenePointBo bo, HttpServletResponse response) {
        List<BizScenePointVo> list = scenePointService.queryList(bo);
        ExcelUtil.exportExcel(list, "场景点位数据", BizScenePointVo.class, response);
    }

    /**
     * 获取场景点位详细信息
     *
     * @param pointId 主键
     */
    @SaCheckPermission("manager:scenePoint:query")
    @GetMapping("/{pointId}")
    public R<BizScenePointVo> getInfo(@NotNull(message = "主键不能为空")
                                      @PathVariable("pointId") Long pointId) {
        return R.ok(scenePointService.queryById(pointId));
    }

    /**
     * 新增场景点位
     */
    @SaCheckPermission("manager:scenePoint:add")
    @Log(title = "场景点位", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping
    public R<Void> add(@Validated(AddGroup.class) @RequestBody BizScenePointBo bo) {
        return toAjax(scenePointService.insertByBo(bo));
    }

    /**
     * 修改场景点位
     */
    @SaCheckPermission("manager:scenePoint:edit")
    @Log(title = "场景点位", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody BizScenePointBo bo) {
        return toAjax(scenePointService.updateByBo(bo));
    }

    /**
     * 删除场景点位
     *
     * @param ids 主键串
     */
    @SaCheckPermission("manager:scenePoint:remove")
    @Log(title = "场景点位", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable("ids") Long[] ids) {
        return toAjax(scenePointService.deleteWithValidByIds(List.of(ids), true));
    }

}
