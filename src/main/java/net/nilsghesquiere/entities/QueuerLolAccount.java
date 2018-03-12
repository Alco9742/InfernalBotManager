package net.nilsghesquiere.entities;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import net.nilsghesquiere.util.enums.Lane;

@Data
@Entity
@Table(name ="queuerlolaccounts")
public class QueuerLolAccount{
	private static final long serialVersionUID = 1L;
	private @Id @GeneratedValue(strategy = GenerationType.IDENTITY) Long id;
	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	@JoinColumn(name = "queuerid")
	@JsonIgnore
	private Queuer queuer;
	private String account;
	private Integer level;
	private Integer maxLevel;
	private Integer xp;
	private Integer xpCap;
	private Integer be;
	private String champ;
	private Lane lane;
	private String LPQ;
	
	public QueuerLolAccount(){};

}
