package net.nilsghesquiere.entities;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
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
	@OneToMany(mappedBy="queuer",cascade=CascadeType.REMOVE,fetch = FetchType.LAZY)
	@OrderBy("id")
	private List<QueuerLolAccount> queuerAccounts;
	
	public Queuer() {
		this.queuerAccounts = new ArrayList<>();
	}
}
