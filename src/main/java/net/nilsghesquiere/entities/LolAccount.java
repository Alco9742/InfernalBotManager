package net.nilsghesquiere.entities;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import lombok.Data;
import lombok.EqualsAndHashCode;
import net.nilsghesquiere.util.enums.AccountStatus;
import net.nilsghesquiere.util.enums.Region;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Data
@Entity
@Table(name ="lolaccounts", uniqueConstraints={@UniqueConstraint(columnNames = {"account", "region"})}) //database constraint on account/region pairs
@EqualsAndHashCode(exclude={"user"})
public class LolAccount implements Serializable{
	private static final long serialVersionUID = 1L;
	private @Id @GeneratedValue(strategy = GenerationType.AUTO) Long id;
	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	@JoinColumn(name = "userid")
	@JsonIgnore
	private User user;
	private String account;
	private String password;
	private String summoner;
	@Enumerated(EnumType.STRING)
	private Region region;
	private Integer level;
	private Integer maxLevel;
	private Integer xp;
	private Integer be;
	private Integer maxBe;
	private Integer priority;
	private Integer playTime;
	private Integer sleepTime;
	private boolean active;
	@Enumerated(EnumType.STRING)
	private AccountStatus accountStatus;
	private String assignedTo;
	private String info;

	
	public LolAccount() {}
	
	public LolAccount(User user, String account, String password, Region region) {
		super();
		this.user = user;
		this.account = account;
		this.password = password;
		this.summoner = "";
		this.region = region;
		this.level = 0;
		this.maxLevel = user.getInfernalSettings().getMaxLevel();
		this.xp = 0;
		this.be = 0;
		this.maxBe = user.getInfernalSettings().getMaxBe();
		this.priority = user.getInfernalSettings().getPrio();
		this.playTime = user.getInfernalSettings().getPlayTime();
		this.sleepTime = user.getInfernalSettings().getSleepTime();
		this.active = user.getInfernalSettings().getAktive();
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
		String account = input[0];
		String password = input[1];
		String regionString = input[2];
		Region region = Region.valueOf(regionString);
		return new LolAccount(user,account,password, region);
	}
}
