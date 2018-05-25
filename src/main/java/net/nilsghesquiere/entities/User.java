package net.nilsghesquiere.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;

import com.fasterxml.jackson.annotation.JsonIgnore;


@Data
@Entity
@Table(name ="users")
@EqualsAndHashCode(exclude={"infernalSettingsList","clientSettingsList", "lolAccounts", "clients", "importSettingsList"})
public class User implements Serializable{
	private static final long serialVersionUID = 1L;
	private @Id @GeneratedValue(strategy = GenerationType.IDENTITY) Long id;
	@Column(unique=true)
	private String email;
	@JsonIgnore
	private String password;
	private boolean enabled;
	@ManyToMany
	@JoinTable(
			name="users_roles",
			joinColumns=@JoinColumn(name="userid", referencedColumnName="id"),
			inverseJoinColumns=@JoinColumn(name="roleid", referencedColumnName="id"))
	private Collection<Role> roles;
	@OneToMany(mappedBy="user",cascade=CascadeType.ALL,fetch = FetchType.LAZY)
	@OrderBy("id")
	@JsonIgnore
	private List<LolAccount>lolAccounts;
	@OneToMany(mappedBy="user",cascade=CascadeType.ALL,fetch = FetchType.LAZY)
	@OrderBy("id")
	@JsonIgnore
	private List<Client>clients;
	@OneToMany(mappedBy="user",cascade=CascadeType.ALL,fetch = FetchType.LAZY)
	@OrderBy("id")
	@JsonIgnore
	private List<InfernalSettings> infernalSettingsList;
	@OneToMany(mappedBy="user",cascade=CascadeType.ALL,fetch = FetchType.LAZY)
	@OrderBy("id")
	@JsonIgnore
	private List<ClientSettings>clientSettingsList;
	@OneToMany(mappedBy="user",cascade=CascadeType.ALL,fetch = FetchType.LAZY)
	@OrderBy("id")
	@JsonIgnore
	private List<ImportSettings>importSettingsList;
	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@OrderBy("id")
	@JsonIgnore
	private UserSettings userSettings;
	
	public User() {
		super();
		this.enabled = false;
	}
	
	public User(String email, String password, Collection<Role> roles, boolean enabled) {
		super();
		this.email = email;
		this.password = password;
		this.roles = roles;
		this.enabled = enabled;
		this.lolAccounts = new ArrayList<>();
		this.clients= new ArrayList<>();
		this.clientSettingsList= new ArrayList<>();
		this.infernalSettingsList= new ArrayList<>();
		this.clientSettingsList= new ArrayList<>();
		this.importSettingsList= new ArrayList<>();
	}
	
	public User(String email,String username, String password, Collection<Role> roles) {
		super();
		this.email = email;
		this.password = password;
		this.roles = roles;
		this.enabled =false;
		this.lolAccounts = new ArrayList<>();
		this.clients= new ArrayList<>();
		this.clientSettingsList= new ArrayList<>();
		this.infernalSettingsList= new ArrayList<>();
		this.clientSettingsList= new ArrayList<>();
		this.importSettingsList= new ArrayList<>();
	}
	
	public void addLolAccount(LolAccount lolAccount){
		lolAccounts.add(lolAccount);
		if(lolAccount.getUser() != this){
			lolAccount.setUser(this);
		}
	}
	
	public void removeLolAccount(LolAccount lolAccount){
		lolAccounts.remove(lolAccount);
		if(lolAccount.getUser() == this){
			lolAccount.setUser(null);
		}
	}
	public void addInfernalSettings(InfernalSettings infernalSettings){
		infernalSettingsList.add(infernalSettings);
		if(infernalSettings.getUser() != this){
			infernalSettings.setUser(this);
		}
	}
	
	public void removeInfernalSettings(InfernalSettings infernalSettings){
		infernalSettingsList.remove(infernalSettings);
		if(infernalSettings.getUser() == this){
			infernalSettings.setUser(null);
		}
	}
	
	public void addClientSettings(ClientSettings clientSettings){
		clientSettingsList.add(clientSettings);
		if(clientSettings.getUser() != this){
			clientSettings.setUser(this);
		}
	}
	
	public void removeClientSettings(ClientSettings clientSettings){
		clientSettingsList.remove(clientSettings);
		if(clientSettings.getUser() == this){
			clientSettings.setUser(null);
		}
	}
	
	public void addClient(Client client){
		clients.add(client);
		if(client.getUser() != this){
			client.setUser(this);
		}
	}
	
	public void removeClient(Client client){
		clients.remove(client);
		if(client.getUser() == this){
			client.setUser(null);
		}
	}
	
	public void addImportSettings(ImportSettings importSettings){
		importSettingsList.add(importSettings);
		if(importSettings.getUser() != this){
			importSettings.setUser(this);
		}
	}
	
	public void removeImportSettings(ImportSettings importSettings){
		importSettingsList.remove(importSettings);
		if(importSettings.getUser() == this){
			importSettings.setUser(null);
		}
	}
	
	@Override
	public String toString() {
		String roleList = "[";
		for (Role role : this.getRoles()){
			roleList = roleList + role.getName() + ",";
		}
		if (roleList.length() !=1){
			roleList = roleList.substring(0, roleList.length()-1);
		}
		roleList = roleList + "]";
		return "User [id=" + id + ", email=" + email 
				+ ", password=" + password + ", enabled=" + enabled
				+ ", roles=" + roleList + " + #LolAccounts=" + lolAccounts.size() + "]";
	}
	
	public void setUserSettings(UserSettings userSettings){
		this.userSettings = userSettings;
		userSettings.setUser(this);
	}

}
