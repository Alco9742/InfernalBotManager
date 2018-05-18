package net.nilsghesquiere.entities;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Data;
import net.nilsghesquiere.util.enums.ActionOnNoQueuers;
import net.nilsghesquiere.util.enums.Region;
import net.nilsghesquiere.web.dto.ClientSettingsDTO;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Data
@Entity
@Table(name ="clientsettings")
public class ClientSettings implements Serializable{
	private static final long serialVersionUID = 1L;
	private @Id @GeneratedValue(strategy = GenerationType.IDENTITY) Long id;
	private String name;
	private String infernalPath;
	private Integer queuerAmount;
	private Integer accountBufferAmount;
	private Boolean uploadNewAccounts;
	private Region clientRegion;
	private Boolean reboot;
	private Integer rebootTime;
	private Boolean fetchInfernalSettings;
	@Enumerated(EnumType.STRING)
	private ActionOnNoQueuers actionOnNoQueuers; 
	private Boolean debug; //enkel zichtbaar maken voor admins, debug parameters uit ini halen
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "userid")
	@JsonIgnore
	private User user;

	public ClientSettings() {
		this.name="";
		this.infernalPath = "";
		this.queuerAmount = 1;
		this.accountBufferAmount = 0;
		this.uploadNewAccounts = false;
		this.clientRegion = Region.EUW;
		this.reboot = false;
		this.rebootTime = 0;
		this.fetchInfernalSettings = false;
		this.actionOnNoQueuers = ActionOnNoQueuers.DO_NOTHING;
		this.debug = false;
	}
	
	public ClientSettings(ClientSettingsDTO dto){
		if (dto.getId() != 0){
			this.id = dto.getId(); 
		}
		this.name=dto.getName();
		this.infernalPath = dto.getInfernalPath();
		this.queuerAmount = dto.getQueuerAmount();
		this.accountBufferAmount = dto.getAccountBufferAmount();
		this.uploadNewAccounts = dto.getUploadNewAccounts();
		this.clientRegion = dto.getClientRegion();
		this.reboot = dto.getReboot();
		this.rebootTime = dto.getRebootTime();
		this.fetchInfernalSettings = dto.getFetchInfernalSettings();
		this.actionOnNoQueuers = dto.getActionOnNoQueuers();
		this.debug = dto.getDebug();
	}
	
	public void updateFromDTO(ClientSettingsDTO dto){
		this.name=dto.getName();
		this.infernalPath = dto.getInfernalPath();
		this.queuerAmount = dto.getQueuerAmount();
		this.accountBufferAmount = dto.getAccountBufferAmount();
		this.uploadNewAccounts = dto.getUploadNewAccounts();
		this.clientRegion = dto.getClientRegion();
		this.reboot = dto.getReboot();
		this.rebootTime = dto.getRebootTime();
		this.fetchInfernalSettings = dto.getFetchInfernalSettings();
		this.actionOnNoQueuers = dto.getActionOnNoQueuers();
		this.debug = dto.getDebug();
	}
	
	public static ClientSettings updateFromDTO(ClientSettings clientSettings, ClientSettingsDTO dto){
		clientSettings.setName(dto.getName());
		clientSettings.setInfernalPath(dto.getInfernalPath());
		clientSettings.setQueuerAmount(dto.getQueuerAmount());
		clientSettings.setAccountBufferAmount(dto.getAccountBufferAmount());
		clientSettings.setUploadNewAccounts(dto.getUploadNewAccounts());
		clientSettings.setClientRegion(dto.getClientRegion());
		clientSettings.setReboot(dto.getReboot());
		clientSettings.setRebootTime(dto.getRebootTime());
		clientSettings.setFetchInfernalSettings(dto.getFetchInfernalSettings());
		clientSettings.setActionOnNoQueuers(dto.getActionOnNoQueuers());
		clientSettings.setDebug(dto.getDebug());
		return clientSettings;
	}
	
	public void setUser(User user) {
		if (this.user != null && this.user.getClientSettingsList().contains(this)){
			this.user.removeClientSettings(this);
		}
		this.user = user;
		if (user != null && !user.getClientSettingsList().contains(this)){
			user.addClientSettings(this);
		}
	}
}
