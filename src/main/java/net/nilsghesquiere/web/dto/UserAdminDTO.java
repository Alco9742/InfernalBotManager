package net.nilsghesquiere.web.dto;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import lombok.Data;
import net.nilsghesquiere.web.validation.PasswordMatches;
import net.nilsghesquiere.web.validation.ValidEmail;

import org.hibernate.validator.constraints.NotEmpty;

@Data
@PasswordMatches
public class UserAdminDTO implements Serializable{
	static final long serialVersionUID = 1L;
	private Long id;
	
	@ValidEmail
	@NotNull
	@NotEmpty
	private String email;
	@NotNull
	@NotEmpty
	private String password;
	@NotNull
	@NotEmpty
	private Integer activeQueuers;
	@NotNull
	@NotEmpty
	private Integer maxQueuers;
	@NotNull
	private Boolean enabled;
	
	public UserAdminDTO(){super();}
	
	public UserAdminDTO(Long id, String email,Integer activeQueuers,Integer maxQueuers, Boolean enabled) {
		super();
		this.id = id;
		this.email = email;
		this.password=null;
		this.enabled= enabled;
		this.activeQueuers = activeQueuers;
		this.maxQueuers = maxQueuers;
	}

	public UserAdminDTO(Long id, String email, String password, Integer activeQueuers,Integer maxQueuers, Boolean enabled) {
		super();
		this.id = id;
		this.email = email;
		this.password = password;
		this.enabled= enabled;
		this.activeQueuers = activeQueuers;
		this.maxQueuers = maxQueuers;
	}
	
}
