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
import net.nilsghesquiere.web.error.UploadedFileMalformedException;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Data
@Entity
@Table(name ="lolaccounts", uniqueConstraints={@UniqueConstraint(columnNames = {"account", "region"})}) //database constraint on account/region pairs
@EqualsAndHashCode(exclude={"user"})
public class LolAccount implements Serializable{
	private static final long serialVersionUID = 1L;
	private @Id @GeneratedValue(strategy = GenerationType.IDENTITY) Long id;
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
	
	//this used to be initialized with settings from the infernalsettings, should take a look at with what we should initialize now
	//Force the user to have a default set of infernalsettings perhaps
	//TODO: check if this always returns default
	public LolAccount(User user, String account, String password, Region region, ImportSettings importSettings) {
		super();
		this.user = user;
		this.account = account;
		this.password = password;
		this.summoner = "";
		this.region = region;
		this.level = 0;
		this.maxLevel = importSettings.getMaxLevel();
		this.xp = 0;
		this.be = 0;
		this.maxBe = importSettings.getMaxBe();
		this.priority = importSettings.getPrio();
		this.playTime = importSettings.getPlayTime();
		this.sleepTime = importSettings.getSleepTime();
		this.active = importSettings.getAktive();
		this.accountStatus = AccountStatus.NEW;
		this.assignedTo = "";
		this.info = "";
	}
	
	public LolAccount(User user, String account, String password, Region region, Integer level, ImportSettings importSettings) {
		super();
		this.user = user;
		this.account = account;
		this.password = password;
		this.summoner = "";
		this.region = region;
		this.level = level;
		this.maxLevel = importSettings.getMaxLevel();
		this.xp = 0;
		this.be = 0;
		this.maxBe = importSettings.getMaxBe();
		this.priority = importSettings.getPrio();
		this.playTime = importSettings.getPlayTime();
		this.sleepTime = importSettings.getSleepTime();
		this.active = importSettings.getAktive();
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
	
	public static LolAccount buildFromString(User user, String line, ImportSettings importSettings){
		String input[] = line.split(":");
		if (input.length == 3){
			String account = input[0];
			String password = input[1];
			String regionString = input[2].toUpperCase();
			Region region = Region.valueOf(regionString);
			return new LolAccount(user,account,password, region, importSettings);		
		} else {
			if (input.length == 4){
				String account = input[0];
				String password = input[1];
				String regionString = input[2].toUpperCase();
				Region region = Region.valueOf(regionString);
				Integer level = Integer.parseInt(input[3]);
				return new LolAccount(user,account,password, region, level, importSettings);		
			} else {
				throw new UploadedFileMalformedException();
			}
		}
	}
	
	public static LolAccount buildFromStringWithRegion(User user, String line, Region region, ImportSettings importSettings){
		String input[] = line.split(":");
		if (input.length == 2){
			String account = input[0];
			String password = input[1];
			return new LolAccount(user,account,password, region, importSettings);		
		} else {
			if (input.length == 3){
				String account = input[0];
				String password = input[1];
				Integer level = Integer.parseInt(input[2]);
				return new LolAccount(user,account,password, region, level, importSettings);		
			} else {
				throw new UploadedFileMalformedException();
			}
		}
	}
}
