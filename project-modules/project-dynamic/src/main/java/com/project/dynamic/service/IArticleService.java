package com.project.dynamic.service;

import com.project.dynamic.domain.Article;
import com.project.dynamic.domain.vo.ArticleVo;
import com.project.dynamic.domain.bo.ArticleBo;
import com.project.common.mybatis.core.page.PageQuery;
import com.project.common.mybatis.core.page.TableDataInfo;

import java.util.Collection;
import java.util.List;

/**
 * 文章Service接口
 *
 * @author huan.li
 * @date 2022-06-10
 */
public interface IArticleService {

    /**
     * 查询文章
     *
     * @param id 文章主键
     * @return 文章
     */
    ArticleVo queryById(Long id);

    /**
     * 查询文章列表
     *
     * @param article 文章
     * @return 文章集合
     */
    TableDataInfo<ArticleVo> queryPageList(ArticleBo bo, PageQuery pageQuery);

    /**
     * 查询文章列表
     *
     * @param article 文章
     * @return 文章集合
     */
    List<ArticleVo> queryList(ArticleBo bo);

    /**
     * 修改文章
     *
     * @param article 文章
     * @return 结果
     */
    Boolean insertByBo(ArticleBo bo);

    /**
     * 修改文章
     *
     * @param article 文章
     * @return 结果
     */
    Boolean updateByBo(ArticleBo bo);

    /**
     * 校验并批量删除文章信息
     *
     * @param ids 需要删除的文章主键集合
     * @param isValid 是否校验,true-删除前校验,false-不校验
     * @return 结果
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}
