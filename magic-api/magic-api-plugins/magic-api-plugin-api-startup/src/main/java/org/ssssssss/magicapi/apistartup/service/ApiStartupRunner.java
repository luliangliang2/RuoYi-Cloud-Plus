package org.ssssssss.magicapi.apistartup.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.boot.web.context.WebServerApplicationContext;
import org.ssssssss.magicapi.apistartup.model.ApiStartupInfo;
import org.ssssssss.magicapi.core.config.MagicAPIProperties;
import org.ssssssss.magicapi.core.model.ApiInfo;
import org.ssssssss.magicapi.core.service.MagicResourceService;
import org.ssssssss.magicapi.utils.PathUtils;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

public class ApiStartupRunner {

	private static final Logger logger = LoggerFactory.getLogger(ApiStartupRunner.class);

	private final MagicResourceService magicResourceService;

	private final MagicAPIProperties properties;

	private final WebServerApplicationContext applicationContext;

	private final RestTemplate restTemplate = new RestTemplate();

	private final AtomicBoolean started = new AtomicBoolean(false);

	public ApiStartupRunner(MagicResourceService magicResourceService,
							MagicAPIProperties properties,
							WebServerApplicationContext applicationContext) {
		this.magicResourceService = magicResourceService;
		this.properties = properties;
		this.applicationContext = applicationContext;
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
		String baseUrl = buildBaseUrl();
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
				executeApi(info, apiInfo, baseUrl);
			}
		}
	}

	private void executeApi(ApiStartupInfo info, ApiInfo apiInfo, String baseUrl) {
		String requestPath = PathUtils.replaceSlash("/" +
				Objects.toString(properties.getPrefix(), "") + "/" +
				Objects.toString(magicResourceService.getGroupPath(apiInfo.getGroupId()), "") + "/" +
				Objects.toString(apiInfo.getPath(), ""));
		String url = baseUrl + requestPath;
		HttpMethod httpMethod = resolveMethod(apiInfo.getMethod());
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(List.of(MediaType.APPLICATION_JSON));
		if (requiresBody(httpMethod)) {
			headers.setContentType(MediaType.APPLICATION_JSON);
		}
		try {
			restTemplate.exchange(
					url,
					httpMethod,
					new HttpEntity<>(requiresBody(httpMethod) ? defaultBody(apiInfo) : null, headers),
					String.class
			);
			logger.info("开机接口执行成功: [{}] -> {} {}", info.getName(), httpMethod, requestPath);
		} catch (Exception e) {
			logger.error("开机接口执行失败: [{}] -> {} {}", info.getName(), httpMethod, requestPath, e);
		}
	}

	private String defaultBody(ApiInfo apiInfo) {
		return StringUtils.hasText(apiInfo.getRequestBody()) ? apiInfo.getRequestBody() : "{}";
	}

	private boolean requiresBody(HttpMethod method) {
		return HttpMethod.POST.equals(method) ||
				HttpMethod.PUT.equals(method) ||
				HttpMethod.PATCH.equals(method);
	}

	private HttpMethod resolveMethod(String method) {
		try {
			return HttpMethod.valueOf(Objects.toString(method, "GET").toUpperCase());
		} catch (Exception ignored) {
			return HttpMethod.GET;
		}
	}

	private String buildBaseUrl() {
		int port = applicationContext.getWebServer().getPort();
		String contextPath = applicationContext.getEnvironment().getProperty("server.servlet.context-path", "");
		return "http://127.0.0.1:" + port + PathUtils.replaceSlash("/" + Objects.toString(contextPath, ""));
	}
}
