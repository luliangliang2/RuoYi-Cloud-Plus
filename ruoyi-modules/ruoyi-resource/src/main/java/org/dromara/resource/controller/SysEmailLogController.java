package org.dromara.resource.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.dromara.common.core.domain.R;
import org.dromara.common.excel.utils.ExcelUtil;
import org.dromara.common.log.annotation.Log;
import org.dromara.common.log.enums.BusinessType;
import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.common.web.core.BaseController;
import org.dromara.resource.domain.bo.SysEmailLogBo;
import org.dromara.resource.domain.vo.SysEmailLogVo;
import org.dromara.resource.service.ISysEmailLogService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 邮件日志
 * 前端访问路由地址为:/resource/emailLog
 *
 * @author 2100
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/emailLog")
public class SysEmailLogController extends BaseController {

    private final ISysEmailLogService sysEmailLogService;

    /**
     * 查询邮件日志列表
     */
    @SaCheckPermission("resource:emailLog:list")
    @GetMapping("/list")
    public TableDataInfo<SysEmailLogVo> list(SysEmailLogBo bo, PageQuery pageQuery) {
        return sysEmailLogService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出邮件日志列表
     */
    @SaCheckPermission("resource:emailLog:export")
    @Log(title = "邮件日志", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(SysEmailLogBo bo, HttpServletResponse response) {
        List<SysEmailLogVo> list = sysEmailLogService.queryList(bo);
        ExcelUtil.exportExcel(list, "邮件日志", SysEmailLogVo.class, response);
    }

    /**
     * 获取邮件日志详细信息
     *
     * @param emailId 主键
     */
    @SaCheckPermission("resource:emailLog:query")
    @GetMapping("/{emailId}")
    public R<SysEmailLogVo> getInfo(@NotNull(message = "主键不能为空") @PathVariable("emailId") Long emailId) {
        return R.ok(sysEmailLogService.queryById(emailId));
    }

}
