package org.dromara.manager.controller;

import java.util.List;

import lombok.RequiredArgsConstructor;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.*;
import cn.dev33.satoken.annotation.SaCheckPermission;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.annotation.Validated;
import org.dromara.common.idempotent.annotation.RepeatSubmit;
import org.dromara.common.log.annotation.Log;
import org.dromara.common.web.core.BaseController;
import org.dromara.manager.api.IBizVehicleService;
import org.dromara.manager.api.domain.bo.BizVehicleBo;
import org.dromara.manager.api.domain.vo.BizVehicleVo;
import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.core.domain.R;
import org.dromara.common.core.validate.AddGroup;
import org.dromara.common.core.validate.EditGroup;
import org.dromara.common.log.enums.BusinessType;
import org.dromara.common.excel.utils.ExcelUtil;
import org.dromara.common.mybatis.core.page.TableDataInfo;

/**
 * 车辆管理
 * 前端访问路由地址为:/manager/vehicle
 *
 * @author LionLi
 * @date 2026-05-21
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/vehicle")
public class BizVehicleController extends BaseController {

    private final IBizVehicleService bizVehicleService;

    /**
     * 查询车辆管理列表
     */
    @SaCheckPermission("manager:vehicle:list")
    @GetMapping("/list")
    public TableDataInfo<BizVehicleVo> list(BizVehicleBo bo, PageQuery pageQuery) {
        return bizVehicleService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出车辆管理列表
     */
    @SaCheckPermission("manager:vehicle:export")
    @Log(title = "车辆管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(BizVehicleBo bo, HttpServletResponse response) {
        List<BizVehicleVo> list = bizVehicleService.queryList(bo);
        ExcelUtil.exportExcel(list, "车辆管理", BizVehicleVo.class, response);
    }

    /**
     * 获取车辆管理详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("manager:vehicle:query")
    @GetMapping("/{id}")
    public R<BizVehicleVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable("id") Long id) {
        return R.ok(bizVehicleService.queryById(id));
    }

    /**
     * 新增车辆管理
     */
    @SaCheckPermission("manager:vehicle:add")
    @Log(title = "车辆管理", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody BizVehicleBo bo) {
        return toAjax(bizVehicleService.insertByBo(bo));
    }

    /**
     * 修改车辆管理
     */
    @SaCheckPermission("manager:vehicle:edit")
    @Log(title = "车辆管理", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody BizVehicleBo bo) {
        return toAjax(bizVehicleService.updateByBo(bo));
    }

    /**
     * 删除车辆管理
     *
     * @param ids 主键串
     */
    @SaCheckPermission("manager:vehicle:remove")
    @Log(title = "车辆管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable("ids") Long[] ids) {
        return toAjax(bizVehicleService.deleteWithValidByIds(List.of(ids), true));
    }
}
