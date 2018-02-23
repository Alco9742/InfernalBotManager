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

import lombok.Data;
import net.nilsghesquiere.util.enums.AccountStatus;
import net.nilsghesquiere.util.enums.Server;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Data
@Entity
@Table(name ="lolaccounts")
public class LolAccount implements Serializable{
	private static final long serialVersionUID = 1L;
	private @Id @GeneratedValue(strategy = GenerationType.AUTO) Long id;
	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	@JoinColumn(name = "userid")
	@JsonIgnore
	private User user;
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
	
	public LolAccount(User user, String username, String password, Server server, Long maxlevel, boolean enabled) {
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

	public LolAccount(User user, String username, String password, Server server, boolean enabled) {
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
	public LolAccount(User user, String username, String password, Long maxlevel, boolean enabled) {
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
	
	public void setUser(User user) {
		if (this.user != null && this.user.getLolAccounts().contains(this)){
			this.user.removeLolAccount(this);
		}
		this.user = user;
		if (user != null && !user.getLolAccounts().contains(this)){
			user.addLolAccount(this);
		}
	}
	
	public static LolAccount buildFromString(User user, String line){
		String input[] = line.split(":");
		String username = input[0];
		String password = input[1];
		String serverString = input[2];
		String maxLevelString = input[3];
		Server server = Server.valueOf(serverString);
		Long maxLevel = Long.valueOf(maxLevelString);
		return new LolAccount(user,username,password, server, maxLevel, true);
	}
}
