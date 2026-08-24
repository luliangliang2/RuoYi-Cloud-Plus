package org.dromara.manager.service.impl;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dromara.common.core.exception.ServiceException;
import org.dromara.common.core.utils.MapstructUtils;
import org.dromara.common.core.utils.StringUtils;
import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.manager.domain.BizCommandConfig;
import org.dromara.manager.domain.bo.BizCommandConfigBo;
import org.dromara.manager.domain.vo.BizCommandConfigVo;
import org.dromara.manager.mapper.BizCommandConfigMapper;
import org.dromara.manager.service.IBizCommandConfigService;
import org.dromara.manager.service.support.TreeCategoryBindSupport;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

/**
 * 指令配置Service业务层处理
 *
 * @author LionLi
 * @date 2026-05-23
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class BizCommandConfigServiceImpl implements IBizCommandConfigService {

    private static final String BUSINESS_TYPE = "commandConfig";

    private final BizCommandConfigMapper baseMapper;
    private final TreeCategoryBindSupport categoryBindSupport;

    /**
     * 查询指令配置
     *
     * @param commandId 主键
     * @return 指令配置
     */
    @Override
    public BizCommandConfigVo queryById(Long commandId) {
        BizCommandConfigVo vo = baseMapper.selectVoById(commandId);
        fillCategoryNodeIds(vo);
        return vo;
    }

    /**
     * 分页查询指令配置列表
     *
     * @param bo 查询条件
     * @param pageQuery 分页参数
     * @return 指令配置分页列表
     */
    @Override
    public TableDataInfo<BizCommandConfigVo> queryPageList(BizCommandConfigBo bo, PageQuery pageQuery) {
        BizCommandConfig query = MapstructUtils.convert(bo, BizCommandConfig.class);
        Page<BizCommandConfigVo> result = baseMapper.selectCommandConfigPage(pageQuery.build(), query);
        result.getRecords().forEach(this::fillCategoryNodeIds);
        return TableDataInfo.build(result);
    }

    /**
     * 查询符合条件的指令配置列表
     *
     * @param bo 查询条件
     * @return 指令配置列表
     */
    @Override
    public List<BizCommandConfigVo> queryList(BizCommandConfigBo bo) {
        BizCommandConfig query = MapstructUtils.convert(bo, BizCommandConfig.class);
        List<BizCommandConfigVo> list = baseMapper.selectCommandConfigList(query);
        list.forEach(this::fillCategoryNodeIds);
        return list;
    }

    /**
     * 新增指令配置
     *
     * @param bo 指令配置
     * @return 是否新增成功
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean insertByBo(BizCommandConfigBo bo) {
        normalizeCategory(bo);
        BizCommandConfig add = MapstructUtils.convert(bo, BizCommandConfig.class);
        fillDefaultValue(add);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setCommandId(add.getCommandId());
            categoryBindSupport.save(BUSINESS_TYPE, bo.getCommandId(), bo.getTreeId(), bo.getCategoryNodeIds());
        }
        return flag;
    }

    /**
     * 修改指令配置
     *
     * @param bo 指令配置
     * @return 是否修改成功
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean updateByBo(BizCommandConfigBo bo) {
        normalizeCategory(bo);
        BizCommandConfig update = MapstructUtils.convert(bo, BizCommandConfig.class);
        fillDefaultValue(update);
        validEntityBeforeSave(update);
        boolean flag = baseMapper.updateById(update) > 0;
        if (flag) {
            categoryBindSupport.save(BUSINESS_TYPE, bo.getCommandId(), bo.getTreeId(), bo.getCategoryNodeIds());
        }
        return flag;
    }

    private void fillDefaultValue(BizCommandConfig entity) {
        if (StringUtils.isBlank(entity.getStatus())) {
            entity.setStatus("0");
        }
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(BizCommandConfig entity) {
        Long count = baseMapper.selectCount(new LambdaQueryWrapper<BizCommandConfig>()
            .eq(BizCommandConfig::getCommandCode, entity.getCommandCode())
            .ne(entity.getCommandId() != null, BizCommandConfig::getCommandId, entity.getCommandId()));
        if (count > 0) {
            throw new ServiceException("指令编码已存在");
        }
        if (!"single".equals(entity.getCommandType()) && !"multiple".equals(entity.getCommandType())) {
            throw new ServiceException("指令类型不正确");
        }
        if (StringUtils.isNotBlank(entity.getCommandTemplate()) && !JSONUtil.isTypeJSON(entity.getCommandTemplate())) {
            throw new ServiceException("指令JSON模板格式不正确");
        }
    }

    /**
     * 校验并批量删除指令配置信息
     *
     * @param ids 待删除的主键集合
     * @param isValid 是否进行有效性校验
     * @return 是否删除成功
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        categoryBindSupport.deleteByBusinessIds(BUSINESS_TYPE, ids);
        return baseMapper.deleteByIds(ids) > 0;
    }

    private void normalizeCategory(BizCommandConfigBo bo) {
        List<Long> nodeIds = categoryBindSupport.normalize(bo.getCategoryNodeId(), bo.getCategoryNodeIds());
        bo.setCategoryNodeIds(nodeIds);
        bo.setCategoryNodeId(nodeIds.isEmpty() ? null : nodeIds.get(0));
    }

    private void fillCategoryNodeIds(BizCommandConfigVo vo) {
        if (vo == null || vo.getCommandId() == null) {
            return;
        }
        vo.setCategoryNodeIds(categoryBindSupport.getNodeIds(BUSINESS_TYPE, vo.getCommandId(), vo.getCategoryNodeId()));
    }

}
