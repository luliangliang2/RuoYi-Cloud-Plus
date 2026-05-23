package org.dromara.manager.service.impl;

import org.dromara.common.core.utils.MapstructUtils;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.manager.mapper.BizVehicleMapper;
import org.dromara.common.mybatis.core.page.PageQuery;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Collection;
import org.dromara.manager.api.IBizVehicleService;
import org.dromara.manager.api.domain.BizVehicle;
import org.dromara.manager.api.domain.bo.BizVehicleBo;
import org.dromara.manager.api.domain.vo.BizVehicleVo;
/**
 * 车辆管理Service业务层处理
 *
 * @author LionLi
 * @date 2026-05-21
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class BizVehicleServiceImpl implements IBizVehicleService {

    private final BizVehicleMapper baseMapper;

    /**
     * 查询车辆管理
     *
     * @param id 主键
     * @return 车辆管理
     */
    @Override
    public BizVehicleVo queryById(Long id){
        return baseMapper.selectVehicleVoById(id);
    }

    /**
     * 分页查询车辆管理列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 车辆管理分页列表
     */
    @Override
    public TableDataInfo<BizVehicleVo> queryPageList(BizVehicleBo bo, PageQuery pageQuery) {
        BizVehicle query = MapstructUtils.convert(bo, BizVehicle.class);
        Page<BizVehicleVo> result = baseMapper.selectVehiclePage(pageQuery.build(), query);
        return TableDataInfo.build(result);
    }

    /**
     * 查询符合条件的车辆管理列表
     *
     * @param bo 查询条件
     * @return 车辆管理列表
     */
    @Override
    public List<BizVehicleVo> queryList(BizVehicleBo bo) {
        BizVehicle query = MapstructUtils.convert(bo, BizVehicle.class);
        return baseMapper.selectVehicleList(query);
    }

    /**
     * 新增车辆管理
     *
     * @param bo 车辆管理
     * @return 是否新增成功
     */
    @Override
    public Boolean insertByBo(BizVehicleBo bo) {
        BizVehicle add = MapstructUtils.convert(bo, BizVehicle.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改车辆管理
     *
     * @param bo 车辆管理
     * @return 是否修改成功
     */
    @Override
    public Boolean updateByBo(BizVehicleBo bo) {
        BizVehicle update = MapstructUtils.convert(bo, BizVehicle.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(BizVehicle entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 校验并批量删除车辆管理信息
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
