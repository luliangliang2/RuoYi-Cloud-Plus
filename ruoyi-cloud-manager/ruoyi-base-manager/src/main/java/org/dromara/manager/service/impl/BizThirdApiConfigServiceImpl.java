package org.dromara.manager.service.impl;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dromara.common.core.exception.ServiceException;
import org.dromara.common.core.utils.MapstructUtils;
import org.dromara.common.core.utils.StringUtils;
import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.manager.domain.BizThirdApiConfig;
import org.dromara.manager.domain.bo.BizThirdApiConfigBo;
import org.dromara.manager.domain.vo.BizThirdApiConfigVo;
import org.dromara.manager.mapper.BizThirdApiConfigMapper;
import org.dromara.manager.service.IBizThirdApiConfigService;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

/**
 * 第三方API配置Service业务层处理
 *
 * @author LionLi
 * @date 2026-05-22
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class BizThirdApiConfigServiceImpl implements IBizThirdApiConfigService {

    private final BizThirdApiConfigMapper baseMapper;

    /**
     * 查询第三方API配置
     *
     * @param configId 主键
     * @return 第三方API配置
     */
    @Override
    public BizThirdApiConfigVo queryById(Long configId) {
        return baseMapper.selectVoById(configId);
    }

    /**
     * 分页查询第三方API配置列表
     *
     * @param bo 查询条件
     * @param pageQuery 分页参数
     * @return 第三方API配置分页列表
     */
    @Override
    public TableDataInfo<BizThirdApiConfigVo> queryPageList(BizThirdApiConfigBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<BizThirdApiConfig> lqw = buildQueryWrapper(bo);
        Page<BizThirdApiConfigVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询符合条件的第三方API配置列表
     *
     * @param bo 查询条件
     * @return 第三方API配置列表
     */
    @Override
    public List<BizThirdApiConfigVo> queryList(BizThirdApiConfigBo bo) {
        LambdaQueryWrapper<BizThirdApiConfig> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<BizThirdApiConfig> buildQueryWrapper(BizThirdApiConfigBo bo) {
        LambdaQueryWrapper<BizThirdApiConfig> lqw = Wrappers.lambdaQuery();
        lqw.like(StringUtils.isNotBlank(bo.getApiName()), BizThirdApiConfig::getApiName, bo.getApiName());
        lqw.like(StringUtils.isNotBlank(bo.getApiCode()), BizThirdApiConfig::getApiCode, bo.getApiCode());
        lqw.eq(StringUtils.isNotBlank(bo.getApiCategory()), BizThirdApiConfig::getApiCategory, bo.getApiCategory());
        lqw.like(StringUtils.isNotBlank(bo.getProviderName()), BizThirdApiConfig::getProviderName, bo.getProviderName());
        lqw.eq(StringUtils.isNotBlank(bo.getStatus()), BizThirdApiConfig::getStatus, bo.getStatus());
        lqw.orderByDesc(BizThirdApiConfig::getCreateTime);
        lqw.orderByDesc(BizThirdApiConfig::getConfigId);
        return lqw;
    }

    /**
     * 新增第三方API配置
     *
     * @param bo 第三方API配置
     * @return 是否新增成功
     */
    @Override
    public Boolean insertByBo(BizThirdApiConfigBo bo) {
        BizThirdApiConfig add = MapstructUtils.convert(bo, BizThirdApiConfig.class);
        fillDefaultValue(add);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setConfigId(add.getConfigId());
        }
        return flag;
    }

    /**
     * 修改第三方API配置
     *
     * @param bo 第三方API配置
     * @return 是否修改成功
     */
    @Override
    public Boolean updateByBo(BizThirdApiConfigBo bo) {
        BizThirdApiConfig update = MapstructUtils.convert(bo, BizThirdApiConfig.class);
        fillDefaultValue(update);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    private void fillDefaultValue(BizThirdApiConfig entity) {
        if (StringUtils.isBlank(entity.getStatus())) {
            entity.setStatus("0");
        }
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(BizThirdApiConfig entity) {
        Long count = baseMapper.selectCount(new LambdaQueryWrapper<BizThirdApiConfig>()
            .eq(BizThirdApiConfig::getApiCode, entity.getApiCode())
            .ne(entity.getConfigId() != null, BizThirdApiConfig::getConfigId, entity.getConfigId()));
        if (count > 0) {
            throw new ServiceException("API编码已存在");
        }
        if (entity.getBillingStartTime() != null
            && entity.getBillingEndTime() != null
            && entity.getBillingStartTime().after(entity.getBillingEndTime())) {
            throw new ServiceException("计费开始时间不能晚于计费到期时间");
        }
        if (StringUtils.isNotBlank(entity.getExtJson()) && !JSONUtil.isTypeJSON(entity.getExtJson())) {
            throw new ServiceException("扩展参数JSON格式不正确");
        }
    }

    /**
     * 校验并批量删除第三方API配置信息
     *
     * @param ids 待删除的主键集合
     * @param isValid 是否进行有效性校验
     * @return 是否删除成功
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        return baseMapper.deleteByIds(ids) > 0;
    }

}
