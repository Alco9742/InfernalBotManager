package net.nilsghesquiere.entities;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import lombok.Data;

import org.hibernate.validator.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Data
@Entity
@Table(name ="roles")
public class Role implements Serializable{
	private static final long serialVersionUID = 1L;
	private @Id @GeneratedValue(strategy = GenerationType.IDENTITY) Long id;
	@Column(unique=true)
	@NotBlank
	private String name;
	@ManyToMany(mappedBy = "roles")
	@JsonIgnore
	private List<User> users;
	@ManyToMany
	@JoinTable(
			name = "roles_privileges", 
			joinColumns = @JoinColumn(name = "roleid", referencedColumnName = "id"), 
			inverseJoinColumns = @JoinColumn(name = "privilegeid", referencedColumnName = "id"))
	@JsonIgnore
	private Collection<Privilege> privileges; 
	public Role() {

	}
  
	public Role(String name) {
		this.name = name;
	}

}
