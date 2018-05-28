package net.nilsghesquiere.util.wrappers;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.nilsghesquiere.web.dto.ClientDTO;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;

public class ClientDTOWrapper implements Serializable{
	private static final long serialVersionUID = 1L;
	private Map<String, List<ClientDTO>> map;
	private String error;

	public ClientDTOWrapper() {
		this.map = new HashMap<String, List<ClientDTO>>();
		this.setError("");
	}	
	
	@JsonAnySetter 
	public void add(String key, List<ClientDTO> clients) {
		map.put(key, clients);
	}

	@JsonAnyGetter
	public Map<String, List<ClientDTO>> getMap() {
		return map;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}
}
