package net.nilsghesquiere.web.dto;

import java.io.Serializable;

import lombok.Data;
import net.nilsghesquiere.web.validation.ChangePasswordMatches;
import net.nilsghesquiere.web.validation.PasswordMatches;
import net.nilsghesquiere.web.validation.ValidPassword;

@Data
@ChangePasswordMatches
public class UserChangePasswordDTO implements Serializable{
	static final long serialVersionUID = 1L;
	private String oldPassword;
	@ValidPassword
	private String newPassword;
	private String matchingPassword;
}
