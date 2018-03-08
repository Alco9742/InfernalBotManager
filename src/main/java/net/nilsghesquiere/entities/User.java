package net.nilsghesquiere.entities;

import java.io.Serializable;
import java.util.ArrayList;
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
@EqualsAndHashCode(exclude={"infernalSettings", "lolAccounts"})
public class User implements Serializable{
	private static final long serialVersionUID = 1L;
	private @Id @GeneratedValue(strategy = GenerationType.AUTO) Long id;
	@Column(unique=true)
	private String email;
	@JsonIgnore
	private String password;
	private boolean enabled;
	@ManyToMany
	@JoinTable(
			name="userroles",
			joinColumns=@JoinColumn(name="userid", referencedColumnName="id"),
			inverseJoinColumns=@JoinColumn(name="roleid", referencedColumnName="id"))
	private List<Role> roles;
	@OneToMany(mappedBy="user",cascade=CascadeType.REMOVE,fetch = FetchType.LAZY)
	@OrderBy("id")
	@JsonIgnore
	private List<LolAccount>lolAccounts;
	@OneToOne
	@JoinColumn(name="infernalsettingsid")
	InfernalSettings infernalSettings;
	
	public User() {
		super();
		this.enabled = false;
	}
	
	public User(String email, String password, List<Role> roles, boolean enabled) {
		super();
		this.email = email;
		this.password = password;
		this.roles = roles;
		this.enabled = enabled;
		this.lolAccounts = new ArrayList<>();
	}
	
	public User(String email,String username, String password, List<Role> roles) {
		super();
		this.email = email;
		this.password = password;
		this.roles = roles;
		this.enabled =false;
		this.lolAccounts = new ArrayList<>();
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

	@Override
	public String toString() {
		return "User [id=" + id + ", email=" + email 
				+ ", password=" + password + ", enabled=" + enabled
				+ ", roles=" + roles + " + #LolAccounts=" + lolAccounts.size() + "]";
	}

	
	
}
