package org.ssssssss.magicapi.hive.service;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.ssssssss.magicapi.core.config.JsonCodeConstants;
import org.ssssssss.magicapi.core.model.JsonCode;
import org.ssssssss.magicapi.core.model.MagicEntity;
import org.ssssssss.magicapi.core.service.MagicResourceService;
import org.ssssssss.magicapi.core.service.MagicResourceStorage;
import org.ssssssss.magicapi.hive.model.HiveInfo;
import org.ssssssss.magicapi.utils.JsonUtils;

public class HiveMagicResourceStorage implements MagicResourceStorage<HiveInfo>, JsonCodeConstants {

	private MagicResourceService magicResourceService;

	@Override
	public String folder() {
		return "hive";
	}

	@Override
	public String suffix() {
		return ".json";
	}

	@Override
	public Class<HiveInfo> magicClass() {
		return HiveInfo.class;
	}

	@Override
	public boolean requirePath() {
		return false;
	}

	@Override
	public boolean requiredScript() {
		return false;
	}

	@Override
	public boolean allowRoot() {
		return true;
	}

	@Override
	public String buildMappingKey(HiveInfo info) {
		return String.format("%s-%s", info.getUrl(), System.currentTimeMillis());
	}

	@Override
	public void validate(HiveInfo entity) {
		notBlank(entity.getUrl(), DS_KEY_REQUIRED);
		notBlank(entity.getDatabase(), new JsonCode(1020, "Hive database 不能为空"));

		boolean noneMatchKey = magicResourceService.listFiles("hive:0").stream()
				.map(it -> (HiveInfo)it)
				.filter(it -> !it.getId().equals(entity.getId()))
				.noneMatch(it -> Objects.equals(it.getUrl(), entity.getUrl()));
		isTrue(noneMatchKey, DS_KEY_CONFLICT);
	}

	@Override
	public void setMagicResourceService(MagicResourceService magicResourceService) {
		this.magicResourceService = magicResourceService;
	}

	@Override
	public HiveInfo read(byte[] bytes) {
		return JsonUtils.readValue(bytes, HiveInfo.class);
	}

	@Override
	public byte[] write(MagicEntity entity) {
		return JsonUtils.toJsonBytes(entity);
	}
}
