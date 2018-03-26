package net.nilsghesquiere.util.wrappers;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.nilsghesquiere.entities.GlobalVariable;
import net.nilsghesquiere.entities.User;
import net.nilsghesquiere.web.dto.UserAdminDTO;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;

public class UserWrapper implements Serializable{
	private static final long serialVersionUID = 1L;
	private Map<String, List<UserAdminDTO>> map;
	private String error;

	public UserWrapper() {
		this.map = new HashMap<String, List<UserAdminDTO>>();
		this.setError("");
	}	
	
	@JsonAnySetter 
	public void add(String key, List<UserAdminDTO> users) {
		map.put(key, users);
	}

	@JsonAnyGetter
	public Map<String, List<UserAdminDTO>> getMap() {
		return map;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}
}
