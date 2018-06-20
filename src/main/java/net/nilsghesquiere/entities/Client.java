package net.nilsghesquiere.entities;

import java.time.LocalDateTime;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;
import net.nilsghesquiere.util.enums.ClientAction;
import net.nilsghesquiere.util.enums.ClientStatus;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonUnwrapped;

@Data
@Entity
@Table(name ="clients")
@EqualsAndHashCode(exclude={"user","clientSettings","infernalSettings","clientData"})
public class Client {
	private @Id @GeneratedValue(strategy = GenerationType.IDENTITY) Long id;
	private String tag;
	private String HWID;
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "userid")
	@JsonIgnore
	private User user;
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "infernalsettingsid")
	@JsonUnwrapped
	private InfernalSettings infernalSettings;
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "clientsettingsid")
	@JsonUnwrapped
	private ClientSettings clientSettings;
	@OneToOne(fetch = FetchType.LAZY, mappedBy = "client",cascade=CascadeType.REMOVE,optional=true)
	@JsonIgnore
	private ClientData clientData;
	private LocalDateTime lastPing;
	private ClientStatus clientStatus;
	@JsonIgnore
	private ClientAction clientAction;
	private Boolean error;
	private Boolean dcMailSent;
	
	public Client() {}
	
	public Client(String tag, User user, InfernalSettings infernalSettings,
			ClientSettings clientSettings) {
		super();
		this.tag = tag;
		this.HWID = "";
		this.user = user;
		this.infernalSettings = infernalSettings;
		this.clientSettings = clientSettings;
		this.clientStatus = ClientStatus.UNASSIGNED;
		this.clientAction = ClientAction.RUN;
		this.lastPing = null;
		this.error = false;
		this.dcMailSent = false;
	}
	
	
	public void setUser(User user) {
		if (this.user != null && this.user.getClients().contains(this)){
			this.user.removeClient(this);
		}
		this.user = user;
		if (user != null && !user.getClients().contains(this)){
			user.addClient(this);
		}
	}
	
	@Override
	public String toString() {
		return "Client [id=" + id + ", tag=" + tag + "#user=" + user.getEmail() + "]";
	}
}
