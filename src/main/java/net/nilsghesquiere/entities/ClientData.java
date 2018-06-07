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
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonUnwrapped;

import lombok.Data;


@Data
@Entity
@Table(name ="clientdata")
public class ClientData implements Serializable{
	private static final long serialVersionUID = 1L;
	private @Id @GeneratedValue(strategy = GenerationType.IDENTITY) Long id;
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "clientid", nullable=true)
	@JsonUnwrapped
	//todo figure out how to only serialize clienttag
	private Client client;
	@OneToMany(mappedBy="clientData",cascade=CascadeType.ALL,fetch = FetchType.LAZY)
	@OrderBy("id")
	private List<Queuer> queuers;
	private LocalDateTime date;
	private String status;
	private String ramInfo;
	private String cpuInfo;
	
	public ClientData() {
		this.queuers = new ArrayList<Queuer>();
	}
	
	public ClientData(Long id,List<Queuer> queuers, LocalDateTime date, String status, String ramInfo, String cpuInfo) {
		super();
		this.id = id;
		this.queuers = queuers;
		this.date = date;
		this.status = status;
		this.ramInfo = ramInfo;
		this.cpuInfo = cpuInfo;
	}
	
	public void addQueuer(Queuer queuer){
		queuers.add(queuer);
		if(queuer.getClientData() != this){
			queuer.setClientData(this);
		}
	}
	
	public void removeQueuer(Queuer queuer){
		queuers.remove(queuer);
		if(queuer.getClientData() == this){
			queuer.setClientData(null);
		}
	}
}
