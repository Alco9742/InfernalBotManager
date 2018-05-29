package net.nilsghesquiere.web.dto;

import java.io.Serializable;

import lombok.Data;

@Data
public class ClientDTO implements Serializable{
	static final long serialVersionUID = 1L;
	private Long id;
	private String tag;
	private String HWID;
	private Long clientSettings;
	private Long infernalSettings;
	
	public ClientDTO(){
		super();
	}

	public ClientDTO(Long id, String tag, String HWID, Long clientSettings,
			Long infernalSettings) {
		super();
		this.id = id;
		this.tag = tag;
		this.clientSettings = clientSettings;
		this.HWID = HWID;
		this.infernalSettings = infernalSettings;
	}
	
	
}
