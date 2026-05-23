package org.ssssssss.magicapi.hive.starter;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.ssssssss.magicapi.core.config.MagicPluginConfiguration;
import org.ssssssss.magicapi.core.model.Plugin;
import org.ssssssss.magicapi.core.web.MagicControllerRegister;
import org.ssssssss.magicapi.hive.HiveModule;

@Configuration
public class MagicHiveConfiguration implements MagicPluginConfiguration {

	@Override
	public Plugin plugin() {
		return new Plugin("hive", "MagicHive", "magic-hive.1.0.0.iife.js");
	}

	@Override
	public MagicControllerRegister controllerRegister() {
		return (mapping, configuration) -> {
			// No controller to register
		};
	}

	@Bean(name = "magicHiveModule")
	public HiveModule magicHiveModule() {
		return new HiveModule();
	}

}
