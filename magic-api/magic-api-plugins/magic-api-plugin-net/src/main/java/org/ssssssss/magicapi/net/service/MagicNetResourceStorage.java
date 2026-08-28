package org.ssssssss.magicapi.net.service;

import org.ssssssss.magicapi.core.config.JsonCodeConstants;
import org.ssssssss.magicapi.core.model.JsonCode;
import org.ssssssss.magicapi.core.model.MagicEntity;
import org.ssssssss.magicapi.core.service.MagicResourceService;
import org.ssssssss.magicapi.core.service.MagicResourceStorage;
import org.ssssssss.magicapi.net.model.NetInfo;
import org.ssssssss.magicapi.utils.JsonUtils;

import java.util.Objects;

/**
 * Net 动态资源存储
 */
public class MagicNetResourceStorage implements MagicResourceStorage<NetInfo>, JsonCodeConstants {

    private MagicResourceService magicResourceService;

    @Override
    public String folder() {
        return "net";
    }

    @Override
    public String suffix() {
        return ".json";
    }

    @Override
    public Class<NetInfo> magicClass() {
        return NetInfo.class;
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
    public String buildMappingKey(NetInfo info) {
        return String.format("%s-%s", info.getKey(), info.getType());
    }

    @Override
    public void validate(NetInfo entity) {
        notBlank(entity.getKey(), DS_KEY_REQUIRED);
        notNull(entity.getType(), new JsonCode(1020, "net type 不能为空"));
        notNull(entity.getPort() > 0, new JsonCode(1021, "net port 必须大于 0"));

        // 验证 key 唯一性
        boolean noneMatchKey = magicResourceService.listFiles("net:0").stream()
                .map(it -> (NetInfo) it)
                .filter(it -> !it.getId().equals(entity.getId()))
                .noneMatch(it -> Objects.equals(it.getKey(), entity.getKey()));
        isTrue(noneMatchKey, DS_KEY_CONFLICT);
    }

    @Override
    public void setMagicResourceService(MagicResourceService magicResourceService) {
        this.magicResourceService = magicResourceService;
    }

    @Override
    public NetInfo read(byte[] bytes) {
        return JsonUtils.readValue(bytes, NetInfo.class);
    }

    @Override
    public byte[] write(MagicEntity entity) {
        return JsonUtils.toJsonBytes(entity);
    }
}
