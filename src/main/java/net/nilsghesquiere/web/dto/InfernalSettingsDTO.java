package net.nilsghesquiere.web.dto;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import lombok.Data;
import net.nilsghesquiere.entities.InfernalSettings;
import net.nilsghesquiere.entities.User;
import net.nilsghesquiere.util.enums.Region;
import net.nilsghesquiere.validation.PasswordMatches;
import net.nilsghesquiere.validation.ValidEmail;

import org.apache.coyote.http2.Setting;
import org.hibernate.validator.constraints.NotEmpty;

@Data
public class InfernalSettingsDTO implements Serializable{
	static final long serialVersionUID = 1L;
	//SystemSettings 
	@NotNull
	@NotEmpty
	private Integer groups;
	@NotNull
	@NotEmpty
	private String clientPath;
	@NotNull
	@NotEmpty
	private String clientVersion;
	@NotNull
	private Integer timeSpan;
	@NotNull
	private Boolean autoLogin;
	@NotNull
	private Boolean autoBotStart;
	//ImExPortSettings VARS
	@NotNull
	@NotEmpty
	private String wildcard;
	@NotNull
	private Integer maxLevel;
	@NotNull
	private Integer sleepTime;
	@NotNull
	private Integer playTime;
	@NotNull
	private Integer maxBe;
	@NotNull
	@NotEmpty
	private Region region;
	@NotNull
	private Boolean aktive;
	//AutoqueuerSettings VARS
	@NotNull
	private Boolean surrender;
	@NotNull
	private Integer levelToBeginnerBot;
	@NotNull
	private Boolean clientHide;
	@NotNull
	private Boolean leaderHide;
	@NotNull
	private Boolean consoleHide;
	@NotNull
	private Boolean softEndDefault;
	@NotNull
	private Integer softEndValue;	
	@NotNull
	private Boolean queuerAutoClose;
	@NotNull
	private Integer queueCloseValue;
	@NotNull
	private Boolean winReboot;
	@NotNull
	private Boolean winShutdown;
	@NotNull
	private Boolean timeUntilCheck;
	@NotNull
	@NotEmpty
	private String  timeUntilReboot; 
	//PerformanceSettings VARS
	@NotNull
	private Boolean renderDisable;
	@NotNull
	private Boolean leaderRenderDisable;
	@NotNull
	private Boolean cpuBoost;
	@NotNull
	private Boolean leaderCpuBoost;
	@NotNull
	private Boolean ramManager;
	@NotNull
	private Integer ramMin;
	@NotNull
	private Integer ramMax;
	//TimeoutsSettings VARS
	@NotNull
	private Integer timeoutLogin;
	@NotNull
	private Integer timeoutLobby;
	@NotNull
	private Integer timeoutChamp;
	@NotNull
	private Integer timeoutMastery;
	@NotNull
	private Integer timeoutLoadGame;
	@NotNull
	private Integer timeoutInGame;
	@NotNull
	private Integer timeoutInGameFF;
	@NotNull
	private Integer timeoutEndOfGame;
	//ChestSettings VARS
	@NotNull
	private Boolean openChest;
	@NotNull
	private Boolean openHexTech;
	@NotNull
	private Boolean disChest;
	//OTHER VARS (Not in API)
	@NotNull
	private Integer prio;
	
	public InfernalSettingsDTO(){super();}

	public InfernalSettingsDTO(Integer groups, String clientPath,
			String clientVersion, Integer timeSpan, Boolean autoLogin,
			Boolean autoBotStart, String wildcard, Integer maxLevel,
			Integer sleepTime, Integer playTime, Integer maxBe, Region region,
			Boolean aktive, Boolean surrender, Integer levelToBeginnerBot,
			Boolean clientHide, Boolean leaderHide, Boolean consoleHide,
			Boolean softEndDefault, Integer softEndValue,
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
			Integer prio) {
		super();
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
	}
	
	public InfernalSettingsDTO(InfernalSettings settings){
		this.groups = settings.getGroups();
		this.clientPath = settings.getClientPath();
		this.clientVersion = settings.getClientVersion();
		this.timeSpan = settings.getTimeSpan();
		this.autoLogin = settings.getAutoLogin();
		this.autoBotStart = settings.getAutoBotStart();
		this.wildcard = settings.getWildcard();
		this.maxLevel = settings.getMaxLevel();
		this.sleepTime = settings.getSleepTime();
		this.playTime = settings.getPlayTime();
		this.maxBe = settings.getMaxBe();
		this.region = settings.getRegion();
		this.aktive = settings.getAktive();
		this.surrender = settings.getSurrender();
		this.levelToBeginnerBot = settings.getLevelToBeginnerBot();
		this.clientHide = settings.getClientHide();
		this.leaderHide = settings.getLeaderHide();
		this.consoleHide = settings.getConsoleHide();
		this.softEndDefault = settings.getSoftEndDefault();
		this.softEndValue = settings.getSoftEndValue();
		this.queuerAutoClose = settings.getQueuerAutoClose();
		this.queueCloseValue = settings.getQueueCloseValue();
		this.winReboot = settings.getWinReboot();
		this.winShutdown = settings.getWinShutdown();
		this.timeUntilCheck = settings.getTimeUntilCheck();
		this.timeUntilReboot = settings.getTimeUntilReboot();
		this.renderDisable = settings.getRenderDisable();
		this.leaderRenderDisable = settings.getLeaderRenderDisable();
		this.cpuBoost = settings.getCpuBoost();
		this.leaderCpuBoost = settings.getLeaderCpuBoost();
		this.ramManager = settings.getRamManager();
		this.ramMin = settings.getRamMin();
		this.ramMax = settings.getRamMax();
		this.timeoutLogin = settings.getTimeoutLogin();
		this.timeoutLobby = settings.getTimeoutLobby();
		this.timeoutChamp = settings.getTimeoutChamp();
		this.timeoutMastery = settings.getTimeoutMastery();
		this.timeoutLoadGame = settings.getTimeoutLoadGame();
		this.timeoutInGame = settings.getTimeoutInGame();
		this.timeoutInGameFF = settings.getTimeoutInGameFF();
		this.timeoutEndOfGame = settings.getTimeoutEndOfGame();
		this.openChest = settings.getOpenChest();
		this.openHexTech = settings.getOpenHexTech();
		this.disChest = settings.getDisChest();
		this.prio = settings.getPrio();
	}
}
