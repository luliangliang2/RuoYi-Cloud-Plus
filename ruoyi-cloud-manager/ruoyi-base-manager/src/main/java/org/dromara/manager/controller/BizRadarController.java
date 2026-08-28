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
import org.dromara.manager.domain.bo.BizRadarBo;
import org.dromara.manager.domain.vo.BizRadarVo;
import org.dromara.manager.service.IBizRadarService;
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
 * 上装雷达
 * 前端访问路由地址为:/manager/radar
 *
 * @author LionLi
 * @date 2026-05-22
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/radar")
public class BizRadarController extends BaseController {

    private final IBizRadarService radarService;

    /**
     * 查询上装雷达分页列表
     */
    @SaCheckPermission("manager:radar:list")
    @GetMapping("/list")
    public TableDataInfo<BizRadarVo> list(BizRadarBo bo, PageQuery pageQuery) {
        return radarService.queryPageList(bo, pageQuery);
    }

    /**
     * 查询可绑定雷达列表
     */
    @SaCheckPermission("manager:vehicleEquipment:list")
    @GetMapping("/bindable")
    public R<List<BizRadarVo>> bindable(@NotNull(message = "车辆ID不能为空")
                                        @RequestParam Long vehicleId,
                                        @RequestParam(required = false) String keyword) {
        return R.ok(radarService.queryBindableList(vehicleId, keyword));
    }

    /**
     * 导出上装雷达列表
     */
    @SaCheckPermission("manager:radar:export")
    @Log(title = "上装雷达", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(BizRadarBo bo, HttpServletResponse response) {
        List<BizRadarVo> list = radarService.queryList(bo);
        ExcelUtil.exportExcel(list, "上装雷达数据", BizRadarVo.class, response);
    }

    /**
     * 获取上装雷达详细信息
     *
     * @param radarId 主键
     */
    @SaCheckPermission("manager:radar:query")
    @GetMapping("/{radarId}")
    public R<BizRadarVo> getInfo(@NotNull(message = "主键不能为空")
                                 @PathVariable("radarId") Long radarId) {
        return R.ok(radarService.queryById(radarId));
    }

    /**
     * 新增上装雷达
     */
    @SaCheckPermission("manager:radar:add")
    @Log(title = "上装雷达", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping
    public R<Void> add(@Validated(AddGroup.class) @RequestBody BizRadarBo bo) {
        return toAjax(radarService.insertByBo(bo));
    }

    /**
     * 修改上装雷达
     */
    @SaCheckPermission("manager:radar:edit")
    @Log(title = "上装雷达", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody BizRadarBo bo) {
        return toAjax(radarService.updateByBo(bo));
    }

    /**
     * 删除上装雷达
     *
     * @param ids 主键串
     */
    @SaCheckPermission("manager:radar:remove")
    @Log(title = "上装雷达", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable("ids") Long[] ids) {
        return toAjax(radarService.deleteWithValidByIds(List.of(ids), true));
    }

}
