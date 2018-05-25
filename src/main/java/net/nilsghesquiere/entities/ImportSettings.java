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
import lombok.EqualsAndHashCode;
import net.nilsghesquiere.util.enums.Region;
import net.nilsghesquiere.web.dto.ImportSettingsDTO;
import net.nilsghesquiere.web.dto.InfernalSettingsDTO;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Data
@Entity
@Table(name ="importsettings")
@EqualsAndHashCode(exclude={"user"})
public class ImportSettings implements Serializable{
	private static final long serialVersionUID = 1L;
	//Manager VARS
	private @Id @GeneratedValue(strategy = GenerationType.IDENTITY) Long id;
	private String name;
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "userid")
	@JsonIgnore
	private User user;
	private Integer maxLevel;
	private Integer sleepTime;
	private Integer playTime;
	private Integer maxBe;
	private Boolean aktive;
	private Integer prio;
	
	public ImportSettings(){} 
	
	public ImportSettings(Long id, String name, User user, Integer maxLevel,
			Integer sleepTime, Integer playTime, Integer maxBe,
			Boolean aktive, Integer prio) {
		super();
		this.id = id;
		this.name = name;
		this.user = user;
		this.maxLevel = maxLevel;
		this.sleepTime = sleepTime;
		this.playTime = playTime;
		this.maxBe = maxBe;
		this.aktive = aktive;
		this.prio = prio;
	}
	
	public ImportSettings(ImportSettingsDTO dto){
		if (dto.getId() != 0){
			this.id = dto.getId(); 
		}
		this.name = dto.getName();
		this.maxLevel = dto.getMaxLevel();
		this.sleepTime = dto.getSleepTime();
		this.playTime = dto.getPlayTime();
		this.maxBe = dto.getMaxBe();
		this.aktive = dto.getAktive();
		this.prio = dto.getPrio();
	}
	
	public void updateFromDTO(ImportSettingsDTO dto){
		this.name = dto.getName();
		this.maxLevel = dto.getMaxLevel();
		this.sleepTime = dto.getSleepTime();
		this.playTime = dto.getPlayTime();
		this.maxBe = dto.getMaxBe();
		this.aktive = dto.getAktive();
		this.prio = dto.getPrio();
	}
	
	public void setUser(User user) {
		if (this.user != null && this.user.getImportSettingsList().contains(this)){
			this.user.removeImportSettings(this);
		}
		this.user = user;
		if (user != null && !user.getImportSettingsList().contains(this)){
			user.addImportSettings(this);
		}
	}
}
