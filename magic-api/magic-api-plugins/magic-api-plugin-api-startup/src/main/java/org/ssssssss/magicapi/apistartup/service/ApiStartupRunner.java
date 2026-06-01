package org.ssssssss.magicapi.apistartup.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.boot.web.context.WebServerApplicationContext;
import org.ssssssss.magicapi.apistartup.model.ApiStartupInfo;
import org.ssssssss.magicapi.core.config.MagicAPIProperties;
import org.ssssssss.magicapi.core.model.ApiInfo;
import org.ssssssss.magicapi.core.service.MagicAPIService;
import org.ssssssss.magicapi.core.service.MagicResourceService;
import org.ssssssss.magicapi.utils.PathUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

public class ApiStartupRunner {

	private static final Logger logger = LoggerFactory.getLogger(ApiStartupRunner.class);

	private final MagicResourceService magicResourceService;

	private final MagicAPIProperties properties;

	private final WebServerApplicationContext applicationContext;

	private final MagicAPIService magicAPIService;

	private final AtomicBoolean started = new AtomicBoolean(false);

	public ApiStartupRunner(MagicResourceService magicResourceService,
							MagicAPIProperties properties,
							WebServerApplicationContext applicationContext,
							MagicAPIService magicAPIService) {
		this.magicResourceService = magicResourceService;
		this.properties = properties;
		this.applicationContext = applicationContext;
		this.magicAPIService = magicAPIService;
	}

	@EventListener(ApplicationReadyEvent.class)
	public void run() {
		if (!started.compareAndSet(false, true)) {
			return;
		}
		List<ApiStartupInfo> infos = magicResourceService.files("api-startup");
		if (infos == null || infos.isEmpty()) {
			return;
		}
		for (ApiStartupInfo info : infos) {
			if (info == null || !info.isEnabled()) {
				continue;
			}
			for (String apiId : info.getApiIds()) {
				ApiInfo apiInfo = magicResourceService.file(apiId);
				if (apiInfo == null) {
					logger.warn("开机接口插件 [{}] 跳过不存在的接口: {}", info.getName(), apiId);
					continue;
				}
				executeApi(info, apiInfo);
			}
		}
	}

	private void executeApi(ApiStartupInfo info, ApiInfo apiInfo) {
		String executePath = PathUtils.replaceSlash("/" +
				Objects.toString(magicResourceService.getGroupPath(apiInfo.getGroupId()), "") + "/" +
				Objects.toString(apiInfo.getPath(), ""));
		String method = Objects.toString(apiInfo.getMethod(), "GET").toUpperCase();
		Map<String, Object> context = new HashMap<>();
		context.put("startup", true);
		context.put("startupInfo", info);
		context.put("serverPort", applicationContext.getWebServer().getPort());
		try {
			Object result = magicAPIService.execute(method, executePath, context);
			logger.info("开机接口执行成功: [{}] -> {} {} result={}", info.getName(), method, executePath, result);
		} catch (Exception e) {
			logger.error("开机接口执行失败: [{}] -> {} {}", info.getName(), method, executePath, e);
		}
	}
}
