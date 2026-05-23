package org.ssssssss.magicapi.apistartup.service;

import org.springframework.context.event.EventListener;
import org.ssssssss.magicapi.apistartup.model.ApiStartupInfo;
import org.ssssssss.magicapi.core.event.FileEvent;
import org.ssssssss.magicapi.core.service.AbstractMagicDynamicRegistry;
import org.ssssssss.magicapi.core.service.MagicResourceStorage;

public class ApiStartupDynamicRegistry extends AbstractMagicDynamicRegistry<ApiStartupInfo> {

	public ApiStartupDynamicRegistry(MagicResourceStorage<ApiStartupInfo> magicResourceStorage) {
		super(magicResourceStorage);
	}

	@EventListener(condition = "#event.type == 'api-startup'")
	public void onFileEvent(FileEvent event) {
		processEvent(event);
	}
}
