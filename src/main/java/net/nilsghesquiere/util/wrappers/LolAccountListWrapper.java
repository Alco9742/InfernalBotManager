package net.nilsghesquiere.util.wrappers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.nilsghesquiere.entities.LolAccount;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;

public class LolAccountListWrapper{
	private Map<String, List<LolAccount>> map;

	public LolAccountListWrapper() {
		this.map = new HashMap<String, List<LolAccount>>();
	}	
	
	@JsonAnySetter 
	public void add(String key, List<LolAccount> lolAccounts) {
		map.put(key, lolAccounts);
	}

	@JsonAnyGetter
	public Map<String, List<LolAccount>> getMap() {
		return map;
	}
	
}
