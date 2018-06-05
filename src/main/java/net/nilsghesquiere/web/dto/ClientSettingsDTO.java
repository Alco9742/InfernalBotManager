package net.nilsghesquiere.web.dto;

import java.io.Serializable;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import lombok.Data;
import net.nilsghesquiere.entities.ClientSettings;
import net.nilsghesquiere.util.enums.ActionOnNoQueuers;
import net.nilsghesquiere.util.enums.Region;

import org.hibernate.validator.constraints.NotBlank;

@Data
public class ClientSettingsDTO implements Serializable{
	private static final long serialVersionUID = 1L;
	@NotNull
	private Long id;
	@NotNull
	@NotBlank
	private String name;
	@NotNull
	private Region clientRegion;
	@NotNull
	@NotBlank
	private String infernalPath;
	@NotNull
	@Min(1)
	private Integer queuerAmount;
	@NotNull
	private Integer accountBufferAmount;
	@NotNull
	private Boolean reboot;
	@NotNull
	private Integer rebootTime;
	@NotNull
	private Boolean fetchInfernalSettings;
	@NotNull
	private ActionOnNoQueuers actionOnNoQueuers; //aanpassen naar enum -> reboot, restart infernal, do nothing
	
	public ClientSettingsDTO() {
		super();
		this.id = 0L;
		this.name="";
		this.clientRegion = Region.EUW;
		this.infernalPath = "";
		this.queuerAmount = 0;
		this.accountBufferAmount = 0;
		this.reboot = false;
		this.rebootTime = 0;
		this.fetchInfernalSettings = false;
		this.actionOnNoQueuers = ActionOnNoQueuers.DO_NOTHING;
	}

	public ClientSettingsDTO(String name, Region clientRegion,
			String infernalPath, Integer queuerAmount,
			Integer accountBufferAmount,
			Boolean reboot, Integer rebootTime, Boolean fetchInfernalSettings,
			ActionOnNoQueuers actionOnNoQueuers, Boolean debug) {
		super();
		this.id = 0L;
		this.name = name;
		this.clientRegion = clientRegion;
		this.infernalPath = infernalPath;
		this.queuerAmount = queuerAmount;
		this.accountBufferAmount = accountBufferAmount;
		this.reboot = reboot;
		this.rebootTime = rebootTime;
		this.fetchInfernalSettings = fetchInfernalSettings;
		this.actionOnNoQueuers = actionOnNoQueuers;
	}
	public ClientSettingsDTO(Long id, String name, Region clientRegion,
			String infernalPath, Integer queuerAmount,
			Integer accountBufferAmount,
			Boolean reboot, Integer rebootTime, Boolean fetchInfernalSettings,
			ActionOnNoQueuers actionOnNoQueuers, Boolean debug) {
		super();
		this.id = id;
		this.name = name;
		this.clientRegion = clientRegion;
		this.infernalPath = infernalPath;
		this.queuerAmount = queuerAmount;
		this.accountBufferAmount = accountBufferAmount;
		this.reboot = reboot;
		this.rebootTime = rebootTime;
		this.fetchInfernalSettings = fetchInfernalSettings;
		this.actionOnNoQueuers = actionOnNoQueuers;
	}
	
	public ClientSettingsDTO(ClientSettings settings){
		this.id = settings.getId();
		this.name = settings.getName();
		this.clientRegion = settings.getClientRegion();
		this.infernalPath = settings.getInfernalPath();
		this.queuerAmount = settings.getQueuerAmount();
		this.accountBufferAmount = settings.getAccountBufferAmount();
		this.reboot = settings.getReboot();
		this.rebootTime = settings.getRebootTime();
		this.fetchInfernalSettings = settings.getFetchInfernalSettings();
		this.actionOnNoQueuers = settings.getActionOnNoQueuers();
	}
}