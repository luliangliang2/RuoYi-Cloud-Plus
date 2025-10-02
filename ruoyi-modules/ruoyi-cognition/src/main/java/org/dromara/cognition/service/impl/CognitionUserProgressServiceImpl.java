package org.dromara.cognition.service.impl;

import org.dromara.common.core.utils.MapstructUtils;
import org.dromara.common.core.utils.StringUtils;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.common.mybatis.core.page.PageQuery;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.dromara.cognition.domain.bo.CognitionUserProgressBo;
import org.dromara.cognition.domain.vo.CognitionUserProgressVo;
import org.dromara.cognition.domain.CognitionUserProgress;
import org.dromara.cognition.mapper.CognitionUserProgressMapper;
import org.dromara.cognition.service.ICognitionUserProgressService;

import java.util.List;
import java.util.Map;
import java.util.Collection;

/**
 * 用户场景学习进度Service业务层处理
 *
 * @author zhang
 * @date 2025-10-02
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class CognitionUserProgressServiceImpl implements ICognitionUserProgressService {

    private final CognitionUserProgressMapper baseMapper;

    /**
     * 查询用户场景学习进度
     *
     * @param id 主键
     * @return 用户场景学习进度
     */
    @Override
    public CognitionUserProgressVo queryById(Long id){
        return baseMapper.selectVoById(id);
    }

    /**
     * 分页查询用户场景学习进度列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 用户场景学习进度分页列表
     */
    @Override
    public TableDataInfo<CognitionUserProgressVo> queryPageList(CognitionUserProgressBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<CognitionUserProgress> lqw = buildQueryWrapper(bo);
        Page<CognitionUserProgressVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询符合条件的用户场景学习进度列表
     *
     * @param bo 查询条件
     * @return 用户场景学习进度列表
     */
    @Override
    public List<CognitionUserProgressVo> queryList(CognitionUserProgressBo bo) {
        LambdaQueryWrapper<CognitionUserProgress> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<CognitionUserProgress> buildQueryWrapper(CognitionUserProgressBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<CognitionUserProgress> lqw = Wrappers.lambdaQuery();
        lqw.orderByAsc(CognitionUserProgress::getId);
        lqw.eq(bo.getSceneId() != null, CognitionUserProgress::getSceneId, bo.getSceneId());
        lqw.eq(bo.getUserId() != null, CognitionUserProgress::getUserId, bo.getUserId());
        lqw.eq(bo.getIsCompleted() != null, CognitionUserProgress::getIsCompleted, bo.getIsCompleted());
        return lqw;
    }

    /**
     * 新增用户场景学习进度
     *
     * @param bo 用户场景学习进度
     * @return 是否新增成功
     */
    @Override
    public Boolean insertByBo(CognitionUserProgressBo bo) {
        CognitionUserProgress add = MapstructUtils.convert(bo, CognitionUserProgress.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改用户场景学习进度
     *
     * @param bo 用户场景学习进度
     * @return 是否修改成功
     */
    @Override
    public Boolean updateByBo(CognitionUserProgressBo bo) {
        CognitionUserProgress update = MapstructUtils.convert(bo, CognitionUserProgress.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(CognitionUserProgress entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 校验并批量删除用户场景学习进度信息
     *
     * @param ids     待删除的主键集合
     * @param isValid 是否进行有效性校验
     * @return 是否删除成功
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if(isValid){
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return baseMapper.deleteByIds(ids) > 0;
    }
}
