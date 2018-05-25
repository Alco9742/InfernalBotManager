package net.nilsghesquiere.web.dto;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import lombok.Data;
import net.nilsghesquiere.entities.ImportSettings;

import org.hibernate.validator.constraints.NotBlank;

@Data
public class ImportSettingsDTO implements Serializable{
	static final long serialVersionUID = 1L;
	//manager settings
	@NotNull
	private Long id;
	@NotNull
	@NotBlank
	private String name;
	@NotNull
	private Integer maxLevel;
	@NotNull
	private Integer sleepTime;
	@NotNull
	private Integer playTime;
	@NotNull
	private Integer maxBe;
	@NotNull
	private Boolean aktive;
	@NotNull
	private Integer prio;
	
	public ImportSettingsDTO(){
		super();
	}
	
	public ImportSettingsDTO(ImportSettings settings){
		this.maxLevel = settings.getMaxLevel();
		this.sleepTime = settings.getSleepTime();
		this.playTime = settings.getPlayTime();
		this.maxBe = settings.getMaxBe();
		this.aktive = settings.getAktive();
		this.prio = settings.getPrio();
	}

	public ImportSettingsDTO(Long id, String name, Integer maxLevel,
			Integer sleepTime, Integer playTime, Integer maxBe,
			Boolean aktive, Integer prio) {
		super();
		this.id = id;
		this.name = name;
		this.maxLevel = maxLevel;
		this.sleepTime = sleepTime;
		this.playTime = playTime;
		this.maxBe = maxBe;
		this.aktive = aktive;
		this.prio = prio;
	}
}
