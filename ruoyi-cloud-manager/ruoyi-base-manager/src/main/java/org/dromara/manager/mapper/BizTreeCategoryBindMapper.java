package org.dromara.manager.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.dromara.common.mybatis.core.mapper.BaseMapperPlus;
import org.dromara.manager.domain.BizTreeCategoryBind;

import java.util.Collection;
import java.util.List;

/**
 * 分类业务绑定Mapper接口
 *
 * @author LionLi
 * @date 2026-05-25
 */
public interface BizTreeCategoryBindMapper extends BaseMapperPlus<BizTreeCategoryBind, BizTreeCategoryBind> {

    /**
     * 查询业务绑定的分类节点
     */
    default List<Long> selectNodeIds(String businessType, Long businessId) {
        return this.selectObjs(new LambdaQueryWrapper<BizTreeCategoryBind>()
                .select(BizTreeCategoryBind::getNodeId)
                .eq(BizTreeCategoryBind::getBusinessType, businessType)
                .eq(BizTreeCategoryBind::getBusinessId, businessId)
                .orderByAsc(BizTreeCategoryBind::getBindId))
            .stream()
            .map(item -> (Long) item)
            .toList();
    }

    /**
     * 删除业务绑定的分类节点
     */
    default void deleteByBusiness(String businessType, Long businessId) {
        this.delete(new LambdaQueryWrapper<BizTreeCategoryBind>()
            .eq(BizTreeCategoryBind::getBusinessType, businessType)
            .eq(BizTreeCategoryBind::getBusinessId, businessId));
    }

    /**
     * 删除多个业务绑定的分类节点
     */
    default void deleteByBusinessIds(String businessType, Collection<Long> businessIds) {
        this.delete(new LambdaQueryWrapper<BizTreeCategoryBind>()
            .eq(BizTreeCategoryBind::getBusinessType, businessType)
            .in(BizTreeCategoryBind::getBusinessId, businessIds));
    }

}
