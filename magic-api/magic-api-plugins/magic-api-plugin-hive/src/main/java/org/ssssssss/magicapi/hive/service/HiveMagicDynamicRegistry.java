package org.ssssssss.magicapi.hive.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.ssssssss.magicapi.core.event.FileEvent;
import org.ssssssss.magicapi.core.service.AbstractMagicDynamicRegistry;
import org.ssssssss.magicapi.core.service.MagicResourceStorage;
import org.ssssssss.magicapi.hive.model.HiveInfo;
import org.ssssssss.magicapi.hive.model.MagicDynamicHiveClient;
import org.ssssssss.magicapi.hive.util.HiveDataSource;

public class HiveMagicDynamicRegistry extends AbstractMagicDynamicRegistry<HiveInfo> {

	private final MagicDynamicHiveClient magicDynamicHiveClient;

	private static final Logger logger = LoggerFactory.getLogger(HiveMagicDynamicRegistry.class);

	public HiveMagicDynamicRegistry(MagicResourceStorage<HiveInfo> magicResourceStorage,
			MagicDynamicHiveClient magicDynamicHiveClient) {
		super(magicResourceStorage);
		this.magicDynamicHiveClient = magicDynamicHiveClient;
	}

	@EventListener(condition = "#event.type == 'hive'")
	public void onFileEvent(FileEvent event) {
		try {
			processEvent(event);
		} catch (Exception e) {
			logger.error("注册Hive数据源失败", e);
		}
	}

	@Override
	protected boolean register(MappingNode<HiveInfo> mappingNode) {
		HiveInfo info = mappingNode.getEntity();
		try {
			HiveDataSource hiveDataSource = new HiveDataSource(info);
			// Test connection
			if(hiveDataSource.testConnection()) {
				magicDynamicHiveClient.put(info.getId(), info.getUrl(), info.getDatabase(), hiveDataSource);
				return true;
			}
		}catch(Exception e) {
			logger.error("注册Hive数据源失败", e);
			return false;
		}
		return false;
	}

	@Override
	protected void unregister(MappingNode<HiveInfo> mappingNode) {
		magicDynamicHiveClient.delete(mappingNode.getEntity().getId());
	}
}
