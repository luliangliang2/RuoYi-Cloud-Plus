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
import org.dromara.manager.constant.EquipmentTypeConstants;
import org.dromara.manager.domain.BizSimCard;
import org.dromara.manager.domain.BizVehicleEquipmentBind;
import org.dromara.manager.domain.bo.BizSimCardBo;
import org.dromara.manager.domain.vo.BizSimCardVo;
import org.dromara.manager.mapper.BizSimCardMapper;
import org.dromara.manager.mapper.BizVehicleEquipmentBindMapper;
import org.dromara.manager.service.IBizSimCardService;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

/**
 * SIM卡Service业务层处理
 *
 * @author LionLi
 * @date 2026-05-22
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class BizSimCardServiceImpl implements IBizSimCardService {

    private final BizSimCardMapper baseMapper;
    private final BizVehicleEquipmentBindMapper bindMapper;

    /**
     * 查询SIM卡
     *
     * @param simId 主键
     * @return SIM卡
     */
    @Override
    public BizSimCardVo queryById(Long simId) {
        return baseMapper.selectVoById(simId);
    }

    /**
     * 分页查询SIM卡列表
     *
     * @param bo 查询条件
     * @param pageQuery 分页参数
     * @return SIM卡分页列表
     */
    @Override
    public TableDataInfo<BizSimCardVo> queryPageList(BizSimCardBo bo, PageQuery pageQuery) {
        BizSimCard query = MapstructUtils.convert(bo, BizSimCard.class);
        Page<BizSimCardVo> result = baseMapper.selectSimCardPage(pageQuery.build(), query);
        return TableDataInfo.build(result);
    }

    /**
     * 查询符合条件的SIM卡列表
     *
     * @param bo 查询条件
     * @return SIM卡列表
     */
    @Override
    public List<BizSimCardVo> queryList(BizSimCardBo bo) {
        BizSimCard query = MapstructUtils.convert(bo, BizSimCard.class);
        return baseMapper.selectSimCardList(query);
    }

    /**
     * 查询可绑定SIM卡列表
     *
     * @param vehicleId 车辆ID
     * @param keyword 关键字
     * @return SIM卡列表
     */
    @Override
    public List<BizSimCardVo> queryBindableList(Long vehicleId, String keyword) {
        return baseMapper.selectBindableList(vehicleId, keyword);
    }

    /**
     * 新增SIM卡
     *
     * @param bo SIM卡
     * @return 是否新增成功
     */
    @Override
    public Boolean insertByBo(BizSimCardBo bo) {
        BizSimCard add = MapstructUtils.convert(bo, BizSimCard.class);
        fillDefaultValue(add);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setSimId(add.getSimId());
        }
        return flag;
    }

    /**
     * 修改SIM卡
     *
     * @param bo SIM卡
     * @return 是否修改成功
     */
    @Override
    public Boolean updateByBo(BizSimCardBo bo) {
        BizSimCard update = MapstructUtils.convert(bo, BizSimCard.class);
        fillDefaultValue(update);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    private void fillDefaultValue(BizSimCard entity) {
        if (StringUtils.isBlank(entity.getStatus())) {
            entity.setStatus("0");
        }
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(BizSimCard entity) {
        Long iccidCount = baseMapper.selectCount(new LambdaQueryWrapper<BizSimCard>()
            .eq(BizSimCard::getIccid, entity.getIccid())
            .ne(entity.getSimId() != null, BizSimCard::getSimId, entity.getSimId()));
        if (iccidCount > 0) {
            throw new ServiceException("ICCID已存在");
        }
        if (StringUtils.isNotBlank(entity.getImei())) {
            Long imeiCount = baseMapper.selectCount(new LambdaQueryWrapper<BizSimCard>()
                .eq(BizSimCard::getImei, entity.getImei())
                .ne(entity.getSimId() != null, BizSimCard::getSimId, entity.getSimId()));
            if (imeiCount > 0) {
                throw new ServiceException("IMEI已存在");
            }
        }
        if (StringUtils.isNotBlank(entity.getPhoneNumber())) {
            Long phoneCount = baseMapper.selectCount(new LambdaQueryWrapper<BizSimCard>()
                .eq(BizSimCard::getPhoneNumber, entity.getPhoneNumber())
                .ne(entity.getSimId() != null, BizSimCard::getSimId, entity.getSimId()));
            if (phoneCount > 0) {
                throw new ServiceException("手机号已存在");
            }
        }
        if (entity.getActivationTime() != null
            && entity.getExpireTime() != null
            && entity.getActivationTime().after(entity.getExpireTime())) {
            throw new ServiceException("开卡时间不能晚于到期时间");
        }
    }

    /**
     * 校验并批量删除SIM卡信息
     *
     * @param ids 待删除的主键集合
     * @param isValid 是否进行有效性校验
     * @return 是否删除成功
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if (isValid) {
            Long count = bindMapper.selectCount(Wrappers.lambdaQuery(BizVehicleEquipmentBind.class)
                .eq(BizVehicleEquipmentBind::getEquipmentType, EquipmentTypeConstants.SIM_CARD)
                .in(BizVehicleEquipmentBind::getEquipmentId, ids));
            if (count > 0) {
                throw new ServiceException("SIM卡已绑定车辆，不允许删除");
            }
        }
        return baseMapper.deleteByIds(ids) > 0;
    }

}
