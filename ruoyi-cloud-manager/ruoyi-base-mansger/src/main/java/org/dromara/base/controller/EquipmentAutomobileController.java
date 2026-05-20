package org.dromara.base.controller;

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
import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.core.domain.R;
import org.dromara.common.core.validate.AddGroup;
import org.dromara.common.core.validate.EditGroup;
import org.dromara.common.log.enums.BusinessType;
import org.dromara.common.excel.utils.ExcelUtil;
import org.dromara.base.domain.vo.EquipmentAutomobileVo;
import org.dromara.base.domain.bo.EquipmentAutomobileBo;
import org.dromara.base.service.IEquipmentAutomobileService;
import org.dromara.common.mybatis.core.page.TableDataInfo;

/**
 * 车辆管理
 * 前端访问路由地址为:/equipment/automobile
 *
 * @author 路亮亮
 * @date 2026-03-18
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/automobile")
public class EquipmentAutomobileController extends BaseController {

    private final IEquipmentAutomobileService equipmentAutomobileService;

    /**
     * 查询车辆管理列表
     */
    @SaCheckPermission("equipment:automobile:list")
    @GetMapping("/list")
    public TableDataInfo<EquipmentAutomobileVo> list(EquipmentAutomobileBo bo, PageQuery pageQuery) {
        return equipmentAutomobileService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出车辆管理列表
     */
    @SaCheckPermission("equipment:automobile:export")
    @Log(title = "车辆管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(EquipmentAutomobileBo bo, HttpServletResponse response) {
        List<EquipmentAutomobileVo> list = equipmentAutomobileService.queryList(bo);
        ExcelUtil.exportExcel(list, "车辆管理", EquipmentAutomobileVo.class, response);
    }

    /**
     * 获取车辆管理详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("equipment:automobile:query")
    @GetMapping("/{id}")
    public R<EquipmentAutomobileVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable("id") Long id) {
        return R.ok(equipmentAutomobileService.queryById(id));
    }

    /**
     * 新增车辆管理
     */
    @SaCheckPermission("equipment:automobile:add")
    @Log(title = "车辆管理", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody EquipmentAutomobileBo bo) {
        return toAjax(equipmentAutomobileService.insertByBo(bo));
    }

    /**
     * 修改车辆管理
     */
    @SaCheckPermission("equipment:automobile:edit")
    @Log(title = "车辆管理", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody EquipmentAutomobileBo bo) {
        return toAjax(equipmentAutomobileService.updateByBo(bo));
    }

    /**
     * 删除车辆管理
     *
     * @param ids 主键串
     */
    @SaCheckPermission("equipment:automobile:remove")
    @Log(title = "车辆管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable("ids") Long[] ids) {
        return toAjax(equipmentAutomobileService.deleteWithValidByIds(List.of(ids), true));
    }
}
