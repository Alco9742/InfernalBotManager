package net.nilsghesquiere.util.wrappers;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;

import net.nilsghesquiere.entities.Metric;

public class MetricWrapper implements Serializable{
	private static final long serialVersionUID = 1L;
	private Map<String, List<Metric>> map;
	private String error;

	public MetricWrapper() {
		this.map = new HashMap<String, List<Metric>>();
		this.setError("");
	}	
	
	@JsonAnySetter 
	public void add(String key, List<Metric> metrics) {
		map.put(key, metrics);
	}

	@JsonAnyGetter
	public Map<String, List<Metric>> getMap() {
		return map;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}
}
