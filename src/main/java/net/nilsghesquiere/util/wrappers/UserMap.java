package net.nilsghesquiere.util.wrappers;

import java.util.HashMap;
import java.util.Map;

import net.nilsghesquiere.entities.User;
import net.nilsghesquiere.web.dto.UserAdminDTO;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;

public class UserMap{
	private Map<String, UserAdminDTO> map;

	public UserMap() {
		this.map = new HashMap<>();
	}	
	
	@JsonAnySetter 
	public void add(String key, UserAdminDTO value) {
		map.put(key, value);
	}

	@JsonAnyGetter
	public Map<String, UserAdminDTO> getMap() {
		return map;
	}
	
}
