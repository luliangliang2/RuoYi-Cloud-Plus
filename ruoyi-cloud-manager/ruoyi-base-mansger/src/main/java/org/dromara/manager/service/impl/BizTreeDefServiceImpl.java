package org.dromara.manager.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dromara.common.core.constant.TenantConstants;
import org.dromara.common.core.exception.ServiceException;
import org.dromara.common.core.utils.MapstructUtils;
import org.dromara.common.core.utils.StreamUtils;
import org.dromara.common.core.utils.StringUtils;
import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.common.tenant.core.TenantEntity;
import org.dromara.common.tenant.helper.TenantHelper;
import org.dromara.manager.domain.BizTreeDef;
import org.dromara.manager.domain.BizTreeNode;
import org.dromara.manager.domain.bo.BizTreeDefBo;
import org.dromara.manager.domain.vo.BizTreeDefVo;
import org.dromara.manager.mapper.BizTreeDefMapper;
import org.dromara.manager.mapper.BizTreeNodeMapper;
import org.dromara.manager.service.IBizTreeDefService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 维护树定义Service业务层处理
 *
 * @author LionLi
 * @date 2026-05-22
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class BizTreeDefServiceImpl implements IBizTreeDefService {

    private final BizTreeDefMapper baseMapper;
    private final BizTreeNodeMapper treeNodeMapper;

    /**
     * 查询维护树定义
     *
     * @param treeId 主键
     * @return 维护树定义
     */
    @Override
    public BizTreeDefVo queryById(Long treeId) {
        return baseMapper.selectVoById(treeId);
    }

    /**
     * 分页查询维护树定义列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 维护树定义分页列表
     */
    @Override
    public TableDataInfo<BizTreeDefVo> queryPageList(BizTreeDefBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<BizTreeDef> lqw = buildQueryWrapper(bo);
        Page<BizTreeDefVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询符合条件的维护树定义列表
     *
     * @param bo 查询条件
     * @return 维护树定义列表
     */
    @Override
    public List<BizTreeDefVo> queryList(BizTreeDefBo bo) {
        LambdaQueryWrapper<BizTreeDef> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<BizTreeDef> buildQueryWrapper(BizTreeDefBo bo) {
        LambdaQueryWrapper<BizTreeDef> lqw = Wrappers.lambdaQuery();
        lqw.like(StringUtils.isNotBlank(bo.getTreeCode()), BizTreeDef::getTreeCode, bo.getTreeCode());
        lqw.like(StringUtils.isNotBlank(bo.getTreeName()), BizTreeDef::getTreeName, bo.getTreeName());
        lqw.eq(StringUtils.isNotBlank(bo.getTreeType()), BizTreeDef::getTreeType, bo.getTreeType());
        lqw.eq(StringUtils.isNotBlank(bo.getModuleCode()), BizTreeDef::getModuleCode, bo.getModuleCode());
        lqw.eq(StringUtils.isNotBlank(bo.getStatus()), BizTreeDef::getStatus, bo.getStatus());
        lqw.orderByAsc(BizTreeDef::getTreeCode);
        lqw.orderByDesc(BizTreeDef::getCreateTime);
        return lqw;
    }

    /**
     * 新增维护树定义
     *
     * @param bo 维护树定义
     * @return 是否新增成功
     */
    @Override
    public Boolean insertByBo(BizTreeDefBo bo) {
        BizTreeDef add = MapstructUtils.convert(bo, BizTreeDef.class);
        fillDefaultValue(add);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setTreeId(add.getTreeId());
        }
        return flag;
    }

    /**
     * 修改维护树定义
     *
     * @param bo 维护树定义
     * @return 是否修改成功
     */
    @Override
    public Boolean updateByBo(BizTreeDefBo bo) {
        BizTreeDef update = MapstructUtils.convert(bo, BizTreeDef.class);
        fillDefaultValue(update);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    private void fillDefaultValue(BizTreeDef entity) {
        if (StringUtils.isBlank(entity.getTreeType())) {
            entity.setTreeType("business");
        }
        if (StringUtils.isBlank(entity.getSelectMode())) {
            entity.setSelectMode("single");
        }
        if (StringUtils.isBlank(entity.getRootMode())) {
            entity.setRootMode("1");
        }
        if (StringUtils.isBlank(entity.getStatus())) {
            entity.setStatus("0");
        }
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(BizTreeDef entity) {
        Long count = baseMapper.selectCount(new LambdaQueryWrapper<BizTreeDef>()
            .eq(BizTreeDef::getTreeCode, entity.getTreeCode())
            .ne(entity.getTreeId() != null, BizTreeDef::getTreeId, entity.getTreeId()));
        if (count > 0) {
            throw new ServiceException("树编码已存在");
        }
    }

    /**
     * 校验并批量删除维护树定义信息
     *
     * @param ids     待删除的主键集合
     * @param isValid 是否进行有效性校验
     * @return 是否删除成功
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        List<BizTreeDef> treeDefs = baseMapper.selectList(new LambdaQueryWrapper<BizTreeDef>()
            .in(BizTreeDef::getTreeId, ids));
        boolean hasSystemTree = treeDefs.stream().anyMatch(treeDef -> "system".equals(treeDef.getTreeType()));
        if (hasSystemTree) {
            throw new ServiceException("系统分类不允许删除");
        }
        treeNodeMapper.delete(new LambdaQueryWrapper<BizTreeNode>()
            .in(BizTreeNode::getTreeId, ids));
        return baseMapper.deleteByIds(ids) > 0;
    }

    /**
     * 同步默认租户的维护树定义到指定租户
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void syncTreeDef(String tenantId) {
        if (StringUtils.isBlank(tenantId) || TenantConstants.DEFAULT_TENANT_ID.equals(tenantId)) {
            return;
        }
        List<BizTreeDef> treeDefList = TenantHelper.ignore(() -> baseMapper.selectList());
        Map<String, List<BizTreeDef>> treeDefMap = StreamUtils.groupByKey(treeDefList, TenantEntity::getTenantId);
        List<BizTreeDef> defaultTreeDefList = treeDefMap.get(TenantConstants.DEFAULT_TENANT_ID);
        if (CollUtil.isEmpty(defaultTreeDefList)) {
            return;
        }
        Set<String> tenantTreeCodes = StreamUtils.toSet(treeDefMap.get(tenantId), BizTreeDef::getTreeCode);
        List<BizTreeDef> saveTreeDefList = new ArrayList<>();
        for (BizTreeDef treeDef : defaultTreeDefList) {
            if (tenantTreeCodes.contains(treeDef.getTreeCode())) {
                continue;
            }
            BizTreeDef add = BeanUtil.toBean(treeDef, BizTreeDef.class);
            add.setTreeId(null);
            add.setTenantId(tenantId);
            add.setCreateDept(null);
            add.setCreateBy(null);
            add.setCreateTime(null);
            add.setUpdateBy(null);
            add.setUpdateTime(null);
            saveTreeDefList.add(add);
        }
        TenantHelper.ignore(() -> {
            if (CollUtil.isNotEmpty(saveTreeDefList)) {
                baseMapper.insertBatch(saveTreeDefList);
            }
        });
        log.info("同步维护树定义完成，tenantId={}，数量={}", tenantId, Convert.toInt(saveTreeDefList.size()));
    }

}
