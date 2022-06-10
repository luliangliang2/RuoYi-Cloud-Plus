package com.project.commodity.service;

import com.project.commodity.domain.OrderDetail;
import com.project.commodity.domain.vo.OrderDetailVo;
import com.project.commodity.domain.bo.OrderDetailBo;
import com.project.common.mybatis.core.page.PageQuery;
import com.project.common.mybatis.core.page.TableDataInfo;

import java.util.Collection;
import java.util.List;

/**
 * 订单详情Service接口
 *
 * @author huan.li
 * @date 2022-06-10
 */
public interface IOrderDetailService {

    /**
     * 查询订单详情
     *
     * @param id 订单详情主键
     * @return 订单详情
     */
    OrderDetailVo queryById(Long id);

    /**
     * 查询订单详情列表
     *
     * @param orderDetail 订单详情
     * @return 订单详情集合
     */
    TableDataInfo<OrderDetailVo> queryPageList(OrderDetailBo bo, PageQuery pageQuery);

    /**
     * 查询订单详情列表
     *
     * @param orderDetail 订单详情
     * @return 订单详情集合
     */
    List<OrderDetailVo> queryList(OrderDetailBo bo);

    /**
     * 修改订单详情
     *
     * @param orderDetail 订单详情
     * @return 结果
     */
    Boolean insertByBo(OrderDetailBo bo);

    /**
     * 修改订单详情
     *
     * @param orderDetail 订单详情
     * @return 结果
     */
    Boolean updateByBo(OrderDetailBo bo);

    /**
     * 校验并批量删除订单详情信息
     *
     * @param ids 需要删除的订单详情主键集合
     * @param isValid 是否校验,true-删除前校验,false-不校验
     * @return 结果
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}
