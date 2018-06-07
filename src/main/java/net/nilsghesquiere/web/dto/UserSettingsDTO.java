package net.nilsghesquiere.web.dto;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import lombok.Data;
import net.nilsghesquiere.entities.UserSettings;

@Data
public class UserSettingsDTO implements Serializable{
	static final long serialVersionUID = 1L;
	@NotNull
	private Long activeImportSettings;
	@NotNull
	private Boolean mailOnDisconnect;
	
	public UserSettingsDTO(){
		super();
	}
	
	public UserSettingsDTO(UserSettings settings){
		this.activeImportSettings = settings.getActiveImportSettings();
		this.mailOnDisconnect = settings.getMailOnDisconnect();
	}

	public UserSettingsDTO(Long activeImportSettings,Boolean mailOnDisconnect) {
		super();
		this.activeImportSettings = activeImportSettings;
		this.mailOnDisconnect = mailOnDisconnect;
	}
}
