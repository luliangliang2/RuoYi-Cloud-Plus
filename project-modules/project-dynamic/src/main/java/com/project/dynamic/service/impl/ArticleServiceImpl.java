package com.project.dynamic.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.project.common.mybatis.core.page.PageQuery;
import com.project.common.mybatis.core.page.TableDataInfo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.project.dynamic.domain.bo.ArticleBo;
import com.project.dynamic.domain.vo.ArticleVo;
import com.project.dynamic.domain.Article;
import com.project.dynamic.mapper.ArticleMapper;
import com.project.dynamic.service.IArticleService;

import java.util.List;
import java.util.Map;
import java.util.Collection;

/**
 * 文章Service业务层处理
 *
 * @author huan.li
 * @date 2022-06-10
 */
@RequiredArgsConstructor
@Service
public class ArticleServiceImpl implements IArticleService {

    private final ArticleMapper baseMapper;

    /**
     * 查询文章
     *
     * @param id 文章主键
     * @return 文章
     */
    @Override
    public ArticleVo queryById(Long id){
        return baseMapper.selectVoById(id);
    }

    /**
     * 查询文章列表
     *
     * @param bo 文章
     * @return 文章
     */
    @Override
    public TableDataInfo<ArticleVo> queryPageList(ArticleBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<Article> lqw = buildQueryWrapper(bo);
        Page<ArticleVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询文章列表
     *
     * @param bo 文章
     * @return 文章
     */
    @Override
    public List<ArticleVo> queryList(ArticleBo bo) {
        LambdaQueryWrapper<Article> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<Article> buildQueryWrapper(ArticleBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<Article> lqw = Wrappers.lambdaQuery();
        lqw.eq(bo.getPushTime() != null, Article::getPushTime, bo.getPushTime());
        lqw.eq(bo.getUserId() != null, Article::getUserId, bo.getUserId());
        lqw.eq(bo.getLikeCount() != null, Article::getLikeCount, bo.getLikeCount());
        lqw.eq(bo.getCommentCount() != null, Article::getCommentCount, bo.getCommentCount());
        lqw.eq(bo.getReadCount() != null, Article::getReadCount, bo.getReadCount());
        lqw.eq(bo.getTopFlag() != null, Article::getTopFlag, bo.getTopFlag());
        lqw.eq(bo.getSubscribedFlag() != null, Article::getSubscribedFlag, bo.getSubscribedFlag());
        return lqw;
    }

    /**
     * 新增文章
     *
     * @param bo 文章
     * @return 结果
     */
    @Override
    public Boolean insertByBo(ArticleBo bo) {
        Article add = BeanUtil.toBean(bo, Article.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改文章
     *
     * @param bo 文章
     * @return 结果
     */
    @Override
    public Boolean updateByBo(ArticleBo bo) {
        Article update = BeanUtil.toBean(bo, Article.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     *
     * @param entity 实体类数据
     */
    private void validEntityBeforeSave(Article entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 批量删除文章
     *
     * @param ids 需要删除的文章主键
     * @return 结果
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if(isValid){
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return baseMapper.deleteBatchIds(ids) > 0;
    }
}
