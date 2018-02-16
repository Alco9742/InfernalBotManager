package net.nilsghesquiere.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import lombok.Data;

import org.hibernate.validator.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;


@Data
@Entity
@Table(name ="users")
public class AppUser implements Serializable{
	private static final long serialVersionUID = 1L;
	private @Id @GeneratedValue Long id;
	@NotBlank
	@Column(unique=true)
	private String username;
	@NotBlank
	@JsonIgnore
	private String password;
	private boolean enabled;
	@ManyToMany
	@JoinTable(
			name="userroles",
			joinColumns=@JoinColumn(name="userid", referencedColumnName="id"),
			inverseJoinColumns=@JoinColumn(name="roleid", referencedColumnName="id"))
	private Set<Role> roles;
	@OneToMany(mappedBy="user",cascade=CascadeType.REMOVE,fetch = FetchType.LAZY)
	@OrderBy("id")
	@JsonIgnore
	private List<LolAccount>lolAccounts;	
	
	public AppUser() {}
	
	public AppUser(String username, String password, Set<Role> roles, boolean enabled) {
		super();
		this.username = username;
		this.password = password;
		this.roles = roles;
		this.enabled = enabled;
		this.lolAccounts = new ArrayList<>();
	}
	
	public AppUser(String username, String password, Set<Role> roles) {
		super();
		this.username = username;
		this.password = password;
		this.roles = roles;
		this.enabled =true;
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
}
