package org.dromara.cognition.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dromara.cognition.domain.CognitionSceneStep;
import org.dromara.cognition.domain.bo.CognitionSceneStepBo;
import org.dromara.cognition.domain.vo.CognitionSceneStepVo;
import org.dromara.cognition.mapper.CognitionSceneStepMapper;
import org.dromara.cognition.service.ICognitionSceneStepService;
import org.dromara.common.core.utils.MapstructUtils;
import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.resource.api.RemoteFileService;
import org.springframework.stereotype.Service;

/**
 * 认知场景步骤Service业务层处理
 *
 * @author zhang
 * @date 2025-10-02
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class CognitionSceneStepServiceImpl implements ICognitionSceneStepService {

    private final CognitionSceneStepMapper baseMapper;
    private final RemoteFileService remoteFileService;

    /**
     * 查询认知场景步骤
     *
     * @param id 主键
     * @return 认知场景步骤
     */
    @Override
    public CognitionSceneStepVo queryById(Long id) {
        return baseMapper.selectVoById(id);
    }

    /**
     * 分页查询认知场景步骤列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 认知场景步骤分页列表
     */
    @Override
    public TableDataInfo<CognitionSceneStepVo> queryPageList(CognitionSceneStepBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<CognitionSceneStep> lqw = buildQueryWrapper(bo);
        Page<CognitionSceneStepVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
//
//        // 提取所有 videoId
//        List<Long> videoIds = result.getRecords().stream()
//            .map(CognitionSceneStepVo::getVideoId)
//            .filter(Objects::nonNull)
//            .collect(Collectors.toList());
//
//        if (CollUtil.isNotEmpty(videoIds)) {
//            // 把 videoIds 拼接成逗号分隔的字符串
//            String ossIds = videoIds.stream()
//                .map(String::valueOf)
//                .collect(Collectors.joining(","));
//
//            // 调用 RemoteFileService 查询文件信息
//            List<RemoteFile> fileList = remoteFileService.selectByIds(ossIds);
//
//            if (CollUtil.isNotEmpty(fileList)) {
//                // 转换为 Map<ossId, url>
//                Map<Long, String> id2UrlMap = fileList.stream()
//                    .collect(Collectors.toMap(RemoteFile::getOssId, RemoteFile::getUrl, (a, b) -> b));
//
//                // 给 VO 填充 videoUrl
//                result.getRecords().forEach(vo -> {
//                    if (vo.getVideoId() != null) {
//                        vo.setVideoUrl(id2UrlMap.get(vo.getVideoId()));
//                    }
//                });
//            }
//        }

        return TableDataInfo.build(result);
    }


    /**
     * 查询符合条件的认知场景步骤列表
     *
     * @param bo 查询条件
     * @return 认知场景步骤列表
     */
    @Override
    public List<CognitionSceneStepVo> queryList(CognitionSceneStepBo bo) {
        LambdaQueryWrapper<CognitionSceneStep> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<CognitionSceneStep> buildQueryWrapper(CognitionSceneStepBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<CognitionSceneStep> lqw = Wrappers.lambdaQuery();
        lqw.orderByAsc(CognitionSceneStep::getId);
        lqw.eq(bo.getSceneId() != null, CognitionSceneStep::getSceneId, bo.getSceneId());
        return lqw;
    }

    /**
     * 新增认知场景步骤
     *
     * @param bo 认知场景步骤
     * @return 是否新增成功
     */
    @Override
    public Boolean insertByBo(CognitionSceneStepBo bo) {
        CognitionSceneStep add = MapstructUtils.convert(bo, CognitionSceneStep.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改认知场景步骤
     *
     * @param bo 认知场景步骤
     * @return 是否修改成功
     */
    @Override
    public Boolean updateByBo(CognitionSceneStepBo bo) {
        CognitionSceneStep update = MapstructUtils.convert(bo, CognitionSceneStep.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    @Override
    public Boolean updateStepOrder(List<CognitionSceneStepBo> bos) {
        List<CognitionSceneStep> convert = MapstructUtils.convert(bos, CognitionSceneStep.class);
        return baseMapper.updateBatchById(convert);
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(CognitionSceneStep entity) {
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 校验并批量删除认知场景步骤信息
     *
     * @param ids     待删除的主键集合
     * @param isValid 是否进行有效性校验
     * @return 是否删除成功
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if (isValid) {
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return baseMapper.deleteByIds(ids) > 0;
    }
}
