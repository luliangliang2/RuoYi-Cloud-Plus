package com.project.dynamic.controller;

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
import com.project.dynamic.domain.vo.ArticleVo;
import com.project.dynamic.domain.bo.ArticleBo;
import com.project.dynamic.service.IArticleService;
import com.project.common.mybatis.core.page.TableDataInfo;

import java.util.List;
import java.util.Arrays;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.servlet.http.HttpServletResponse;

/**
 * 文章Controller
 * 前端访问路由地址为:/dynamic/article
 * @author huan.li
 * @date 2022-06-10
 */
@Validated
@Api(value = "文章控制器", tags = {"文章管理"})
@RequiredArgsConstructor
@RestController
@RequestMapping("/article")
public class ArticleController extends BaseController {

    private final IArticleService iArticleService;

    /**
     * 查询文章列表
     */
    @ApiOperation("查询文章列表")
    @SaCheckPermission("dynamic:article:list")
    @GetMapping("/list")
    public TableDataInfo<ArticleVo> list(@Validated(QueryGroup.class) ArticleBo bo, PageQuery pageQuery) {
        return iArticleService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出文章列表
     */
    @ApiOperation("导出文章列表")
    @SaCheckPermission("dynamic:article:export")
    @Log(title = "文章", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(@Validated ArticleBo bo, HttpServletResponse response) {
        List<ArticleVo> list = iArticleService.queryList(bo);
        ExcelUtil.exportExcel(list, "文章", ArticleVo.class, response);
    }

    /**
     * 获取文章详细信息
     */
    @ApiOperation("获取文章详细信息")
    @SaCheckPermission("dynamic:article:query")
    @GetMapping("/{id}")
    public R<ArticleVo> getInfo(@ApiParam("主键")
                                     @NotNull(message = "主键不能为空")
                                     @PathVariable("id") Long id) {
        return R.ok(iArticleService.queryById(id));
    }

    /**
     * 新增文章
     */
    @ApiOperation("新增文章")
    @SaCheckPermission("dynamic:article:add")
    @Log(title = "文章", businessType = BusinessType.INSERT)
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody ArticleBo bo) {
        return toAjax(iArticleService.insertByBo(bo) ? 1 : 0);
    }

    /**
     * 修改文章
     */
    @ApiOperation("修改文章")
    @SaCheckPermission("dynamic:article:edit")
    @Log(title = "文章", businessType = BusinessType.UPDATE)
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody ArticleBo bo) {
        return toAjax(iArticleService.updateByBo(bo) ? 1 : 0);
    }

    /**
     * 删除文章
     */
    @ApiOperation("删除文章")
    @SaCheckPermission("dynamic:article:remove")
    @Log(title = "文章", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@ApiParam("主键串")
                          @NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(iArticleService.deleteWithValidByIds(Arrays.asList(ids), true) ? 1 : 0);
    }
}
