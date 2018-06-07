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
	@ManyToOne(fetch = FetchType.EAGER,cascade=CascadeType.PERSIST, optional = false)
	@JoinColumn(name = "clientdataid")
	@JsonIgnore
	private ClientData clientData;
	private String queuer;
	//This is backwards: False is softEnd enabled
	private Boolean softEnd;
	private Integer afterGame;
	private Integer playedGames;
	private Integer winGames;
	private Integer defeatGames;
	private String  state;
	@OneToMany(mappedBy="queuer",cascade=CascadeType.ALL,fetch = FetchType.LAZY)
	@OrderBy("id")
	private List<QueuerLolAccount> queuerLolAccounts;
	private Boolean lpq;
	
	public Queuer() {
		this.queuerLolAccounts = new ArrayList<>();
	}
	
	public Queuer(Long id, String queuer, Boolean softEnd,
			Integer afterGame, Integer playedGames, Integer winGames,
			Integer defeatGames, String state, List<QueuerLolAccount> queuerLolAccounts, Boolean lpq) {
		super();
		this.id = id;
		this.queuer = queuer;
		this.softEnd = softEnd;
		this.afterGame = afterGame;
		this.playedGames = playedGames;
		this.winGames = winGames;
		this.defeatGames = defeatGames;
		this.state = state;
		this.queuerLolAccounts = queuerLolAccounts;
		this.lpq = lpq;
	}
	
	public void setClientData(ClientData clientData) {
		if (this.clientData != null && this.clientData.getQueuers().contains(this)){
			this.clientData.removeQueuer(this);
		}
		this.clientData = clientData;
		if (clientData != null && !clientData.getQueuers().contains(this)){
			clientData.addQueuer(this);
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

	@Override
	public String toString() {
		return "Queuer [id=" + id + ", client=" + clientData.getClient().getTag() + ", queuer=" + queuer
				+ ", softEnd=" + softEnd + ", afterGame=" + afterGame
				+ ", playedGames=" + playedGames + ", winGames=" + winGames
				+ ", defeatGames=" + defeatGames + ", state=" + state
				+ ", queuerLolAccounts=" + queuerLolAccounts + ", lpq=" + lpq
				+ "]";
	}
	
}
