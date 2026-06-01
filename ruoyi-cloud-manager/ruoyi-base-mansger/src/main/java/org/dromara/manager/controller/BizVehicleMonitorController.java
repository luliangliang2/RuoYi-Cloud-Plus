package org.dromara.manager.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import lombok.RequiredArgsConstructor;
import org.dromara.common.core.domain.R;
import org.dromara.common.web.core.BaseController;
import org.dromara.manager.domain.vo.BizVehicleMonitorTreeVo;
import org.dromara.manager.service.IBizVehicleMonitorService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 车辆监控
 * 前端访问路由地址为:/manager/vehicleMonitor
 *
 * @author LionLi
 * @date 2026-06-01
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/vehicle/monitor")
public class BizVehicleMonitorController extends BaseController {

    private final IBizVehicleMonitorService vehicleMonitorService;

    /**
     * 查询车辆监控分类车辆树
     */
    @SaCheckPermission("manager:vehicleMonitor:list")
    @GetMapping("/tree")
    public R<List<BizVehicleMonitorTreeVo>> tree(@RequestParam(required = false) Long treeId,
                                                 @RequestParam(required = false) String keyword) {
        return R.ok(vehicleMonitorService.queryVehicleTree(treeId, keyword));
    }

}
