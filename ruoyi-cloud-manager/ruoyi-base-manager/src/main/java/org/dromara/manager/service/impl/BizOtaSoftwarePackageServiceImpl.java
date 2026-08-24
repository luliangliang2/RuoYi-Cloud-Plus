package org.dromara.manager.service.impl;

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
import org.dromara.manager.domain.BizOtaSoftwarePackage;
import org.dromara.manager.domain.bo.BizOtaSoftwarePackageBo;
import org.dromara.manager.domain.vo.BizOtaSoftwarePackageVo;
import org.dromara.manager.mapper.BizOtaSoftwarePackageMapper;
import org.dromara.manager.service.IBizOtaSoftwarePackageService;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

/**
 * OTA软件包Service业务层处理
 *
 * @author LionLi
 * @date 2026-05-24
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class BizOtaSoftwarePackageServiceImpl implements IBizOtaSoftwarePackageService {

    private final BizOtaSoftwarePackageMapper baseMapper;

    /**
     * 查询OTA软件包
     *
     * @param packageId 主键
     * @return OTA软件包
     */
    @Override
    public BizOtaSoftwarePackageVo queryById(Long packageId) {
        return baseMapper.selectVoById(packageId);
    }

    /**
     * 分页查询OTA软件包列表
     *
     * @param bo 查询条件
     * @param pageQuery 分页参数
     * @return OTA软件包分页列表
     */
    @Override
    public TableDataInfo<BizOtaSoftwarePackageVo> queryPageList(BizOtaSoftwarePackageBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<BizOtaSoftwarePackage> lqw = buildQueryWrapper(bo);
        Page<BizOtaSoftwarePackageVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询符合条件的OTA软件包列表
     *
     * @param bo 查询条件
     * @return OTA软件包列表
     */
    @Override
    public List<BizOtaSoftwarePackageVo> queryList(BizOtaSoftwarePackageBo bo) {
        LambdaQueryWrapper<BizOtaSoftwarePackage> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<BizOtaSoftwarePackage> buildQueryWrapper(BizOtaSoftwarePackageBo bo) {
        LambdaQueryWrapper<BizOtaSoftwarePackage> lqw = Wrappers.lambdaQuery();
        lqw.like(StringUtils.isNotBlank(bo.getPackageName()), BizOtaSoftwarePackage::getPackageName, bo.getPackageName());
        lqw.like(StringUtils.isNotBlank(bo.getVersion()), BizOtaSoftwarePackage::getVersion, bo.getVersion());
        lqw.eq(StringUtils.isNotBlank(bo.getStatus()), BizOtaSoftwarePackage::getStatus, bo.getStatus());
        lqw.orderByDesc(BizOtaSoftwarePackage::getCreateTime);
        lqw.orderByDesc(BizOtaSoftwarePackage::getPackageId);
        return lqw;
    }

    /**
     * 新增OTA软件包
     *
     * @param bo OTA软件包
     * @return 是否新增成功
     */
    @Override
    public Boolean insertByBo(BizOtaSoftwarePackageBo bo) {
        BizOtaSoftwarePackage add = MapstructUtils.convert(bo, BizOtaSoftwarePackage.class);
        fillDefaultValue(add);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setPackageId(add.getPackageId());
        }
        return flag;
    }

    /**
     * 修改OTA软件包
     *
     * @param bo OTA软件包
     * @return 是否修改成功
     */
    @Override
    public Boolean updateByBo(BizOtaSoftwarePackageBo bo) {
        BizOtaSoftwarePackage update = MapstructUtils.convert(bo, BizOtaSoftwarePackage.class);
        fillDefaultValue(update);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    private void fillDefaultValue(BizOtaSoftwarePackage entity) {
        if (StringUtils.isBlank(entity.getStatus())) {
            entity.setStatus("0");
        }
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(BizOtaSoftwarePackage entity) {
        Long count = baseMapper.selectCount(new LambdaQueryWrapper<BizOtaSoftwarePackage>()
            .eq(BizOtaSoftwarePackage::getPackageName, entity.getPackageName())
            .eq(BizOtaSoftwarePackage::getVersion, entity.getVersion())
            .ne(entity.getPackageId() != null, BizOtaSoftwarePackage::getPackageId, entity.getPackageId()));
        if (count > 0) {
            throw new ServiceException("同名同版本软件包已存在");
        }
    }

    /**
     * 校验并批量删除OTA软件包信息
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
