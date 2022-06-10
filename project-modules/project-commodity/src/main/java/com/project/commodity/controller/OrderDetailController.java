package com.project.commodity.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.project.common.core.domain.R;
import com.project.common.core.validate.AddGroup;
import com.project.common.core.validate.EditGroup;
import com.project.common.core.validate.QueryGroup;
import com.project.common.core.web.controller.BaseController;
import com.project.common.excel.utils.ExcelUtil;
import com.project.common.log.annotation.Log;
import com.project.common.log.enums.BusinessType;
import com.project.common.mybatis.core.page.PageQuery;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import com.project.commodity.domain.vo.OrderDetailVo;
import com.project.commodity.domain.bo.OrderDetailBo;
import com.project.commodity.service.IOrderDetailService;
import com.project.common.mybatis.core.page.TableDataInfo;

import java.util.List;
import java.util.Arrays;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.servlet.http.HttpServletResponse;

/**
 * 订单详情Controller
 * 前端访问路由地址为:/commodity/orderdetail
 * @author huan.li
 * @date 2022-06-10
 */
@Validated
@Api(value = "订单详情控制器", tags = {"订单详情管理"})
@RequiredArgsConstructor
@RestController
@RequestMapping("/orderdetail")
public class OrderDetailController extends BaseController {

    private final IOrderDetailService iOrderDetailService;

    /**
     * 查询订单详情列表
     */
    @ApiOperation("查询订单详情列表")
    @SaCheckPermission("commodity:orderdetail:list")
    @GetMapping("/list")
    public TableDataInfo<OrderDetailVo> list(@Validated(QueryGroup.class) OrderDetailBo bo, PageQuery pageQuery) {
        return iOrderDetailService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出订单详情列表
     */
    @ApiOperation("导出订单详情列表")
    @SaCheckPermission("commodity:orderdetail:export")
    @Log(title = "订单详情", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(@Validated OrderDetailBo bo, HttpServletResponse response) {
        List<OrderDetailVo> list = iOrderDetailService.queryList(bo);
        ExcelUtil.exportExcel(list, "订单详情", OrderDetailVo.class, response);
    }

    /**
     * 获取订单详情详细信息
     */
    @ApiOperation("获取订单详情详细信息")
    @SaCheckPermission("commodity:orderdetail:query")
    @GetMapping("/{id}")
    public R<OrderDetailVo> getInfo(@ApiParam("主键")
                                     @NotNull(message = "主键不能为空")
                                     @PathVariable("id") Long id) {
        return R.ok(iOrderDetailService.queryById(id));
    }

    /**
     * 新增订单详情
     */
    @ApiOperation("新增订单详情")
    @SaCheckPermission("commodity:orderdetail:add")
    @Log(title = "订单详情", businessType = BusinessType.INSERT)
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody OrderDetailBo bo) {
        return toAjax(iOrderDetailService.insertByBo(bo) ? 1 : 0);
    }

    /**
     * 修改订单详情
     */
    @ApiOperation("修改订单详情")
    @SaCheckPermission("commodity:orderdetail:edit")
    @Log(title = "订单详情", businessType = BusinessType.UPDATE)
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody OrderDetailBo bo) {
        return toAjax(iOrderDetailService.updateByBo(bo) ? 1 : 0);
    }

    /**
     * 删除订单详情
     */
    @ApiOperation("删除订单详情")
    @SaCheckPermission("commodity:orderdetail:remove")
    @Log(title = "订单详情", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@ApiParam("主键串")
                          @NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(iOrderDetailService.deleteWithValidByIds(Arrays.asList(ids), true) ? 1 : 0);
    }
}
