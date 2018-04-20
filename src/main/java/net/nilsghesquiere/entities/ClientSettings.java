package net.nilsghesquiere.entities;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Data;
import net.nilsghesquiere.util.enums.Region;

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
	private Integer accountAmount;
	private Integer accountBufferAmount;
	private Boolean uploadNewAccounts;
	private Region clientRegion;
	private Boolean reboot;
	private Integer rebootTime;
	private Boolean fetchInfernalSettings;
	private Boolean actionOnNoQueuers; //aanpassen naar enum -> reboot, restart infernal, do nothing
	private Boolean debug; //enkel zichtbaar maken voor admins, debug parameters uit ini halen
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "userid")
	@JsonIgnore
	private User user;

	public ClientSettings() {
		this.infernalPath = "";
		this.queuerAmount = 1;
		this.accountAmount = 5;
		this.accountBufferAmount = 0;
		this.uploadNewAccounts = false;
		this.clientRegion = Region.EUW;
		this.reboot = false;
		this.rebootTime = 10800;
		this.fetchInfernalSettings = false;
		this.actionOnNoQueuers = false;
		this.debug = false;
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
