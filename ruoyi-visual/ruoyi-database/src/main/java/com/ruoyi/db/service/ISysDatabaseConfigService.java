package com.ruoyi.db.service;

import com.ruoyi.common.mybatis.core.page.PageQuery;
import com.ruoyi.common.mybatis.core.page.TableDataInfo;
import com.ruoyi.db.domain.bo.SysDatabaseConfigBo;
import com.ruoyi.db.domain.vo.SysDatabaseConfigVo;

import java.util.Collection;
import java.util.List;

/**
 * 数据库配置Service接口
 *
 * @author lishuyan
 * @date 2022-08-29
 */
public interface ISysDatabaseConfigService {

    /**
     * 查询数据库配置
     */
    SysDatabaseConfigVo queryById(Long dbId);

    /**
     * 查询数据库配置列表
     */
    TableDataInfo<SysDatabaseConfigVo> queryPageList(SysDatabaseConfigBo bo, PageQuery pageQuery);

    /**
     * 查询数据库配置列表
     */
    List<SysDatabaseConfigVo> queryList(SysDatabaseConfigBo bo);

    /**
     * 修改数据库配置
     */
    Boolean insertByBo(SysDatabaseConfigBo bo);

    /**
     * 修改数据库配置
     */
    Boolean updateByBo(SysDatabaseConfigBo bo);

    /**
     * 校验并批量删除数据库配置信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

    /**
     * 启用停用状态
     */
    int updateDatabaseConfigStatus(SysDatabaseConfigBo bo);

    /**
     * 测试连接数据库
     */
    boolean testConnection(SysDatabaseConfigBo bo);
}
