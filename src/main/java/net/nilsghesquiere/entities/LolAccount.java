package net.nilsghesquiere.entities;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Data;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.merakianalytics.orianna.types.common.Region;

@Data
@Entity
@Table(name ="lolaccounts")
@JsonIdentityInfo(
		  generator = ObjectIdGenerators.PropertyGenerator.class, 
		  property = "id")
public class LolAccount implements Serializable{
	private static final long serialVersionUID = 1L;
	private @Id @GeneratedValue Long id;
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "userid")
	@JsonManagedReference
	private AppUser user;
	private boolean enabled;
	private String username;
	private String password;
	private Region region;
	
	public LolAccount() {}

	public LolAccount(String username, String password, Region region, boolean enabled) {
		super();
		this.username = username;
		this.password = password;
		this.region = region;
		this.enabled = enabled;
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
