package net.nilsghesquiere.valueobjects;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.nilsghesquiere.entities.LolAccount;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class JSONResponse implements Serializable{
	private static final long serialVersionUID = 1L;
	private Map<String, List<LolAccount>> map;
	@JsonIgnore
	private String error;

	public JSONResponse() {
		this.map = new HashMap<String, List<LolAccount>>();
		this.setError("");
	}	
	
	@JsonAnySetter 
	public void add(String key, List<LolAccount> lolAccounts) {
		map.put(key, lolAccounts);
	}

	@JsonAnyGetter
	public Map<String, List<LolAccount>> getMap() {
		return map;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}
}
