package net.nilsghesquiere.util.wrappers;

import java.util.HashMap;
import java.util.Map;

import net.nilsghesquiere.web.dto.ClientDTO;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;

public class ClientDTOMap{
	private Map<String, ClientDTO> map;

	public ClientDTOMap() {
		this.map = new HashMap<>();
	}	
	
	@JsonAnySetter 
	public void add(String key, ClientDTO value) {
		map.put(key, value);
	}

	@JsonAnyGetter
	public Map<String, ClientDTO> getMap() {
		return map;
	}
	
}
