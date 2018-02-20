package net.nilsghesquiere.entities;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Data;
import net.nilsghesquiere.enums.AccountStatus;
import net.nilsghesquiere.enums.Server;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Data
@Entity
@Table(name ="lolaccounts")
public class LolAccount implements Serializable{
	private static final long serialVersionUID = 1L;
	private @Id @GeneratedValue Long id;
	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	@JoinColumn(name = "userid")
	@JsonIgnore
	private AppUser user;
	private String username;
	private String summonername;
	private String password;
	private Server server;
	private Long level;
	private Long maxlevel;
	private AccountStatus accountStatus;
	private String assignedTo;
	private String info;
	private boolean enabled;
	
	public LolAccount() {}
	
	public LolAccount(AppUser user, String username, String password, Server server, Long maxlevel, boolean enabled) {
		super();
		this.user = user;
		this.username = username;
		this.summonername = "";
		this.password = password;
		this.level = 0L;
		this.maxlevel = maxlevel;
		this.server = server;
		this.enabled = enabled;
		this.accountStatus = AccountStatus.NEW;
		this.assignedTo = "";
		this.info = "";
	}
	public LolAccount(String username, String password, Server server, Long maxlevel, boolean enabled) {
		super();
		this.username = username;
		this.summonername = "";
		this.password = password;
		this.level = 0L;
		this.maxlevel = maxlevel;
		this.server = server;
		this.enabled = enabled;
		this.accountStatus = AccountStatus.NEW;
		this.assignedTo = "";
		this.info = "";
	}

	public LolAccount(AppUser user, String username, String password, Server server, boolean enabled) {
		super();
		this.user = user;
		this.username = username;
		this.summonername = "";
		this.password = password;
		this.level = 0L;
		this.maxlevel = this.user.getStandardLevel();
		this.server = server;
		this.enabled = enabled;
		this.accountStatus = AccountStatus.NEW;
		this.assignedTo = "";
		this.info = "";
	}

	//temp class for testing until servers are added in datatables
	public LolAccount(AppUser user, String username, String password, Long maxlevel, boolean enabled) {
		super();
		this.user = user;
		this.username = username;
		this.summonername = "";
		this.password = password;
		this.level = 0L;
		this.maxlevel = maxlevel;
		this.server = Server.EUROPE_WEST;
		this.enabled = enabled;
		this.accountStatus = AccountStatus.NEW;
		this.assignedTo = "";
		this.info = "";
	}
	
	public void setUser(AppUser user) {
		if (this.user != null && this.user.getLolAccounts().contains(this)){
			this.user.removeLolAccount(this);
		}
		this.user = user;
		if (user != null && !user.getLolAccounts().contains(this)){
			user.addLolAccount(this);
		}
	}
}
