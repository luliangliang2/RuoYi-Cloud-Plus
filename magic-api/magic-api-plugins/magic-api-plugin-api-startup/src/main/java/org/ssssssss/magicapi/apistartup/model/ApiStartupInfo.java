package org.ssssssss.magicapi.apistartup.model;

import org.ssssssss.magicapi.core.model.MagicEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ApiStartupInfo extends MagicEntity {

	private String key;

	private boolean enabled = true;

	private String description;

	private List<String> apiIds = new ArrayList<>();

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<String> getApiIds() {
		return apiIds;
	}

	public void setApiIds(List<String> apiIds) {
		this.apiIds = apiIds == null ? new ArrayList<>() : new ArrayList<>(apiIds);
	}

	@Override
	public MagicEntity simple() {
		ApiStartupInfo target = new ApiStartupInfo();
		super.simple(target);
		target.setKey(this.key);
		target.setEnabled(this.enabled);
		target.setApiIds(this.apiIds);
		return target;
	}

	@Override
	public MagicEntity copy() {
		ApiStartupInfo target = new ApiStartupInfo();
		super.copyTo(target);
		target.setKey(this.key);
		target.setEnabled(this.enabled);
		target.setDescription(this.description);
		target.setApiIds(this.apiIds);
		return target;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof ApiStartupInfo)) return false;
		if (!super.equals(o)) return false;
		ApiStartupInfo that = (ApiStartupInfo) o;
		return enabled == that.enabled &&
				Objects.equals(key, that.key) &&
				Objects.equals(description, that.description) &&
				Objects.equals(apiIds, that.apiIds);
	}

	@Override
	public int hashCode() {
		return Objects.hash(super.hashCode(), key, enabled, description, apiIds);
	}
}
