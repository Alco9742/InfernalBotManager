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

import com.fasterxml.jackson.annotation.JsonIgnore;

@Data
@Entity
@Table(name ="clients")
public class Client {
	private @Id @GeneratedValue(strategy = GenerationType.IDENTITY) Long id;
	private String tag;
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
	private ClientData clientData;
	
	public void setUser(User user) {
		if (this.user != null && this.user.getClients().contains(this)){
			this.user.removeClient(this);
		}
		this.user = user;
		if (user != null && !user.getClients().contains(this)){
			user.addClient(this);
		}
	}
	
}
