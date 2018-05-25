package net.nilsghesquiere.util.wrappers;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import net.nilsghesquiere.entities.UserSettings;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;

public class UserSettingsWrapper implements Serializable {
	private static final long serialVersionUID = 1L;
	private Map<String, UserSettings> map;
	private String error;

	public UserSettingsWrapper() {
		this.map = new HashMap<String, UserSettings>();
		this.setError("");
	}	
	
	@JsonAnySetter 
	public void add(String key, UserSettings userSettings) {
		map.put(key, userSettings);
	}

	@JsonAnyGetter
	public Map<String, UserSettings> getMap() {
		return map;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}
}
