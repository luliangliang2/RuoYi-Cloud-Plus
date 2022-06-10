package com.project.commodity.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.project.common.core.utils.StringUtils;
import com.project.common.mybatis.core.page.PageQuery;
import com.project.common.mybatis.core.page.TableDataInfo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.project.commodity.domain.bo.OrderDetailBo;
import com.project.commodity.domain.vo.OrderDetailVo;
import com.project.commodity.domain.OrderDetail;
import com.project.commodity.mapper.OrderDetailMapper;
import com.project.commodity.service.IOrderDetailService;

import java.util.List;
import java.util.Map;
import java.util.Collection;

/**
 * 订单详情Service业务层处理
 *
 * @author huan.li
 * @date 2022-06-10
 */
@RequiredArgsConstructor
@Service
public class OrderDetailServiceImpl implements IOrderDetailService {

    private final OrderDetailMapper baseMapper;

    /**
     * 查询订单详情
     *
     * @param id 订单详情主键
     * @return 订单详情
     */
    @Override
    public OrderDetailVo queryById(Long id){
        return baseMapper.selectVoById(id);
    }

    /**
     * 查询订单详情列表
     *
     * @param bo 订单详情
     * @return 订单详情
     */
    @Override
    public TableDataInfo<OrderDetailVo> queryPageList(OrderDetailBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<OrderDetail> lqw = buildQueryWrapper(bo);
        Page<OrderDetailVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询订单详情列表
     *
     * @param bo 订单详情
     * @return 订单详情
     */
    @Override
    public List<OrderDetailVo> queryList(OrderDetailBo bo) {
        LambdaQueryWrapper<OrderDetail> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<OrderDetail> buildQueryWrapper(OrderDetailBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<OrderDetail> lqw = Wrappers.lambdaQuery();
        lqw.eq(StringUtils.isNotBlank(bo.getOrderId()), OrderDetail::getOrderId, bo.getOrderId());
        lqw.eq(StringUtils.isNotBlank(bo.getOrderNum()), OrderDetail::getOrderNum, bo.getOrderNum());
        lqw.eq(bo.getProductId() != null, OrderDetail::getProductId, bo.getProductId());
        lqw.eq(StringUtils.isNotBlank(bo.getProductPic()), OrderDetail::getProductPic, bo.getProductPic());
        lqw.like(StringUtils.isNotBlank(bo.getProductName()), OrderDetail::getProductName, bo.getProductName());
        lqw.eq(bo.getProductPrice() != null, OrderDetail::getProductPrice, bo.getProductPrice());
        lqw.eq(bo.getProductQuantity() != null, OrderDetail::getProductQuantity, bo.getProductQuantity());
        lqw.eq(bo.getProductCategoryId() != null, OrderDetail::getProductCategoryId, bo.getProductCategoryId());
        lqw.eq(bo.getTotalPrice() != null, OrderDetail::getTotalPrice, bo.getTotalPrice());
        return lqw;
    }

    /**
     * 新增订单详情
     *
     * @param bo 订单详情
     * @return 结果
     */
    @Override
    public Boolean insertByBo(OrderDetailBo bo) {
        OrderDetail add = BeanUtil.toBean(bo, OrderDetail.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改订单详情
     *
     * @param bo 订单详情
     * @return 结果
     */
    @Override
    public Boolean updateByBo(OrderDetailBo bo) {
        OrderDetail update = BeanUtil.toBean(bo, OrderDetail.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     *
     * @param entity 实体类数据
     */
    private void validEntityBeforeSave(OrderDetail entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 批量删除订单详情
     *
     * @param ids 需要删除的订单详情主键
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
