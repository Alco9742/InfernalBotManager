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

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import net.nilsghesquiere.util.enums.Lane;

@Data
@Entity
@Table(name ="queuerlolaccounts")
public class QueuerLolAccount implements Serializable{
	private static final long serialVersionUID = 1L;
	private @Id @GeneratedValue(strategy = GenerationType.IDENTITY) Long id;
	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	@JoinColumn(name = "queuerid")
	@JsonIgnore
	private Queuer queuer;
	private String account;
	private Integer level;
	private Integer maxLevel;
	private Integer xp;
	private Integer xpCap;
	private Integer be;
	private String champ;
	private Lane lane;
	private Boolean lpq;
	
	public QueuerLolAccount(){};

	
	public void setQueuer(Queuer queuer) {
		if (this.queuer != null && this.queuer.getQueuerLolAccounts().contains(this)){
			this.queuer.removeQueuerLolAccount(this);
		}
		this.queuer = queuer;
		if (queuer != null && !queuer.getQueuerLolAccounts().contains(this)){
			queuer.addQueuerLolAccount(this);
		}
	}


	public QueuerLolAccount(Long id, String account, Integer level,
			Integer maxLevel, Integer xp, Integer xpCap, Integer be,
			String champ, Lane lane, Boolean lpq) {
		super();
		this.id = id;
		this.account = account;
		this.level = level;
		this.maxLevel = maxLevel;
		this.xp = xp;
		this.xpCap = xpCap;
		this.be = be;
		this.champ = champ;
		this.lane = lane;
		this.lpq = lpq;
	}


	@Override
	public String toString() {
		return "QueuerLolAccount [id=" + id + ", queuer=" + queuer.getQueuer()
				+ ", account=" + account + ", level=" + level + ", maxLevel="
				+ maxLevel + ", xp=" + xp + ", xpCap=" + xpCap + ", be=" + be
				+ ", champ=" + champ + ", lane=" + lane + ", lpq=" + lpq + "]";
	}
	
}
