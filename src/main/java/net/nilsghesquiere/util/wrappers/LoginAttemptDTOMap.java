package net.nilsghesquiere.util.wrappers;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;

import net.nilsghesquiere.web.dto.LoginAttemptDTO;

public class LoginAttemptDTOMap{
	private Map<String, LoginAttemptDTO> map;

	public LoginAttemptDTOMap() {
		this.map = new HashMap<>();
	}	
	
	@JsonAnySetter 
	public void add(String key, LoginAttemptDTO value) {
		map.put(key, value);
	}

	@JsonAnyGetter
	public Map<String, LoginAttemptDTO> getMap() {
		return map;
	}
	
}
