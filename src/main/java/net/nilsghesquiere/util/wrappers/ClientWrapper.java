package net.nilsghesquiere.util.wrappers;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.nilsghesquiere.entities.Client;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;

public class ClientWrapper implements Serializable{
	private static final long serialVersionUID = 1L;
	private Map<String, List<Client>> map;
	private String error;

	public ClientWrapper() {
		this.map = new HashMap<String, List<Client>>();
		this.setError("");
	}	
	
	@JsonAnySetter 
	public void add(String key, List<Client> clients) {
		map.put(key, clients);
	}

	@JsonAnyGetter
	public Map<String, List<Client>> getMap() {
		return map;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}
}
