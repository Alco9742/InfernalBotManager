package net.nilsghesquiere.web.dto;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import lombok.Data;
import net.nilsghesquiere.validation.PasswordMatches;
import net.nilsghesquiere.validation.ValidEmail;
import net.nilsghesquiere.validation.ValidPassword;

import org.hibernate.validator.constraints.NotEmpty;

@Data
@PasswordMatches
public class UserDTO implements Serializable{
	static final long serialVersionUID = 1L;
	@ValidEmail
	@NotNull
	@NotEmpty
	private String email;
	
	@NotNull
	@NotEmpty
	@ValidPassword
	private String password;
	private String matchingPassword;
	
	public UserDTO(){super();}
	
	public UserDTO(String email, String password) {
		super();
		this.email = email;
		this.password = password;
		this.matchingPassword= password;
	}
	
}
