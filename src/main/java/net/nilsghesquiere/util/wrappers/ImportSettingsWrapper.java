package net.nilsghesquiere.util.wrappers;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import net.nilsghesquiere.entities.ImportSettings;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;

public class ImportSettingsWrapper implements Serializable {
	private static final long serialVersionUID = 1L;
	private Map<String, ImportSettings> map;
	private String error;

	public ImportSettingsWrapper() {
		this.map = new HashMap<String, ImportSettings>();
		this.setError("");
	}	
	
	@JsonAnySetter 
	public void add(String key, ImportSettings importSettings) {
		map.put(key, importSettings);
	}

	@JsonAnyGetter
	public Map<String, ImportSettings> getMap() {
		return map;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}
}
