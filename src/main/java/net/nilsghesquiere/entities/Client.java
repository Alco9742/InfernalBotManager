package net.nilsghesquiere.entities;

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

import com.fasterxml.jackson.annotation.JsonIgnore;

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
	private InfernalSettings infernalSettings;
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "clientsettingsid")
	private ClientSettings clientSettings;
	@OneToOne(fetch = FetchType.LAZY, mappedBy = "client")
	@JsonIgnore
	private ClientData clientData;
	
	public Client() {}
	
	public Client(String tag, User user, InfernalSettings infernalSettings,
			ClientSettings clientSettings) {
		super();
		this.tag = tag;
		this.HWID = "";
		this.user = user;
		this.infernalSettings = infernalSettings;
		this.clientSettings = clientSettings;
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
