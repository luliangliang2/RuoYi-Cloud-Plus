package com.ruoyi.db.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.validate.AddGroup;
import com.ruoyi.common.core.validate.EditGroup;
import com.ruoyi.common.core.web.controller.BaseController;
import com.ruoyi.common.excel.utils.ExcelUtil;
import com.ruoyi.common.log.annotation.Log;
import com.ruoyi.common.log.enums.BusinessType;
import com.ruoyi.common.mybatis.core.page.PageQuery;
import com.ruoyi.common.mybatis.core.page.TableDataInfo;
import com.ruoyi.db.domain.bo.SysDatabaseConfigBo;
import com.ruoyi.db.domain.vo.SysDatabaseConfigVo;
import com.ruoyi.db.service.ISysDatabaseConfigService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.List;

/**
 * 数据库配置控制器
 * 前端访问路由地址为:/database/config
 *
 * @author lishuyan wrote on 2022/8/29.
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/config")
public class SysDatabaseConfigController extends BaseController {

    private final ISysDatabaseConfigService iSysDatabaseConfigService;

    /**
     * 查询数据库配置列表
     */
    @SaCheckPermission("database:config:list")
    @GetMapping("/list")
    public TableDataInfo<SysDatabaseConfigVo> list(SysDatabaseConfigBo bo, PageQuery pageQuery) {
        return iSysDatabaseConfigService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出数据库配置列表
     */
    @SaCheckPermission("database:config:export")
    @Log(title = "数据库配置", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(SysDatabaseConfigBo bo, HttpServletResponse response) {
        List<SysDatabaseConfigVo> list = iSysDatabaseConfigService.queryList(bo);
        ExcelUtil.exportExcel(list, "数据库配置", SysDatabaseConfigVo.class, response);
    }

    /**
     * 获取数据库配置详细信息
     *
     * @param dbId 主键
     */
    @SaCheckPermission("database:config:query")
    @GetMapping("/{dbId}")
    public R<SysDatabaseConfigVo> getInfo(@NotNull(message = "主键不能为空") @PathVariable Long dbId) {
        return R.ok(iSysDatabaseConfigService.queryById(dbId));
    }

    /**
     * 新增数据库配置
     */
    @SaCheckPermission("database:config:add")
    @Log(title = "数据库配置", businessType = BusinessType.INSERT)
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody SysDatabaseConfigBo bo) {
        return toAjax(iSysDatabaseConfigService.insertByBo(bo) ? 1 : 0);
    }

    /**
     * 修改数据库配置
     */
    @SaCheckPermission("database:config:edit")
    @Log(title = "数据库配置", businessType = BusinessType.UPDATE)
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody SysDatabaseConfigBo bo) {
        return toAjax(iSysDatabaseConfigService.updateByBo(bo) ? 1 : 0);
    }

    /**
     * 删除数据库配置
     *
     * @param dbIds 主键串
     */
    @SaCheckPermission("database:config:remove")
    @Log(title = "数据库配置", businessType = BusinessType.DELETE)
    @DeleteMapping("/{dbIds}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空") @PathVariable Long[] dbIds) {
        return toAjax(iSysDatabaseConfigService.deleteWithValidByIds(Arrays.asList(dbIds), true) ? 1 : 0);
    }

    /**
     * 数据库连接测试
     * @param bo 数据库配置业务对象
     */
    @PostMapping("/testConnect")
    @SaCheckPermission("database:config:edit")
    public R<Void> testConnect(@Validated(EditGroup.class) @RequestBody SysDatabaseConfigBo bo) {
        return iSysDatabaseConfigService.testConnection(bo) ? R.ok() : R.fail("连接失败");
    }

    /**
     * 状态修改
     */
    @SaCheckPermission("database:config:edit")
    @Log(title = "数据库配置状态修改", businessType = BusinessType.UPDATE)
    @PutMapping("/changeStatus")
    public R<Void> changeStatus(@RequestBody SysDatabaseConfigBo bo) {
        return toAjax(iSysDatabaseConfigService.updateDatabaseConfigStatus(bo));
    }
}
