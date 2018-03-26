package net.nilsghesquiere.entities;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import net.nilsghesquiere.util.enums.Region;
import net.nilsghesquiere.web.dto.InfernalSettingsDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import com.fasterxml.jackson.annotation.JsonIgnore;

//TODO overlopen en onnodige settings niet verzenden via rest
@Data
@Entity
@Table(name ="infernalsettings")
@EqualsAndHashCode(exclude={"user"})
public class InfernalSettings implements Serializable{
	private static final long serialVersionUID = 1L;
	//Manager VARS
	private @Id @GeneratedValue(strategy = GenerationType.IDENTITY) Long id;
	@OneToOne(fetch = FetchType.LAZY, cascade =CascadeType.ALL)
	@JoinColumn(name = "userid")
	@JsonIgnore
	private User user;
	private String sets;
	//SystemSettings 
	private Integer groups;
	private String clientPath;
	private String clientVersion;
	private Integer timeSpan;
	private Boolean autoLogin;
	private Boolean autoBotStart;
	//ImExPortSettings VARS
	private String wildcard;
	private Integer maxLevel;
	private Integer sleepTime;
	private Integer playTime;
	private Integer maxBe;
	private Region region;
	private Boolean aktive;
	//AutoqueuerSettings VARS
	private Boolean surrender;
	private Integer levelToBeginnerBot;
	private Boolean clientHide;
	private Boolean leaderHide;
	private Boolean consoleHide;
	private Boolean softEndDefault;
	private Integer softEndValue;	
	private Boolean queuerAutoClose;
	private Integer queueCloseValue;
	private Boolean winReboot;
	private Boolean winShutdown;
	private Boolean timeUntilCheck;
	private String  timeUntilReboot; 
	//PerformanceSettings VARS
	private Boolean renderDisable;
	private Boolean leaderRenderDisable;
	private Boolean cpuBoost;
	private Boolean leaderCpuBoost;
	private Boolean ramManager;
	private Integer ramMin;
	private Integer ramMax;
	//TimeoutsSettings VARS
	private Integer timeoutLogin;
	private Integer timeoutLobby;
	private Integer timeoutChamp;
	private Integer timeoutMastery;
	private Integer timeoutLoadGame;
	private Integer timeoutInGame;
	private Integer timeoutInGameFF;
	private Integer timeoutEndOfGame;
	//ChestSettings VARS
	private Boolean openChest;
	private Boolean openHexTech;
	private Boolean disChest;
	//OTHER VARS (Not in API)
	private Integer prio;
	private Boolean replaceConfig;
	private Integer lolHeight;
	private Integer lolWidth;

	public InfernalSettings(){} 
	public InfernalSettings(User user) {
		super();
		this.user= user;
		this.sets = "Default";
		this.groups = 1;
		this.clientPath = "C:\\Riot Games\\League of Legends";
		this.wildcard = ":";
		this.maxLevel = 30;
		this.sleepTime = 0;
		this.playTime = 24;
		this.prio = 9;
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
		this.openChest = true;
		this.openHexTech = true;
		this.disChest = true;
	}
	
	public InfernalSettings(Long id, User user, String sets, Integer groups,
			String clientPath, String clientVersion, Integer timeSpan,
			Boolean autoLogin, Boolean autoBotStart, String wildcard,
			Integer maxLevel, Integer sleepTime, Integer playTime,
			Integer maxBe, Region region, Boolean aktive, Boolean surrender,
			Integer levelToBeginnerBot, Boolean clientHide, Boolean leaderHide,
			Boolean consoleHide, Boolean softEndDefault, Integer softEndValue,
			Boolean queuerAutoClose, Integer queueCloseValue,
			Boolean winReboot, Boolean winShutdown, Boolean timeUntilCheck,
			String timeUntilReboot, Boolean renderDisable,
			Boolean leaderRenderDisable, Boolean cpuBoost,
			Boolean leaderCpuBoost, Boolean ramManager, Integer ramMin,
			Integer ramMax, Integer timeoutLogin, Integer timeoutLobby,
			Integer timeoutChamp, Integer timeoutMastery,
			Integer timeoutLoadGame, Integer timeoutInGame,
			Integer timeoutInGameFF, Integer timeoutEndOfGame,
			Boolean openChest, Boolean openHexTech, Boolean disChest,
			Integer prio, Boolean replaceConfig, Integer lolHeight,
			Integer lolWidth) {
		super();
		this.id = id;
		this.user = user;
		this.sets = sets;
		this.groups = groups;
		this.clientPath = clientPath;
		this.clientVersion = clientVersion;
		this.timeSpan = timeSpan;
		this.autoLogin = autoLogin;
		this.autoBotStart = autoBotStart;
		this.wildcard = wildcard;
		this.maxLevel = maxLevel;
		this.sleepTime = sleepTime;
		this.playTime = playTime;
		this.maxBe = maxBe;
		this.region = region;
		this.aktive = aktive;
		this.surrender = surrender;
		this.levelToBeginnerBot = levelToBeginnerBot;
		this.clientHide = clientHide;
		this.leaderHide = leaderHide;
		this.consoleHide = consoleHide;
		this.softEndDefault = softEndDefault;
		this.softEndValue = softEndValue;
		this.queuerAutoClose = queuerAutoClose;
		this.queueCloseValue = queueCloseValue;
		this.winReboot = winReboot;
		this.winShutdown = winShutdown;
		this.timeUntilCheck = timeUntilCheck;
		this.timeUntilReboot = timeUntilReboot;
		this.renderDisable = renderDisable;
		this.leaderRenderDisable = leaderRenderDisable;
		this.cpuBoost = cpuBoost;
		this.leaderCpuBoost = leaderCpuBoost;
		this.ramManager = ramManager;
		this.ramMin = ramMin;
		this.ramMax = ramMax;
		this.timeoutLogin = timeoutLogin;
		this.timeoutLobby = timeoutLobby;
		this.timeoutChamp = timeoutChamp;
		this.timeoutMastery = timeoutMastery;
		this.timeoutLoadGame = timeoutLoadGame;
		this.timeoutInGame = timeoutInGame;
		this.timeoutInGameFF = timeoutInGameFF;
		this.timeoutEndOfGame = timeoutEndOfGame;
		this.openChest = openChest;
		this.openHexTech = openHexTech;
		this.disChest = disChest;
		this.prio = prio;
		this.replaceConfig = replaceConfig;
		this.lolHeight = lolHeight;
		this.lolWidth = lolWidth;
	}
	
	public InfernalSettings(InfernalSettingsDTO dto){
		this.groups = dto.getGroups();
		this.clientPath = dto.getClientPath();
		this.clientVersion = dto.getClientVersion();
		this.timeSpan = dto.getTimeSpan();
		this.autoLogin = dto.getAutoLogin();
		this.autoBotStart = dto.getAutoBotStart();
		this.wildcard = dto.getWildcard();
		this.maxLevel = dto.getMaxLevel();
		this.sleepTime = dto.getSleepTime();
		this.playTime = dto.getPlayTime();
		this.maxBe = dto.getMaxBe();
		this.region = dto.getRegion();
		this.aktive = dto.getAktive();
		this.surrender = dto.getSurrender();
		this.levelToBeginnerBot = dto.getLevelToBeginnerBot();
		this.clientHide = dto.getClientHide();
		this.leaderHide = dto.getLeaderHide();
		this.consoleHide = dto.getConsoleHide();
		this.softEndDefault = dto.getSoftEndDefault();
		this.softEndValue = dto.getSoftEndValue();
		this.queuerAutoClose = dto.getQueuerAutoClose();
		this.queueCloseValue = dto.getQueueCloseValue();
		this.winReboot = dto.getWinReboot();
		this.winShutdown = dto.getWinShutdown();
		this.timeUntilCheck = dto.getTimeUntilCheck();
		this.timeUntilReboot = dto.getTimeUntilReboot();
		this.renderDisable = dto.getRenderDisable();
		this.leaderRenderDisable = dto.getLeaderRenderDisable();
		this.cpuBoost = dto.getCpuBoost();
		this.leaderCpuBoost = dto.getLeaderCpuBoost();
		this.ramManager = dto.getRamManager();
		this.ramMin = dto.getRamMin();
		this.ramMax = dto.getRamMax();
		this.timeoutLogin = dto.getTimeoutLogin();
		this.timeoutLobby = dto.getTimeoutLobby();
		this.timeoutChamp = dto.getTimeoutChamp();
		this.timeoutMastery = dto.getTimeoutMastery();
		this.timeoutLoadGame = dto.getTimeoutLoadGame();
		this.timeoutInGame = dto.getTimeoutInGame();
		this.timeoutInGameFF = dto.getTimeoutInGameFF();
		this.timeoutEndOfGame = dto.getTimeoutEndOfGame();
		this.openChest = dto.getOpenChest();
		this.openHexTech = dto.getOpenHexTech();
		this.disChest = dto.getDisChest();
		this.prio = dto.getPrio();
	}
}
