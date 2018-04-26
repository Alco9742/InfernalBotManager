package net.nilsghesquiere.util.wrappers;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import net.nilsghesquiere.entities.ClientSettings;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;

public class ClientSettingsWrapper implements Serializable {
	private static final long serialVersionUID = 1L;
	private Map<String, ClientSettings> map;
	private String error;

	public ClientSettingsWrapper() {
		this.map = new HashMap<String, ClientSettings>();
		this.setError("");
	}	
	
	@JsonAnySetter 
	public void add(String key, ClientSettings clientSettings) {
		map.put(key, clientSettings);
	}

	@JsonAnyGetter
	public Map<String, ClientSettings> getMap() {
		return map;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}
}
