package net.nilsghesquiere.dto;

import javax.validation.constraints.NotNull;

import lombok.Data;
import net.nilsghesquiere.annotations.PasswordMatches;
import net.nilsghesquiere.annotations.ValidEmail;

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
