package net.nilsghesquiere.entities;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import lombok.Data;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Data
@Entity
@Table(name ="clients")
public class ClientData implements Serializable{
	private static final long serialVersionUID = 1L;
	private @Id @GeneratedValue(strategy = GenerationType.IDENTITY) Long id;
	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	@JoinColumn(name = "userid")
	@JsonIgnore
	private User user;
	private String tag;
	@OneToMany(mappedBy="client",cascade=CascadeType.ALL,fetch = FetchType.LAZY)
	@OrderBy("id")
	private List<Queuer> queuers;
	private LocalDateTime date;
	private String status;
	
	public ClientData() {
		this.queuers = new ArrayList<Queuer>();
	}
	
	public ClientData(Long id, String tag, List<Queuer> queuers, LocalDateTime date, String status) {
		super();
		this.id = id;
		this.tag = tag;
		this.queuers = queuers;
		this.date = date;
		this.status = status;
	}
	
	public void setUser(User user) {
		if (this.user != null && this.user.getClients().contains(this)){
			this.user.removeClientData(this);
		}
		this.user = user;
		if (user != null && !user.getClients().contains(this)){
			user.addClientData(this);
		}
	}
	
	
	public void addQueuer(Queuer queuer){
		queuers.add(queuer);
		if(queuer.getClient() != this){
			queuer.setClient(this);
		}
	}
	
	public void removeQueuer(Queuer queuer){
		queuers.remove(queuer);
		if(queuer.getClient() == this){
			queuer.setClient(null);
		}
	}
}
