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
import org.dromara.manager.domain.bo.BizCameraBo;
import org.dromara.manager.domain.vo.BizCameraVo;
import org.dromara.manager.service.IBizCameraService;
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
 * 上装相机
 * 前端访问路由地址为:/manager/camera
 *
 * @author LionLi
 * @date 2026-05-22
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/camera")
public class BizCameraController extends BaseController {

    private final IBizCameraService cameraService;

    /**
     * 查询上装相机分页列表
     */
    @SaCheckPermission("manager:camera:list")
    @GetMapping("/list")
    public TableDataInfo<BizCameraVo> list(BizCameraBo bo, PageQuery pageQuery) {
        return cameraService.queryPageList(bo, pageQuery);
    }

    /**
     * 查询可绑定相机列表
     */
    @SaCheckPermission("manager:vehicleEquipment:list")
    @GetMapping("/bindable")
    public R<List<BizCameraVo>> bindable(@NotNull(message = "车辆ID不能为空")
                                         @RequestParam Long vehicleId,
                                         @RequestParam(required = false) String keyword) {
        return R.ok(cameraService.queryBindableList(vehicleId, keyword));
    }

    /**
     * 导出上装相机列表
     */
    @SaCheckPermission("manager:camera:export")
    @Log(title = "上装相机", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(BizCameraBo bo, HttpServletResponse response) {
        List<BizCameraVo> list = cameraService.queryList(bo);
        ExcelUtil.exportExcel(list, "上装相机数据", BizCameraVo.class, response);
    }

    /**
     * 获取上装相机详细信息
     *
     * @param cameraId 主键
     */
    @SaCheckPermission("manager:camera:query")
    @GetMapping("/{cameraId}")
    public R<BizCameraVo> getInfo(@NotNull(message = "主键不能为空")
                                  @PathVariable("cameraId") Long cameraId) {
        return R.ok(cameraService.queryById(cameraId));
    }

    /**
     * 新增上装相机
     */
    @SaCheckPermission("manager:camera:add")
    @Log(title = "上装相机", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping
    public R<Void> add(@Validated(AddGroup.class) @RequestBody BizCameraBo bo) {
        return toAjax(cameraService.insertByBo(bo));
    }

    /**
     * 修改上装相机
     */
    @SaCheckPermission("manager:camera:edit")
    @Log(title = "上装相机", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody BizCameraBo bo) {
        return toAjax(cameraService.updateByBo(bo));
    }

    /**
     * 删除上装相机
     *
     * @param ids 主键串
     */
    @SaCheckPermission("manager:camera:remove")
    @Log(title = "上装相机", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable("ids") Long[] ids) {
        return toAjax(cameraService.deleteWithValidByIds(List.of(ids), true));
    }

}
