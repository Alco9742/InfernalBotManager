package net.nilsghesquiere.web.dto;

import java.io.Serializable;
import java.time.Duration;
import java.time.LocalDateTime;

import net.nilsghesquiere.entities.Client;
import net.nilsghesquiere.util.enums.ClientStatus;
import lombok.Data;

@Data
public class ClientDTO implements Serializable{
	static final long serialVersionUID = 1L;
	private Long id;
	private String tag;
	private String HWID;
	private Long clientSettings;
	private Long infernalSettings;
	private ClientStatus clientStatus;
	private Boolean error;
	private String timeSinceLastPing;
	

	public ClientDTO(){
		super();
	}

	public ClientDTO(Long id, String tag, String HWID, Long clientSettings,
			Long infernalSettings, ClientStatus clientStatus, Boolean error, String timeSinceLastPing) {
		super();
		this.id = id;
		this.tag = tag;
		this.clientSettings = clientSettings;
		this.HWID = HWID;
		this.infernalSettings = infernalSettings;
		this.clientStatus = clientStatus;
		this.error = error;
		this.timeSinceLastPing = timeSinceLastPing;
	}
	
	public ClientDTO(Client client){
		this.id = client.getId();
		this.tag = client.getTag();
		this.clientSettings = client.getClientSettings().getId();
		this.HWID = client.getHWID();
		this.infernalSettings = client.getInfernalSettings().getId();
		this.clientStatus = client.getClientStatus();
		this.error = client.getError();
		switch(this.clientStatus){
			case CONNECTED:{
				Duration duration = Duration.between(client.getLastPing(), LocalDateTime.now());
				this.timeSinceLastPing = duration.toMillis() / 1000 + "s";
				break;
			}
			case DISCONNECTED:{
				Duration duration = Duration.between(client.getLastPing(), LocalDateTime.now());
				this.timeSinceLastPing = duration.toMillis() / 1000 + "s";
				break;
			}
			case OFFLINE:{
				this.timeSinceLastPing = "-";
				break;
			}
			case UNASSIGNED:{
				this.timeSinceLastPing = "-";
				break;
			}
			default:{
				break;
			}
		}
	}
}
