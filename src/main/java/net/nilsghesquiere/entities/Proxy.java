package net.nilsghesquiere.entities;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;
import net.nilsghesquiere.enums.ProxyType;

import org.hibernate.validator.constraints.NotBlank;
//TODO when everything else works

@Data
@Entity
@Table(name ="proxies")
public class Proxy implements Serializable {
	private static final long serialVersionUID = 1L;
	private @Id @GeneratedValue Long id;
	@NotBlank
	private String handle;
	@NotBlank
	private String proxyHost;
	@NotBlank
	private String proxyPort;
	@NotBlank
	private String proxyUser;
	@NotBlank
	private String proxyPassword;	
	@NotBlank
	private ProxyType proxyType;
	
	public Proxy() {}
	
	public Proxy(String handle, String proxyHost, String proxyPort,
			String proxyUser, String proxyPassword, ProxyType proxyType) {
		super();
		this.handle = handle;
		this.proxyHost = proxyHost;
		this.proxyPort = proxyPort;
		this.proxyUser = proxyUser;
		this.proxyPassword = proxyPassword;
		this.proxyType = proxyType;
	}
	
}
