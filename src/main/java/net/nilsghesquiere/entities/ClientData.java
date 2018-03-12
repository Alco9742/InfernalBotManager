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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Data
@Entity
@Table(name ="clients")
public class ClientData implements Serializable{
	private static final long serialVersionUID = 1L;
	private @Id @GeneratedValue(strategy = GenerationType.IDENTITY) Long id;
	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	@JoinColumn(name = "userid")
	@JsonIgnore
	private User user;
	private String tag;
	@OneToMany(mappedBy="client",cascade=CascadeType.REMOVE,fetch = FetchType.LAZY)
	@OrderBy("id")
	private List<Queuer> queuers;
	@OneToMany(mappedBy="client",cascade=CascadeType.REMOVE,fetch = FetchType.LAZY)
	@OrderBy("id")
	private List<ClientStatus> statusList;
	
	public ClientData() {}
	
	
}
