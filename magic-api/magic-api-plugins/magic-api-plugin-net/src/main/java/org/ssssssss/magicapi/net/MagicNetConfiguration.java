package org.ssssssss.magicapi.net;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.ssssssss.magicapi.core.config.MagicPluginConfiguration;
import org.ssssssss.magicapi.core.model.Plugin;
import org.ssssssss.magicapi.core.web.MagicControllerRegister;
import org.ssssssss.magicapi.net.service.MagicNetResourceStorage;
import org.ssssssss.magicapi.net.service.NetMagicDynamicRegistry;
import org.ssssssss.magicapi.net.web.MagicNetController;

@Configuration
public class MagicNetConfiguration implements MagicPluginConfiguration {

    @Bean
    public NetModule netFunctions() {
        return new NetModule();
    }

    @Override
    public Plugin plugin() {
        return new Plugin("Net网络连接", "net", "magic-net.1.0.0.iife.js");
    }

    @Override
    public MagicControllerRegister controllerRegister() {
        return (mapping, configuration) -> mapping.registerController(new MagicNetController(configuration));
    }

    @Bean(name = "magicNetResourceStorage")
    @ConditionalOnMissingBean
    public MagicNetResourceStorage magicNetResourceStorage() {
        return new MagicNetResourceStorage();
    }

    @Bean
    @ConditionalOnMissingBean
    public NetMagicDynamicRegistry netMagicDynamicRegistry(
            @Qualifier("magicNetResourceStorage") MagicNetResourceStorage magicNetResourceStorage) {
        return new NetMagicDynamicRegistry(magicNetResourceStorage);
    }
}
