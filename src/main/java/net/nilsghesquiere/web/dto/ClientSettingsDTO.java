package net.nilsghesquiere.web.dto;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import lombok.Data;
import net.nilsghesquiere.entities.ClientSettings;
import net.nilsghesquiere.util.enums.ActionOnNoQueuers;
import net.nilsghesquiere.util.enums.Region;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

@Data
public class ClientSettingsDTO implements Serializable{
	private static final long serialVersionUID = 1L;
	@NotNull
	private long id;
	@NotNull
	@NotEmpty
	@NotBlank
	private String name;
	@NotNull
	private Region clientRegion;
	@NotNull
	@NotEmpty
	@NotBlank
	private String infernalPath;
	@NotNull
	private Integer queuerAmount;
	@NotNull
	private Integer accountBufferAmount;
	@NotNull
	private Boolean uploadNewAccounts;
	@NotNull
	private Boolean reboot;
	private Integer rebootTime;
	@NotNull
	private Boolean fetchInfernalSettings;
	@NotNull
	private ActionOnNoQueuers actionOnNoQueuers; //aanpassen naar enum -> reboot, restart infernal, do nothing
	private Boolean debug; //enkel zichtbaar maken voor admins, debug parameters uit ini halen
	
	public ClientSettingsDTO() {
		super();
		this.id = 0L;
		this.name="";
		this.clientRegion = Region.EUW;
		this.infernalPath = "";
		this.queuerAmount = 0;
		this.accountBufferAmount = 0;
		this.uploadNewAccounts = false;
		this.reboot = false;
		this.rebootTime = 0;
		this.fetchInfernalSettings = false;
		this.actionOnNoQueuers = ActionOnNoQueuers.DO_NOTHING;
		this.debug = false;
	}

	public ClientSettingsDTO(String name, Region clientRegion,
			String infernalPath, Integer queuerAmount,
			Integer accountBufferAmount, Boolean uploadNewAccounts,
			Boolean reboot, Integer rebootTime, Boolean fetchInfernalSettings,
			ActionOnNoQueuers actionOnNoQueuers, Boolean debug) {
		super();
		this.id = 0L;
		this.name = name;
		this.clientRegion = clientRegion;
		this.infernalPath = infernalPath;
		this.queuerAmount = queuerAmount;
		this.accountBufferAmount = accountBufferAmount;
		this.uploadNewAccounts = uploadNewAccounts;
		this.reboot = reboot;
		this.rebootTime = rebootTime;
		this.fetchInfernalSettings = fetchInfernalSettings;
		this.actionOnNoQueuers = actionOnNoQueuers;
		this.debug = debug;
	}
	public ClientSettingsDTO(Long id, String name, Region clientRegion,
			String infernalPath, Integer queuerAmount,
			Integer accountBufferAmount, Boolean uploadNewAccounts,
			Boolean reboot, Integer rebootTime, Boolean fetchInfernalSettings,
			ActionOnNoQueuers actionOnNoQueuers, Boolean debug) {
		super();
		this.id = id;
		this.name = name;
		this.clientRegion = clientRegion;
		this.infernalPath = infernalPath;
		this.queuerAmount = queuerAmount;
		this.accountBufferAmount = accountBufferAmount;
		this.uploadNewAccounts = uploadNewAccounts;
		this.reboot = reboot;
		this.rebootTime = rebootTime;
		this.fetchInfernalSettings = fetchInfernalSettings;
		this.actionOnNoQueuers = actionOnNoQueuers;
		this.debug = debug;
	}
	
	public ClientSettingsDTO(ClientSettings settings){
		this.id = settings.getId();
		this.name = settings.getName();
		this.clientRegion = settings.getClientRegion();
		this.infernalPath = settings.getInfernalPath();
		this.queuerAmount = settings.getQueuerAmount();
		this.accountBufferAmount = settings.getAccountBufferAmount();
		this.uploadNewAccounts = settings.getUploadNewAccounts();
		this.reboot = settings.getReboot();
		this.rebootTime = settings.getRebootTime();
		this.fetchInfernalSettings = settings.getFetchInfernalSettings();
		this.actionOnNoQueuers = settings.getActionOnNoQueuers();
		this.debug = settings.getDebug();
	}
}