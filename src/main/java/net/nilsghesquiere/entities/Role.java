package net.nilsghesquiere.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

import org.hibernate.validator.constraints.NotBlank;

@Data
@Entity
@Table(name ="roles")
public class Role implements Serializable{
	private static final long serialVersionUID = 1L;
	private @Id @GeneratedValue(strategy = GenerationType.IDENTITY) Long id;
	@Column(unique=true)
	@NotBlank
	private String name;
	
	public Role() {

	}

	public Role(String name) {
		this.name = name;
	}

}
