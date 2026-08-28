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
import org.dromara.manager.domain.bo.BizSceneAreaBo;
import org.dromara.manager.domain.vo.BizSceneAreaVo;
import org.dromara.manager.service.IBizSceneAreaService;
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
 * 场景区域
 * 前端访问路由地址为:/manager/scene/area
 *
 * @author LionLi
 * @date 2026-05-24
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/scene/area")
public class BizSceneAreaController extends BaseController {

    private final IBizSceneAreaService sceneAreaService;

    /**
     * 查询场景区域分页列表
     */
    @SaCheckPermission("manager:sceneArea:list")
    @GetMapping("/list")
    public TableDataInfo<BizSceneAreaVo> list(BizSceneAreaBo bo, PageQuery pageQuery) {
        return sceneAreaService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出场景区域列表
     */
    @SaCheckPermission("manager:sceneArea:export")
    @Log(title = "场景区域", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(BizSceneAreaBo bo, HttpServletResponse response) {
        List<BizSceneAreaVo> list = sceneAreaService.queryList(bo);
        ExcelUtil.exportExcel(list, "场景区域数据", BizSceneAreaVo.class, response);
    }

    /**
     * 获取场景区域详细信息
     *
     * @param areaId 主键
     */
    @SaCheckPermission("manager:sceneArea:query")
    @GetMapping("/{areaId}")
    public R<BizSceneAreaVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable("areaId") Long areaId) {
        return R.ok(sceneAreaService.queryById(areaId));
    }

    /**
     * 新增场景区域
     */
    @SaCheckPermission("manager:sceneArea:add")
    @Log(title = "场景区域", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping
    public R<Void> add(@Validated(AddGroup.class) @RequestBody BizSceneAreaBo bo) {
        return toAjax(sceneAreaService.insertByBo(bo));
    }

    /**
     * 修改场景区域
     */
    @SaCheckPermission("manager:sceneArea:edit")
    @Log(title = "场景区域", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody BizSceneAreaBo bo) {
        return toAjax(sceneAreaService.updateByBo(bo));
    }

    /**
     * 删除场景区域
     *
     * @param ids 主键串
     */
    @SaCheckPermission("manager:sceneArea:remove")
    @Log(title = "场景区域", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable("ids") Long[] ids) {
        return toAjax(sceneAreaService.deleteWithValidByIds(List.of(ids), true));
    }

}
