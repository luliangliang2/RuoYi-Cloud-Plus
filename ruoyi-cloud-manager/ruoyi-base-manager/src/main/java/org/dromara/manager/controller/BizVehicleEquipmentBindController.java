package org.dromara.manager.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.dromara.common.core.domain.R;
import org.dromara.common.core.validate.AddGroup;
import org.dromara.common.core.validate.EditGroup;
import org.dromara.common.idempotent.annotation.RepeatSubmit;
import org.dromara.common.log.annotation.Log;
import org.dromara.common.log.enums.BusinessType;
import org.dromara.common.web.core.BaseController;
import org.dromara.manager.domain.bo.BizVehicleEquipmentBindBo;
import org.dromara.manager.domain.vo.BizVehicleEquipmentBindVo;
import org.dromara.manager.service.IBizVehicleEquipmentBindService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 车辆上装绑定
 * 前端访问路由地址为:/manager/vehicle/equipment
 *
 * @author LionLi
 * @date 2026-05-22
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/vehicle/equipment")
public class BizVehicleEquipmentBindController extends BaseController {

    private final IBizVehicleEquipmentBindService bindService;

    /**
     * 查询车辆上装绑定列表
     */
    @SaCheckPermission("manager:vehicleEquipment:list")
    @GetMapping("/list")
    public R<List<BizVehicleEquipmentBindVo>> list(@NotNull(message = "车辆ID不能为空")
                                                   @RequestParam Long vehicleId,
                                                   @RequestParam(required = false) String equipmentType) {
        return R.ok(bindService.queryList(vehicleId, equipmentType));
    }

    /**
     * 获取车辆上装绑定详细信息
     *
     * @param bindId 主键
     */
    @SaCheckPermission("manager:vehicleEquipment:list")
    @GetMapping("/{bindId}")
    public R<BizVehicleEquipmentBindVo> getInfo(@NotNull(message = "主键不能为空")
                                                @PathVariable("bindId") Long bindId) {
        return R.ok(bindService.queryById(bindId));
    }

    /**
     * 新增车辆上装绑定
     */
    @SaCheckPermission("manager:vehicleEquipment:add")
    @Log(title = "车辆上装绑定", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping
    public R<Void> add(@Validated(AddGroup.class) @RequestBody BizVehicleEquipmentBindBo bo) {
        return toAjax(bindService.insertByBo(bo));
    }

    /**
     * 修改车辆上装绑定
     */
    @SaCheckPermission("manager:vehicleEquipment:edit")
    @Log(title = "车辆上装绑定", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody BizVehicleEquipmentBindBo bo) {
        return toAjax(bindService.updateByBo(bo));
    }

    /**
     * 删除车辆上装绑定
     *
     * @param ids 主键串
     */
    @SaCheckPermission("manager:vehicleEquipment:remove")
    @Log(title = "车辆上装绑定", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable("ids") Long[] ids) {
        return toAjax(bindService.deleteWithValidByIds(List.of(ids), true));
    }

}
