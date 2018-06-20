package net.nilsghesquiere.web.dto;

import java.io.Serializable;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
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
	@Min(300) // 5 minutes
	@Max(86400) // 1 day
	private Integer secondsBeforeMail;
	
	public UserSettingsDTO(){
		super();
	}
	
	public UserSettingsDTO(UserSettings settings){
		this.activeImportSettings = settings.getActiveImportSettings();
		this.mailOnDisconnect = settings.getMailOnDisconnect();
		this.secondsBeforeMail = settings.getSecondsBeforeMail();
	}

	public UserSettingsDTO(Long activeImportSettings,Boolean mailOnDisconnect,Integer secondsBeforeMail) {
		super();
		this.activeImportSettings = activeImportSettings;
		this.mailOnDisconnect = mailOnDisconnect;
		this.secondsBeforeMail = secondsBeforeMail;
	}
}
