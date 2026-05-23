package org.ssssssss.magicapi.hive.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.ssssssss.magicapi.hive.HiveModule;
import org.ssssssss.magicapi.hive.util.HiveDataSource;
import org.ssssssss.magicapi.utils.Assert;

/**
 * 动态Hive客户端对象
 */
public class MagicDynamicHiveClient {

	private static final Logger logger = LoggerFactory.getLogger(MagicDynamicHiveClient.class);

	private final Map<String, HiveDataSource> dataSourceMap = new HashMap<>();
	private final Map<String, HiveModule> hiveModuleMap = new HashMap<>();

	/**
	 * 注册数据源（可以运行时注册）
	 *
	 * @param id             数据源ID
	 * @param dataSourceKey  数据源Key
	 * @param datasourceName 数据源名称
	 */
	public void put(String id, String dataSourceKey, String datasourceName, HiveDataSource hiveDataSource) {
		if (dataSourceKey == null) {
			dataSourceKey = "";
		}
		logger.info("注册Hive数据源：{}", StringUtils.isNotBlank(dataSourceKey) ? dataSourceKey : "没有注册任何数据源");
		this.dataSourceMap.put(dataSourceKey, hiveDataSource);

		this.hiveModuleMap.put(dataSourceKey, new HiveModule());

		if (id != null) {
			String finalDataSourceKey = dataSourceKey;
			this.dataSourceMap.entrySet().stream()
					.filter(it -> id.equals(it.getValue().getId()) && !finalDataSourceKey.equals(it.getKey()))
					.findFirst()
					.ifPresent(it -> {
						logger.info("移除Hive旧数据源:{}", it.getKey());
						this.dataSourceMap.remove(it.getKey()).close();
						this.hiveModuleMap.remove(it.getKey());
					});
		}
	}

	/**
	 * 获取全部数据源
	 */
	public List<String> datasources() {
		return new ArrayList<>(this.dataSourceMap.keySet());
	}

	public boolean isEmpty() {
		return this.dataSourceMap.isEmpty();
	}

	/**
	 * 获取全部数据源
	 */
	public Collection<HiveDataSource> datasourceNodes() {
		return this.dataSourceMap.values();
	}

	/**
	 * 删除数据源
	 *
	 * @param datasourceKey 数据源Key
	 */
	public boolean delete(String datasourceKey) {
		boolean result = false;
		// 检查参数是否合法
		if (datasourceKey != null && !datasourceKey.isEmpty()) {
			if (this.dataSourceMap.containsKey(datasourceKey)) {
				this.dataSourceMap.remove(datasourceKey).close();
				this.hiveModuleMap.remove(datasourceKey);
				result = true;
			}
		}
		logger.info("删除Hive数据源：{}:{}", datasourceKey, result ? "成功" : "失败");
		return result;
	}

	/**
	 * 获取数据源
	 *
	 * @param datasourceKey 数据源Key
	 */
	public HiveDataSource getDataSource(String datasourceKey) {
		HiveDataSource hiveDataSource = dataSourceMap.get(datasourceKey);
		Assert.isNotNull(hiveDataSource, String.format("找不到Hive数据源%s", datasourceKey));
		return hiveDataSource;
	}

	/**
	 * 获取module
	 *
	 * @param datasourceKey 数据源Key
	 */
	public HiveModule getModule(String datasourceKey) {
		HiveModule hiveModule = hiveModuleMap.get(datasourceKey);
		Assert.isNotNull(hiveModule, String.format("找不到 Hive 可用 module %s", datasourceKey));
		return hiveModule;
	}
}
