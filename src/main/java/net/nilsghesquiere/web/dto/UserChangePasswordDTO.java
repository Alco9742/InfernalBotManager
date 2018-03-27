package net.nilsghesquiere.web.dto;

import net.nilsghesquiere.validation.ChangePasswordMatches;
import net.nilsghesquiere.validation.PasswordMatches;
import net.nilsghesquiere.validation.ValidPassword;

import java.io.Serializable;

import lombok.Data;

@Data
@ChangePasswordMatches
public class UserChangePasswordDTO implements Serializable{
	static final long serialVersionUID = 1L;
	private String oldPassword;
	@ValidPassword
	private String newPassword;
	private String matchingPassword;
}
