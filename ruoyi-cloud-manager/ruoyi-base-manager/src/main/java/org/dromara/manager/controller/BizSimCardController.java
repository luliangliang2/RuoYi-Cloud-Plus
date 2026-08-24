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
import org.dromara.manager.domain.bo.BizSimCardBo;
import org.dromara.manager.domain.vo.BizSimCardVo;
import org.dromara.manager.service.IBizSimCardService;
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
 * SIM卡
 * 前端访问路由地址为:/manager/simCard
 *
 * @author LionLi
 * @date 2026-05-22
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/simCard")
public class BizSimCardController extends BaseController {

    private final IBizSimCardService simCardService;

    /**
     * 查询SIM卡分页列表
     */
    @SaCheckPermission("manager:simCard:list")
    @GetMapping("/list")
    public TableDataInfo<BizSimCardVo> list(BizSimCardBo bo, PageQuery pageQuery) {
        return simCardService.queryPageList(bo, pageQuery);
    }

    /**
     * 查询可绑定SIM卡列表
     */
    @SaCheckPermission("manager:vehicleEquipment:list")
    @GetMapping("/bindable")
    public R<List<BizSimCardVo>> bindable(@NotNull(message = "车辆ID不能为空")
                                          @RequestParam Long vehicleId,
                                          @RequestParam(required = false) String keyword) {
        return R.ok(simCardService.queryBindableList(vehicleId, keyword));
    }

    /**
     * 导出SIM卡列表
     */
    @SaCheckPermission("manager:simCard:export")
    @Log(title = "SIM卡", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(BizSimCardBo bo, HttpServletResponse response) {
        List<BizSimCardVo> list = simCardService.queryList(bo);
        ExcelUtil.exportExcel(list, "SIM卡数据", BizSimCardVo.class, response);
    }

    /**
     * 获取SIM卡详细信息
     *
     * @param simId 主键
     */
    @SaCheckPermission("manager:simCard:query")
    @GetMapping("/{simId}")
    public R<BizSimCardVo> getInfo(@NotNull(message = "主键不能为空")
                                   @PathVariable("simId") Long simId) {
        return R.ok(simCardService.queryById(simId));
    }

    /**
     * 新增SIM卡
     */
    @SaCheckPermission("manager:simCard:add")
    @Log(title = "SIM卡", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping
    public R<Void> add(@Validated(AddGroup.class) @RequestBody BizSimCardBo bo) {
        return toAjax(simCardService.insertByBo(bo));
    }

    /**
     * 修改SIM卡
     */
    @SaCheckPermission("manager:simCard:edit")
    @Log(title = "SIM卡", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody BizSimCardBo bo) {
        return toAjax(simCardService.updateByBo(bo));
    }

    /**
     * 删除SIM卡
     *
     * @param ids 主键串
     */
    @SaCheckPermission("manager:simCard:remove")
    @Log(title = "SIM卡", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable("ids") Long[] ids) {
        return toAjax(simCardService.deleteWithValidByIds(List.of(ids), true));
    }

}
