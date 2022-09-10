package com.ruoyi.db.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import com.alibaba.druid.pool.DruidDataSource;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.common.core.exception.ServiceException;
import com.ruoyi.common.core.utils.StringUtils;
import com.ruoyi.common.mybatis.core.page.PageQuery;
import com.ruoyi.common.mybatis.core.page.TableDataInfo;
import com.ruoyi.db.constant.DatabaseConstant;
import com.ruoyi.db.domain.SysDatabaseConfig;
import com.ruoyi.db.domain.bo.SysDatabaseConfigBo;
import com.ruoyi.db.domain.vo.SysDatabaseConfigVo;
import com.ruoyi.db.mapper.SysDatabaseConfigMapper;
import com.ruoyi.db.service.ISysDatabaseConfigService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

/**
 * 数据库配置Service业务层处理
 *
 * @author lishuyan
 * @date 2022-08-29
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class SysDatabaseConfigServiceImpl implements ISysDatabaseConfigService {

    private final SysDatabaseConfigMapper baseMapper;

    /**
     * 查询数据库配置
     */
    @Override
    public SysDatabaseConfigVo queryById(Long dbId) {
        return baseMapper.selectVoById(dbId);
    }

    /**
     * 查询数据库配置列表
     */
    @Override
    public TableDataInfo<SysDatabaseConfigVo> queryPageList(SysDatabaseConfigBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<SysDatabaseConfig> lqw = buildQueryWrapper(bo);
        Page<SysDatabaseConfigVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询数据库配置列表
     */
    @Override
    public List<SysDatabaseConfigVo> queryList(SysDatabaseConfigBo bo) {
        LambdaQueryWrapper<SysDatabaseConfig> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<SysDatabaseConfig> buildQueryWrapper(SysDatabaseConfigBo bo) {
        LambdaQueryWrapper<SysDatabaseConfig> lqw = Wrappers.lambdaQuery();
        lqw.like(StringUtils.isNotBlank(bo.getName()), SysDatabaseConfig::getName, bo.getName());
        lqw.like(StringUtils.isNotBlank(bo.getUsername()), SysDatabaseConfig::getUsername, bo.getUsername());
        lqw.eq(StringUtils.isNotBlank(bo.getStatus()), SysDatabaseConfig::getStatus, bo.getStatus());
        return lqw;
    }

    /**
     * 新增数据库配置
     */
    @Override
    public Boolean insertByBo(SysDatabaseConfigBo bo) {
        SysDatabaseConfig add = BeanUtil.toBean(bo, SysDatabaseConfig.class);
        validEntityBeforeSave(add);
        add.setPassword(SecureUtil.aes(DatabaseConstant.KEY).encryptHex(add.getPassword()));
        return baseMapper.insert(add) > 0;
    }

    /**
     * 修改数据库配置
     */
    @Override
    public Boolean updateByBo(SysDatabaseConfigBo bo) {
        String password = baseMapper.selectById(bo.getDbId()).getPassword();

        SysDatabaseConfig update = BeanUtil.toBean(bo, SysDatabaseConfig.class);
        validEntityBeforeSave(update);
        //比对密码是否一致，不一致重新加密
        if (!StrUtil.equals(password, update.getPassword())) {
            update.setPassword(SecureUtil.aes(DatabaseConstant.KEY).encryptHex(update.getPassword()));
        }

        LambdaUpdateWrapper<SysDatabaseConfig> luw = new LambdaUpdateWrapper<>();
        luw.set(ObjectUtil.isNull(update.getRemark()), SysDatabaseConfig::getRemark, "");
        luw.eq(SysDatabaseConfig::getDbId, update.getDbId());
        return baseMapper.update(update, luw) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(SysDatabaseConfig entity) {
        //做一些数据校验,如唯一约束
    }

    /**
     * 批量删除数据库配置
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if (isValid) {
            if (CollUtil.containsAny(ids, DatabaseConstant.SYSTEM_DATA_IDS)) {
                throw new ServiceException("系统内置, 不可删除!");
            }
        }
        return baseMapper.deleteBatchIds(ids) > 0;
    }

    /**
     * 启用禁用状态
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateDatabaseConfigStatus(SysDatabaseConfigBo bo) {
        SysDatabaseConfig sysDatabaseConfig = BeanUtil.toBean(bo, SysDatabaseConfig.class);
        int row = baseMapper.update(null, new LambdaUpdateWrapper<SysDatabaseConfig>()
            .set(SysDatabaseConfig::getStatus, "1"));
        row += baseMapper.updateById(sysDatabaseConfig);
        return row;
    }

    /**
     * 测试连接数据库
     */
    @Override
    public boolean testConnection(SysDatabaseConfigBo bo) {
        try {
            return null != getConnection(bo.getUrl(), bo.getUsername(), SecureUtil.aes(DatabaseConstant.KEY).decryptStr(bo.getPassword()));
        } catch (Exception e) {
            log.error(e.getMessage());
            return false;
        }
    }

    /**
     * 获取数据库连接
     *
     * @param jdbcUrl  连接地址
     * @param userName 用户名
     * @param password 密码
     * @return 数据库连接对象
     */
    private static Connection getConnection(String jdbcUrl, String userName, String password) {
        DataSource dataSource = getDataSource(jdbcUrl, userName, password);
        if (null == dataSource) return null;
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
        } catch (Exception ignored) {
            return null;
        } finally {
            release(connection);
        }
        return connection;
    }

    /**
     * 获取数据源
     */
    private static DataSource getDataSource(String url, String userName, String password) {
        DruidDataSource druidDataSource = new DruidDataSource();
        String className;
        try {
            className = DriverManager.getDriver(url).getClass().getName();
        } catch (Exception e) {
            return null;
        }
        druidDataSource.setDriverClassName(className);
        druidDataSource.setUrl(url);
        druidDataSource.setUsername(userName);
        druidDataSource.setPassword(password);
        // 配置获取连接等待超时的时间
        druidDataSource.setMaxWait(3000);
        // 配置初始化大小、最小、最大
        druidDataSource.setInitialSize(1);
        druidDataSource.setMinIdle(1);
        druidDataSource.setMaxActive(1);
        // 如果链接出现异常则直接判定为失败而不是一直重试
        druidDataSource.setBreakAfterAcquireFailure(true);
        try {
            druidDataSource.init();
        } catch (Exception e) {
            log.error("Exception during pool initialization", e);
            return null;
        }
        return druidDataSource;
    }

    /**
     * 释放连接资源
     */
    private static void release(Connection connection) {
        try {
            if (connection != null) {
                connection.close();
                log.info("database connection close success");
            }
        } catch (SQLException e) {
            log.error(e.getMessage(), e);
            log.error("database connection close error：" + e.getMessage());
        }
    }
}
