package net.nilsghesquiere.entities;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.EqualsAndHashCode;
import net.nilsghesquiere.web.dto.UserSettingsDTO;

@Data
@Entity
@Table(name ="usersettings")
@EqualsAndHashCode(exclude={"user"})
public class UserSettings implements Serializable{
	private static final long serialVersionUID = 1L;
	//Manager VARS
	private @Id @GeneratedValue(strategy = GenerationType.IDENTITY) Long id;
	@OneToOne(cascade=CascadeType.ALL)
	@JoinColumn(name="importsettingsid")
	@JsonIgnore
	private User user;
	private Long activeImportSettings;
	
	public UserSettings(){
		this.activeImportSettings = 0L;
	} 
	
	public UserSettings(Long id, Long activeImportSettings) {
		super();
		this.id = id;
		this.activeImportSettings = activeImportSettings;
	}
	
	public void updateFromDTO(UserSettingsDTO dto){
		this.activeImportSettings = dto.getActiveImportSettings();
	}
	
	public void setUser(User user){
		this.user = user;
	}
}
