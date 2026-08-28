package org.dromara.manager.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.dromara.common.excel.utils.ExcelUtil;
import org.dromara.common.log.annotation.Log;
import org.dromara.common.log.enums.BusinessType;
import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.common.web.core.BaseController;
import org.dromara.manager.domain.bo.BizAlarmBo;
import org.dromara.manager.domain.vo.BizAlarmVo;
import org.dromara.manager.service.IBizAlarmService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 告警列表
 * 前端访问路由地址为:/manager/alarm/list
 *
 * @author LionLi
 * @date 2026-05-28
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/alarm")
public class BizAlarmController extends BaseController {

    private final IBizAlarmService alarmService;

    /**
     * 查询告警分页列表
     */
    @SaCheckPermission("manager:alarm:list")
    @GetMapping("/list")
    public TableDataInfo<BizAlarmVo> list(BizAlarmBo bo, PageQuery pageQuery) {
        return alarmService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出告警列表
     */
    @SaCheckPermission("manager:alarm:export")
    @Log(title = "告警列表", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(BizAlarmBo bo, HttpServletResponse response) {
        List<BizAlarmVo> list = alarmService.queryList(bo);
        ExcelUtil.exportExcel(list, "告警列表数据", BizAlarmVo.class, response);
    }

}
