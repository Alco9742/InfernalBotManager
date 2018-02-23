package net.nilsghesquiere.web.dto;

import javax.validation.constraints.NotNull;

import lombok.Data;
import net.nilsghesquiere.validation.PasswordMatches;
import net.nilsghesquiere.validation.ValidEmail;

import org.hibernate.validator.constraints.NotEmpty;

@Data
@PasswordMatches
public class UserDTO {
	@NotNull
	@NotEmpty
	private String username;
	
	@NotNull
	@NotEmpty
	private String password;
	private String matchingPassword;
	
	@ValidEmail
	@NotNull
	@NotEmpty
	private String email;
	
}
