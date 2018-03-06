package net.nilsghesquiere.entities;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.Data;
import net.nilsghesquiere.util.enums.Region;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Data
@Entity
@Table(name ="infernalsettings")
public class InfernalSettings implements Serializable{
	private static final long serialVersionUID = 1L;
	private @Id @GeneratedValue(strategy = GenerationType.AUTO) Long id;
	@OneToOne(fetch = FetchType.EAGER, optional = false)
	@JoinColumn(name = "userid")
	@JsonIgnore
	private User user;
	private String sets;
	private String username;
	private String password;
	private Integer groups;
	private String level;
	private String clientPath;
	private String currentVersion;
	private String wildcard;
	private Integer maxLevel;
	private Integer sleepTime;
	private Integer playTime;
	private Region region;
	private Integer prio;
	private Integer grSize;
	private Boolean clientUpdateSel;
	private Boolean replaceConfig;
	private Integer lolHeight;
	private Integer lolWidth;
	private Integer maxBe;
	private Boolean aktive;
	private Boolean clientHide;
	private Boolean consoleHide;
	private Boolean ramManager;
	private Integer ramMin;
	private Integer ramMax;
	private Boolean leaderHide;
	private Boolean surrender;
	private Boolean renderDisable;
	private Boolean leaderRenderDisable;
	private Boolean cpuBoost;
	private Boolean leaderCpuBoost;
	private Integer levelToBeginnerBot;
	private Integer timeSpan;
	private Boolean softEndDefault;
	private Integer softEndValue;
	private Boolean queuerAutoClose;
	private Integer queueCloseValue;
	private Boolean winReboot;
	private Boolean winShutdown;
	private Integer timeoutLogin;
	private Integer timeoutLobby;
	private Integer timeoutChamp;
	private Integer timeoutMastery;
	private Integer timeoutLoadGame;
	private Integer timeoutInGame;
	private Integer timeoutInGameFF;
	private Integer timeoutEndOfGame;
	private Boolean timeUntilCheck;
	private String timeUntilReboot; //TODO check this out
	private Boolean serverCon;
	private Integer serverPort;
	private Boolean openChest;
	private Boolean openHexTech;
	private Boolean disChest;
	private Boolean apiClient;
	private String mySQLServer;
	private String mySQLDatabase;
	private String mySQLUser;
	private String mySQLPassword;
	private String mySQLQueueTable;
	private String mySQLAktivTable;

	
	public InfernalSettings(){} 
	public InfernalSettings(User user) {
		super();
		this.user= user;
		this.sets = "InfernalManager";
		this.username = "";
		this.password = "";
		this.groups = 1;
		this.level = "";
		this.clientPath = ""; //TODO
		this.currentVersion = ""; //TODO
		this.wildcard = ":";
		this.maxLevel = 30;
		this.sleepTime = 0;
		this.playTime = 24;
		this.region = Region.NA;
		this.prio = 1;
		this.grSize = 5;
		this.clientUpdateSel = true;
		this.replaceConfig = false;
		this.lolHeight = 240;
		this.lolWidth = 320;
		this.maxBe = 30000;
		this.aktive = true;
		this.clientHide = true;
		this.consoleHide = true;
		this.ramManager = true;
		this.ramMin = 200;
		this.ramMax = 300;
		this.leaderHide = true;
		this.surrender = false;
		this.renderDisable = true;
		this.leaderRenderDisable = true;
		this.cpuBoost = true;
		this.leaderCpuBoost = true;
		this.levelToBeginnerBot = 99;
		this.timeSpan = 30;
		this.softEndDefault = true;
		this.softEndValue = 5;
		this.queuerAutoClose = false;
		this.queueCloseValue = 1;
		this.winReboot = true;
		this.winShutdown = false;
		this.timeoutLogin = 5;
		this.timeoutLobby = 5;
		this.timeoutChamp = 5;
		this.timeoutMastery = 5;
		this.timeoutLoadGame = 5;
		this.timeoutInGame = 60;
		this.timeoutInGameFF = 17;
		this.timeoutEndOfGame = 5;
		this.timeUntilCheck = false;
		this.timeUntilReboot = "01:00";
		this.serverCon = false;
		this.serverPort = 100;
		this.openChest = true;
		this.openHexTech = true;
		this.disChest = true;
		this.apiClient = false;
		this.mySQLServer = "localhost";
		this.mySQLDatabase = "InfernalBot";
		this.mySQLUser = "admin";
		this.mySQLPassword = "PuschelHase123";
		this.mySQLQueueTable = "Infernal_Queue";
		this.mySQLAktivTable = "Infernal_Aktiv";
	}
}
