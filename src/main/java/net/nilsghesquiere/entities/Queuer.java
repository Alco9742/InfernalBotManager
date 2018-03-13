package net.nilsghesquiere.entities;

import java.io.Serializable;
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

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Data
@Entity
@Table(name ="queuers")
public class Queuer implements Serializable{
	private static final long serialVersionUID = 1L;
	private @Id @GeneratedValue(strategy = GenerationType.IDENTITY) Long id;
	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	@JoinColumn(name = "clientid")
	@JsonIgnore
	private ClientData client;
	private String queuer;
	private Boolean softEnd;
	private Integer afterGame;
	private Integer playedGames;
	private Integer winGames;
	private Integer defeatGames;
	@OneToMany(mappedBy="queuer",cascade=CascadeType.ALL,fetch = FetchType.LAZY)
	@OrderBy("id")
	private List<QueuerLolAccount> queuerLolAccounts;
	
	public Queuer() {
		this.queuerLolAccounts = new ArrayList<>();
	}
	
	public Queuer(Long id, String queuer, Boolean softEnd,
			Integer afterGame, Integer playedGames, Integer winGames,
			Integer defeatGames, List<QueuerLolAccount> queuerLolAccounts) {
		super();
		this.id = id;
		this.queuer = queuer;
		this.softEnd = softEnd;
		this.afterGame = afterGame;
		this.playedGames = playedGames;
		this.winGames = winGames;
		this.defeatGames = defeatGames;
		this.queuerLolAccounts = queuerLolAccounts;
	}
	
	public void setClient(ClientData client) {
		if (this.client != null && this.client.getQueuers().contains(this)){
			this.client.removeQueuer(this);
		}
		this.client = client;
		if (client != null && !client.getQueuers().contains(this)){
			client.addQueuer(this);
		}
	}
	
	public void addQueuerLolAccount(QueuerLolAccount queuerLolAccount){
		queuerLolAccounts.add(queuerLolAccount);
		if(queuerLolAccount.getQueuer() != this){
			queuerLolAccount.setQueuer(this);
		}
	}
	
	public void removeQueuerLolAccount(QueuerLolAccount queuerLolAccount){
		queuerLolAccounts.remove(queuerLolAccount);
		if(queuerLolAccount.getQueuer() == this){
			queuerLolAccount.setQueuer(null);
		}
	}
	
}
