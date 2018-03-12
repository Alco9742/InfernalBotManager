package net.nilsghesquiere.util.wrappers;

import java.util.HashMap;
import java.util.Map;

import net.nilsghesquiere.entities.GlobalVariable;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;

public class GlobalVariableMap{
	private Map<String, GlobalVariable> map;

	public GlobalVariableMap() {
		this.map = new HashMap<>();
	}	
	
	@JsonAnySetter 
	public void add(String key, GlobalVariable value) {
		map.put(key, value);
	}

	@JsonAnyGetter
	public Map<String, GlobalVariable> getMap() {
		return map;
	}
	
}
