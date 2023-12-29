package org.dromara.resource.service;

import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.resource.domain.bo.SysEmailLogBo;
import org.dromara.resource.domain.vo.SysEmailLogVo;

import java.util.List;

/**
 * 邮件日志Service接口
 *
 * @author 2100
 */
public interface ISysEmailLogService {

    /**
     * 根据邮件日志主键查询单个邮件日志
     *
     * @param emailId 邮件日志主键
     * @return 查询到的邮件日志，如果不存在则返回 null
     */
    SysEmailLogVo queryById(Long emailId);

    /**
     * 分页查询邮件日志列表
     *
     * @param bo        包含查询条件的业务对象
     * @param pageQuery 分页查询参数
     * @return 分页查询结果
     */
    TableDataInfo<SysEmailLogVo> queryPageList(SysEmailLogBo bo, PageQuery pageQuery);

    /**
     * 查询邮件日志列表
     *
     * @param bo 包含查询条件的业务对象
     * @return 邮件日志列表
     */
    List<SysEmailLogVo> queryList(SysEmailLogBo bo);

    /**
     * 插入邮件日志数据
     *
     * @param bo 包含邮件日志信息的业务对象
     * @return 插入是否成功，true 表示成功，false 表示失败
     */
    Boolean insertByBo(SysEmailLogBo bo);

}
