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
import org.dromara.cognition.domain.bo.CognitionSceneBo;
import org.dromara.cognition.domain.vo.CognitionSceneVo;
import org.dromara.cognition.domain.CognitionScene;
import org.dromara.cognition.mapper.CognitionSceneMapper;
import org.dromara.cognition.service.ICognitionSceneService;

import java.util.List;
import java.util.Map;
import java.util.Collection;

/**
 * 认知场景Service业务层处理
 *
 * @author zhang
 * @date 2025-10-02
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class CognitionSceneServiceImpl implements ICognitionSceneService {

    private final CognitionSceneMapper baseMapper;

    /**
     * 查询认知场景
     *
     * @param id 主键
     * @return 认知场景
     */
    @Override
    public CognitionSceneVo queryById(Long id){
        return baseMapper.selectVoById(id);
    }

    /**
     * 分页查询认知场景列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 认知场景分页列表
     */
    @Override
    public TableDataInfo<CognitionSceneVo> queryPageList(CognitionSceneBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<CognitionScene> lqw = buildQueryWrapper(bo);
        Page<CognitionSceneVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询符合条件的认知场景列表
     *
     * @param bo 查询条件
     * @return 认知场景列表
     */
    @Override
    public List<CognitionSceneVo> queryList(CognitionSceneBo bo) {
        LambdaQueryWrapper<CognitionScene> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<CognitionScene> buildQueryWrapper(CognitionSceneBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<CognitionScene> lqw = Wrappers.lambdaQuery();
        lqw.orderByAsc(CognitionScene::getId);
        lqw.like(StringUtils.isNotBlank(bo.getSceneName()), CognitionScene::getSceneName, bo.getSceneName());
        return lqw;
    }

    /**
     * 新增认知场景
     *
     * @param bo 认知场景
     * @return 是否新增成功
     */
    @Override
    public Boolean insertByBo(CognitionSceneBo bo) {
        CognitionScene add = MapstructUtils.convert(bo, CognitionScene.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改认知场景
     *
     * @param bo 认知场景
     * @return 是否修改成功
     */
    @Override
    public Boolean updateByBo(CognitionSceneBo bo) {
        CognitionScene update = MapstructUtils.convert(bo, CognitionScene.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(CognitionScene entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 校验并批量删除认知场景信息
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
