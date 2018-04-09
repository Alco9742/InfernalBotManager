package net.nilsghesquiere.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;
@Data
@Entity
@Table(name ="metrics")
public class Metric implements Serializable{
	private static final long serialVersionUID = 1L;
	private @Id @GeneratedValue(strategy = GenerationType.IDENTITY) Long id;
	@Column(unique=true)
	private String name;
	private String value;
	
	public Metric() {
	}

	public Metric(String name, String value) {
		super();
		this.name = name;
		this.value = value;
	}
	
}
