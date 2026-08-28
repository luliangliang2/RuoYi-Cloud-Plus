package org.ssssssss.magicapi.apistartup;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.web.context.WebServerApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.ssssssss.magicapi.apistartup.service.ApiStartupDynamicRegistry;
import org.ssssssss.magicapi.apistartup.service.ApiStartupResourceStorage;
import org.ssssssss.magicapi.apistartup.service.ApiStartupRunner;
import org.ssssssss.magicapi.core.config.MagicAPIProperties;
import org.ssssssss.magicapi.core.config.MagicPluginConfiguration;
import org.ssssssss.magicapi.core.model.Plugin;
import org.ssssssss.magicapi.core.service.MagicAPIService;
import org.ssssssss.magicapi.core.service.MagicResourceService;

@Configuration
public class MagicApiStartupConfiguration implements MagicPluginConfiguration {

	@Override
	public Plugin plugin() {
		return new Plugin("开机执行接口", "MagicApiStartup", "magic-api-startup.1.0.0.iife.js");
	}

	@Bean(name = "apiStartupResourceStorage")
	@ConditionalOnMissingBean
	public ApiStartupResourceStorage apiStartupResourceStorage() {
		return new ApiStartupResourceStorage();
	}

	@Bean
	@ConditionalOnMissingBean
	public ApiStartupDynamicRegistry apiStartupDynamicRegistry(
			@Qualifier("apiStartupResourceStorage") ApiStartupResourceStorage apiStartupResourceStorage) {
		return new ApiStartupDynamicRegistry(apiStartupResourceStorage);
	}

	@Bean
	@ConditionalOnMissingBean
	public ApiStartupRunner apiStartupRunner(MagicResourceService magicResourceService,
											MagicAPIProperties properties,
											WebServerApplicationContext applicationContext,
											MagicAPIService magicAPIService) {
		return new ApiStartupRunner(magicResourceService, properties, applicationContext, magicAPIService);
	}
}
