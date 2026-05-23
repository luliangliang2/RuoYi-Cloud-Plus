package org.ssssssss.magicapi.apistartup.service;

import org.ssssssss.magicapi.apistartup.model.ApiStartupInfo;
import org.ssssssss.magicapi.core.config.JsonCodeConstants;
import org.ssssssss.magicapi.core.model.JsonCode;
import org.ssssssss.magicapi.core.model.MagicEntity;
import org.ssssssss.magicapi.core.service.MagicResourceService;
import org.ssssssss.magicapi.core.service.MagicResourceStorage;
import org.ssssssss.magicapi.utils.JsonUtils;

import java.util.List;
import java.util.Objects;

public class ApiStartupResourceStorage implements MagicResourceStorage<ApiStartupInfo>, JsonCodeConstants {

	private MagicResourceService magicResourceService;

	@Override
	public String folder() {
		return "api-startup";
	}

	@Override
	public String suffix() {
		return ".json";
	}

	@Override
	public Class<ApiStartupInfo> magicClass() {
		return ApiStartupInfo.class;
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
	public String buildMappingKey(ApiStartupInfo entity) {
		return Objects.toString(entity.getKey(), "");
	}

	@Override
	public void validate(ApiStartupInfo entity) {
		notBlank(entity.getName(), new JsonCode(1100, "名称不能为空"));
		notBlank(entity.getKey(), DS_KEY_REQUIRED);
		List<String> apiIds = entity.getApiIds();
		isTrue(apiIds != null && !apiIds.isEmpty(), new JsonCode(1101, "至少选择一个接口"));
		boolean noneMatchKey = magicResourceService.listFiles("api-startup:0").stream()
				.map(it -> (ApiStartupInfo) it)
				.filter(it -> !Objects.equals(it.getId(), entity.getId()))
				.noneMatch(it -> Objects.equals(it.getKey(), entity.getKey()));
		isTrue(noneMatchKey, DS_KEY_CONFLICT);
	}

	@Override
	public void setMagicResourceService(MagicResourceService magicResourceService) {
		this.magicResourceService = magicResourceService;
	}

	@Override
	public ApiStartupInfo read(byte[] bytes) {
		return JsonUtils.readValue(bytes, ApiStartupInfo.class);
	}

	@Override
	public byte[] write(MagicEntity entity) {
		return JsonUtils.toJsonBytes(entity);
	}
}
