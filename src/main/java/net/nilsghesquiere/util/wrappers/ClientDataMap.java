package net.nilsghesquiere.util.wrappers;

import java.util.HashMap;
import java.util.Map;

import net.nilsghesquiere.entities.ClientData;
import net.nilsghesquiere.util.mappers.ClientDataMapDeserializer;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(using = ClientDataMapDeserializer.class)
public class ClientDataMap{
	private Map<String, ClientData> map;

	public ClientDataMap() {
		this.map = new HashMap<>();
	}	
	
	@JsonAnySetter 
	public void add(String key, ClientData value) {
		map.put(key, value);
	}

	@JsonAnyGetter
	public Map<String, ClientData> getMap() {
		return map;
	}
	
}
