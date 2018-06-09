package net.nilsghesquiere.util.wrappers;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;

import net.nilsghesquiere.web.dto.LoginAttemptDTO;

public class LoginAttemptDTOWrapper implements Serializable{
	private static final long serialVersionUID = 1L;
	private Map<String, List<LoginAttemptDTO>> map;
	private String error;

	public LoginAttemptDTOWrapper() {
		this.map = new HashMap<String, List<LoginAttemptDTO>>();
		this.setError("");
	}	
	
	@JsonAnySetter 
	public void add(String key, List<LoginAttemptDTO> attempts) {
		map.put(key, attempts);
	}

	@JsonAnyGetter
	public Map<String, List<LoginAttemptDTO>> getMap() {
		return map;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}
}
